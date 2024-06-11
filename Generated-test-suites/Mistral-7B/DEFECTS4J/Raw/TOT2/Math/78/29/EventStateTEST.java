package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.ode.DerivativeException;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.eventhandlers.AbstractEventhandler;
import org.apache.commons.math3.ode.events.eventhandlers.ContinuousEvent;
import org.apache.commons.math3.ode.events.eventhandlers.DiscreteEvent;
import org.apache.commons.math3.ode.events.eventhandlers.SampleEvent;
import org.junit.Test;

/**
 * Test suite for {@link EventState} class.
 *
 * @version $Revision$ $Date$
 */
public class EventStateTest {

    /** Test event handler that always triggers an event. */
    private static class TestEventHandler implements EventHandler {

        private double previousEventTime;

        @Override
        public double g(double t, double[] y) {
            final double value = Math.sin(t * Math.PI / 4);
            previousEventTime = t;
            return value;
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean forward) {
            // no op
        }

        @Override
        public void resetState(double t, double[] y) {
            // no op
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // no op
        }

        public double getPreviousEventTime() {
            return previousEventTime;
        }

    }

    /** Test event handler that never triggers an event. */
    private static class NoEventEventHandler implements EventHandler {

        @Override
        public double g(double t, double[] y) {
            return Math.cos(t);
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean forward) {
            fail("An event was expected but not triggered.");
        }

        @Override
        public void resetState(double t, double[] y) {
            // no op
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // no op
        }

    }

    /** Test event handler that triggers an event exactly at a given time. */
    private static class SampleEventEventHandler implements EventHandler {

        private final double eventTime;

        public SampleEventEventHandler(double eventTime) {
            this.eventTime = eventTime;
        }

        @Override
        public double g(double t, double[] y) {
            return Math.sin(t);
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean forward) {
            if (t == eventTime) {
                throw new SampleEvent(eventTime);
            }
        }

        @Override
        public void resetState(double t, double[] y) {
            // no op
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // no op
        }

    }

