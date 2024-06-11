package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    private EventHandler handler;
    private EventState eventState;

    /**
     * Setup method for the test.
     * @throws FunctionEvaluationException when initializing the event handler
     */
    @Before
    public void setUp() throws FunctionEvaluationException {
        handler = new MockEventHandler();
        eventState = new EventState(handler, 0.001, 1e-6, 100);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(eventState);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 1e-6);
        assertEquals(1e-6, eventState.getConvergence(), 1e-6);
        assertEquals(100, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0(), 1e-6);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-6);
        assertEquals(Double.NaN, eventState.getPreviousEventTime(), 1e-6);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test {@link EventState#reinitializeBegin(double, double[])}.
     * @throws EventException when evaluating the event handler
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        double t = 0.0;
        double[] y = new double[] { 1.0 };
        eventState.reinitializeBegin(t, y);
        assertEquals(t, eventState.getT0(), 1e-6);
        assertEquals(handler.g(t, y), eventState.getG0(), 1e-6);
        assertTrue(eventState.isG0Positive());
    }

    /**
     * Test {@link EventState#evaluateStep(StepInterpolator)}.
     * @throws DerivativeException when interpolating the switching function
     * @throws EventException when evaluating the event handler
     * @throws ConvergenceException when localizing the event
     */
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.1);
        interpolator.setInterpolatedState(new double[] { 1.0 });
        double t1 = 1.0;

        // Test event at the beginning of the step
        interpolator.setInterpolatedTime(0.0);
        eventState.evaluateStep(interpolator);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-6);

        // Test event in the middle of the step
        interpolator.setInterpolatedTime(0.5);
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(0.5, eventState.getPendingEventTime(), 1e-6);

        // Test event at the end of the step
        interpolator.setInterpolatedTime(1.0);
        eventState.evaluateStep(interpolator);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-6);

        // Test event with convergence issue
        handler = new MockEventHandlerWithConvergenceIssue();
        eventState = new EventState(handler, 0.001, 1e-6, 100);
        interpolator.setInterpolatedState(new double[] { 1.0 });
        interpolator.setInterpolatedTime(0.5);
        try {
            eventState.evaluateStep(interpolator);
            fail();
        } catch (ConvergenceException e) {
            // expected
        }

    }

    /**
     * Test {@link EventState#getEventTime()}.
     */
    @Test
    public void testGetEventTime() {
        double t = 1.0;
        double[] y = new double[] { 1.0 };
        double eventTime = 1.5;

        eventState.stepAccepted(t, y);
        eventState.setPendingEventTime(eventTime);

        assertEquals(eventTime, eventState.getEventTime(), 1e-6);
    }

    /**
     * Test {@link EventState#stepAccepted(double, double[])}.
     * @throws EventException when evaluating the event handler
     */
    @Test
    public void testStepAccepted() throws EventException {
        double t = 1.0;
        double[] y = new double[] { 1.0, 2.0 };
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 1e-6);
        assertEquals(handler.g(t, y), eventState.getG0(), 1e-6);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-6);
    }

    /**
     * Test {@link EventState#stop()}.
     */
    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    /**
     * Test {@link EventState#reset(double, double[])}.
     * @throws EventException when resetting the state
     */
    @Test
    public void testReset() throws EventException {
        double t = 1.0;
        double[] y = new double[] { 1.0, 2.0 };
        eventState.setNextAction(EventHandler.RESET_STATE);
        assertTrue(eventState.reset(t, y));
        assertEquals(Double.NaN, eventState.getT0(), 1e-6);
        assertEquals(Double.NaN, eventState.getG0(), 1e-6);
        assertFalse(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-6);
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertTrue(eventState.reset(t, y));
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

}

class MockEventHandler implements EventHandler {

    private boolean triggered;

    @Override
    public double g(double t, double[] y) throws EventException {
        double value = BesselFunctions.sin(t);
        if (value < 0) {
            triggered = true;
        }
        return value;
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean increasing) {
        // empty implementation
    }

    @Override
    public void resetState(double t, double[] y) throws EventException {
        // empty implementation
    }

}

class MockEventHandlerWithConvergenceIssue extends MockEventHandler {

    @Override
    public double g(double t, double[] y) throws EventException {
        double value = BesselFunctions.sin(t);
        if (Math.abs(value) < 1e-8) {
            throw new FunctionEvaluationException(
                "Cannot evaluate g with the current input", t);
        }
        return value;
    }

}