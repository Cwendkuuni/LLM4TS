import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.EventException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepRejectReason;
import org.apache.commons.math3.special.BrentSolver;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.apache.commons.math.ode.events.EventState;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(0.001, eventState.getConvergence(), 0.0);
        Assert.assertEquals(10, eventState.getMaxIterationCount());
        Assert.assertNull(eventState.getEventHandler().g(0.0, new double[0]));
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);
        double[] state = new double[5];

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);

        eventState.reinitializeBegin(0.0, state);
        Assert.assertEquals(1.0, eventState.getEventHandler().g(0.0, state), 0.0);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);
        double[] state = new double[5];

        when(handlerMock.g(anyDouble(), anyArray())).thenThrow(new EventException());

        eventState.reinitializeBegin(0.0, state);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);

        when(interpolatorMock.getCurrentTime()).thenReturn(1.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        double[] state = new double[5];

        eventState.evaluateStep(interpolatorMock);
        Assert.assertTrue(eventState.pendingEvent);
        Assert.assertEquals(1.1, eventState.pendingEventTime, 0.0);

        reset(handlerMock, interpolatorMock);

        when(interpolatorMock.getCurrentTime()).thenReturn(1.2);
        when(interpolatorMock.isForward()).thenReturn(false);

        eventState.evaluateStep(interpolatorMock);
        Assert.assertFalse(eventState.pendingEvent);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(interpolatorMock.getCurrentTime()).thenReturn(1.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        doThrow(new DerivativeException("Derivative error"))
                .when(handlerMock)
                .g(anyDouble(), anyArray());

        eventState.evaluateStep(interpolatorMock);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(interpolatorMock.getCurrentTime()).thenReturn(1.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        doThrow(new EventException("Event error"))
                .when(handlerMock)
                .g(anyDouble(), anyArray());

        eventState.evaluateStep(interpolatorMock);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(interpolatorMock.getCurrentTime()).thenReturn(1.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        doThrow(new ConvergenceException("Convergence error"))
                .when(handlerMock)
                .g(anyDouble(), anyArray());

        eventState.evaluateStep(interpolatorMock);
    }

    @Test
    public void testGetEventTime() {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);
        double[] state = new double[5];

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(handlerMock.eventOccurred(anyDouble(), anyArray(), anyBoolean()))
                .thenReturn(EventHandler.RESET_STATE);

        eventState.pendingEvent = true;
        eventState.pendingEventTime = 1.1;
        Assert.assertEquals(1.1, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);
        double[] state = new double[5];

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(handlerMock.eventOccurred(anyDouble(), anyArray(), anyBoolean()))
                .thenReturn(EventHandler.CONTINUE);

        eventState.stepAccepted(1.1, state);
        Assert.assertFalse(eventState.pendingEvent);
        Assert.assertEquals(1.1, eventState.getEventHandler().g(1.1, state), 0.0);
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(null, 0.001, 0.001, 10);
        Assert.assertFalse(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);
        double[] state = new double[5];

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(handlerMock.resetState(anyDouble(), anyArray()))
                .thenReturn(true);

        eventState.pendingEvent = true;
        eventState.previousEventTime = 1.1;
        eventState.reset(1.2, state);
        Assert.assertFalse(eventState.pendingEvent);
        Assert.assertFalse(eventState.stop());
    }

    @Test
    public void testResetWithStop() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.001, 0.001, 10);
        double[] state = new double[5];

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(handlerMock.resetState(anyDouble(), anyArray()))
                .thenReturn(true);
        when(handlerMock.resetState(anyDouble(), anyArray()))
                .thenReturn(EventHandler.STOP);

        eventState.pendingEvent = true;
        eventState.previousEventTime = 1.1;
        eventState.reset(1.2, state);
        Assert.assertTrue(eventState.stop());
    }

    private void reset(EventHandler handlerMock, StepInterpolator interpolatorMock) {
        when(handlerMock.g(anyDouble(), anyArray())).thenAnswer((invocation) -> {
            double t = interpolatorMock.getCurrentTime();
            interpolatorMock.setInterpolatedTime(t);
            return handlerMock.g(t, interpolatorMock.getInterpolatedState());
        });
    }
}