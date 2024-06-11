package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        assertEquals(mockHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(1e-6, eventState.getConvergence(), 0.0);
        assertEquals(100, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertNull(eventState.getEventTime());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);

        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 1.0};

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);

        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        BrentSolver solver = mock(BrentSolver.class);

        when(mockHandler.g(anyDouble(), anyDoubleArray())).thenThrow(new EventException());
        when(mockHandler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.STOP);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        interpolator.setInterpolatedFunction(function);

        assertFalse(eventState.evaluateStep(interpolator));

        verify(mockHandler).g(anyDouble(), anyDoubleArray());
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);

        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        BrentSolver solver = mock(BrentSolver.class);

        double eventTime = 2.0;

        when(mockHandler.g(anyDouble(), anyDoubleArray())).thenReturn(0.0);
        when(mockHandler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.RESET_STATE);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        interpolator.setInterpolatedFunction(function);

        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});

        double t1 = 1.0;
        double h = 0.01;

        interpolator.setInterpolatedTime(t1);
        double gb = mockHandler.g(t1, interpolator.getInterpolatedState());

        eventState.evaluateStep(interpolator);

        assertEquals(t1, eventState.getT0(), 0.0);
        assertEquals(gb, eventState.getG0(), 0.0);
        assertTrue(eventState.isPendingEvent());
        assertEquals(eventTime, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testEvaluateStepWithEventOccurredInsideStep()
            throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);

        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        BrentSolver solver = mock(BrentSolver.class);

        double eventTime = 1.2;

        when(mockHandler.g(anyDouble(), anyDoubleArray())).thenReturn(0.0);
        when(mockHandler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.RESET_STATE);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        interpolator.setInterpolatedFunction(function);

        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});

        double t1 = 1.0;
        double h = 0.1;

        interpolator.setInterpolatedTime(t1);
        double gb = mockHandler.g(t1, interpolator.getInterpolatedState());

        eventState.evaluateStep(interpolator);

        assertEquals(t1, eventState.getT0(), 0.0);
        assertEquals(gb, eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        interpolator.setInterpolatedTime(t1 + 0.1);
        gb = mockHandler.g(t1 + 0.1, interpolator.getInterpolatedState());

        eventState.evaluateStep(interpolator);

        assertEquals(t1 + 0.1, eventState.getT0(), 0.0);
        assertEquals(gb, eventState.getG0(), 0.0);
        assertTrue(eventState.isPendingEvent());
        assertEquals(eventTime, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});

        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testStepAcceptedWithEvent() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(0.01);

        when(mockHandler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.CONTINUE);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});

        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        when(mockHandler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.RESET_STATE);

        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});

        t = 1.5;
        y = new double[]{1.5, 1.5};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});

        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        when(mockHandler.resetState(anyDouble(), anyDoubleArray())).thenReturn(new double[]{0.0, 0.0});

        eventState.reset(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testResetWithEventOccurred() throws EventException {
        EventHandler mockHandler = mock(EventHandler.class);

        when(mockHandler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.RESET_STATE);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});

        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        when(mockHandler.resetState(anyDouble(), anyDoubleArray())).thenReturn(new double[]{0.0, 0.0});

        eventState.reset(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        when(mockHandler.resetState(anyDouble(), anyDoubleArray())).thenThrow(new EventException());

        eventState.reset(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testStop() {
        EventHandler mockHandler = mock(EventHandler.class);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        assertFalse(eventState.stop());
    }

    @Test
    public void testStopWithEventHandlerStop() {
        EventHandler mockHandler = mock(EventHandler.class);
        when(mockHandler.eventOccurred(anyDouble(), anyDoubleArray(), anyBoolean())).thenReturn(EventHandler.STOP);

        EventState eventState = new EventState(mockHandler, 0.01, 1e-6, 100);

        assertTrue(eventState.stop());
    }
}