package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventHandlerTest;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.events.TriggerEvent;
import org.junit.Test;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        EventHandler handler = new StepHandler() {
            public int handleEvent(double t, double[] y, double[] dy, int type) {
                return TriggerEvent.CONTINUE;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);
        assertEquals("Unexpected handler", handler, eventState.getEventHandler());
        assertEquals("Unexpected maxCheckInterval", 0.001, eventState.getMaxCheckInterval(), 0);
        assertEquals("Unexpected convergence", 0.001, eventState.getConvergence(), 0);
        assertEquals("Unexpected maxIterationCount", 10, eventState.getMaxIterationCount());
        assertNull("Unexpected t0", eventState.getT0());
        assertNull("Unexpected g0", eventState.getG0());
        assertFalse("Unexpected g0Positive", eventState.isG0Positive());
        assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
        assertNull("Unexpected pendingEventTime", eventState.getPendingEventTime());
        assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
        assertTrue("Unexpected forward", eventState.isForward());
        assertFalse("Unexpected increasing", eventState.isIncreasing());
        assertEquals("Unexpected nextAction", EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method {@link EventState#reinitializeBegin(double, double[])}.
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new StepHandler() {
            public int handleEvent(double t, double[] y, double[] dy, int type) {
                return TriggerEvent.CONTINUE;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);
        double[] y = new double[] { 1.0, 2.0 };
        try {
            eventState.reinitializeBegin(0.0, y);
            assertEquals("Unexpected t0", 0.0, eventState.getT0(), 0);
            assertEquals("Unexpected g0", 1.0 * handler.g(0.0, y), eventState.getG0(), 0);
            assertTrue("Unexpected g0Positive", eventState.isG0Positive());
            assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
            assertNull("Unexpected pendingEventTime", eventState.getPendingEventTime());
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method {@link EventState#evaluateStep(StepInterpolator)}.
     */
    @Test
    public void testEvaluateStep() throws EventException, DerivativeException, ConvergenceException {
        EventHandler handler = new StepHandler() {
            public int handleEvent(double t, double[] y, double[] dy, int type) {
                return TriggerEvent.CONTINUE;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(4, 0.001);
        interpolator.setInterpolatedValues(0.0, new double[] { 1.0, 2.0 });
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double t) throws FunctionEvaluationException {
                try {
                    interpolator.setInterpolatedTime(t);
                    return handler.g(t, interpolator.getInterpolatedState());
                } catch (DerivativeException e) {
                    throw new FunctionEvaluationException(e, t);
                } catch (EventException e) {
                    throw new FunctionEvaluationException(e, t);
                }
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(0.001);
        solver.setMaximalIterationCount(10);
        try {
            boolean result = eventState.evaluateStep(interpolator);
            assertFalse("Unexpected result", result);
            assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
            assertNull("Unexpected pendingEventTime", eventState.getPendingEventTime());
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
        } catch (DerivativeException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        interpolator.setInterpolatedValues(0.1, new double[] { 1.1, 2.1 });
        try {
            boolean result = eventState.evaluateStep(interpolator);
            assertFalse("Unexpected result", result);
            assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
            assertNull("Unexpected pendingEventTime", eventState.getPendingEventTime());
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
        } catch (DerivativeException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        final double time = 0.15;
        final double[] state = new double[] { 1.5, 2.5 };
        interpolator.setInterpolatedValues(time, state);
        UnivariateRealFunction gFunction = new UnivariateRealFunction() {
            public double value(double t) throws FunctionEvaluationException {
                try {
                    interpolator.setInterpolatedTime(t);
                    return handler.g(t, interpolator.getInterpolatedState());
                } catch (DerivativeException e) {
                    throw new FunctionEvaluationException(e, t);
                } catch (EventException e) {
                    throw new FunctionEvaluationException(e, t);
                }
            }
        };
        try {
            double root = solver.solve(gFunction, 0.1, time);
            double expectedRoot = root;
            boolean result = eventState.evaluateStep(interpolator);
            assertTrue("Unexpected result", result);
            assertTrue("Unexpected pendingEvent", eventState.isPendingEvent());
            assertEquals("Unexpected pendingEventTime", expectedRoot, eventState.getPendingEventTime(), 0);
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
        } catch (DerivativeException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        interpolator.setInterpolatedValues(time + 0.001, state);
        try {
            double root = solver.solve(gFunction, 0.1, time + 0.001);
            double expectedRoot = root;
            boolean result = eventState.evaluateStep(interpolator);
            assertTrue("Unexpected result", result);
            assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
            assertEquals("Unexpected pendingEventTime", Double.NaN, eventState.getPendingEventTime(), 0);
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
        } catch (DerivativeException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method {@link EventState#getEventTime()}.
     */
    @Test
    public void testGetEventTime() {
        EventHandler handler = new StepHandler() {
            public int handleEvent(double t, double[] y, double[] dy, int type) {
                return TriggerEvent.CONTINUE;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);
        assertNull("Unexpected eventTime", eventState.getEventTime());

        eventState.setPendingEventTime(0.1);
        double eventTime = eventState.getEventTime();
        assertEquals("Unexpected eventTime", 0.1, eventTime, 0);
    }

    /**
     * Test method {@link EventState#stepAccepted(double, double[])}.
     */
    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = new StepHandler() {
            public int handleEvent(double t, double[] y, double[] dy, int type) {
                return TriggerEvent.CONTINUE;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);
        double[] y = new double[] { 1.0, 2.0 };
        try {
            eventState.stepAccepted(0.1, y);
            assertEquals("Unexpected t0", 0.1, eventState.getT0(), 0);
            assertEquals("Unexpected g0", handler.g(0.1, y), eventState.getG0(), 0);
            assertTrue("Unexpected g0Positive", eventState.isG0Positive());
            assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
            assertNull("Unexpected pendingEventTime", eventState.getPendingEventTime());
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
            int nextAction = eventState.getNextAction();
            assertEquals("Unexpected nextAction", EventHandler.CONTINUE, nextAction);
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(0.2);
        eventState.setPreviousEventTime(0.15);
        try {
            eventState.stepAccepted(0.2, y);
            assertTrue("Unexpected pendingEvent", eventState.isPendingEvent());
            assertEquals("Unexpected pendingEventTime", 0.2, eventState.getPendingEventTime(), 0);
            assertEquals("Unexpected previousEventTime", 0.15, eventState.getPreviousEventTime(), 0);
            int nextAction = eventState.getNextAction();
            assertFalse("Unexpected nextAction", (EventHandler.CONTINUE == nextAction));
            assertFalse("Unexpected nextAction", (EventHandler.RESET_STATE == nextAction));
            assertFalse("Unexpected nextAction", (EventHandler.RESET_DERIVATIVES == nextAction));
            assertFalse("Unexpected nextAction", (EventHandler.STOP == nextAction));
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method {@link EventState#stop()}.
     */
    @Test
    public void testStop() {
        EventHandler handler = new StepHandler() {
            public int handleEvent(double t, double[] y, double[] dy, int type) {
                return EventHandler.STOP;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);
        assertFalse("Unexpected stop", eventState.stop());

        eventState.setNextAction(EventHandler.STOP);
        assertTrue("Unexpected stop", eventState.stop());
    }

    /**
     * Test method {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testReset() throws EventException {
        EventHandler handler = new StepHandler() {
            public int handleEvent(double t, double[] y, double[] dy, int type) {
                return TriggerEvent.CONTINUE;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);
        double[] y = new double[] { 1.0, 2.0 };
        try {
            eventState.reset(0.1, y);
            assertEquals("Unexpected t", 0.1, eventState.getT0(), 0);
            assertEquals("Unexpected g0", handler.g(0.1, y), eventState.getG0(), 0);
            assertTrue("Unexpected g0Positive", eventState.isG0Positive());
            assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
            assertNull("Unexpected pendingEventTime", eventState.getPendingEventTime());
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
            int nextAction = eventState.getNextAction();
            assertEquals("Unexpected nextAction", EventHandler.CONTINUE, nextAction);
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(0.2);
        eventState.setPreviousEventTime(0.15);
        try {
            eventState.reset(0.2, y);
            assertFalse("Unexpected pendingEvent", eventState.isPendingEvent());
            assertNull("Unexpected pendingEventTime", eventState.getPendingEventTime());
            assertNull("Unexpected previousEventTime", eventState.getPreviousEventTime());
            int nextAction = eventState.getNextAction();
            assertFalse("Unexpected nextAction", (EventHandler.RESET_STATE == nextAction));
            assertFalse("Unexpected nextAction", (EventHandler.RESET_DERIVATIVES == nextAction));
            assertFalse("Unexpected nextAction", (EventHandler.STOP == nextAction));
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        eventState.setNextAction(EventHandler.RESET_STATE);
        eventState.reset(0.1, y);
        assertTrue("Unexpected resetState", eventState.isPendingEvent());
        assertTrue("Unexpected resetState", eventState.isPendingEvent());
    }

}