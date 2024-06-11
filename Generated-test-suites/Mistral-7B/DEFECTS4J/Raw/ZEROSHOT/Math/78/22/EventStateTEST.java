import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventStateTest {

    @Test
    public void testEventStateInitialization() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        Assert.assertSame(mockEventHandler, eventState.getEventHandler());
        Assert.assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(convergence, eventState.getConvergence(), 0.0);
        Assert.assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        Assert.assertEquals(Double.NaN, eventState.getT0(), 0.0);
        Assert.assertEquals(Double.NaN, eventState.getG0(), 0.0);
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.hasPendingEvent());
        Assert.assertEquals(Double.NaN, eventState.getPendingEventTime(), 0.0);
        Assert.assertEquals(Double.NaN, eventState.getPreviousEventTime(), 0.0);
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double tStart = 1.0;
        double[] yStart = new double[] {0.1, 0.2};

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        eventState.reinitializeBegin(tStart, yStart);

        verify(mockEventHandler, times(1)).g(eq(tStart), isA(double[].class));
        Assert.assertEquals(tStart, eventState.getT0(), 0.0);
        Assert.assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0.0);
        Assert.assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double tStart = 1.0;
        double[] yStart = new double[] {0.1, 0.2};

        doThrow(new EventException("Test exception"))
                .when(mockEventHandler)
                .g(eq(tStart), isA(double[].class));

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double t0 = 1.0;
        double g0 = 1.0;
        boolean g0Positive = true;
        double t1 = 2.0;
        boolean forward = true;
        boolean increasing = true;
        double t2 = 1.5;
        double g1 = -1.0;
        double pendingEventTime = 1.2;

        StepInterpolator stepInterpolator = mock(StepInterpolator.class);
        when(stepInterpolator.isForward()).thenReturn(forward);
        when(stepInterpolator.getCurrentTime()).thenReturn(t1);

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, new double[]{0.0, 0.0});

        verify(mockEventHandler, never()).eventOccurred(anyDouble(), any(double[].class), anyBoolean());
        verify(mockEventHandler, never()).resetState(anyDouble(), any(double[].class));

        eventState.evaluateStep(stepInterpolator);

        verify(mockEventHandler, never()).eventOccurred(anyDouble(), any(double[].class), anyBoolean());
        verify(mockEventHandler, never()).resetState(anyDouble(), any(double[].class));

        Assert.assertFalse(eventState.hasPendingEvent());

        when(mockEventHandler.g(eq(t1), isA(double[].class))).thenReturn(g1);

        eventState.evaluateStep(stepInterpolator);

        verify(mockEventHandler, times(1)).eventOccurred(eq(t2), any(double[].class), eq(increasing));
        verify(mockEventHandler, times(1)).resetState(eq(t2), any(double[].class));

        Assert.assertTrue(eventState.hasPendingEvent());
        Assert.assertEquals(pendingEventTime, eventState.getPendingEventTime(), 0.0);

        when(mockEventHandler.eventOccurred(anyDouble(), any(double[].class), anyBoolean())).thenReturn(EventHandler.CONTINUE);

        eventState.stepAccepted(t2, new double[]{0.0, 0.0});

        Assert.assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double t = 2.0;
        double[] y = new double[]{0.0, 0.0};

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(1.0, y);

        eventState.stepAccepted(t, y);

        Assert.assertEquals(t, eventState.getT0(), 0.0);
        Assert.assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.0);
        Assert.assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithException() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double t = 2.0;
        double[] y = new double[]{0.0, 0.0};

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(1.0, y);

        doThrow(new EventException("Test exception"))
                .when(mockEventHandler)
                .g(eq(t), isA(double[].class));

        eventState.stepAccepted(t, y);
    }

    @Test
    public void testHasPendingEvent() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        boolean hasPendingEvent = false;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        Assert.assertFalse(eventState.hasPendingEvent());

        eventState.setPendingEvent(true);

        Assert.assertTrue(eventState.hasPendingEvent());

        eventState.setPendingEvent(false);

        Assert.assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testGetPendingEventTime() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double pendingEventTime = 1.5;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        eventState.setPendingEventTime(pendingEventTime);

        Assert.assertEquals(pendingEventTime, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testGetPreviousEventTime() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double previousEventTime = 1.2;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        eventState.setPreviousEventTime(previousEventTime);

        Assert.assertEquals(previousEventTime, eventState.getPreviousEventTime(), 0.0);
    }

    @Test
    public void testIsForward() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        boolean forward = true;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        eventState.setForward(forward);

        Assert.assertEquals(forward, eventState.isForward());
    }

    @Test
    public void testIsIncreasing() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        boolean increasing = true;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        eventState.setIncreasing(increasing);

        Assert.assertEquals(increasing, eventState.isIncreasing());
    }

    @Test
    public void testGetNextAction() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        int nextAction = EventHandler.CONTINUE;

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        eventState.setNextAction(nextAction);

        Assert.assertEquals(nextAction, eventState.getNextAction());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double t = 2.0;
        double[] y = new double[]{0.0, 0.0};

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(1.0, y);
        eventState.stepAccepted(t, y);

        Assert.assertFalse(eventState.hasPendingEvent());

        eventState.reset(t, y);

        verify(mockEventHandler, never()).resetState(anyDouble(), any(double[].class));

        Assert.assertFalse(eventState.hasPendingEvent());

        when(mockEventHandler.resetState(eq(t), isA(double[].class))).thenReturn(EventHandler.CONTINUE);

        eventState.reset(t, y);

        verify(mockEventHandler, times(1)).resetState(eq(t), any(double[].class));

        Assert.assertFalse(eventState.hasPendingEvent());
    }

    @Test(expected = EventException.class)
    public void testResetWithException() throws EventException {
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        double t = 2.0;
        double[] y = new double[]{0.0, 0.0};

        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(1.0, y);
        eventState.stepAccepted(t, y);

        doThrow(new EventException("Test exception"))
                .when(mockEventHandler)
                .resetState(eq(t), isA(double[].class));

        eventState.reset(t, y);
    }
}