package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventException;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

public class EventStateTest {

    @Test
    public void testEventStateConstruction() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(1.0E-6, eventState.getConvergence(), 0.0);
        assertEquals(100, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(Arrays.toString(yStart), Arrays.toString(eventState.getYStart()), 0.0);
        assertEquals(handler, eventState.getEventHandler());
    }

    @Test
    public void testEvaluateStepWithNoEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.5);
        when(handler.g(1.5, (double[]) anyArray())).thenReturn(0.5);
        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
        assertEquals(1.5, eventState.getT0(), 0.0);
        assertEquals(0.5, eventState.getG0(), 0.0);
        assertEquals(true, eventState.isForward());
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.5);
        when(handler.g(1.5, (double[]) anyArray())).thenReturn(-1.0);
        when(handler.g(1.6, (double[]) anyArray())).thenReturn(0.5);
        ArgumentCaptor<UnivariateRealFunction> functionCaptor = ArgumentCaptor.forClass(UnivariateRealFunction.class);
        BrentSolver solver = mock(BrentSolver.class);
        when(solver.solve(functionCaptor.capture(), anyDouble(), anyDouble())).thenReturn(1.55);
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.55, eventState.getPendingEventTime(), 0.0);
        assertEquals(-1.0, functionCaptor.getValue().value(1.55), 0.0);
        assertEquals(0.5, functionCaptor.getValue().value(1.6), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double t = 1.5;
        double[] y = new double[]{1.0, 2.0, 3.0};
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        eventState.evaluateStep(mock(StepInterpolator.class));
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(Arrays.toString(y), Arrays.toString(eventState.getYStart()), 0.0);
        assertEquals(handler, eventState.getEventHandler());
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testStop() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double t = 1.5;
        double[] y = new double[]{1.0, 2.0, 3.0};
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        eventState.setNextAction(EventHandler.RESET_STATE);
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testResetWithDerivatives() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double t = 1.5;
        double[] y = new double[]{1.0, 2.0, 3.0};
        double[] yPrime = new double[]{1.1, 2.1, 3.1};
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        eventState.setNextAction(EventHandler.RESET_STATE_DERIVATIVES);
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(yPrime, eventState.getYStart());
    }

    @Test
    public void testStepAcceptedWithEvent() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double t = 1.5;
        double[] y = new double[]{1.0, 2.0, 3.0};
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        eventState.evaluateStep(mock(StepInterpolator.class));
        eventState.stepAccepted(t, y);
        assertFalse(eventState.isPendingEvent());
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(Arrays.toString(y), Arrays.toString(eventState.getYStart()), 0.0);
        assertEquals(handler, eventState.getEventHandler());
    }

    @Test
    public void testEvaluateStepWithException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenThrow(new DerivativeException());
        assertThrows(EventException.class, () -> eventState.evaluateStep(interpolator));
    }

    @Test
    public void testEventOccurredWithException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double t = 1.5;
        double[] y = new double[]{1.0, 2.0, 3.0};
        when(handler.eventOccurred(t, y, true)).thenThrow(new EventException());
        EventState eventState = new EventState(handler, 1.0, 1.0E-6, 100);
        eventState.stepAccepted(t, y);
        assertTrue(eventState.stop());
    }

}