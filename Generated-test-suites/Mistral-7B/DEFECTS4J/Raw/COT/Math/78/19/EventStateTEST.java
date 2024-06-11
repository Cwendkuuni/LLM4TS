import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);
        assertEquals(handlerMock, eventState.getEventHandler());
        assertEquals(0.1d, eventState.getMaxCheckInterval(), 0.001d);
        assertEquals(0.01d, eventState.getConvergence(), 0.0001d);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertNull(eventState.t0);
        assertNull(eventState.g0);
        assertFalse(eventState.g0Positive);
        assertFalse(eventState.pendingEvent);
        assertNull(eventState.pendingEventTime);
        assertNull(eventState.previousEventTime);
        assertTrue(eventState.forward);
        assertTrue(eventState.increasing);
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0d;
        double[] yStart = new double[1];
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        when(handlerMock.g(eq(0.0d), anyArray())).thenReturn(0.0d);

        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.t0, 0.001d);
        assertEquals(0.0d, eventState.g0, 0.0001d);
        assertTrue(eventState.g0Positive);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        double tStart = 0.0d;
        double[] yStart = new double[1];
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        when(handlerMock.g(eq(0.0d), anyArray())).thenThrow(new EventException());

        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        when(handlerMock.g(eq(0.0d), anyArray())).thenReturn(0.0d);
        when(interpolatorMock.getCurrentTime()).thenReturn(0.1d);
        when(interpolatorMock.isForward()).thenReturn(true);

        eventState.evaluateStep(interpolatorMock);
        assertFalse(eventState.pendingEvent);
        assertNull(eventState.pendingEventTime);

        when(handlerMock.g(eq(0.0d), anyArray())).thenReturn(-0.1d);
        assertTrue(eventState.evaluateStep(interpolatorMock));
        assertTrue(eventState.pendingEvent);
        assertEquals(-0.05d, eventState.pendingEventTime, 0.001d);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        when(handlerMock.g(anyDouble(), anyArray())).thenThrow(new DerivativeException());

        eventState.evaluateStep(interpolatorMock);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        when(handlerMock.g(anyDouble(), anyArray())).thenThrow(new EventException());

        eventState.evaluateStep(interpolatorMock);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        doThrow(new ConvergenceException()).when(handlerMock).g(anyDouble(), anyArray());

        eventState.evaluateStep(interpolatorMock);
    }

    @Test
    public void testGetEventTime() {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);
        eventState.pendingEventTime = 0.05d;

        assertEquals(0.05d, eventState.getEventTime(), 0.001d);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 0.0d;
        double[] y = new double[1];
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.t0, 0.001d);
        assertEquals(handler.g(t, y), eventState.g0, 0.0001d);
        assertTrue(eventState.g0Positive);
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);

        when(handlerMock.eventOccurred(eq(t), anyArray(), anyBoolean())).thenReturn(EventHandler.STOP);
        eventState.stepAccepted(t, y);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 0.0d;
        double[] y = new double[1];
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.1d, 0.01d, 10);

        eventState.pendingEvent = true;
        eventState.pendingEventTime = 0.05d;

        when(handlerMock.resetState(eq(t), anyArray())).thenReturn(true);
        eventState.reset(t, y);
        assertFalse(eventState.pendingEvent);
        assertNull(eventState.pendingEventTime);

        when(handlerMock.resetState(eq(t), anyArray())).thenReturn(false);
        eventState.reset(t, y);
        assertTrue(eventState.pendingEvent);
        assertEquals(EventHandler.RESET_STATE, eventState.nextAction);

        when(handlerMock.resetDerivatives()).thenReturn(true);
        eventState.reset(t, y);
        assertTrue(eventState.nextAction == EventHandler.RESET_STATE || eventState.nextAction == EventHandler.RESET_DERIVATIVES);
    }

}