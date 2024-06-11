package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.step.StepInterpolator;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link EventState} class.
 */
public class EventStateTest {

    /** Event handler. */
    private EventHandler handler;

    /** Event state. */
    private EventState eventState;

    /** Maximal time interval between events handler checks. */
    private double maxCheckInterval;

    /** Convergence threshold for event localization. */
    private double convergence;

    /** Upper limit in the iteration count for event localization. */
    private int maxIterationCount;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        handler = new MockEventHandler();
        maxCheckInterval = 1.0e-6;
        convergence = 1.0e-6;
        maxIterationCount = 10;
        eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
    }

    /**
     * Test method {@link EventState#EventState(EventHandler, double, double, int)}.
     */
    @Test
    public void testConstructor() {
        assertNotNull(eventState);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 1e-12);
        assertEquals(convergence, eventState.getConvergence(), 1e-12);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
    }

    /**
     * Test method {@link EventState#reinitializeBegin(double, double[])}.
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        double[] state = new double[2];
        eventState.reinitializeBegin(0.0, state);
        assertEquals(Double.NaN, eventState.getT0(), 1e-12);
        assertEquals(Double.NaN, eventState.getG0(), 1e-12);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        state[0] = 1.0;
        state[1] = E.E;
        eventState.reinitializeBegin(1.0, state);
        assertEquals(1.0, eventState.getT0(), 1e-12);
        assertEquals(E.E, eventState.getG0(), 1e-12);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method {@link EventState#evaluateStep(StepInterpolator)}.
     */
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setCurrentTime(1.0);

        double[] state = new double[2];
        state[0] = 1.0;
        state[1] = E.E;
        interpolator.setInterpolatedState(state);

        handler.setEvent(1.0, state, 1.0);

        eventState.reinitializeBegin(1.0, state);

        // no event
        assertFalse(eventState.evaluateStep(interpolator));
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // event
        interpolator.setCurrentTime(1.2);
        handler.setEvent(1.1, state, 1.0);
        assertEquals(1.1, eventState.getPendingEventTime(), 1e-12);
        assertTrue(eventState.isPendingEvent());
        assertEquals(EventHandler.EVENT_FOUND, eventState.getNextAction());

        // event found during previous call
        interpolator.setCurrentTime(1.15);
        eventState.stepAccepted(1.2, state);
        assertEquals(1.1, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isPendingEvent());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // event missed
        interpolator.setCurrentTime(1.05);
        eventState.evaluateStep(interpolator);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // event found on wrong side of step
        interpolator.setCurrentTime(1.3);
        eventState.evaluateStep(interpolator);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // convergence during step
        interpolator.setCurrentTime(1.15);
        handler.setEvent(1.17, state, 1.0);
        eventState.evaluateStep(interpolator);
        assertEquals(EventHandler.EVENT_LOCATED, eventState.getNextAction());
        assertEquals(1.17, eventState.getPendingEventTime(), 1e-12);
        assertTrue(eventState.isPendingEvent());
    }

    /**
     * Test method {@link EventState#stepAccepted(double, double[])}.
     */
    @Test
    public void testStepAccepted() throws EventException {
        double[] state = new double[2];
        state[0] = 1.0;
        state[1] = E.E;

        eventState.stepAccepted(1.0, state);
        assertEquals(1.0, eventState.getT0(), 1e-12);
        assertEquals(E.E, eventState.getG0(), 1e-12);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        handler.setEvent(1.5, state, 1.0);
        eventState.stepAccepted(1.5, state);
        assertEquals(1.5, eventState.getT0(), 1e-12);
        assertEquals(E.E, eventState.getG0(), 1e-12);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

        handler.setEvent(2.0, state, 1.0);
        eventState.stepAccepted(2.0, state);
        assertEquals(2.0, eventState.getT0(), 1e-12);
        assertEquals(0.0, eventState.getG0(), 1e-12);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());

        handler.setEvent(2.5, state, 1.0);
        state[1] = E.E;
        eventState.stepAccepted(2.5, state);
        assertEquals(2.5, eventState.getT0(), 1e-12);
        assertEquals(E.E, eventState.getG0(), 1e-12);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

}