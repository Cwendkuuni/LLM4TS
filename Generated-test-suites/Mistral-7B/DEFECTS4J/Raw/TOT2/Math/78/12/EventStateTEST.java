import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /** Mocked event handler. */
    private EventHandler mockEventHandler;

    /** Tested event state. */
    private EventState eventState;

    /**
     * Set up mocked event handler and initialize tested event state.
     */
    @Before
    public void setUp() {
        mockEventHandler = Mockito.mock(EventHandler.class);
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 10);
    }

    /**
     * Test method {@link EventState#reinitializeBegin(double, double[])}.
     * @throws EventException if an error occurs during evaluation of mocked event handler
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        double[] state = new double[] {1.0, 2.0, 3.0};
        eventState.reinitializeBegin(0.0, state);
        assertEquals(0.0, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertFalse(eventState.isPendingEvent());
        assertFalse(Double.isFinite(eventState.getPendingEventTime()));
        assertFalse(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method {@link EventState#evaluateStep(StepInterpolator)} with no event during the step.
     * @throws DerivativeException if an error occurs during evaluation of mocked event handler
     * @throws EventException if an error occurs during evaluation of mocked event handler
     * @throws ConvergenceException if an event cannot be located
     */
    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator stepInterpolator = Mockito.mock(StepInterpolator.class);
        when(stepInterpolator.isForward()).thenReturn(true);
        when(stepInterpolator.getCurrentTime()).thenReturn(1.0);
        when(mockEventHandler.g(anyDouble(), anyArray())).thenReturn(1.0);
        eventState.evaluateStep(stepInterpolator);
        verify(mockEventHandler).g(anyDouble(), anyArray());
        assertFalse(eventState.isPendingEvent());
        assertFalse(Double.isFinite(eventState.getPendingEventTime()));
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method {@link EventState#evaluateStep(StepInterpolator)} with an event during the step.
     * @throws DerivativeException if an error occurs during evaluation of mocked event handler
     * @throws EventException if an error occurs during evaluation of mocked event handler
     * @throws ConvergenceException if an event cannot be located
     */
    @Test
    public void testEvaluateStepEvent() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator stepInterpolator = Mockito.mock(StepInterpolator.class);
        when(stepInterpolator.isForward()).thenReturn(true);
        when(stepInterpolator.getCurrentTime()).thenReturn(1.0);
        when(mockEventHandler.g(anyDouble(), anyArray())).thenReturn(0.0);
        when(mockEventHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(EventHandler.RESET_DERIVATIVES);
        eventState.evaluateStep(stepInterpolator);
        verify(mockEventHandler).g(anyDouble(), anyArray());
        verify(mockEventHandler).eventOccurred(anyDouble(), anyArray(), anyBoolean());
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

    /**
     * Test method {@link EventState#stepAccepted(double, double[])}.
     * @throws EventException if an error occurs during evaluation of mocked event handler
     */
    @Test
    public void testStepAccepted() throws EventException {
        double[] state = new double[] {1.0, 2.0, 3.0};
        eventState.stepAccepted(1.0, state);
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(0.0, eventState.getG0(), 0.0);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertFalse(Double.isFinite(eventState.getPendingEventTime()));
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method {@link EventState#stop()}.
     */
    @Test
    public void testStop() {
        assertTrue(eventState.stop());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    /**
     * Test method {@link EventState#reset(double, double[])}.
     * @throws EventException if an error occurs during evaluation of mocked event handler
     */
    @Test
    public void testReset() throws EventException {
        double[] state = new double[] {1.0, 2.0, 3.0};
        eventState.stepAccepted(1.0, state);
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(1.0);
        eventState.setNextAction(EventHandler.RESET_STATE);
        eventState.reset(1.0, state);
        assertFalse(eventState.isPendingEvent());
        assertFalse(Double.isFinite(eventState.getPendingEventTime()));
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

}