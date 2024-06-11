package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.ode.*;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.*;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler mockEventHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public void resetState(double t, double[] y) {
                // empty implementation
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // empty implementation
            }
        };

        double maxCheckInterval = 0.1;
        double convergence = 1e-6;
        int maxIterationCount = 10;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        // test getters
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, eventState.getConvergence(), 0.0);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0(), 0.0);
        assertEquals(Double.NaN, eventState.getG0(), 0.0);
        assertEquals(true, eventState.isG0Positive());
        assertEquals(false, eventState.hasPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 0.0);
        assertEquals(Double.NaN, eventState.getPreviousEventTime(), 0.0);
        assertEquals(true, eventState.isForwardIntegration());
        assertEquals(UnsupportedOperationException.class, eventState.getVariationDirection());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // test reinitializeBegin
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        try {
            eventState.reinitializeBegin(t0, y0);
            assertEquals(t0, eventState.getT0(), 0.0);
            assertEquals(0.0, eventState.getG0(), 0.0);
            assertEquals(true, eventState.isG0Positive());
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        // test evaluateStep
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.1;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{1.0};
            }

            @Override
            public void setInterpolatedTime(double time) {
                // empty implementation
            }
        };

        try {
            boolean eventTriggered = eventState.evaluateStep(interpolator);
            assertFalse(eventTriggered);
        } catch (DerivativeException e) {
            fail("Unexpected DerivativeException in test");
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException in test");
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        // test getEventTime
        eventState.setPendingEventTime(0.1);
        assertEquals(0.1, eventState.getEventTime(), 0.0);

        // test stepAccepted
        double t1 = 0.1;
        double[] y1 = new double[]{1.0};
        try {
            eventState.stepAccepted(t1, y1);
            assertEquals(t1, eventState.getT0(), 0.0);
            assertEquals(0.0, eventState.getG0(), 0.0);
            assertEquals(true, eventState.isG0Positive());
            assertEquals(false, eventState.hasPendingEvent());
            assertEquals(Double.NaN, eventState.getPendingEventTime(), 0.0);
            assertEquals(t1, eventState.getPreviousEventTime(), 0.0);
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        // test stop
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());

        // test reset
        double t2 = 0.2;
        double[] y2 = new double[]{2.0};
        try {
            boolean reset = eventState.reset(t2, y2);
            assertEquals(false, reset);
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        eventState.setNextAction(EventHandler.RESET_STATE);
        try {
            reset = eventState.reset(t2, y2);
            assertTrue(reset);
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        assertEquals(0.0, eventState.getG0(), 0.0);

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        try {
            reset = eventState.reset(t2, y2);
            assertTrue(reset);
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        try {
            UnivariateRealFunction f = eventState.getVariationDirection();
            fail("Unexpected State object returned");
        } catch (UnsupportedOperationException e) {
            // expected
        }

    }

    @Test
    public void testEventStateWithSignChange() {
        EventHandler mockEventHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                if (y[0] > 1.0) {
                    return -1.0;
                } else {
                    return 1.0;
                }
            }

            @Override
            public void resetState(double t, double[] y) {
                // empty implementation
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // empty implementation
            }
        };

        double maxCheckInterval = 0.1;
        double convergence = 1e-6;
        int maxIterationCount = 10;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        double t0 = 0.0;
        double[] y0 = new double[]{0.5};
        try {
            eventState.reinitializeBegin(t0, y0);
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.1;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{1.5};
            }

            @Override
            public void setInterpolatedTime(double time) {
                // empty implementation
            }
        };

        try {
            boolean eventTriggered = eventState.evaluateStep(interpolator);
            assertTrue(eventTriggered);
        } catch (DerivativeException e) {
            fail("Unexpected DerivativeException in test");
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException in test");
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        // test getEventTime
        assertEquals(0.1, eventState.getEventTime(), 0.0);

        // test stepAccepted
        double t1 = 0.1;
        double[] y1 = new double[]{1.5};
        try {
            eventState.stepAccepted(t1, y1);
            assertEquals(t1, eventState.getT0(), 0.0);
            assertEquals(-1.0, eventState.getG0(), 0.0);
            assertFalse(eventState.isG0Positive());
            assertFalse(eventState.hasPendingEvent());
            assertEquals(Double.NaN, eventState.getPendingEventTime(), 0.0);
            assertEquals(t1, eventState.getPreviousEventTime(), 0.0);
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        // test reset
        double t2 = 0.2;
        double[] y2 = new double[]{2.0};
        try {
            boolean reset = eventState.reset(t2, y2);
            assertEquals(false, reset);
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        try {
            reset = eventState.reset(t2, y2);
            assertTrue(reset);
        } catch (EventException e) {
            fail("Unexpected EventException in test");
        }

        try {
            UnivariateRealFunction f = eventState.getVariationDirection();
            fail("Unexpected State object returned");
        } catch (UnsupportedOperationException e) {
            // expected
        }

    }

}