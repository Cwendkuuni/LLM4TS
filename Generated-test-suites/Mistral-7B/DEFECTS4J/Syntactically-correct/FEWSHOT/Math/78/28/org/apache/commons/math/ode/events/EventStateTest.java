package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;

import java.util.Arrays;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler handlerMock = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);

        assertTrue(Double.isNaN(eventState.getEventHandler().g(0.0, new double[0])));
        assertTrue(Double.isNaN(eventState.getT0()));
        assertTrue(Double.isNaN(eventState.getG0()));
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        assertTrue(Double.isNaN(eventState.getPreviousEventTime()));
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        double t0 = 1.0;
        double[] y0 = {1.0, 2.0};

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, y0);

        assertEquals(t0, eventState.getT0(), 0.0);
        assertArrayEquals(y0, eventState.getY0(), 0.0);
        assertEquals(handlerMock.g(t0, y0), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        assertTrue(Double.isNaN(eventState.getPreviousEventTime()));
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        double t0 = 1.0;
        double[] y0 = {1.0, 2.0};

        when(handlerMock.g(t0, y0)).thenThrow(new EventException("Test Exception"));

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, y0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handlerMock = mock(EventHandler.class);
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};

        when(interpolatorMock.getCurrentTime()).thenReturn(0.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        double pendingEventTime = 0.2;
        when(handlerMock.g(pendingEventTime, new double[0]))
                .thenReturn(Double.NEGATIVE_INFINITY);

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, y0);

        assertTrue(eventState.evaluateStep(interpolatorMock));
        assertEquals(pendingEventTime, eventState.getPendingEventTime(), 0.0);
        assertTrue(eventState.hasPendingEvent());
        assertFalse(eventState.isG0Positive());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handlerMock = mock(EventHandler.class);
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};

        when(interpolatorMock.getCurrentTime()).thenReturn(0.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        when(handlerMock.g(0.1, new double[0]))
                .thenReturn(1.0);

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, y0);

        assertFalse(eventState.evaluateStep(interpolatorMock));
        assertFalse(eventState.hasPendingEvent());
        assertTrue(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handlerMock = mock(EventHandler.class);
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};

        when(interpolatorMock.getCurrentTime()).thenReturn(0.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        when(handlerMock.g(0.1, new double[0]))
                .thenThrow(new DerivativeException("Test DerivativeException"));

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, y0);

        eventState.evaluateStep(interpolatorMock);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handlerMock = mock(EventHandler.class);
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};

        when(interpolatorMock.getCurrentTime()).thenReturn(0.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        when(handlerMock.g(0.1, new double[0]))
                .thenThrow(new EventException("Test EventException"));

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, y0);

        eventState.evaluateStep(interpolatorMock);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handlerMock = mock(EventHandler.class);
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};

        when(interpolatorMock.getCurrentTime()).thenReturn(0.1);
        when(interpolatorMock.isForward()).thenReturn(true);

        when(handlerMock.g(0.1, new double[0]))
                .thenThrow(new ConvergenceException("Test ConvergenceException"));

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);
        eventState.reinitializeBegin(t0, y0);

        eventState.evaluateStep(interpolatorMock);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        double[] y = new double[2];

        double t = 1.0;

        EventState eventState = new EventState(handlerMock, 0.01, 0.001, 10);
        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertArrayEquals(y, eventState.getY0(), 0.0);
        assertEquals(handlerMock.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.hasPendingEvent());
        assertTrue(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(null, 0.01, 0.001, 10);

        assertTrue(eventState.stop());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        double t = 2.0;
        double[] y = {1.0, 2.0};

        EventState eventState = new EventState(handlerMock, 0.01, 0.001, 10);
        eventState.stepAccepted(1.0, new double[0]);
        eventState.setPendingEventTime(1.1);

        eventState.reset(t, y);

        assertArrayEquals(new double[0], eventState.getY0(), 0.0);
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
    }

    @Test
    public void testResetState() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        double t = 2.0;
        double[] y = {1.0, 2.0};

        EventState eventState = new EventState(handlerMock, 0.01, 0.001, 10);
        eventState.stepAccepted(1.0, new double[0]);
        eventState.setPendingEventTime(1.1);

        when(handlerMock.resetState(t, y)).thenReturn(true);

        eventState.reset(t, y);

        assertArrayEquals(y, eventState.getY0(), 0.0);
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
    }

    @Test
    public void testResetDerivatives() throws EventException {
        EventHandler handlerMock = mock(EventHandler.class);
        double t = 2.0;
        double[] y = {1.0, 2.0};

        EventState eventState = new EventState(handlerMock, 0.01, 0.001, 10);
        eventState.stepAccepted(1.0, new double[0]);
        eventState.setPendingEventTime(1.1);

        when(handlerMock.resetState(t, y)).thenReturn(false);

        eventState.reset(t, y);

        assertArrayEquals(new double[0], eventState.getY0(), 0.0);
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());

        when(handlerMock.resetDerivatives(t, y)).thenReturn(true);

        eventState.reset(t, y);

        assertArrayEquals(new double[0], eventState.getY0(), 0.0);
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.isG0Positive());
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
    }

    private void assertArrayEquals(double[] expected, double[] actual, double epsilon) {
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i], epsilon);
        }
    }

}