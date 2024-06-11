package org.apache.commons.math.ode.events;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventStateTest {

    @Mock
    private EventHandler handler;

    private EventState eventState;

    @Before
    public void setUp() {
        eventState = new EventState(handler, 0.01, 0.001, 10);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(0.001, eventState.getConvergence(), 0.0);
        Assert.assertEquals(10, eventState.getMaxIterationCount());
        Assert.assertTrue(Double.isNaN(eventState.getT0()));
        Assert.assertTrue(Double.isNaN(eventState.getG0()));
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        Assert.assertTrue(Double.isNaN(eventState.getPreviousEventTime()));
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(0.0, yStart);
        Assert.assertEquals(0.0, eventState.getT0(), 0.0);
        Assert.assertEquals(handler.g(0.0, yStart), eventState.getG0(), 0.0);
        Assert.assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        Mockito.when(handler.g(Mockito.anyDouble(), Mockito.anyDouble())).thenThrow(new EventException());
        eventState.reinitializeBegin(0.0, new double[]{1.0, 2.0});
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepHandlerAdapter();
        eventState.evaluateStep(interpolator);
        Assert.assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testEvaluateStepWithEventAtBeginning() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(handler.g(Mockito.anyDouble(), Mockito.any(double[].class))).thenReturn(0.0).thenReturn(1.0).thenReturn(-1.0);
        eventState.reinitializeBegin(0.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepHandlerAdapter();
        interpolator.setCurrentTime(0.001);
        eventState.evaluateStep(interpolator);
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(0.001, eventState.getPendingEventTime(), 0.0);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(handler.g(Mockito.anyDouble(), Mockito.any(double[].class))).thenThrow(new DerivativeException());
        StepInterpolator interpolator = new StepHandlerAdapter();
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(handler.g(Mockito.anyDouble(), Mockito.any(double[].class))).thenThrow(new EventException());
        StepInterpolator interpolator = new StepHandlerAdapter();
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(handler.g(Mockito.anyDouble(), Mockito.any(double[].class))).thenThrow(new ConvergenceException());
        StepInterpolator interpolator = new StepHandlerAdapter();
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testGetEventTime() {
        Mockito.when(handler.g(Mockito.anyDouble(), Mockito.any(double[].class))).thenReturn(0.0).thenReturn(1.0).thenReturn(-1.0);
        eventState.reinitializeBegin(0.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepHandlerAdapter();
        interpolator.setCurrentTime(0.001);
        eventState.evaluateStep(interpolator);
        Assert.assertEquals(0.001, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double[] y = new double[]{1.0, 2.0};
        eventState.stepAccepted(0.01, y);
        Assert.assertEquals(0.01, eventState.getT0(), 0.0);
        Assert.assertEquals(handler.g(0.01, y), eventState.getG0(), 0.0);
        Assert.assertEquals(eventState.isG0Positive(), eventState.isForward());
        if (eventState.isPendingEvent()) {
            Assert.assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
        } else {
            Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        }
    }

    @Test
    public void testStop() {
        Assert.assertFalse(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double[] y = new double[]{1.0, 2.0};
        eventState.reset(0.01, y);
        Assert.assertEquals(0.01, eventState.getT0(), 0.0);
        Assert.assertEquals(handler.g(0.01, y), eventState.getG0(), 0.0);
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        Assert.assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testResetWithEventOccurred() throws EventException {
        Mockito.when(handler.g(Mockito.anyDouble(), Mockito.any(double[].class))).thenReturn(0.0).thenReturn(1.0).thenReturn(-1.0);
        eventState.reinitializeBegin(0.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepHandlerAdapter();
        interpolator.setCurrentTime(0.001);
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(0.002, new double[]{1.0, 2.0});
        double[] y = new double[]{1.5, 2.5};
        eventState.reset(0.003, y);
        Assert.assertEquals(0.003, eventState.getT0(), 0.0);
        Assert.assertEquals(handler.g(0.003, y), eventState.getG0(), 0.0);
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        Assert.assertFalse(eventState.isPendingEvent());
    }

    @Test(expected = MathRuntimeException.class)
    public void testResetWithMathRuntimeException() throws EventException {
        Mockito.when(handler.resetState(Mockito.anyDouble(), Mockito.any(double[][].class))).thenThrow(new MathRuntimeException());
        double[] y = new double[]{1.0, 2.0};
        eventState.reset(0.01, y);
    }
}