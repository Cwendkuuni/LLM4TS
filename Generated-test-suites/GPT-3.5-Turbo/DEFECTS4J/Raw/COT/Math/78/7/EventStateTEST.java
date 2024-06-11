import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;

    @Before
    public void setUp() {
        eventState = new EventState(null, 0.0, 0.0, 0);
    }

    @Test
    public void testGetEventHandler() {
        assertNull(eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.0, eventState.getMaxCheckInterval(), 0.0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(0.0, eventState.getConvergence(), 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(0, eventState.getMaxIterationCount());
    }

    @Test
    public void testStepAccepted() throws EventException {
        eventState.stepAccepted(0, new double[0]);
        assertEquals(0, eventState.getEventTime(), 0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        boolean result = eventState.evaluateStep(null);
        assertFalse(result);
    }

    @Test
    public void testStop() {
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        boolean result = eventState.reset(0, new double[0]);
        assertFalse(result);
    }

}