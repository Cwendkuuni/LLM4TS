import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;
    private EventHandlerMock eventHandlerMock;

    @Before
    public void setUp() {
        eventHandlerMock = new EventHandlerMock();
        eventState = new EventState(eventHandlerMock, 0.01, 0.001, 100);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(eventState.getEventHandler(), eventHandlerMock);
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(eventState.getMaxCheckInterval(), 0.01, 0.001);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(eventState.getConvergence(), 0.001, 0.001);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(eventState.getMaxIterationCount(), 100);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(eventState.t0, tStart, 0.001);
        assertEquals(eventState.g0, eventHandlerMock.g(tStart, yStart), 0.001);
        assertEquals(eventState.g0Positive, eventState.g0 >= 0);
    }

    @Test
    public void testEvaluateStep() {
        // TODO: Implement test cases for evaluateStep() method
    }

    @Test
    public void testGetEventTime() {
        // TODO: Implement test cases for getEventTime() method
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 1.0;
        double[] y = {4.0, 5.0, 6.0};

        eventState.stepAccepted(t, y);

        assertEquals(eventState.t0, t, 0.001);
        assertEquals(eventState.g0, eventHandlerMock.g(t, y), 0.001);
        if (eventState.pendingEvent) {
            assertEquals(eventState.g0Positive, eventState.increasing);
        }
        assertEquals(eventState.nextAction, eventHandlerMock.eventOccurred(t, y, !(eventState.increasing ^ eventState.forward)));
    }

    @Test
    public void testStop() {
        // TODO: Implement test cases for stop() method
    }

    @Test
    public void testReset() throws EventException {
        double t = 2.0;
        double[] y = {7.0, 8.0, 9.0};

        eventState.reset(t, y);

        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
        if (eventState.nextAction == EventHandler.RESET_STATE) {
            assertEquals(eventHandlerMock.resetStateCalls, 1);
            assertArrayEquals(eventHandlerMock.resetStateArgs, new Object[]{t, y});
        }
    }
}