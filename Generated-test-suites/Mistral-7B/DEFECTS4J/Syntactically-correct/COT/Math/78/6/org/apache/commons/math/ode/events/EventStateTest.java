package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.ode.DerivativeException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    @ org.junit.Test
    public void testConstructor() {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        assertNotNull(eventState);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0);
        assertEquals(0.01, eventState.getConvergence(), 0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getT0());
        assertNull(eventState.getG0());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @ org.junit.Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        double tStart = 1.0;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertFalse(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @ org.junit.Test(expected = EventException.class)
    public void testEvaluateStep_eventException() throws DerivativeException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        when(handler.g(anyDouble(), anyArray())).thenThrow(new EventException());
        eventState.evaluateStep(interpolator);
        fail("An EventException should have been thrown");
    }

    @ org.junit.Test(expected = DerivativeException.class)
    public void testEvaluateStep_derivativeException() throws EventException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        when(handler.g(anyDouble(), anyArray())).thenThrow(new DerivativeException());
        eventState.evaluateStep(interpolator);
        fail("A DerivativeException should have been thrown");
    }

    @ org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateStep_convergenceException() throws EventException, DerivativeException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        when(handler.g(anyDouble(), anyArray())).thenReturn(0.0);
        when(interpolator.getCurrentTime()).thenReturn(1.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[1]);
        BrentSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(eventState.getConvergence());
        solver.setMaximalIterationCount(eventState.getMaxIterationCount());
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(final double t) throws FunctionEvaluationException {
                try {
                    interpolator.setInterpolatedTime(t);
                    return handler.g(t, interpolator.getInterpolatedState());
                } catch (DerivativeException e) {
                    throw new FunctionEvaluationException(e, t);
                } catch (EventException e) {
                    throw new FunctionEvaluationException(e, t);
                }
            }
        };
        eventState.evaluateStep(interpolator);
        fail("A ConvergenceException should have been thrown");
    }

    @ org.junit.Test
    public void testEvaluateStep_noEvent() throws EventException, DerivativeException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        when(handler.g(anyDouble(), anyArray())).thenReturn(1.0);
        when(interpolator.getCurrentTime()).thenReturn(1.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[1]);
        eventState.evaluateStep(interpolator);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @ org.junit.Test
    public void testEvaluateStep_eventOccurs() throws EventException, DerivativeException, ConvergenceException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        when(handler.g(anyDouble(), anyArray())).thenReturn(0.0, 1.0, -1.0, 0.0, 1.0);
        when(interpolator.getCurrentTime()).thenReturn(1.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[1]);
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.0, eventState.getPendingEventTime(), 0);
    }

    @ org.junit.Test
    public void testGetEventTime() {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        when(handler.g(anyDouble(), anyArray())).thenReturn(0.0, 1.0, -1.0, 0.0, 1.0);
        when(interpolator.getCurrentTime()).thenReturn(1.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[1]);
        eventState.evaluateStep(interpolator);
        assertEquals(1.0, eventState.getEventTime(), 0);
    }

    @ org.junit.Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        when(handler.g(anyDouble(), anyArray())).thenReturn(0.0, 1.0, -1.0, 0.0, 1.0);
        when(interpolator.getCurrentTime()).thenReturn(1.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[1]);
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(1.1, new double[1]);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @ org.junit.Test
    public void testStop() {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @ org.junit.Test
    public void testReset() throws EventException {
        EventHandler handler = mock(EventHandler.class); // you need to implement EventHandler mocking or use a real handler
        StepInterpolator interpolator = mock(StepInterpolator.class); // you need to implement StepInterpolator mocking or use a real interpolator
        EventState eventState = new EventState(handler, 0.1, 0.01, 10);
        double t = 1.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

}