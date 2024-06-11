package org.apache.commons.math.ode.events;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.SimpleEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    private EventState eventState;
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        eventHandler = new SimpleEventHandler();
        eventState = new EventState(eventHandler, 0.01, 0.001, 10);
    }

    @Test
    public void testReinitializeBegin() throws Exception {
        double[] y = new double[3];
        eventState.reinitializeBegin(0.0, y);
        assertEquals(eventHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNaN(eventState.getT0());
        assertNaN(eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertNaN(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEvaluateStep() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertFalse(eventState.hasPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertFalse(Double.isNaN(eventState.getT0()));
        assertFalse(Double.isNaN(eventState.getG0()));
        assertEquals(eventState.getG0(), eventHandler.g(eventState.getT0(), yStart), 0.0);
        assertTrue(eventState.isG0Positive());

        interpolator.setForward(false);
        eventState.evaluateStep(interpolator);

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertFalse(eventState.hasPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertFalse(Double.isNaN(eventState.getT0()));
        assertFalse(Double.isNaN(eventState.getG0()));
        assertEquals(eventState.getG0(), eventHandler.g(eventState.getT0(), yStart), 0.0);
        assertTrue(eventState.isG0Positive());

        interpolator.setForward(true);
        double g = eventHandler.g(t, y);
        eventState.setG0(g);
        eventState.setG0Positive(g >= 0);

        eventState.evaluateStep(interpolator);

        if (g >= 0) {
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } else {
            assertEquals(EventHandler.EVENT, eventState.getNextAction());
            assertTrue(eventState.hasPendingEvent());
            assertEquals(g, eventState.getPendingEventTime(), 0.0);
        }
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        interpolator.setException(new DerivativeException("Test"));
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        eventHandler.setThrowException(true);
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        eventState.setNextAction(EventHandler.FIND_EVENT);
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testStepAccepted() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(t + 0.01, y);

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertFalse(eventState.hasPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertFalse(Double.isNaN(eventState.getT0()));
        assertFalse(Double.isNaN(eventState.getG0()));
        assertEquals(eventState.getG0(), eventHandler.g(eventState.getT0(), yStart), 0.0);
        assertTrue(eventState.isG0Positive());
        assertEquals(t + 0.01, eventState.getT0(), 0.0);
        assertArrayEquals(y, eventState.getInterpolatedState(), 0.0);

        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(t + 0.01);
        eventState.setNextAction(EventHandler.EVENT);

        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(t + 0.01, y);

        assertEquals(EventHandler.EVENT, eventState.getNextAction());
        assertTrue(eventState.hasPendingEvent());
        assertEquals(t + 0.01, eventState.getPendingEventTime(), 0.0);
        assertFalse(Double.isNaN(eventState.getG0()));
        assertEquals(eventState.getG0(), eventHandler.g(eventState.getT0(), yStart), 0.0);
        assertTrue(eventState.isG0Positive());
        assertEquals(t + 0.01, eventState.getT0(), 0.0);
        assertArrayEquals(y, eventState.getInterpolatedState(), 0.0);
    }

    @Test
    public void testReset() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(t + 0.01);
        eventState.setNextAction(EventHandler.RESET_STATE);

        eventState.stepAccepted(t + 0.01, y);

        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
        assertFalse(eventState.hasPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertFalse(Double.isNaN(eventState.getT0()));
        assertFalse(Double.isNaN(eventState.getG0()));
        assertEquals(eventState.getG0(), eventHandler.g(eventState.getT0(), yStart), 0.0);
        assertTrue(eventState.isG0Positive());
        assertEquals(t, eventState.getT0(), 0.0);
        assertArrayEquals(yStart, eventState.getInterpolatedState(), 0.0);
    }

    @Test
    public void testStop() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(t + 0.01);
        eventState.setNextAction(EventHandler.STOP);

        eventState.stepAccepted(t + 0.01, y);

        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    @Test
    public void testResetDerivatives() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(t + 0.01);
        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);

        eventState.stepAccepted(t + 0.01, y);

        assertFalse(eventState.hasPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertFalse(Double.isNaN(eventState.getT0()));
        assertFalse(Double.isNaN(eventState.getG0()));
        assertEquals(eventState.getG0(), eventHandler.g(eventState.getT0(), yStart), 0.0);
        assertTrue(eventState.isG0Positive());
        assertEquals(t, eventState.getT0(), 0.0);
        assertArrayEquals(yStart, eventState.getInterpolatedState(), 0.0);
        assertArrayEquals(new double[3], eventState.getInterpolatedDerivative(), 0.0);
    }

    @Test
    public void testGetEventTime() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[3];
        double t = 1.0;
        interpolator.setInterpolatedState(y);
        interpolator.setInterpolatedTime(t);

        eventState.reinitializeBegin(0.0, y);
        double tStart = t;
        double[] yStart = y;

        eventState.evaluateStep(interpolator);

        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(t + 0.01);
        eventState.setNextAction(EventHandler.EVENT);

        eventState.stepAccepted(t + 0.01, y);

        assertEquals(t + 0.01, eventState.getEventTime(), 0.0);
    }
}