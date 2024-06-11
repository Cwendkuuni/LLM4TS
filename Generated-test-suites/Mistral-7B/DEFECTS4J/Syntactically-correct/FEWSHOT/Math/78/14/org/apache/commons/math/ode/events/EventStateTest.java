package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.SamplingInterval;
import org.apache.commons.math.ode.step.StepInterpolator;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.solvers.BrentSolver;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventHandler mockEventHandler;
    private StepInterpolator mockInterpolator;
    private EventState eventState;

    @Before
    public void setup() {
        mockEventHandler = mock(EventHandler.class);
        mockInterpolator = mock(StepInterpolator.class);
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 100);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler, eventState.getEventHandler());
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        when(mockInterpolator.getCurrentTime()).thenReturn(1.5);
        when(mockInterpolator.isForward()).thenReturn(true);

        UnivariateRealFunction mockedFunction = mock(UnivariateRealFunction.class);
        BrentSolver mockedSolver = mock(BrentSolver.class);

        when(mockEventHandler.g(anyDouble(), anyArray())).thenReturn(mockedFunction);
        when(mockedFunction.value(anyDouble())).thenThrow(new FunctionEvaluationException("Test"));
        when(mockedSolver.solve(any(UnivariateRealFunction.class), anyDouble(), anyDouble())).thenReturn(2.0);

        eventState.evaluateStep(mockInterpolator);

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(1.5, eventState.getT0(), 0.0);

        verify(mockEventHandler, times(2)).g(anyDouble(), anyArray());
        verify(mockedFunction, times(1)).value(anyDouble());
        verify(mockedSolver, times(1)).solve(any(UnivariateRealFunction.class), anyDouble(), anyDouble());
    }

    @Test
    public void testGetEventTime() {
        eventState.setPendingEventTime(1.0);

        assertEquals(1.0, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 2.0;
        double[] y = new double[]{3.0, 4.0};

        eventState.stepAccepted(t, y);

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(2.0, eventState.getT0(), 0.0);
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 3.0;
        double[] y = new double[]{5.0, 6.0};

        eventState.reset(t, y);

        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
        assertFalse(eventState.stop());
    }

}