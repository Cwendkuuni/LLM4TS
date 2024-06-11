package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test class for EventState.
 */
public class EventStateTest {

    /** Event handler instance. */
    private EventHandler handler;

    /** EventState under test. */
    private EventState eventState;

    /**
     * Sets up a new test instance.
     */
    @Before
    public void setUp() {
        handler = new MockEventHandler();
        eventState = new EventState(handler, 0.001, 0.001, 10);
    }

    /**
     * Test method reinitializeBegin().
     */
    @Test
    public void testReinitializeBegin() {
        eventState.reinitializeBegin(0.0, new double[0]);
        assertEquals(0.0, eventState.gett0(), 0.0);
        assertEquals(Double.NaN, eventState.getg0(), 0.0);
        assertTrue(eventState.isg0Positive());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 0.0);
        assertEquals(Double.NaN, eventState.getPreviousEventTime(), 0.0);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method evaluateStep().
     */
    @Test
    public void testEvaluateStep() {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setCurrentTime(0.01);

        try {
            eventState.evaluateStep(interpolator);
            fail("An event should have been triggered.");
        } catch (DerivativeException e) {
            // expected
        } catch (EventException e) {
            fail("No EventException expected.");
        } catch (ConvergenceException e) {
            fail("No ConvergenceException expected.");
        }

        interpolator.setCurrentTime(0.02);
        assertTrue(eventState.isPendingEvent());
        assertEquals(0.02, eventState.getPendingEventTime(), 0.0);
        assertEquals(0.01, eventState.getPreviousEventTime(), 0.0);
        assertFalse(eventState.isg0Positive());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.EVENT_TRIGGERED, eventState.getNextAction());
    }

    /**
     * Test method getEventTime().
     */
    @Test
    public void testGetEventTime() {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setCurrentTime(0.01);

        try {
            eventState.evaluateStep(interpolator);
            fail("An event should have been triggered.");
        } catch (DerivativeException e) {
            // expected
        } catch (EventException e) {
            fail("No EventException expected.");
        } catch (ConvergenceException e) {
            fail("No ConvergenceException expected.");
        }

        assertEquals(0.02, eventState.getEventTime(), 0.0);
    }

    /**
     * Test method stepAccepted().
     */
    @Test
    public void testStepAccepted() {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setCurrentTime(0.02);

        try {
            eventState.evaluateStep(interpolator);
            fail("An event should have been triggered.");
        } catch (DerivativeException e) {
            // expected
        } catch (EventException e) {
            fail("No EventException expected.");
        } catch (ConvergenceException e) {
            fail("No ConvergenceException expected.");
        }

        double[] state = new double[1];
        eventState.stepAccepted(0.025, state);
        assertEquals(0.025, eventState.gett0(), 0.0);
        assertEquals(handler.g(0.025, state), eventState.getg0(), 0.0);
        assertTrue(eventState.isg0Positive());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 0.0);
        assertEquals(0.02, eventState.getPreviousEventTime(), 0.0);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method stop().
     */
    @Test
    public void testStop() {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setCurrentTime(0.01);

        try {
            eventState.evaluateStep(interpolator);
            fail("An event should have been triggered.");
        } catch (DerivativeException e) {
            // expected
        } catch (EventException e) {
            fail("No EventException expected.");
        } catch (ConvergenceException e) {
            fail("No ConvergenceException expected.");
        }

        interpolator.setCurrentTime(0.03);
        assertTrue(eventState.isPendingEvent());
        assertEquals(0.03, eventState.getPendingEventTime(), 0.0);
        assertEquals(0.02, eventState.getPreviousEventTime(), 0.0);
        assertFalse(eventState.isg0Positive());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.EVENT_TRIGGERED, eventState.getNextAction());

        eventState.stepAccepted(0.035, new double[0]);

        assertTrue(eventState.stop());
    }

    /**
     * Test method reset().
     */
    @Test
    public void testReset() {
        eventState.reinitializeBegin(0.0, new double[0]);
        eventState.evaluateStep(new StepInterpolator());
        assertTrue(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPreviousEventTime(), 0.0);
        assertTrue(eventState.reset(0.01, new double[0]));
        assertFalse(eventState.isPendingEvent());
    }

}