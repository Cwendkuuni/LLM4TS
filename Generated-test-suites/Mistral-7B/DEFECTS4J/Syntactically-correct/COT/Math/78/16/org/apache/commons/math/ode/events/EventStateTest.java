package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

/**
 * JUnit test cases for {@link EventState} class.
 */
public class EventStateTest {

    @Test
    public void testConstructor() {
        final EventHandler handler = new MockEventHandler();
        final EventState state = new EventState(handler, 0.01, 0.001, 10);
        assertEquals(handler, state.getEventHandler());
        assertEquals(0.01, state.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, state.getConvergence(), 0.0);
        assertEquals(10, state.getMaxIterationCount());
        assertFalse(Double.isFinite(state.getT0()));
        assertFalse(Double.isFinite(state.getG0()));
        assertFalse(state.isPendingEvent());
        assertFalse(Double.isFinite(state.getPendingEventTime()));
        assertFalse(Double.isFinite(state.getPreviousEventTime()));
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws Exception {
        final EventHandler handler = new MockEventHandler();
        final double[] state = new double[] { 1.0, 2.0 };
        final EventState stateObj = new EventState(handler, 0.0, 0.0, state);

        final double tStart = 3.0;
        stateObj.reinitializeBegin(tStart, state);
        assertEquals(tStart, stateObj.getT0(), 0.0);
        assertArrayEquals(state, stateObj.getInterpolatedState(), 0.0);
        assertEquals(handler.g(tStart, state), stateObj.getG0(), 0.0);
        assertTrue(stateObj.isG0Positive());

        final double g = handler.g(tStart, state);
        assertTrue((g >= 0) == stateObj.isG0Positive());
        assertEquals(EventHandler.CONTINUE, stateObj.getNextAction());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws Exception {
        final EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) throws EventException {
                throw new EventException("Test exception");
            }
        };
        final EventState state = new EventState(handler, 0.0, 0.0, new double[0]);
        try {
            state.reinitializeBegin(0.0, new double[0]);
        } finally {
            // If test fails, exception is already thrown
        }
    }

    @Test
    public void testEvaluateStep() throws Exception {
        final EventHandler handler = new MockEventHandler();
        final EventState state = new EventState(handler, 0.01, 0.001, 10);

        final StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(new double[] { 0.0, 1.0, 1.0, 1.5, 2.0, 2.5 });
        interpolator.add(new double[] { 0.0, 0.5, 1.0, 1.2, 1.5, 2.0 });

        final double t1 = 1.2;
        final int n = Math.max(1, (int) Math.ceil((t1 - state.getT0()) / state.getMaxCheckInterval()));
        state.evaluateStep(interpolator);

        assertEquals(t1, state.getT0(), 0.0);
        assertEquals(1.2, interpolator.getCurrentTime(), 0.0);
        assertEquals(handler.g(t1, interpolator.getInterpolatedState()), state.getG0(), 0.0);
        assertTrue(state.isPendingEvent());
        assertEquals(1.2, state.getPendingEventTime(), 0.0);
        assertTrue(state.isIncreasing());
    }

    @Test
    public void testEvaluateStepNoEvent() throws Exception {
        final EventHandler handler = new MockEventHandler();
        final EventState state = new EventState(handler, 0.01, 0.001, 10);

        final StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(new double[] { 0.0, 1.0, 1.0, 1.5, 2.0, 2.5 });
        interpolator.add(new double[] { 0.0, 0.5, 1.0, 1.2, 1.5, 2.0 });

        state.evaluateStep(interpolator);
        assertFalse(state.isPendingEvent());
        assertEquals(-1.0, state.getPendingEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws Exception {
        final EventHandler handler = new MockEventHandler();
        final EventState state = new EventState(handler, 0.01, 0.001, 10);

        double[] stateBefore = new double[] { 1.0, 2.0 };
        state.stepAccepted(1.5, stateBefore);

        assertEquals(1.5, state.getT0(), 0.0);
        assertArrayEquals(stateBefore, state.getInterpolatedState(), 0.0);
        assertEquals(handler.g(1.5, state.getInterpolatedState()), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        double[] stateAfter = new double[] { 3.0, 4.0 };
        state.stepAccepted(2.0, stateAfter);
        assertEquals(2.0, state.getT0(), 0.0);
        assertArrayEquals(stateAfter, state.getInterpolatedState(), 0.0);
        assertEquals(handler.g(2.0, state.getInterpolatedState()), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    @Test
    public void testStepAcceptedEvent() throws Exception {
        final EventHandler handler = new MockEventHandler();
        final EventState state = new EventState(handler, 0.01, 0.001, 10);

        double[] stateBefore = new double[] { 1.0, 2.0 };
        state.stepAccepted(1.5, stateBefore);

        assertEquals(1.5, state.getT0(), 0.0);
        assertArrayEquals(stateBefore, state.getInterpolatedState(), 0.0);
        assertEquals(handler.g(1.5, state.getInterpolatedState()), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        state.stepAccepted(1.6, stateBefore);

        assertTrue(state.isPendingEvent());
        assertEquals(1.6, state.getPendingEventTime(), 0.0);
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());
    }

    @Test
    public void testStop() {
        final EventHandler handler = new MockEventHandler();
        final EventState state = new EventState(handler, 0.01, 0.001, 10);
        assertFalse(state.stop());

        state.setNextAction(EventHandler.STOP);
        assertTrue(state.stop());
    }

    @Test
    public void testReset() throws Exception {
        final EventHandler handler = new MockEventHandler();
        final EventState state = new EventState(handler, 0.01, 0.001, 10);

        state.setNextAction(EventHandler.RESET_STATE);
        double[] stateBefore = new double[] { 1.0, 2.0 };
        state.reset(1.5, stateBefore);
        assertEquals(1.5, state.getT0(), 0.0);
        assertArrayEquals(new double[0], state.getInterpolatedState(), 0.0);
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());

        state.setNextAction(EventHandler.RESET_DERIVATIVES);
        state.reset(1.5, stateBefore);
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(new double[0], state.getInterpolatedState(), 0.0);
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());

        state.setNextAction(EventHandler.CONTINUE);
        try {
            state.reset(1.5, stateBefore);
        } catch (IllegalArgumentException e) {
            assertEquals("Handler must return a non-null state", e.getMessage());
        }
    }

    private static class MockEventHandler implements EventHandler {
        private double eventTime;

        @Override
        public double g(double t, double[] y) throws EventException {
            if (Double.isNaN(eventTime)) {
                eventTime = t + 0.5;
            }
            return eventTime - t;
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean isLocalMaximum) {
            // Do nothing
        }

        @Override
        public void resetState(double t, double[] y) {
            // Do nothing
        }

        public void setNextAction(int nextAction) {
            this.nextAction = nextAction;
        }
    }

}