import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.ode.EventsState;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventHandler.Action;
import org.apache.commons.math3.ode.events.EventHandler.EventType;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        EventHandler handler = new TestEventHandler();
        EventsState state = new EventState(handler, 0.1, 0.001, 10);
        assertNull(state.getEventHandler());
        assertEquals(0.1, state.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, state.getConvergence(), 0.0);
        assertEquals(10, state.getMaxIterationCount());
        assertTrue(Double.isNaN(state.getT0()));
        assertTrue(Double.isNaN(state.getG0()));
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertNull(state.getPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test reinitializeBegin().
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new TestEventHandler();
        EventsState state = new EventState(handler, 0.1, 0.001, 10);

        double[] y = new double[3];
        state.reinitializeBegin(0.0, y);
        assertEquals(0.0, state.getT0(), 0.0);
        assertEquals(handler.g(0.0, y), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());

        state.setG0Positive(false);
        state.reinitializeBegin(0.0, y);
        assertFalse(state.isG0Positive());
    }

    /**
     * Test evaluateStep().
     */
    @Test
    public void testEvaluateStep() throws EventException, DerivativeException, ConvergenceException {
        EventHandler handler = new TestEventHandler();
        EventsState state = new EventState(handler, 0.1, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addInterpolatedValue(0.0, state.getG0());
        interpolator.addInterpolatedValue(0.1, 0.0);
        interpolator.addInterpolatedValue(0.2, 1.0);

        assertTrue(state.evaluateStep(interpolator));
        assertEquals(0.1, state.getPendingEventTime(), 0.0);
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());

        interpolator.setCurrentTime(0.15);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(0.15, state.getPendingEventTime(), 0.0);
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());

        interpolator.setCurrentTime(0.13);
        assertFalse(state.evaluateStep(interpolator));
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        interpolator.setCurrentTime(0.21);
        assertFalse(state.evaluateStep(interpolator));
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test getEventTime().
     */
    @Test
    public void testGetEventTime() {
        EventHandler handler = new TestEventHandler();
        EventsState state = new EventState(handler, 0.1, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.0);
        assertEquals(1.0, state.getEventTime(), 0.0);
    }

    /**
     * Test stepAccepted().
     */
    @Test
    public void testStepAccepted() {
        EventHandler handler = new TestEventHandler();
        EventsState state = new EventState(handler, 0.1, 0.001, 10);

        double[] y = new double[3];
        state.stepAccepted(0.1, y);
        assertEquals(0.1, state.getT0(), 0.0);
        assertEquals(handler.g(0.1, y), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        state.setPendingEvent(true);
        state.stepAccepted(1.0, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());
    }

    /**
     * Test stop().
     */
    @Test
    public void testStop() {
        EventHandler handler = new TestEventHandler();
        EventsState state = new EventState(handler, 0.1, 0.001, 10);
        state.setNextAction(EventHandler.STOP);
        assertTrue(state.stop());
    }

    /**
     * Test reset().
     */
    @Test
    public void testReset() throws EventException {
        EventHandler handler = new TestEventHandler();
        EventsState state = new EventState(handler, 0.1, 0.001, 10);

        state.setPendingEvent(true);
        state.setPendingEventTime(1.0);
        state.setPreviousEventTime(0.5);
        state.reset(0.2, new double[3]);

        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
        assertEquals(0.2, state.getPreviousEventTime(), 0.0);
    }

    /**
     * Test setG0Positive().
     */
    @Test
    public void testSetG0Positive() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        state.setG0Positive(false);
        assertFalse(state.isG0Positive());
        state.setG0Positive(true);
        assertTrue(state.isG0Positive());
    }

    /**
     * Test isG0Positive().
     */
    @Test
    public void testIsG0Positive() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        state.setG0Positive(true);
        assertTrue(state.isG0Positive());
        state.setG0Positive(false);
        assertFalse(state.isG0Positive());
    }

    /**
     * Test isPendingEvent().
     */
    @Test
    public void testIsPendingEvent() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertFalse(state.isPendingEvent());
        state.setPendingEvent(true);
        assertTrue(state.isPendingEvent());
        state.setPendingEvent(false);
        assertFalse(state.isPendingEvent());
    }

    /**
     * Test isForward().
     */
    @Test
    public void testIsForward() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertTrue(state.isForward());
        state.setForward(false);
        assertFalse(state.isForward());
    }

    /**
     * Test isIncreasing().
     */
    @Test
    public void testIsIncreasing() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertTrue(state.isIncreasing());
        state.setIncreasing(false);
        assertFalse(state.isIncreasing());
    }

    /**
     * Test getNextAction().
     */
    @Test
    public void testGetNextAction() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
        state.setNextAction(EventHandler.EVENT_OCCURRED);
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());
        state.setNextAction(EventHandler.RESET_STATE);
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());
        state.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());
        state.setNextAction(EventHandler.STOP);
        assertEquals(EventHandler.STOP, state.getNextAction());
    }

    /**
     * Test setNextAction().
     */
    @Test
    public void testSetNextAction() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
        state.setNextAction(EventHandler.EVENT_OCCURRED);
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());
        state.setNextAction(EventHandler.RESET_STATE);
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());
        state.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());
        state.setNextAction(EventHandler.STOP);
        assertEquals(EventHandler.STOP, state.getNextAction());
    }

    /**
     * Test setPendingEvent().
     */
    @Test
    public void testSetPendingEvent() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertFalse(state.isPendingEvent());
        state.setPendingEvent(true);
        assertTrue(state.isPendingEvent());
        state.setPendingEvent(false);
        assertFalse(state.isPendingEvent());
    }

    /**
     * Test setPendingEventTime().
     */
    @Test
    public void testSetPendingEventTime() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertNull(state.getPendingEventTime());
        state.setPendingEventTime(1.0);
        assertEquals(1.0, state.getPendingEventTime(), 0.0);
        state.setPendingEventTime(0.0);
        assertNull(state.getPendingEventTime());
    }

    /**
     * Test setPreviousEventTime().
     */
    @Test
    public void testSetPreviousEventTime() {
        EventState state = new EventState(null, 0.1, 0.001, 10);
        assertNull(state.getPreviousEventTime());
        state.setPreviousEventTime(1.0);
        assertEquals(1.0, state.getPreviousEventTime(), 0.0);
        state.setPreviousEventTime(0.0);
        assertNull(state.getPreviousEventTime());
    }

    /**
     * Test new TestEventHandler().
     */
    public static class TestEventHandler implements EventHandler {

        @Override
        public double g(double t, double[] y) throws EventException {
            if (t > 0.0) {
                return Math.sin(t);
            } else {
                throw new EventException("Test event");
            }
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean forward) {
            return Action.RESET_STATE;
        }

        @Override
        public void resetState(double t, double[] y) {
            // do nothing
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // do nothing
        }

        @Override
        public EventType getEventType() {
            return EventType.TRANSVERSE;
        }

        @Override
        public double getEventTime(double t, double[] y) throws EventException {
            if (t > 0.0) {
                return Math.sin(t) > 0.0 ? 0.0 : t;
            } else {
                throw new EventException("Test event");
            }
        }

        @Override
        public boolean isStateDefined(double t, double[] y) {
            return true;
        }

        @Override
        public void setState(double t, double[] y) {
            // do nothing
        }

    }

}