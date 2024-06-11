package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math.ode.events.EventException;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(1.0E-10, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertFalse(Double.isFinite(eventState.getT0()));
        assertFalse(Double.isFinite(eventState.getG0()));
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isPreviousEventTimeSet());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double[] state = new double[1];
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        double tStart = 1.0;
        double[] yStart = {1.0};
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithEventException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double[] state = new double[1];
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        double tStart = 1.0;
        double[] yStart = {1.0};
        when(handler.g(anyDouble(), anyDouble())).thenThrow(new EventException("test"));
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);

        when(handler.g(anyDouble(), anyDouble())).thenReturn(1.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[0]);

        double t1 = 2.0;
        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
        assertEquals(1.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isPreviousEventTimeSet());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        when(handler.g(anyDouble(), anyDouble())).thenReturn(-1.0);
        result = eventState.evaluateStep(interpolator);
        assertTrue(result);
        assertTrue(eventState.isPendingEvent());
        double pendingEventTime = eventState.getPendingEventTime();
        assertEquals(2.0, pendingEventTime, 0.0);
        assertTrue(eventState.isPreviousEventTimeSet());
        assertEquals(1.0, eventState.getPreviousEventTime(), 0.0);
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        when(handler.g(anyDouble(), anyDouble())).thenReturn(-1.0);
        when(handler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.STOP);
        result = eventState.evaluateStep(interpolator);
        assertTrue(result);
        assertTrue(eventState.isPendingEvent());
        assertEquals(2.0, eventState.getPendingEventTime(), 0.0);
        assertTrue(eventState.isPreviousEventTimeSet());
        assertEquals(1.0, eventState.getPreviousEventTime(), 0.0);
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.STOP, eventState.getNextAction());

        when(handler.g(anyDouble(), anyDouble())).thenReturn(1.0);
        result = eventState.evaluateStep(interpolator);
        assertFalse(result);
        assertEquals(1.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isPreviousEventTimeSet());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);

        when(handler.g(anyDouble(), anyDouble())).thenThrow(new DerivativeException("test"));
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);

        when(handler.g(anyDouble(), anyDouble())).thenThrow(new EventException("test"));
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);

        when(handler.g(anyDouble(), anyDouble())).thenThrow(new ConvergenceException("test"));
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testGetEventTime() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        eventState.setPendingEventTime(2.0);
        assertEquals(2.0, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double[] state = new double[1];
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        double t = 2.0;
        double[] y = {1.0};
        eventState.stepAccepted(t, y);
        assertEquals(2.0, eventState.getT0(), 0.0);
        assertEquals(handler.g(2.0, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isPreviousEventTimeSet());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        when(handler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.STOP);
        eventState.stepAccepted(t, y);
        assertTrue(eventState.isStopped());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithEventException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double[] state = new double[1];
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        double t = 2.0;
        double[] y = {1.0};
        when(handler.g(anyDouble(), anyDouble())).thenThrow(new EventException("test"));
        eventState.stepAccepted(t, y);
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double t = 1.0;
        double[] y = new double[1];
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testResetWithEventResetState() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double[] state = new double[1];
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        double t = 1.0;
        double[] y = {1.0};
        when(handler.resetState(anyDouble(), anyDoubleArray())).thenReturn(true);
        eventState.reset(t, y);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testResetWithEventResetStateAndResetDerivatives() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double[] state = new double[1];
        EventState eventState = new EventState(handler, 1.0, 1.0E-10, 10);
        double t = 1.0;
        double[] y = {1.0};
        when(handler.resetState(anyDouble(), anyDoubleArray())).thenReturn(true);
        when(handler.resetDerivatives(anyDouble(), anyDoubleArray())).thenReturn(true);
        eventState.reset(t, y);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }
}