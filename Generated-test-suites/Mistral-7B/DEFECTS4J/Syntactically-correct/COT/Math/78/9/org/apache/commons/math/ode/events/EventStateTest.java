import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.StepInterpolator;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        Assert.assertSame(handler, state.getEventHandler());
        Assert.assertEquals(0.1, state.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(0.01, state.getConvergence(), 0.0);
        Assert.assertEquals(100, state.getMaxIterationCount());
        Assert.assertNull(state.getT0());
        Assert.assertNull(state.getG0());
        Assert.assertTrue(state.isG0Positive());
        Assert.assertFalse(state.isPendingEvent());
        Assert.assertNull(state.getPendingEventTime());
        Assert.assertNull(state.getPreviousEventTime());
        Assert.assertTrue(state.isForward());
        Assert.assertTrue(state.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        double tStart = 1.0;
        double[] yStart = new double[1];
        state.reinitializeBegin(tStart, yStart);
        Assert.assertEquals(tStart, state.getT0(), 0.0);
        Assert.assertNotNull(state.getG0(), 0.0);
        Assert.assertTrue(state.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithEventException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        double tStart = 1.0;
        double[] yStart = new double[1];
        when(handler.g(1.0, new double[1])).thenThrow(new EventException("Test exception"));
        state.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStepWithNoEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.2);
        when(handler.g(1.2, new double[1])).thenReturn(0.3);
        boolean result = state.evaluateStep(interpolator);
        Assert.assertFalse(result);
        Assert.assertFalse(state.isPendingEvent());
        Assert.assertNull(state.getPendingEventTime());
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.2);
        when(handler.g(1.2, new double[1])).thenReturn(-0.1);
        when(handler.g(1.15, new double[1])).thenReturn(0.1);
        boolean result = state.evaluateStep(interpolator);
        Assert.assertTrue(result);
        Assert.assertTrue(state.isPendingEvent());
        Assert.assertEquals(1.15, state.getPendingEventTime(), 0.0);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenThrow(new DerivativeException("Test exception"));
        state.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.2);
        when(handler.g(1.2, new double[1])).thenThrow(new EventException("Test exception"));
        state.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.2);
        when(handler.g(1.2, new double[1])).thenThrow(new ConvergenceException("Test exception"));
        state.evaluateStep(interpolator);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        double t = 1.3;
        double[] y = new double[1];
        state.stepAccepted(t, y);
        Assert.assertEquals(t, state.getT0(), 0.0);
        Assert.assertNotNull(state.getG0(), 0.0);
        Assert.assertTrue(state.isG0Positive());
        Assert.assertFalse(state.isPendingEvent());
        Assert.assertNull(state.getPendingEventTime());
        Assert.assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    @Test
    public void testStepAcceptedWithEvent() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        double t = 1.3;
        double[] y = new double[1];
        when(handler.eventOccurred(1.3, y, false)).thenReturn(EventHandler.RESET_DERIVATIVES);
        state.stepAccepted(t, y);
        Assert.assertEquals(t, state.getT0(), 0.0);
        Assert.assertNotNull(state.getG0(), 0.0);
        Assert.assertTrue(state.isG0Positive());
        Assert.assertFalse(state.isPendingEvent());
        Assert.assertNull(state.getPendingEventTime());
        Assert.assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithEventException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        double t = 1.3;
        double[] y = new double[1];
        when(handler.eventOccurred(1.3, y, false)).thenThrow(new EventException("Test exception"));
        state.stepAccepted(t, y);
    }

    @Test
    public void testStop() {
        EventState state = new EventState(mock(EventHandler.class), 0.1, 0.01, 100);
        state.setNextAction(EventHandler.STOP);
        Assert.assertTrue(state.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.2);
        state.setNextAction(EventHandler.RESET_STATE);
        double t = 1.4;
        double[] y = new double[1];
        Assert.assertTrue(state.reset(t, y));
        Assert.assertFalse(state.isPendingEvent());
        Assert.assertNull(state.getPendingEventTime());
        Assert.assertEquals(EventHandler.RESET_STATE, state.getNextAction());
    }

    @Test
    public void testResetWithDerivativesReset() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState state = new EventState(handler, 0.1, 0.01, 100);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.2);
        state.setNextAction(EventHandler.RESET_DERIVATIVES);
        double t = 1.4;
        double[] y = new double[1];
        Assert.assertTrue(state.reset(t, y));
        Assert.assertFalse(state.isPendingEvent());
        Assert.assertNull(state.getPendingEventTime());
        Assert.assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());
    }

}