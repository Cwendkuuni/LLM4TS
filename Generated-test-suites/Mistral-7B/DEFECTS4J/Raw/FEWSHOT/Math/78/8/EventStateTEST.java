package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyDoubleArray;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventHandler handler;
    private EventState eventState;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        handler = mock(EventHandler.class);
        interpolator = mock(StepInterpolator.class);
        eventState = new EventState(handler, 0.01, 0.001, 10);
        when(handler.g(anyDouble(), anyDoubleArray())).thenCallRealMethod();
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(0.0);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertEquals(true, eventState.isG0Positive());
        assertEquals(false, eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(Double.NaN, eventState.getPreviousEventTime());
        assertEquals(true, eventState.isForward());
        assertEquals(true, eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(0.0, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        double t1 = 1.2;
        eventState.reinitializeBegin(0.0, new double[1]);

        eventState.setInterpolator(interpolator);
        when(interpolator.getCurrentTime()).thenReturn(t1);

        when(handler.g(anyDouble(), anyDoubleArray())).thenReturn(-1.0);

        assertFalse(eventState.evaluateStep(interpolator));
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(false, eventState.isPendingEvent());

        when(handler.g(anyDouble(), anyDoubleArray())).thenReturn(0.0);

        double ta = 0.5;
        double tb = 1.0;
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{0.0});

        UnivariateFunction f = mock(UnivariateFunction.class);
        when(f.value(anyDouble())).thenCallRealMethod();
        BrentSolver solver = new BrentSolver();
        when(solver.solve(f, ta, tb)).thenReturn(1.0);

        eventState.evaluateStep(interpolator);

        assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
        assertEquals(true, eventState.isPendingEvent());
        assertEquals(true, eventState.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

        double root = 0.8;
        when(solver.solve(f, ta, tb)).thenReturn(root);

        eventState.evaluateStep(interpolator);

        assertEquals(root, eventState.getPendingEventTime(), 0.0);
        assertEquals(true, eventState.isPendingEvent());
        assertEquals(true, eventState.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 2.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0());
        assertEquals(-1.0, eventState.getG0(), 0.0);
        assertEquals(false, eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(t, eventState.getPreviousEventTime());
        assertEquals(true, eventState.isG0Positive());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReset() throws EventException {
        double t = 3.0;
        double[] y = new double[1];
        eventState.reset(t, y);

        assertEquals(false, eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());

        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
        assertNull(eventState.getPreviousEventTime());

        assertEquals(0.0, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
    }

    @Test
    public void testStop() {
        assertEquals(false, eventState.stop());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        eventState.setNextAction(EventHandler.STOP);

        assertEquals(true, eventState.stop());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

}