package eu.chargetime.ocpp.test.core.soap

import eu.chargetime.ocpp.test.FakeCentralSystem
import eu.chargetime.ocpp.test.FakeChargePoint
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class SOAPHeartbeatSpec extends Specification {
    @Shared
    FakeCentralSystem centralSystem = FakeCentralSystem.getInstance()
    @Shared
    FakeChargePoint chargePoint = new FakeChargePoint(FakeChargePoint.clientType.SOAP)

    def setupSpec() {
        // When a Central System is running
        centralSystem.started(FakeCentralSystem.serverType.SOAP)
    }

    def setup() {
        chargePoint.connect()
    }

    def cleanup() {
        chargePoint.disconnect()
    }

    def "Charge point sends Heartbeat and receives a response"() {
        def conditions = new PollingConditions(timeout: 1)
        when:
        chargePoint.sendHeartbeatRequest()

        then:
        conditions.eventually {
            assert centralSystem.hasHandledHeartbeat()
        }

        then:
        conditions.eventually {
            assert chargePoint.hasReceivedHeartbeatConfirmation()
        }

    }

}
