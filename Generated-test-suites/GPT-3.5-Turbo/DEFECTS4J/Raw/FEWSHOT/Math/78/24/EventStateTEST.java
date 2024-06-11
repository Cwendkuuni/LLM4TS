package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class EventStateTest {

    private EventState eventState;
    private EventHandler handler;

    @Before
    public void setup() {
        handler = Mockito.mock(EventHandler.class);
        eventState = new EventState(handler, 0.001, 0.00001, 100);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(handler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(0.00001, eventState.getConvergence(), 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(100, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0};
        Mockito.when(handler.g(ArgumentMatchers.anyDouble(), ArgumentMatchers.any(double[].class))).thenReturn(0.0);
        eventState.reinitializeBegin(tStart, yStart);
        Mockito.verify(handler).g(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException {
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.isForward()).thenReturn(true);
        Mockito.when(interpolator.getCurrentTime()).thenReturn(1.0, 2.0);
        double[] y = {1.0, 2.0};
        Mockito.when(interpolator.getInterpolatedState()).thenReturn(y);
        Mockito.when(handler.g(ArgumentMatchers.anyDouble(), ArgumentMatchers.any(double[].class))).thenReturn(-1.0, 1.0);
        boolean result = eventState.evaluateStep(interpolator);
        assertTrue(result);
    }

    @Test
    public void testGetEventTime() {
        assertEquals(Double.NaN, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 0.0;
        double[] y = {1.0, 2.0};
        Mockito.when(handler.g(ArgumentMatchers.anyDouble(), ArgumentMatchers.any(double[].class))).thenReturn(0.0);
        Mockito.when(handler.eventOccurred(t, y, true)).thenReturn(EventHandler.STOP);
        eventState.stepAccepted(t, y);
        Mockito.verify(handler).g(t, y);
        Mockito.verify(handler).eventOccurred(t, y, true);
    }

    @Test
    public void testStop() {
        eventState.stepAccepted(0.0, new double[]{1.0, 2.0});
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 0.0;
        double[] y = {1.0, 2.0};
        Mockito.when(handler.eventOccurred(t, y, true)).thenReturn(EventHandler.RESET_STATE);
        boolean result = eventState.reset(t, y);
        assertTrue(result);
        Mockito.verify(handler).resetState(t, y);
    }

}