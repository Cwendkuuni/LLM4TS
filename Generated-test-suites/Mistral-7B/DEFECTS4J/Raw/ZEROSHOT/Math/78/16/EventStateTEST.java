import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class EventStateTest {

    private EventHandler mockEventHandler;
    private EventState eventState;

    @Before
    public void setUp() {
        mockEventHandler = Mockito.mock(EventHandler.class);

        eventState = new EventState(mockEventHandler, 0.01, 0.001, 20);
    }

    @Test
    public void testInitialState() {
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(20, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(Double.NaN, eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[] { 1.0, 1.0 };

        Mockito.when(mockEventHandler.g(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDoubleArray()))
            .thenReturn(0.0);

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(0.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginFailedEvaluation() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[] { 1.0, 1.0 };

        Mockito.when(mockEventHandler.g(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDoubleArray()))
            .thenThrow(new EventException("Test error"));

        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator mockInterpolator = Mockito.mock(StepInterpolator.class);

        double t1 = 2.0;
        double h = 0.1;

        Mockito.when(mockInterpolator.getCurrentTime()).thenReturn(t1);
        Mockito.when(mockInterpolator.isForward()).thenReturn(true);

        double[] y = new double[] { 1.0, 1.0 };

        Mockito.when(mockEventHandler.g(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDoubleArray()))
            .thenAnswer(invocation -> invocation.getArguments()[0] > 0.0 ? 1.0 : -1.0);

        eventState.evaluateStep(mockInterpolator);

        assertTrue(eventState.isPendingEvent());
        assertEquals(t1, eventState.getPendingEventTime(), 0.0);

        Mockito.verify(mockEventHandler, Mockito.times(2)).g(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDoubleArray());
    }

    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator mockInterpolator = Mockito.mock(StepInterpolator.class);

        double t1 = 2.0;
        double h = 0.1;

        Mockito.when(mockInterpolator.getCurrentTime()).thenReturn(t1);
        Mockito.when(mockInterpolator.isForward()).thenReturn(true);

        double[] y = new double[] { 1.0, 1.0 };

        Mockito.when(mockEventHandler.g(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDoubleArray()))
            .thenAnswer(invocation -> invocation.getArguments()[0] > 0.0 ? 1.0 : -1.0);

        eventState.evaluateStep(mockInterpolator);

        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 3.0;
        double[] y = new double[] { 1.0, 1.0 };

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(0.0, eventState.getG0(), 0.0);
        Mockito.verify(mockEventHandler, Mockito.times(1)).eventOccurred(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDoubleArray(), ArgumentMatchers.booleanIs(false));
    }

    @Test
    public void testStop() {
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        eventState.setNextAction(EventHandler.STOP);
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    @Test
    public void testReset() throws EventException {
        double t = 4.0;
        double[] y = new double[] { 1.0, 1.0 };

        eventState.reset(t, y);

        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(Double.NaN, eventState.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.verify(mockEventHandler, Mockito.times(1)).resetState(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDoubleArray());
    }

}