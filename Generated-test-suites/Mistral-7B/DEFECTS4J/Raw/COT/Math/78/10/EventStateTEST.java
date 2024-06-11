package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.SingularFunctionException;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.solvers.BrentSolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EventStateTest {

    private EventState eventState;
    private EventHandler mockEventHandler;
    private StepInterpolator mockInterpolator;

    @Before
    public void setUp() {
        mockEventHandler = Mockito.mock(EventHandler.class);
        mockInterpolator = Mockito.mock(StepInterpolator.class);
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 100);
    }

    @Test
    public void testConstructorWithValidArguments() {
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(0.001, eventState.getConvergence(), 0.0);
        Assert.assertEquals(100, eventState.getMaxIterationCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEventHandler() {
        new EventState(null, 0.01, 0.001, 100);
    }

    @Test
    public void testReinitializeBeginWithValidArguments() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        Assert.assertEquals(tStart, eventState.getT0(), 0.0);
        Assert.assertArrayEquals(yStart, eventState.getY0(), 0.0);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.anyArray(Double.class)))
               .thenThrow(new EventException());
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 100);
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStepWithNoEvent() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.anyArray(Double.class)))
               .thenReturn(1.0);
        eventState.reinitializeBegin(1.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolator(new StepInterpolator.PolynomialInterpolator(3));
        boolean result = eventState.evaluateStep(interpolator);
        Assert.assertFalse(result);
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.anyArray(Double.class)))
               .thenReturn(0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0);
        eventState.reinitializeBegin(1.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolator(new StepInterpolator.PolynomialInterpolator(12));
        boolean result = eventState.evaluateStep(interpolator);
        Assert.assertTrue(result);
        Assert.assertEquals(2.0, eventState.getPendingEventTime(), 0.0);
    }

    @Test(expected = PrecisionException.class)
    public void testEvaluateStepWithEventTooClose() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.anyArray(Double.class)))
               .thenReturn(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        eventState.reinitializeBegin(1.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolator(new StepInterpolator.PolynomialInterpolator(12));
        boolean result = eventState.evaluateStep(interpolator);
        Assert.assertTrue(result);
        Assert.assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
    }

    @Test(expected = SingularFunctionException.class)
    public void testEvaluateStepWithSingularFunction() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.anyArray(Double.class)))
               .thenThrow(new SingularFunctionException());
        eventState.reinitializeBegin(1.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolator(new StepInterpolator.PolynomialInterpolator(12));
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testEvaluateStepWithMaxIterationsExceeded() throws DerivativeException, EventException, ConvergenceException {
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.anyArray(Double.class)))
               .thenReturn(0.0);
        eventState.reinitializeBegin(1.0, new double[]{1.0, 2.0});
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolator(new StepInterpolator.PolynomialInterpolator(1));
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testGetEventTime() {
        double pendingEventTime = 2.0;
        eventState.setPendingEventTime(pendingEventTime);
        Assert.assertEquals(pendingEventTime, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAcceptedWithValidArguments() throws EventException {
        double t = 3.0;
        double[] y = new double[]{3.0, 4.0};
        eventState.stepAccepted(t, y);
        Assert.assertEquals(t, eventState.getT0(), 0.0);
        Assert.assertArrayEquals(y, eventState.getY0(), 0.0);
        Assert.assertFalse(eventState.pendingEvent);
        Assert.assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        Assert.assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 4.0;
        double[] y = new double[]{4.0, 5.0};
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(2.0);
        eventState.setNextAction(EventHandler.RESET_STATE);
        boolean result = eventState.reset(t, y);
        Assert.assertFalse(eventState.pendingEvent);
        Assert.assertEquals(EventHandler.RESET_STATE, eventState.nextAction);
        if (result) {
            Assert.assertArrayEquals(new double[]{0.0, 0.0}, eventState.getY0(), 0.0);
        }

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        result = eventState.reset(t, y);
        Assert.assertFalse(eventState.pendingEvent);
        Assert.assertEquals(EventHandler.RESET_DERIVATIVES, eventState.nextAction);
    }
}