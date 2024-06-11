package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler dummyHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0.0;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };

        EventState eventState = new EventState(dummyHandler, 0.001, 0.001, 10);

        assertEquals(dummyHandler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().g(eventState.getT0(), eventState.getYStart()));
        assertTrue(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler dummyHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return Math.sin(t);
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };

        EventState eventState = new EventState(dummyHandler, 0.001, 0.001, 10);

        double[] yStart = new double[1];
        eventState.reinitializeBegin(1.0, yStart);

        assertEquals(Math.sin(1.0), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());

        // test invalid reinitialization
        try {
            eventState.reinitializeBegin(Double.NaN, yStart);
            fail();
        } catch (EventException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler dummyHandler = new EventHandler() {
            private double t;
            private double y;

            @Override
            public double g(double t, double[] y) {
                this.t = t;
                this.y = y[0];
                return Math.sin(t) - y;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                assertEquals(this.t, eventState.getPendingEventTime(), 0.0);
                assertEquals(this.y, eventState.getYStart()[0], 0.0);
                assertEquals(forward, eventState.isForward());
            }

            @Override
            public void resetState(double t, double[] y) {
                this.t = t;
                this.y = y[0];
            }
        };

        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return eventState.getT0() + 0.1;
            }

            @Override
            public double[] getInterpolatedState() {
                return eventState.getYStart().clone();
            }

            @Override
            public void setInterpolatedTime(double t) {
                eventState.setT(t);
            }
        };

        EventState eventState = new EventState(dummyHandler, 0.001, 0.001, 10);
        double[] yStart = new double[1];

        // test no event
        eventState.reinitializeBegin(1.0, yStart);
        assertFalse(eventState.evaluateStep(interpolator));

        // test event
        eventState.setT(1.5);
        eventState.setY(yStart[0] + 0.5);
        eventState.setG0(Math.sin(1.5) - (yStart[0] + 0.5));
        eventState.setG0Positive(Math.sin(1.5) - (yStart[0] + 0.5) > 0);
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(1.6);
        eventState.setPreviousEventTime(Double.NaN);
        eventState.setForward(true);
        eventState.setIncreasing(true);
        eventState.setNextAction(EventHandler.CONTINUE);

        assertEquals(1.6, eventState.getPendingEventTime(), 0.0);
        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(1.6, eventState.getPendingEventTime(), 0.0);

        // test event occurrence
        double[] yEnd = new double[1];
        eventState.stepAccepted(1.6, yEnd);
        assertEquals(1.6, eventState.getT0(), 0.0);
        assertEquals(Math.sin(1.6), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive() ^ eventState.isForward());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

        // test reset
        eventState.setT(1.7);
        eventState.setY(yEnd[0]);
        eventState.setNextAction(EventHandler.RESET_STATE);
        assertFalse(eventState.evaluateStep(interpolator));
        assertEquals(1.7, eventState.getT0(), 0.0);
        assertEquals(Math.sin(1.7), eventState.getG0(), 0.0);
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());

        // test reset derivatives
        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        eventState.reset(1.8, new double[1]);
        assertFalse(eventState.evaluateStep(interpolator));
        assertEquals(1.8, eventState.getT0(), 0.0);
        assertArrayEquals(new double[0], eventState.getYStart(), 0.0);
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

    @Test
    public void testStop() {
        EventHandler dummyHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0.0;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }

            @Override
            public int eventAction(double t, double[] y, boolean forward, double tStep) {
                return EventHandler.STOP;
            }
        };

        EventState eventState = new EventState(dummyHandler, 0.001, 0.001, 10);

        // test no stop
        assertFalse(eventState.stop());

        // test stop
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }
}