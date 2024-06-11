package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.solvers.BrentSolver;
import org.junit.Test;

/**
 * JUnit 4 test cases for EventState class.
 *
 */
public class EventStateTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        assertNotNull(eventState);
    }

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertNull(eventState.getEventHandler().getPreviousState());
        assertNull(eventState.getEventHandler().getEventTime());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test reinitializeBegin method.
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double[] state = new double[] {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(0.0, state);

        assertEquals(0.0, eventState.getT0(), 0.0);
        assertEquals(handler.g(0.0, state), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test evaluateStep method with no event.
     */
    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getInterpolatedState(double time) {
                return new double[] {time, 0.0};
            }

            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };

        assertTrue(eventState.evaluateStep(interpolator));

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test evaluateStep method with event.
     */
    @Test
    public void testEvaluateStepEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                return t * t;
            }
        };

        StepInterpolator interpolator = new StepInterpolator() {
            private double t;

            @Override
            public double getInterpolatedState(double time) {
                return new double[] {time, t};
            }

            @Override
            public double getInterpolatedTime() {
                return time;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public void setInterpolatedTime(double time) {
                this.t = time;
            }
        };

        interpolator.setInterpolatedTime(0.0);
        handler.setG(0.0, new double[] {0.0}, 0.0);
        handler.setG(0.1, new double[] {0.0}, 0.2);
        handler.setG(0.2, new double[] {0.0}, 0.1);
        handler.setG(0.3, new double[] {0.0}, 0.0);

        assertTrue(eventState.evaluateStep(interpolator));

        assertTrue(eventState.isPendingEvent());
        assertEquals(0.16, eventState.getPendingEventTime(), 0.001);
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
    }

    /**
     * Test getEventTime method with no event.
     */
    @Test
    public void testGetEventTimeNoEvent() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        assertNull(eventState.getEventTime());
    }

    /**
     * Test getEventTime method with event.
     */
    @Test
    public void testGetEventTimeEvent() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                return t * t;
            }
        };

        StepInterpolator interpolator = new StepInterpolator() {
            private double t;

            @Override
            public double getInterpolatedState(double time) {
                return new double[] {time, t};
            }

            @Override
            public double getInterpolatedTime() {
                return time;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public void setInterpolatedTime(double time) {
                this.t = time;
            }
        };

        interpolator.setInterpolatedTime(0.0);
        handler.setG(0.0, new double[] {0.0}, 0.0);
        handler.setG(0.1, new double[] {0.0}, 0.2);
        handler.setG(0.2, new double[] {0.0}, 0.1);
        handler.setG(0.3, new double[] {0.0}, 0.0);

        eventState.evaluateStep(interpolator);

        assertEquals(0.16, eventState.getEventTime(), 0.001);
    }

    /**
     * Test stepAccepted method.
     */
    @Test
    public void testStepAccepted() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        eventState.stepAccepted(1.0, new double[] {1.0, 1.0});

        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(handler.g(1.0, new double[] {1.0, 1.0}), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test stop method.
     */
    @Test
    public void testStop() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        eventState.setNextAction(EventHandler.STOP);

        assertTrue(eventState.stop());
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() throws EventException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double[] state = new double[] {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(0.0, state);
        eventState.setNextAction(EventHandler.RESET_STATE);
        eventState.reset(1.0, new double[] {1.1, 1.1, 1.1});

        assertNull(eventState.getEventHandler().getState());

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        eventState.reset(1.0, new double[] {1.1, 1.1, 1.1});

        assertEquals(0.0, eventState.getEventHandler().getDerivatives()[0], 0.0);
        assertEquals(0.0, eventState.getEventHandler().getDerivatives()[1], 0.0);
        assertEquals(0.0, eventState.getEventHandler().getDerivatives()[2], 0.0);
    }

    /**
     * Mock EventHandler class for testing.
     */
    public static class MockEventHandler implements EventHandler {

        private double[] state;
        private double[] previousState;
        private double eventTime;
        private int nextAction;

        @Override
        public void resetState(double time, double[] state) {
            this.state = state;
        }

        @Override
        public void resetDerivatives() {
            // empty
        }

        @Override
        public double g(double time, double[] state) throws EventException {
            if (previousState == null) {
                previousState = state;
            }
            double g = Math.signum(state[0] - state[1]);
            this.eventTime = time;
            this.nextAction = g > 0 ? EventHandler.EVENT_OCCURRED : EventHandler.CONTINUE;
            return g;
        }

        @Override
        public double eventOccurred(double time, double[] state, boolean forward) {
            return time + 0.1;
        }

        public void setEventHandlerState(double[] state) {
            this.state = state;
        }

        public void setEventHandlerPreviousState(double[] previousState) {
            this.previousState = previousState;
        }

        public void setEventHandlerEventTime(double eventTime) {
            this.eventTime = eventTime;
        }

        public void setNextAction(int nextAction) {
            this.nextAction = nextAction;
        }

        public double[] getState() {
            return state;
        }

        public double[] getPreviousState() {
            return previousState;
        }

        public double getEventTime() {
            return eventTime;
        }

        public boolean isG0Positive() {
            return nextAction > 0;
        }

    }

}