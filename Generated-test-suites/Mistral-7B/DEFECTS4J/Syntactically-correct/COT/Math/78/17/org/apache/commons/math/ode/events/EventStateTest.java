import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        assertFalse(eventState.isPendingEvent());
        assertTrue(Double.isNaN(eventState.getEventTime()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEventStateConstructorWithNegativeConvergence() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, -0.001, 10);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        double tStart = 0.1;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        assertTrue(Double.isFinite(eventState.getT0()));
        assertTrue(Double.isFinite(eventState.getG0()));
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithInvalidEventHandlerValue() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        when(handler.g(0.0, new double[0])).thenThrow(new EventException());
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        double tStart = 0.1;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStepWithNoEvent() throws DerivativeException, ConvergenceException, EventException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        when(interpolator.getDerivative(anyDouble(), any(double[].class))).thenReturn(0.0);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.0);
        when(handler.g(anyDouble(), any(double[].class))).thenReturn(0.0);
        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, ConvergenceException, EventException {
        EventHandler handler = mock(EventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        when(interpolator.getDerivative(anyDouble(), any(double[].class))).thenReturn(0.0);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(2.0);
        when(handler.g(anyDouble(), any(double[].class))).thenReturn(-0.1);
        boolean result = eventState.evaluateStep(interpolator);
        assertTrue(result);
    }

    @Test
    public void testGetEventTime() {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        eventState.setPendingEventTime(1.0);
        double eventTime = eventState.getEventTime();
        assertEquals(1.0, eventTime, 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        double t = 2.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        assertEquals(2.0, eventState.getT0(), 0.0);
        assertEquals(-0.1, eventState.getG0(), 0.0);
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(mock(EventHandler.class), 1.0, 0.001, 10);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        double t = 2.0;
        double[] y = new double[1];
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(2.1);
        eventState.setNextAction(EventHandler.RESET_STATE);
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

}