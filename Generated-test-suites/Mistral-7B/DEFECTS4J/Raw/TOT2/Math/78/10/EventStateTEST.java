package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EventState} class.
 */
public class EventStateTest {

    /** Event handler used for testing. */
    private static EventHandler handler;

    /** EventState object under test. */
    private EventState eventState;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        handler = new DummyEventHandler();
        eventState = new EventState(handler, 0.01, 0.01, 10);
    }

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        EventState eventState = new EventState(handler, 0.01, 0.01, 10);
        assertNotNull(eventState);
    }

    /**
     * Test getEventHandler method.
     */
    @Test
    public void testGetEventHandler() {
        EventState eventState = new EventState(handler, 0.01, 0.01, 10);
        assertEquals(handler, eventState.getEventHandler());
    }

    /**
     * Test getMaxCheckInterval method.
     */
    @Test
    public void testGetMaxCheckInterval() {
        EventState eventState = new EventState(handler, 0.01, 0.01, 10);
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
    }

    /**
     * Test getConvergence method.
     */
    @Test
    public void testGetConvergence() {
        EventState eventState = new EventState(handler, 0.01, 0.01, 10);
        assertEquals(0.01, eventState.getConvergence(), 0.0);
    }

    /**
     * Test getMaxIterationCount method.
     */
    @Test
    public void testGetMaxIterationCount() {
        EventState eventState = new EventState(handler, 0.01, 0.01, 10);
        assertEquals(10, eventState.getMaxIterationCount());
    }

    /**
     * Test reinitializeBegin method with valid arguments.
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(1.0, new double[]{1.0});
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(handler.g(1.0, new double[]{1.0}), eventState.getG0(), 0.0);
    }

    /**
     * Test evaluateStep method with valid arguments.
     */
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addStep(1.0, new double[]{1.0}, new double[]{1.1, 1.2});

        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(-1.0, eventState.getPendingEventTime(), 0.0);
    }

    /**
     * Test evaluateStep method with invalid arguments.
     */
    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException()
        throws DerivativeException, EventException, ConvergenceException {

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addStep(1.0, new double[]{1.0}, new double[]{1.1, 1.2});
        interpolator.setInterpolatedTime(Double.NaN);

        eventState.evaluateStep(interpolator);
    }

    /**
     * Test evaluateStep method with EventException.
     */
    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException()
        throws DerivativeException, EventException, ConvergenceException {

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addStep(1.0, new double[]{1.0}, new double[]{1.1, 1.2});
        interpolator.setEventDetected(true);

        eventState.evaluateStep(interpolator);
    }

    /**
     * Test evaluateStep method with ConvergenceException.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException()
        throws DerivativeException, EventException, ConvergenceException {

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addStep(1.0, new double[]{1.0}, new double[]{1.1, 1.2});
        interpolator.setAvailable(false);

        eventState.evaluateStep(interpolator);
    }

    /**
     * Test getEventTime method.
     */
    @Test
    public void testGetEventTime() {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addStep(1.0, new double[]{1.0}, new double[]{1.1, 1.2});
        eventState.evaluateStep(interpolator);
        assertEquals(-1.0, eventState.getPendingEventTime(), 0.0);

        interpolator.setEventDetected(true);
        eventState.evaluateStep(interpolator);
        assertEquals(1.1, eventState.getPendingEventTime(), 0.0);
    }

    /**
     * Test stepAccepted method with valid arguments.
     */
    @Test
    public void testStepAccepted() throws EventException {
        double[] y = new double[]{1.0};
        double t = 2.0;

        eventState.stepAccepted(t, y);
        assertEquals(2.0, eventState.getT0(), 0.0);
        assertEquals(handler.g(2.0, y), eventState.getG0(), 0.0);
    }

    /**
     * Test stepAccepted method with EventException.
     */
    @Test(expected = EventException.class)
    public void testStepAcceptedWithEventException()
        throws EventException {

        double[] y = new double[]{1.0};
        double t = 2.0;

        eventState.pendingEvent = true;
        eventState.stepAccepted(t, y);
    }

    /**
     * Test stop method.
     */
    @Test
    public void testStop() {
        assertTrue(eventState.stop());
    }

    /**
     * Test reset method with valid arguments.
     */
    @Test
    public void testReset() throws EventException {
        double t = 1.0;
        double[] y = new double[]{1.0};

        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
    }

    /**
     * Test reset method with EventException.
     */
    @Test(expected = EventException.class)
    public void testResetWithEventException() throws EventException {
        double t = 1.0;
        double[] y = new double[]{1.0};

        eventState.pendingEvent = true;
        eventState.reset(t, y);
    }
}

/**
 * Dummy event handler for testing.
 */
class DummyEventHandler implements EventHandler {

    /**
     * Returns the event handler value for a given time and state vector.
     * @param time current time
     * @param state current state vector
     * @return event handler value
     */
    @Override
    public double g(double time, double[] state) {
        return Math.sin(time) * Math.sin(time);
    }

    /**
     * Resets the state of the event handler.
     * @param time new time value
     * @param state new state vector
     * @throws EventException if the state cannot be reseted
     */
    @Override
    public void resetState(double time, double[] state) throws EventException {
        // No-op
    }

    /**
     * Event occurs.
     * @param time time of the event
     * @param state state vector at the event
     * @param forward integration direction
     * @return next action
     */
    @Override
    public int eventOccurred(double time, double[] state, boolean forward) {
        return EventHandler.RESET_STATE;
    }
}