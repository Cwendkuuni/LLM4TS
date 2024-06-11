import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventStateTest {

    @Mock
    private EventHandler mockEventHandler;

    @Captor
    private ArgumentCaptor<Double[]> mockYCaptor;

    private EventState eventState;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventState = new EventState(mockEventHandler, 0.001, 0.01, 10);
    }

    @Test
    public void testEventStateConstructor() {
        // Test constructor with given arguments
        assertTrue(eventState instanceof EventState);
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.01, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(Double.NaN, eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double[] mockY = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(0.0, mockY);
        assertEquals(0.0, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(0.0, mockY), eventState.getG0(), 0.0);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        double[] mockY = new double[]{1.0, 2.0};
        Mockito.when(mockEventHandler.g(0.0, mockY)).thenThrow(new EventException());
        eventState.reinitializeBegin(0.0, mockY);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator mockInterpolator = Mockito.mock(StepInterpolator.class);
        Double t1 = 1.0;
        Double h = 0.1;

        Mockito.when(mockInterpolator.getCurrentTime()).thenReturn(t1);
        Mockito.when(mockInterpolator.getInterpolatedState(Mockito.anyDouble())).thenReturn(mockYCaptor.capture());

        eventState.evaluateStep(mockInterpolator);

        Double[] capturedY = mockYCaptor.getValue();
        Mockito.verify(mockEventHandler).g(eventState.getT0(), capturedY);

        // Test sign change and event occurrence
        double[] state = new double[]{1.0, 2.0, 3.0};
        Mockito.when(mockEventHandler.g(eventState.getT0(), state)).thenReturn(Double.NEGATIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.001, state)).thenReturn(3.0);

        eventState.evaluateStep(mockInterpolator);

        assertTrue(eventState.isPendingEvent());
        assertEquals(eventState.getT0() + 0.001, eventState.getPendingEventTime(), 0.0);

        // Test no sign change and no event occurrence
        Mockito.when(mockEventHandler.g(eventState.getT0(), state)).thenReturn(Double.POSITIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.001, state)).thenReturn(Double.POSITIVE_INFINITY);

        eventState.evaluateStep(mockInterpolator);

        assertFalse(eventState.isPendingEvent());

        // Test convergence during event search
        Mockito.when(mockEventHandler.g(eventState.getT0(), state)).thenReturn(Double.NEGATIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.001, state)).thenReturn(Double.POSITIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.002, state)).thenReturn(Double.NEGATIVE_INFINITY);

        eventState.evaluateStep(mockInterpolator);

        assertFalse(eventState.isPendingEvent());

        // Test maximum iteration count
        Mockito.when(mockEventHandler.g(eventState.getT0(), state)).thenReturn(Double.NEGATIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.001, state)).thenReturn(Double.POSITIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.002, state)).thenReturn(Double.NEGATIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.003, state)).thenReturn(Double.NEGATIVE_INFINITY);
        Mockito.when(mockEventHandler.g(eventState.getT0() + 0.004, state)).thenReturn(Double.POSITIVE_INFINITY);

        eventState.evaluateStep(mockInterpolator);

        assertFalse(eventState.isPendingEvent());

        // Test step acceptance
        Double[] acceptedY = new double[]{1.0, 2.0, 4.0};
        eventState.stepAccepted(t1 + h, acceptedY);
        assertEquals(t1 + h, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(eventState.getT0(), acceptedY), eventState.getG0(), 0.0);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // Test stop event
        Mockito.when(mockEventHandler.eventOccurred(Double.NaN, mockYCaptor.capture(), Mockito.anyBoolean())).thenReturn(EventHandler.STOP);

        eventState.evaluateStep(mockInterpolator);

        assertTrue(eventState.isPendingEvent());
        assertEquals(eventState.getT0() + h / 2.0, eventState.getPendingEventTime(), 0.0);

        eventState.stepAccepted(t1 + h, acceptedY);

        assertTrue(eventState.isStopped());

        // Test reset event
        Mockito.when(mockEventHandler.eventOccurred(Double.NaN, mockYCaptor.capture(), Mockito.anyBoolean())).thenReturn(EventHandler.RESET_STATE);

        eventState.evaluateStep(mockInterpolator);

        assertTrue(eventState.isPendingEvent());
        assertEquals(eventState.getT0() + h / 2.0, eventState.getPendingEventTime(), 0.0);

        eventState.stepAccepted(t1 + h, acceptedY);

        assertFalse(eventState.isStopped());
        assertFalse(eventState.isPendingEvent());
        assertEquals(acceptedY, mockYCaptor.getValue());

        // Test reset derivatives event
        Mockito.when(mockEventHandler.eventOccurred(Double.NaN, mockYCaptor.capture(), Mockito.anyBoolean())).thenReturn(EventHandler.RESET_DERIVATIVES);

        eventState.evaluateStep(mockInterpolator);

        assertTrue(eventState.isPendingEvent());
        assertEquals(eventState.getT0() + h / 2.0, eventState.getPendingEventTime(), 0.0);

        eventState.stepAccepted(t1 + h, acceptedY);

        assertFalse(eventState.isStopped());
        assertFalse(eventState.isPendingEvent());
        assertEquals(acceptedY, mockYCaptor.getValue());
    }

    @Test
    public void testStepAccepted() {
        // Test step acceptance without event
        double[] y = new double[]{1.0, 2.0};
        eventState.stepAccepted(1.0, y);
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(1.0, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // Test step acceptance with event
        Double[] eventY = new double[]{1.0, 2.0, 3.0};
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(1.0);
        eventState.stepAccepted(1.0, eventY);
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(1.0, eventY), eventState.getG0(), 0.0);
        assertFalse(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(eventY, mockEventHandler.getState());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isStopped());
    }

    @Test
    public void testStop() {
        // Test stop event
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.isStopped());

        // Test reset event
        eventState.setNextAction(EventHandler.RESET_STATE);
        assertFalse(eventState.isStopped());

        // Test continue event
        eventState.setNextAction(EventHandler.CONTINUE);
        assertFalse(eventState.isStopped());
    }

    @Test
    public void testReset() throws EventException {
        // Test state reset
        double[] y = new double[]{1.0, 2.0};
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(1.0);
        eventState.setNextAction(EventHandler.RESET_STATE);
        eventState.stepAccepted(1.0, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isStopped());
        assertEquals(y, mockEventHandler.getState());

        // Test derivative reset
        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        eventState.stepAccepted(1.0, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isStopped());
        assertNull(mockEventHandler.getDerivatives());
    }

    private EventState setPendingEvent(EventState eventState, boolean pendingEvent) {
        eventState.setPendingEvent(pendingEvent);
        return eventState;
    }

    private void setPendingEventTime(EventState eventState, double pendingEventTime) {
        eventState.setPendingEventTime(pendingEventTime);
    }
}