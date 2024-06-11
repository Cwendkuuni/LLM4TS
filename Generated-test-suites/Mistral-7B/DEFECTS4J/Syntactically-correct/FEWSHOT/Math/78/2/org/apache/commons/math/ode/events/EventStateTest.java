package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math.ode.sampling.RungeKuttaFehlbergIntegrator;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testInitialState() {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.001, 0.001, 50);
        assertTrue(Double.isNaN(state.getT0()));
        assertTrue(Double.isNaN(state.getG0()));
        assertTrue(state.isG0Positive());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.hasPendingEventTime());
        assertFalse(state.hasPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double tStart = 1.0;
        double[] yStart = new double[] {1.1, 2.2, 3.3};
        EventState state = new EventState(handler, 0.001, 0.001, 50);
        state.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, state.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double tStart = 1.0;
        double[] yStart = new double[] {1.1, 2.2, 3.3};
        EventState state = new EventState(handler, 0.001, 0.001, 50);
        when(handler.g(tStart, yStart)).thenThrow(new EventException("Exception"));
        state.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaFehlbergIntegrator(5, 5);
        double t1 = 1.5;
        double h = 0.1;
        EventState state = new EventState(handler, 0.001, 0.001, 50);
        state.reinitializeBegin(1.4, new double[] {1.1, 2.2, 3.3});

        // No event during the step
        interpolator.setIntegratorState(state);
        assertFalse(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(Double.NaN, state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.hasPendingEventTime());
        assertFalse(state.hasPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // Event during the step
        when(handler.g(1.5, new double[] {1.1, 2.2, 3.3})).thenReturn(0.0);
        interpolator.setIntegratorState(state);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(0.0, state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertTrue(state.hasPendingEvent());
        assertEquals(1.5, state.getPendingEventTime(), 0.0);
        assertFalse(state.hasPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());

        // Event during the step with previous event
        when(handler.g(1.5, new double[] {1.1, 2.2, 3.3})).thenReturn(0.0);
        state.setPreviousEventTime(1.45);
        interpolator.setIntegratorState(state);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(0.0, state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertTrue(state.hasPendingEvent());
        assertEquals(1.5, state.getPendingEventTime(), 0.0);
        assertTrue(state.hasPreviousEventTime());
        assertEquals(1.45, state.getPreviousEventTime(), 0.0);
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaFehlbergIntegrator(5, 5);
        double t1 = 1.5;
        double h = 0.1;
        EventState state = new EventState(handler, 0.001, 0.001, 50);
        state.reinitializeBegin(1.4, new double[] {1.1, 2.2, 3.3});

        // DerivativeException during the step
        when(handler.g(1.5, new double[] {1.1, 2.2, 3.3})).thenThrow(new DerivativeException("Exception"));
        interpolator.setIntegratorState(state);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(Double.NaN, state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.hasPendingEventTime());
        assertFalse(state.hasPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaFehlbergIntegrator(5, 5);
        double t1 = 1.5;
        double h = 0.1;
        EventState state = new EventState(handler, 0.001, 0.001, 50);
        state.reinitializeBegin(1.4, new double[] {1.1, 2.2, 3.3});

        // EventException during the step
        when(handler.g(1.5, new double[] {1.1, 2.2, 3.3})).thenThrow(new EventException("Exception"));
        interpolator.setIntegratorState(state);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(Double.NaN, state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.hasPendingEventTime());
        assertFalse(state.hasPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaFehlbergIntegrator(5, 5);
        double t1 = 1.5;
        double h = 0.1;
        EventState state = new EventState(handler, 0.001, 0.001, 50);
        state.reinitializeBegin(1.4, new double[] {1.1, 2.2, 3.3});

        // ConvergenceException during the step
        when(handler.g(1.5, new double[] {1.1, 2.2, 3.3})).thenThrow(new ConvergenceException("Exception"));
        interpolator.setIntegratorState(state);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(Double.NaN, state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.hasPendingEventTime());
        assertFalse(state.hasPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

}