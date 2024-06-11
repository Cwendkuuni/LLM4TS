package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.interpolators.StepInterpolator;

import java.util.Arrays;

public class EventStateTest {

    @Test
    public void testNewInstance() {
        EventHandler mockHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        assertEquals(mockHandler, eventState.getEventHandler());
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(tStart, yStart), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.g(0.0, new double[]{1.0, 2.0})).thenThrow(new EventException("Test Exception"));
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.5);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        when(mockHandler.g(0.0, new double[]{1.0, 2.0})).thenReturn(1.0);
        when(mockHandler.g(1.0, new double[]{1.1, 2.1})).thenReturn(-1.0);
        when(mockHandler.g(1.1, new double[]{1.2, 2.2})).thenReturn(0.0);

        boolean result = eventState.evaluateStep(mockInterpolator);
        assertTrue(result);
        assertEquals(1.1, eventState.getPendingEventTime(), 0.0);
        assertTrue(eventState.hasPendingEvent());
    }

    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(1.5);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        when(mockHandler.g(0.0, new double[]{1.0, 2.0})).thenReturn(1.0);
        when(mockHandler.g(1.5, new double[]{1.1, 2.1})).thenReturn(1.0);

        boolean result = eventState.evaluateStep(mockInterpolator);
        assertFalse(result);
        assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        double t = 1.5;
        double[] y = new double[]{1.1, 2.1};
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertEquals(eventState.isG0Positive(), eventState.isG0PositiveAfterStep());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStepAcceptedWithEvent() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        double t = 1.5;
        double[] y = new double[]{1.1, 2.1};
        when(mockHandler.eventOccurred(1.5, new double[]{1.1, 2.1}, false)).thenReturn(EventHandler.STOP);
        eventState.stepAccepted(t, y);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        double t = 1.5;
        double[] y = new double[]{1.1, 2.1};
        when(mockHandler.resetState(t, y)).thenReturn(new double[]{3.1, 4.1});
        boolean result = eventState.reset(t, y);
        assertEquals(new double[]{3.1, 4.1}, Arrays.copyOf(eventState.getState(), eventState.getState().length));
        assertFalse(eventState.hasPendingEvent());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testResetWithNoReset() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockHandler, 1.0, 0.001, 10);
        double t = 1.5;
        double[] y = new double[]{1.1, 2.1};
        when(mockHandler.resetState(t, y)).thenReturn(null);
        boolean result = eventState.reset(t, y);
        assertFalse(result);
    }

}