package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math.ode.events.EventException;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        double[] yStart = new double[1];
        double tStart = 0.5;

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0());
        assertEquals(mockHandler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval());
        assertEquals(0.001, eventState.getConvergence());
        assertEquals(10, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithEventHandlerException() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenThrow(new EventException("Exception message"));

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        double[] yStart = new double[1];
        double tStart = 0.5;

        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenThrow(new DerivativeException("Exception message"));

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        eventState.evaluateStep(mockInterpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventHandlerException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenThrow(new EventException("Exception message"));

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        eventState.evaluateStep(mockInterpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(true);

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        when(f.value(anyDouble())).thenThrow(new ConvergenceException("Exception message"));

        BrentSolver solver = mock(BrentSolver.class);
        when(solver.solve(f, anyDouble(), anyDouble())).thenReturn(Double.NaN);

        eventState.evaluateStep(mockInterpolator);
    }

    @Test
    public void testEvaluateStepWithoutEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0);

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        eventState.evaluateStep(mockInterpolator);

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(true);

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        when(f.value(anyDouble())).thenReturn(0.5);

        BrentSolver solver = mock(BrentSolver.class);
        when(solver.solve(f, anyDouble(), anyDouble())).thenReturn(0.6);

        eventState.evaluateStep(mockInterpolator);

        assertTrue(eventState.isPendingEvent());
        assertEquals(0.6, eventState.getPendingEventTime(), 0.001);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(false);

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        eventState.stepAccepted(1.1, new double[1]);

        assertEquals(1.1, eventState.getT0());
        assertEquals(1.0, eventState.getG0(), 0.001);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertTrue(eventState.isG0Positive());
    }

    @Test
    public void testStepAcceptedWithEventOccurred() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(true);

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        eventState.stepAccepted(1.1, new double[1]);

        assertEquals(1.1, eventState.getT0());
        assertEquals(1.0, eventState.getG0(), 0.001);
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.1, eventState.getPendingEventTime(), 0.001);
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithEventHandlerException() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenThrow(new EventException("Exception message"));

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.0);

        eventState.stepAccepted(1.1, new double[1]);
    }

    @Test
    public void testStop() {
        EventHandler mockHandler = mock(EventHandler.class);

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);
        eventState.setNextAction(EventHandler.STOP);

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() {
        EventHandler mockHandler = mock(EventHandler.class);

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);
        double t = 1.0;
        double[] y = new double[1];

        eventState.reset(t, y);

        assertEquals(t, eventState.getT0());
        assertEquals(y, eventState.getY());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testResetWithEventHandlerException() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.resetState(anyDouble(), anyArray())).thenThrow(new EventException("Exception message"));

        EventState eventState = new EventState(mockHandler, 0.001, 0.001, 10);

        double t = 1.0;
        double[] y = new double[1];

        eventState.reset(t, y);
    }

}