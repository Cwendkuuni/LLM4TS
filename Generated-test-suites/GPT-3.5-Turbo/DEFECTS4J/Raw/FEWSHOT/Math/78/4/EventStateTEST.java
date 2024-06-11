import org.junit.Test;

import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventState eventState = new EventState(null, 0.0, 0.0, 0);
        assertNotNull(eventState);
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) throws EventException {
                return 0;
            }

            @Override
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return 0;
            }

            @Override
            public void resetState(double t, double[] y) throws EventException {

            }
        };
        EventState eventState = new EventState(eventHandler, 0.0, 0.0, 0);
        assertEquals(eventHandler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        double maxCheckInterval = 0.0;
        EventState eventState = new EventState(null, maxCheckInterval, 0.0, 0);
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
    }

    @Test
    public void testGetConvergence() {
        double convergence = 0.0;
        EventState eventState = new EventState(null, 0.0, convergence, 0);
        assertEquals(convergence, eventState.getConvergence(), 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        int maxIterationCount = 0;
        EventState eventState = new EventState(null, 0.0, 0.0, maxIterationCount);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() {
        EventState eventState = new EventState(null, 0.0, 0.0, 0);
        assertDoesNotThrow(() -> eventState.reinitializeBegin(0.0, new double[0]));
    }

    @Test
    public void testEvaluateStep() {
        EventState eventState = new EventState(null, 0.0, 0.0, 0);
        assertThrows(DerivativeException.class, () -> eventState.evaluateStep(null));
    }

    @Test
    public void testGetEventTime() {
        EventState eventState = new EventState(null, 0.0, 0.0, 0);
        assertEquals(Double.NaN, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(null, 0.0, 0.0, 0);
        assertDoesNotThrow(() -> eventState.stepAccepted(0.0, new double[0]));
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(null, 0.0, 0.0, 0);
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() {
        EventState eventState = new EventState(null, 0.0, 0.0, 0);
        assertFalse(eventState.reset(0.0, new double[0]));
    }
}