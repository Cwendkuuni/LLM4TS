import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(0.1, eventState.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(0.01, eventState.getConvergence(), 0.0);
        Assert.assertEquals(100, eventState.getMaxIterationCount());
        Assert.assertNull(eventState.getT0());
        Assert.assertNull(eventState.getG0());
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertNull(eventState.getPendingEventTime());
        Assert.assertNull(eventState.getPreviousEventTime());
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        eventState.reinitializeBegin(tStart, yStart);
        Assert.assertEquals(tStart, eventState.getT0(), 0.0);
        Assert.assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0.0);
        Assert.assertTrue(eventState.isG0Positive());

        verify(mockEventHandler, times(1)).g(eq(tStart), eq(yStart));
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginHandlerException() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new EventException());
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(2.0);
        double t1 = 2.0;
        double[] yStart = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        boolean result = eventState.evaluateStep(mockInterpolator);
        Assert.assertFalse(result);

        verify(mockEventHandler, times(0)).eventOccurred(anyDouble(), anyArray(), anyBoolean());
        verify(mockEventHandler, times(0)).resetState(anyDouble(), anyArray());
        verify(mockInterpolator, times(1)).setInterpolatedTime(eq(2.0));
        verify(mockInterpolator, times(1)).getInterpolatedState();
        verify(mockEventHandler, times(1)).g(eq(2.0), anyArray());
        verifyNoMoreInteractions(mockEventHandler, mockInterpolator);

        // Test with a step that triggers an event
        when(mockInterpolator.getCurrentTime()).thenReturn(2.5);
        double t2 = 2.5;
        eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        result = eventState.evaluateStep(mockInterpolator);
        Assert.assertTrue(result);
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(2.5, eventState.getPendingEventTime(), 0.0);

        verify(mockEventHandler, times(1)).eventOccurred(eq(2.5), anyArray(), anyBoolean());
        verify(mockEventHandler, times(1)).resetState(eq(2.5), anyArray());
        verify(mockInterpolator, times(1)).setInterpolatedTime(eq(2.5));
        verify(mockInterpolator, times(1)).getInterpolatedState();
        verify(mockEventHandler, times(1)).g(eq(2.5), anyArray());
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(2.0);
        double t1 = 2.0;
        double[] yStart = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new DerivativeException());
        result = eventState.evaluateStep(mockInterpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(2.0);
        double t1 = 2.0;
        double[] yStart = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new EventException());
        result = eventState.evaluateStep(mockInterpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        StepInterpolator mockInterpolator = mock(StepInterpolator.class);
        when(mockInterpolator.isForward()).thenReturn(true);
        when(mockInterpolator.getCurrentTime()).thenReturn(2.0);
        double t1 = 2.0;
        double[] yStart = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new ConvergenceException());
        result = eventState.evaluateStep(mockInterpolator);
    }

    @Test
    public void testGetEventTime() {
        double pendingEventTime = 2.5;
        EventState eventState = new EventState();
        eventState.setPendingEventTime(pendingEventTime);
        Assert.assertEquals(pendingEventTime, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double t = 3.0;
        double[] y = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        eventState.stepAccepted(t, y);
        Assert.assertEquals(t, eventState.getT0(), 0.0);
        Assert.assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.0);
        Assert.assertTrue(eventState.isG0Positive());

        verify(mockEventHandler, times(1)).g(eq(t), eq(y));
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedHandlerException() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double t = 3.0;
        double[] y = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new EventException());
        eventState.stepAccepted(t, y);
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState();
        eventState.setNextAction(EventHandler.STOP);
        Assert.assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double t = 3.0;
        double[] y = new double[]{1.0, 2.0};
        EventState eventState = new EventState(mockEventHandler, 0.1, 0.01, 100);
        eventState.reset(t, y);
        Assert.assertFalse(eventState.isPendingEvent());

        verify(mockEventHandler, times(1)).resetState(eq(t), eq(y));
    }

}