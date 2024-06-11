import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        assertNotNull(eventState);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(50, eventState.getMaxIterationCount());
        assertTrue(Double.isNaN(eventState.getT0()));
        assertTrue(Double.isNaN(eventState.getG0()));
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        double tStart = 0.0;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        double tStart = Double.NaN;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(2.0);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            double t = ((Double) args[0]).doubleValue();
            double[] y = (double[]) args[1];
            handler.g(t, y);
            return null;
        }).when(handler).g(anyDouble(), anyArray());

        boolean result = eventState.evaluateStep(interpolator);
        assertTrue(result);
        assertTrue(eventState.isPendingEvent());
        assertNotNull(eventState.getPendingEventTime());
        verify(handler).g(anyDouble(), anyArray());
        verifyNoMoreInteractions(handler, interpolator);

        when(interpolator.getCurrentTime()).thenReturn(1.5);
        result = eventState.evaluateStep(interpolator);
        assertFalse(result);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenThrow(new DerivativeException());

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            double t = ((Double) args[0]).doubleValue();
            double[] y = (double[]) args[1];
            handler.g(t, y);
            return null;
        }).when(handler).g(anyDouble(), anyArray());

        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenThrow(new EventException());

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            double t = ((Double) args[0]).doubleValue();
            double[] y = (double[]) args[1];
            handler.g(t, y);
            return null;
        }).when(handler).g(anyDouble(), anyArray());

        eventState.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenThrow(new ConvergenceException());

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            double t = ((Double) args[0]).doubleValue();
            double[] y = (double[]) args[1];
            handler.g(t, y);
            return null;
        }).when(handler).g(anyDouble(), anyArray());

        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testGetEventTime() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        double pendingEventTime = 2.0;
        eventState.setPendingEventTime(pendingEventTime);
        assertEquals(pendingEventTime, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        double t = 2.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(handler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStop() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        double t = 2.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testResetWithResetDerivatives() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        double t = 2.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertTrue(eventState.reset(t, y));
    }

    @Test(expected = EventException.class)
    public void testResetWithException() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 50);
        double t = 2.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        eventState.setNextAction(EventHandler.RESET_STATE);
        when(handler.resetState(anyDouble(), anyArray()))
                .thenThrow(new EventException());
        eventState.reset(t, y);
    }

}