package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private static final double EPSILON = 1e-12;
    private static final double MAX_CHECK_INTERVAL = 0.001;
    private static final double CONVERGENCE = 1e-6;
    private static final int MAX_ITERATION_COUNT = 20;

    private EventHandler handler;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        handler = new EventHandlerTest();
        interpolator = new StepInterpolator(new BrentSolver(), MAX_CHECK_INTERVAL);
    }

    @Test
    public void testEventStateConstruction() {
        EventState eventState = new EventState(handler, MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT);
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(MAX_CHECK_INTERVAL, eventState.getMaxCheckInterval(), EPSILON);
        Assert.assertEquals(CONVERGENCE, eventState.getConvergence(), EPSILON);
        Assert.assertEquals(MAX_ITERATION_COUNT, eventState.getMaxIterationCount(), EPSILON);
        Assert.assertTrue(Double.isNaN(eventState.getT0()));
        Assert.assertTrue(Double.isNaN(eventState.getG0()));
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        Assert.assertTrue(eventState.isPreviousEventTimeSet());
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEventStateReinitializeBegin() throws EventException {
        EventState eventState = new EventState(handler, MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT);
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        Assert.assertEquals(tStart, eventState.getT0(), EPSILON);
        Assert.assertEquals(handler.g(tStart, yStart), eventState.getG0(), EPSILON);
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        Assert.assertTrue(eventState.isPreviousEventTimeSet());
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEventStateEvaluateStep() throws DerivativeException, EventException, ConvergenceException, MaxEvalException, NoBracketingException {
        EventState eventState = new EventState(handler, MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT);
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(t0, y0);

        double t1 = 0.1;
        StepInterpolator stepInterpolator = new StepInterpolator(new BrentSolver(), MAX_CHECK_INTERVAL);
        stepInterpolator.setInterpolatedState(y0);
        stepInterpolator.setInterpolatedTime(t0);

        Assert.assertFalse(eventState.evaluateStep(stepInterpolator));

        t0 += 0.01;
        stepInterpolator.setInterpolatedTime(t0);
        double g1 = handler.g(t0, y0);
        Assert.assertFalse(g1 >= 0);

        Assert.assertTrue(eventState.evaluateStep(stepInterpolator));
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(t0, eventState.getPendingEventTime(), EPSILON);
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
    }

    @Test
    public void testEventStateStepAccepted() throws EventException {
        EventState eventState = new EventState(handler, MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT);
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(t0, y0);

        double t1 = 0.1;
        StepInterpolator stepInterpolator = new StepInterpolator(new BrentSolver(), MAX_CHECK_INTERVAL);
        stepInterpolator.setInterpolatedState(y0);
        stepInterpolator.setInterpolatedTime(t0);

        Assert.assertFalse(eventState.evaluateStep(stepInterpolator));

        t0 += 0.01;
        stepInterpolator.setInterpolatedTime(t0);
        double g1 = handler.g(t0, y0);
        Assert.assertFalse(g1 >= 0);

        Assert.assertTrue(eventState.evaluateStep(stepInterpolator));
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(t0, eventState.getPendingEventTime(), EPSILON);
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

        eventState.stepAccepted(t1, new double[]{1.1, 2.1});

        Assert.assertEquals(t1, eventState.getT0(), EPSILON);
        Assert.assertEquals(handler.g(t1, new double[]{1.1, 2.1}), eventState.getG0(), EPSILON);
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEventStateReset() throws EventException {
        EventState eventState = new EventState(handler, MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT);
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(t0, y0);

        double t1 = 0.1;
        StepInterpolator stepInterpolator = new StepInterpolator(new BrentSolver(), MAX_CHECK_INTERVAL);
        stepInterpolator.setInterpolatedState(y0);
        stepInterpolator.setInterpolatedTime(t0);

        Assert.assertFalse(eventState.evaluateStep(stepInterpolator));

        t0 += 0.01;
        stepInterpolator.setInterpolatedTime(t0);
        double g1 = handler.g(t0, y0);
        Assert.assertFalse(g1 >= 0);

        Assert.assertTrue(eventState.evaluateStep(stepInterpolator));
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(t0, eventState.getPendingEventTime(), EPSILON);
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

        Assert.assertTrue(eventState.reset(t1, new double[]{1.1, 2.1}));
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

}