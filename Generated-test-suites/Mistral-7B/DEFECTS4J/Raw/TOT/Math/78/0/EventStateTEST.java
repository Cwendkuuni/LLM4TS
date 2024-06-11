package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventHandlerAdapter;
import org.apache.commons.math3.ode.events.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Test for {@link EventState}.
 */
public class EventStateTest {

    /**
     * Test for the constructor.
     */
    @Test
    public void testConstructor() {
        final EventHandler handler = new EventHandlerAdapter() {
            @Override
            public double g(double time, double[] y) {
                return Math.sin(time);
            }

            @Override
            public void eventOccurred(double time, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double time, double[] y) {
                // no-op
            }
        };
        final double maxCheckInterval = 0.1;
        final double convergence = 0.01;
        final int maxIterationCount = 10;
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        assertEquals("Handler", handler, state.getEventHandler());
        assertEquals("Max check interval", maxCheckInterval, state.getMaxCheckInterval(), 0.0);
        assertEquals("Convergence", convergence, state.getConvergence(), 0.0);
        assertEquals("Max iteration count", maxIterationCount, state.getMaxIterationCount());
        assertFalse("Pending event", state.isPendingEvent());
        assertNull("Pending event time", state.getPendingEventTime());
        assertNull("Previous event time", state.getPreviousEventTime());
        assertTrue("Forward", state.isForward());
        assertFalse("Increasing", state.isIncreasing());
        assertEquals("Next action", EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test for {@link EventState#reinitializeBegin(double, double[])}.
     */
    @Test
    public void testReinitializeBegin() throws Exception {
        final EventHandler handler = new EventHandlerAdapter() {
            @Override
            public double g(double time, double[] y) {
                throw new UnsupportedOperationException("Should not be called");
            }

            @Override
            public void eventOccurred(double time, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double time, double[] y) {
                // no-op
            }
        };
        final double maxCheckInterval = 0.1;
        final double convergence = 0.01;
        final int maxIterationCount = 10;
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double[] y = new double[5];
        double tStart = 0.0;
        state.reinitializeBegin(tStart, y);
        assertEquals("Time at the beginning of the step", tStart, state.getT0(), 0.0);
        // g should be evaluated by the handler
        assertNaN("Value of the events handler at the beginning of the step", state.getG0());
        assertTrue("Simulated sign of g0 is positive", state.isG0Positive());
    }

    /**
     * Test for {@link EventState#evaluateStep(StepInterpolator)}.
     */
    @Test
    public void testEvaluateStep() throws Exception {
        final EventHandler handler = new EventHandlerAdapter() {
            @Override
            public double g(double time, double[] y) {
                return Math.sin(time);
            }

            @Override
            public void eventOccurred(double time, double[] y, boolean forward) {
                if (forward) {
                    throw new EventException("Event occurred");
                }
            }

            @Override
            public void resetState(double time, double[] y) {
                // no-op
            }
        };
        final double maxCheckInterval = 0.1;
        final double convergence = 0.01;
        final int maxIterationCount = 10;
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        final double h = 0.1;
        final StepInterpolator interpolator = new StepInterpolator(h);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(h);
        interpolator.setStepNumber(0);
        final double t1 = 1.0;
        double[] y1 = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        state.reinitializeBegin(0.0, y1);
        assertFalse("No event during the step", state.evaluateStep(interpolator));
        interpolator.setStepNumber(1);
        interpolator.setInterpolatedTime(h * 2.0);
        state.reinitializeBegin(t1, y1);
        assertTrue("Event during the step", state.evaluateStep(interpolator));
        assertEquals("Occurrence time of the event", t1 + h, state.getPendingEventTime(), 0.0);
        assertFalse("Pending event", state.isPendingEvent());
        assertEquals("Next action", EventHandler.STOP, state.getNextAction());
    }

    /**
     * Test for {@link EventState#getEventTime()}.
     */
    @Test
    public void testGetEventTime() throws Exception {
        final EventHandler handler = new EventHandlerAdapter() {
            @Override
            public double g(double time, double[] y) {
                return Math.sin(time);
            }

            @Override
            public void eventOccurred(double time, double[] y, boolean forward) {
                if (forward) {
                    throw new EventException("Event occurred");
                }
            }

            @Override
            public void resetState(double time, double[] y) {
                // no-op
            }
        };
        final double maxCheckInterval = 0.1;
        final double convergence = 0.01;
        final int maxIterationCount = 10;
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        final double h = 0.1;
        final StepInterpolator interpolator = new StepInterpolator(h);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(h);
        interpolator.setStepNumber(0);
        double[] y1 = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        state.reinitializeBegin(0.0, y1);
        assertNull("Pending event time when no event", state.getPendingEventTime());
        interpolator.setStepNumber(1);
        interpolator.setInterpolatedTime(h * 2.0);
        state.reinitializeBegin(1.0, y1);
        assertEquals("Occurrence time of the event", 1.0, state.getPendingEventTime(), 0.0);
    }

    /**
     * Test for {@link EventState#stepAccepted(double, double[])}.
     */
    @Test
    public void testStepAccepted() throws Exception {
        final EventHandler handler = new EventHandlerAdapter() {
            @Override
            public double g(double time, double[] y) {
                return Math.sin(time);
            }

            @Override
            public void eventOccurred(double time, double[] y, boolean forward) {
                if (forward) {
                    throw new EventException("Event occurred");
                }
            }

            @Override
            public void resetState(double time, double[] y) {
                // no-op
            }
        };
        final double maxCheckInterval = 0.1;
        final double convergence = 0.01;
        final int maxIterationCount = 10;
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double[] y = new double[5];
        double t = 1.0;
        state.stepAccepted(t, y);
        assertEquals("Value of the events handler at the end of the step", handler.g(t, y), state.getG0(), 0.0);
        assertFalse("No pending event", state.isPendingEvent());
        assertNull("Pending event time", state.getPendingEventTime());
        assertFalse("Forward", state.isForward());
        assertTrue("Increasing", state.isIncreasing());
        assertEquals("Next action", EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test for {@link EventState#stop()}.
     */
    @Test
    public void testStop() throws Exception {
        final EventHandler handler = new EventHandlerAdapter() {
            @Override
            public double g(double time, double[] y) {
                return Math.sin(time);
            }

            @Override
            public void eventOccurred(double time, double[] y, boolean forward) {
                if (forward) {
                    throw new EventException("Event occurred");
                }
            }

            @Override
            public void resetState(double time, double[] y) {
                // no-op
            }
        };
        final double maxCheckInterval = 0.1;
        final double convergence = 0.01;
        final int maxIterationCount = 10;
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        assertFalse("Integration should not be stopped at the end of the first step", state.stop());
        double[] y1 = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        state.reinitializeBegin(0.0, y1);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(h);
        state.stepAccepted(1.0, y1);
        assertTrue("Integration should be stopped at the end of the second step", state.stop());
    }

    /**
     * Test for {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testReset() throws Exception {
        final EventHandler handler = new EventHandlerAdapter() {
            @Override
            public double g(double time, double[] y) {
                return Math.sin(time);
            }

            @Override
            public void eventOccurred(double time, double[] y, boolean forward) {
                if (forward) {
                    throw new EventException("Event occurred");
                }
            }

            @Override
            public void resetState(double time, double[] y) {
                for (int i = 0; i < y.length; ++i) {
                    y[i] *= 2.0;
                }
            }
        };
        final double maxCheckInterval = 0.1;
        final double convergence = 0.01;
        final int maxIterationCount = 10;
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double[] y = new double[5];
        double t = 1.0;
        state.reinitializeBegin(t, y);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(h);
        state.stepAccepted(1.0, y);
        assertArrayEquals("State vector after the first step", new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, y);
        state.reset(1.0, y);
        assertArrayEquals("State vector after the reset", new double[] { 2.0, 4.0, 6.0, 8.0, 10.0 }, y);
        state.stepAccepted(1.0, y);
        assertArrayEquals("State vector after the second step", new double[] { 2.0, 8.0, 12.0, 16.0, 20.0 }, y);
    }

}