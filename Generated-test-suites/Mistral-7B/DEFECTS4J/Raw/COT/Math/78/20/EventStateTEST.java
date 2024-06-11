package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

/**
 * JUnit 4 test cases for {@link EventState} class.
 */
public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 1.0e-10, 100);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 1e-7);
        assertEquals(1.0e-10, eventState.getConvergence(), 1e-11);
        assertEquals(100, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertFalse(eventState.hasPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEvaluateStepWithEventBeforeStep() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                return BesselFunctions.besselJZero(0.5).getValue() * Math.sin(t);
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.1, 1.0 });
        interpolator.addEvaluation(1.0, new double[] { 2.1, 2.0 });
        interpolator.addEvaluation(2.0, new double[] { 3.1, 3.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.1, 1.0 });
        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(1.5, eventState.getPendingEventTime(), 1e-12);
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
    }

    @Test
    public void testEvaluateStepWithEventInsideStep() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                return Math.sin(t) * Math.cos(Math.PI * t);
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.addEvaluation(1.0, new double[] { 2.0, 2.0 });
        interpolator.addEvaluation(1.5, new double[] { 3.0, 3.0 });
        interpolator.addEvaluation(2.0, new double[] { 4.0, 4.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 1.0, 1.0 });
        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(1.7320508075688772, eventState.getPendingEventTime(), 1e-12);
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
    }

    @Test
    public void testEvaluateStepWithEventAfterStep() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                return Math.exp(-t) * Math.sin(Math.PI * t);
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.addEvaluation(1.0, new double[] { 0.1, 1.1 });
        interpolator.addEvaluation(2.0, new double[] { 0.2, 1.2 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.0, 1.0 });
        assertFalse(eventState.evaluateStep(interpolator));
    }

    @Test
    public void testEvaluateStepWithEventInsideStepAndWrongDirection() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                return Math.sin(t) * Math.cos(Math.PI * t);
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.addEvaluation(1.0, new double[] { 2.0, 2.0 });
        interpolator.addEvaluation(1.5, new double[] { 3.0, 3.0 });
        interpolator.addEvaluation(2.0, new double[] { 4.0, 4.0 });
        interpolator.setForward(false);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 1.0, 1.0 });
        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(0.2679491924311227, eventState.getPendingEventTime(), 1e-12);
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                throw new DerivativeException("test");
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.0, 1.0 });
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluateStepWithFunctionEvaluationException() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                throw new FunctionEvaluationException("test");
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.0, 1.0 });
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) throws EventException {
                throw new EventException("test");
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.0, 1.0 });
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testEvaluateStepWithoutEvent() throws Exception {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.addEvaluation(1.0, new double[] { 1.0, 1.0 });
        interpolator.addEvaluation(2.0, new double[] { 2.0, 1.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.0, 1.0 });
        assertFalse(eventState.evaluateStep(interpolator));
    }

    @Test
    public void testResetState() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                return y[0];
            }

            @Override
            public void resetState(double t, double[] y) {
                y[0] = 1.0;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.addEvaluation(1.0, new double[] { 2.0, 2.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.0, 1.0 });
        eventState.evaluateStep(interpolator);
        eventState.reset(t1, new double[] { 0.0, 1.0 });
        assertEquals(1.0, eventState.getG0(), 1e-12);
    }

    @Test(expected = EventException.class)
    public void testResetStateWithException() throws Exception {
        EventHandler handler = new MockEventHandler() {
            @Override
            public void resetState(double t, double[] y) {
                throw new EventException("test");
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1.0e-12, 100);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluation(0.0, new double[] { 0.0, 1.0 });
        interpolator.addEvaluation(1.0, new double[] { 2.0, 2.0 });
        interpolator.setForward(true);
        double t1 = interpolator.getCurrentTime();
        eventState.reinitializeBegin(t1, new double[] { 0.0, 1.0 });
        eventState.evaluateStep(interpolator);
        eventState.reset(t1, new double[] { 0.0, 1.0 });
    }
}