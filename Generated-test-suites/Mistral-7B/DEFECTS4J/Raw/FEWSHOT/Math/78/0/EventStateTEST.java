package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler mockHandler = mock(EventHandler.class);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 20);
        assertEquals(mockHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(1e-6, eventState.getConvergence(), 0.0);
        assertEquals(20, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.stop());
        assertFalse(eventState.reset(0.0, new double[]{}));
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);

        when(mockHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockInterpolator.getInterpolatedState()).thenReturn(new double[0]);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 20);
        double[] yStart = new double[1];
        eventState.reinitializeBegin(0.0, yStart);
        assertEquals(1.0, eventState.g0, 0.0);
        assertEquals(true, eventState.g0Positive);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);

        when(mockHandler.g(0.0, new double[0])).thenThrow(new EventException());

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 20);
        double[] yStart = new double[1];
        eventState.reinitializeBegin(0.0, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);

        when(mockHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockHandler.g(1.0, new double[0])).thenReturn(-1.0);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(0.5);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 20);

        boolean result = eventState.evaluateStep(mockInterpolator);
        assertTrue(result);
        assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
        assertEquals(-1.0, eventState.g0, 0.0);
        assertEquals(false, eventState.g0Positive);
        assertEquals(EventAction.EVENT_OCCURRED, eventState.nextAction);

        when(mockHandler.eventOccurred(eventState.getPendingEventTime(), new double[0], true)).thenReturn(EventAction.RESET_STATE);
        result = eventState.evaluateStep(mockInterpolator);
        assertTrue(result);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        when(mockHandler.eventOccurred(eventState.getPendingEventTime(), new double[0], false)).thenReturn(EventAction.RESET_DERIVATIVES);
        result = eventState.evaluateStep(mockInterpolator);
        assertTrue(result);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertTrue(eventState.reset(0.0, new double[1]));

        when(mockHandler.g(0.0, new double[0])).thenThrow(new FunctionEvaluationException());
        result = eventState.evaluateStep(mockInterpolator);
        assertFalse(result);

        when(mockHandler.g(1.0, new double[0])).thenThrow(new EventException());
        result = eventState.evaluateStep(mockInterpolator);
        assertFalse(result);

        when(mockHandler.g(0.5, new double[0])).thenThrow(new ConvergenceException());
        result = eventState.evaluateStep(mockInterpolator);
        assertFalse(result);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);

        when(mockHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockHandler.g(1.0, new double[0])).thenReturn(-1.0);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 20);
        eventState.evaluateStep(mockInterpolator);
        double[] y = new double[1];
        eventState.stepAccepted(0.5, y);
        assertEquals(0.5, eventState.t0, 0.0);
        assertEquals(-1.0, eventState.g0, 0.0);
        assertEquals(false, eventState.g0Positive);
        assertEquals(EventAction.CONTINUE, eventState.nextAction);
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);

        when(mockHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockHandler.g(1.0, new double[0])).thenReturn(-1.0);
        when(mockHandler.resetState(0.0, new double[0])).thenReturn();

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 20);
        eventState.evaluateStep(mockInterpolator);
        eventState.stepAccepted(0.5, new double[1]);
        eventState.reset(0.0, new double[1]);
        assertEquals(1.0, eventState.g0, 0.0);
        assertEquals(true, eventState.g0Positive);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

}