    /** Test method for {@link EventState#EventState(EventHandler, double, double, int)}. */
    @Test
    public void testEventStateConstruction() {
        final TestEventHandler testEventHandler = new TestEventHandler();
        final EventState eventState = new EventState(testEventHandler, 0.1, 0.1, 10);
        assertEquals(testEventHandler, eventState.getEventHandler());
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.1, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getPreviousEventTime());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertFalse(eventState.isForward());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /** Test method for {@link EventState#reinitializeBegin(double, double[])}. */
    @Test
    public void testEventStateReinitializeBegin()
        throws EventException {
        final TestEventHandler testEventHandler = new TestEventHandler();
        final EventState eventState = new EventState(testEventHandler, 0.1, 0.1, 10);
        final double tStart = 1.0;
        final double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(testEventHandler.g(1.0, yStart), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /** Test method for {@link EventState#evaluateStep(StepInterpolator)}. */
    @Test
    public void testEventStateEvaluateStep()
        throws DerivativeException, EventException, ConvergenceException {
        final TestEventHandler testEventHandler = new TestEventHandler();
        final EventState eventState = new EventState(testEventHandler, 0.1, 0.1, 10);
        final StepInterpolator interpolator = new StepInterpolator();
        final double h = 0.1;
        interpolator.setIntegratorStep(h);
        interpolator.setInterpolatedTime(interpolator.getCurrentTime());
        interpolator.setInterpolatedState(new double[1]);
        interpolator.setInterpolatedState(0, 1.0);
        final double t1 = interpolator.getCurrentTime() + h;
        final int n = (int) Math.ceil((t1 - eventState.getT0()) / eventState.getMaxCheckInterval());
        assertTrue(n > 0);
        for (int i = 0; i < n; ++i) {
            interpolator.setInterpolatedTime(eventState.getT0() + h * (i + 1) / n);
            final double g = testEventHandler.g(eventState.getT0() + h * (i + 1) / n, interpolator.getInterpolatedState());
            final boolean eventTriggered = eventState.evaluateStep(interpolator);
            if (i == 0) {
                assertEquals(g, eventState.getG0(), 0.0);
                assertFalse(eventState.isPendingEvent());
                assertNull(eventState.getPendingEventTime());
                assertNull(eventState.getPreviousEventTime());
                assertFalse(eventState.isForward());
                assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
            } else {
                if (i % 2 == 0) {
                    assertTrue(g > 0);
                    assertTrue(eventTriggered);
                    assertEquals(i % 2, eventState.getIncreasing());
                    assertEquals(eventState.getT0() + h * (i + 1) / n, eventState.getPendingEventTime(), 0.0);
                    assertFalse(eventState.isPendingEventBeforeStep());
                    assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
                } else {
                    assertFalse(g > 0);
                    assertFalse(eventTriggered);
                    assertFalse(eventState.isPendingEventBeforeStep());
                    assertEquals(EventHandler.TRIGGER, eventState.getNextAction());
                    // The event should be triggered at the previous event time,
                    // but since we cannot directly access it, we check that the next step
                    // accepts the step, which is only possible if an event was triggered.
                    interpolator.setInterpolatedTime(eventState.getT0() + h * (i + 2) / n);
                    final boolean stepAccepted = eventState.stepAccepted(interpolator.getCurrentTime(), interpolator.getInterpolatedState());
                    assertTrue(stepAccepted);
                }
            }
        }
    }

    /** Test method for {@link EventState#getEventTime()}. */
    @Test
    public void testEventStateGetEventTime() {
        final TestEventHandler testEventHandler = new TestEventHandler();
        final EventState eventState = new EventState(testEventHandler, 0.1, 0.1, 10);
        final StepInterpolator interpolator = new StepInterpolator();
        final double h = 0.1;
        interpolator.setIntegratorStep(h);
        interpolator.setInterpolatedTime(0.0);
        interpolator.setInterpolatedState(new double[1]);
        interpolator.setInterpolatedState(0, 1.0);
        final double t1 = 1.5;
        interpolator.setInterpolatedTime(t1);
        eventState.evaluateStep(interpolator);
        assertEquals(t1, eventState.getPendingEventTime(), 0.0);
        assertNull(eventState.getEventTime());
        eventState.stepAccepted(t1, interpolator.getInterpolatedState());
        assertEquals(t1, eventState.getEventTime(), 0.0);
    }

    /** Test method for {@link EventState#stepAccepted(double, double[])}. */
    @Test
    public void testEventStateStepAccepted()
        throws EventException {
        final TestEventHandler testEventHandler = new TestEventHandler();
        final EventState eventState = new EventState(testEventHandler, 0.1, 0.1, 10);
        final StepInterpolator interpolator = new StepInterpolator();
        final double t = 1.5;
        final double[] y = new double[1];
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(testEventHandler.g(t, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(t, eventState.getPreviousEventTime(), 0.0);
        assertTrue(eventState.isForward());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /** Test method for {@link EventState#stop()}. */
    @Test
    public void testEventStateStop() {
        final TestEventHandler testEventHandler = new TestEventHandler();
        final EventState eventState = new EventState(testEventHandler, 0.1, 0.1, 10);
        assertFalse(eventState.stop());
        final DiscreteEvent discreteEvent = new AbstractEventhandler() {
            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                eventState.setNextAction(EventHandler.STOP);
            }
        };
        eventState.setEventHandler(discreteEvent);
        assertTrue(eventState.stop());
    }

    /** Test method for {@link EventState#reset(double, double[])}. */
    @Test
    public void testEventStateReset()
        throws EventException {
        final TestEventHandler testEventHandler = new TestEventHandler();
        final EventState eventState = new EventState(testEventHandler, 0.1, 0.1, 10);
        final double t = 1.5;
        final double[] y = new double[1];
        final ContinuousEvent continuousEvent = new AbstractEventhandler() {
            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                eventState.setNextAction(EventHandler.RESET_STATE);
            }
        };
        eventState.setEventHandler(continuousEvent);
        eventState.stepAccepted(t, y);
        eventState.reset(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(testEventHandler.g(t, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(t, eventState.getPreviousEventTime(), 0.0);
        assertTrue(eventState.isForward());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        final SampleEvent sampleEvent = new SampleEventEventHandler(t);
        eventState.setEventHandler(sampleEvent);
        try {
            eventState.stepAccepted(t, y);
            fail("An event was expected but not triggered.");
        } catch (SampleEvent e) {
            // expected
        }
        eventState.reset(t, y);
        assertTrue(eventState.isPendingEvent());
        assertEquals(t, eventState.getPendingEventTime(), 0.0);
        assertFalse(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        try {
            eventState.stepAccepted(t, y);
            fail("An event was expected but not triggered.");
        } catch (SampleEvent e) {
            // expected
        }
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

}