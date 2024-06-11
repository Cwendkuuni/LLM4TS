package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link EventState} class.
 */
public class EventStateTest {

    private EventHandler handler;
    private StepInterpolator interpolator;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        handler = new FakeEventHandler();
        interpolator = new StepInterpolator();
    }

    /**
     * Tests constructor.
     */
    @Test
    public void testConstructor() {
        EventState state = new EventState(handler, 0.1, 0.01, 10);
        assertEquals(handler, state.getEventHandler());
        assertEquals(0.1, state.getMaxCheckInterval(), 0);
        assertEquals(0.01, state.getConvergence(), 0);
        assertEquals(10, state.getMaxIterationCount());
        assertNull(state.getEventHandler().getState());
        assertNull(state.getEventHandler().getPreviousState());
    }

    /**
     * Tests reinitialization.
     */
    @Test
    public void testReinitializeBegin() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.reinitializeBegin(t0, y0);
        assertEquals(t0, state.getT0(), 0);
        assertEquals(y0, state.getY0(), 0);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
    }

    /**
     * Tests {@link EventState#evaluateStep(StepInterpolator)} with no event.
     */
    @Test
    public void testEvaluateStepNoEvent() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.reinitializeBegin(t0, y0);
        interpolator.setInterpolatedState(y0);
        interpolator.setInterpolatedTime(0.1);
        assertFalse(state.evaluateStep(interpolator));
        assertEquals(t0, state.getT0(), 0);
        assertEquals(y0, state.getY0(), 0);
        assertEquals(0.1, state.getT(), 0);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
    }

    /**
     * Tests {@link EventState#evaluateStep(StepInterpolator)} with event.
     */
    @Test
    public void testEvaluateStepEvent() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t1 = 0.5;
        double[] y1 = { 0.3, -1.2 };
        EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.reinitializeBegin(t0, y0);
        interpolator.setInterpolatedState(y0);
        interpolator.setInterpolatedTime(t0);
        handler.setState(y1);
        handler.setPreviousState(y0);

        interpolator.setForward(true);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(t1, state.getPendingEventTime(), 0);
        assertTrue(state.isPendingEvent());
        assertEquals(y1, state.getY(), 0);
        assertEquals(t1, state.getT(), 0);
    }

    /**
     * Tests {@link EventState#stepAccepted(double, double[])}.
     */
    @Test
    public void testStepAccepted() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t1 = 0.5;
        double[] y1 = { 0.3, -1.2 };
        EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.reinitializeBegin(t0, y0);
        interpolator.setInterpolatedState(y0);
        interpolator.setInterpolatedTime(t0);
        handler.setState(y1);
        handler.setPreviousState(y0);

        interpolator.setForward(true);
        state.evaluateStep(interpolator);
        state.stepAccepted(t1, y1);
        assertEquals(t1, state.getT0(), 0);
        assertEquals(y1, state.getY0(), 0);
        assertEquals(t1, state.getT(), 0);
        assertEquals(y1, state.getY(), 0);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        state.reset();
        assertEquals(t1, state.getT0(), 0);
        assertEquals(y1, state.getY0(), 0);
        assertEquals(t1, state.getT(), 0);
        assertEquals(y1, state.getY(), 0);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Tests {@link EventState#stop()}.
     */
    @Test
    public void testStop() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t1 = 0.5;
        double[] y1 = { 0.3, -1.2 };
        EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.reinitializeBegin(t0, y0);
        interpolator.setInterpolatedState(y0);
        interpolator.setInterpolatedTime(t0);
        handler.setState(y1);
        handler.setPreviousState(y0);

        interpolator.setForward(true);
        state.evaluateStep(interpolator);
        state.stepAccepted(t1, y1);
        state.stepAccepted(t1 + 0.1, y1);
        state.reset();
        state.setEventHandler(new StoppingEventHandler());
        state.stepAccepted(t1, y1);
        assertTrue(state.stop());
    }

    /**
     * Tests {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testReset() throws Exception {
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t1 = 0.5;
        double[] y1 = { 0.3, -1.2 };
        EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.reinitializeBegin(t0, y0);
        interpolator.setInterpolatedState(y0);
        interpolator.setInterpolatedTime(t0);
        handler.setState(y1);
        handler.setPreviousState(y0);

        interpolator.setForward(true);
        state.evaluateStep(interpolator);
        state.stepAccepted(t1, y1);
        state.reset(t1, y1);
        assertEquals(t1, state.getT0(), 0);
        assertEquals(y1, state.getY0(), 0);
        assertEquals(t1, state.getT(), 0);
        assertEquals(y1, state.getY(), 0);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        state.setEventHandler(new ResetEventHandler());
        state.evaluateStep(interpolator);
        state.stepAccepted(t1, y1);
        state.reset(t1, y1);
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());
    }

    /**
     * Verifies that the state can be reseted by the event handler.
     */
    private static class ResetEventHandler implements EventHandler {

        /**
         * {@inheritDoc}
         */
        @Override
        public void eventOccurred(double t, double[] y, boolean increaseEvent)
                throws EventException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resetState(double t, double[] y) {
            y[1] = 0.0;
        }

    }

    /**
     * Verifies that the integration is stopped by the event handler.
     */
    private static class StoppingEventHandler implements EventHandler {

        /**
         * {@inheritDoc}
         */
        @Override
        public void eventOccurred(double t, double[] y, boolean increaseEvent)
                throws EventException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getNextAction() {
            return EventHandler.STOP;
        }

    }

    /**
     * Verifies that the state is not changed by the event handler.
     */
    private static class FakeEventHandler implements EventHandler {

        /**
         * {@inheritDoc}
         */
        @Override
        public void eventOccurred(double t, double[] y, boolean increaseEvent)
                throws EventException {
            // do nothing
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getNextAction() {
            return EventHandler.CONTINUE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public UnivariateFunction getSwitchingFunction() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resetState(double t, double[] y) {
            // do nothing
        }

    }

}