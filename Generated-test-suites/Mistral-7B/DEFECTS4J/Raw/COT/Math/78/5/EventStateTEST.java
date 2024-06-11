package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.TestEventHandler;
import org.apache.commons.math3.ode.step.StepInterpolator;
import org.junit.Test;

/**
 * JUnit 4 test cases for EventState class
 */
public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        // Test the constructor
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        assertNull(eventState.getEventHandler());
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.01, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getT0());
        assertNull(eventState.getG0());
        assertFalse(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEventStateReinitializeBegin() throws Exception {
        // Test the reinitializeBegin method
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        final double[] state = new double[1];
        double tStart = 1.0;
        double yStart = 2.0;
        eventState.reinitializeBegin(tStart, state);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(yStart, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testEventStateReinitializeBeginWithException() throws Exception {
        // Test the reinitializeBegin method with an exception
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        final double[] state = new double[1];
        double tStart = 1.0;
        eventState.reinitializeBegin(tStart, state);
        handler.setThrowException(true);
        eventState.handler.g(tStart, state);
    }

    @Test
    public void testEventStateEvaluateStepWithNoEvent() throws Exception {
        // Test the evaluateStep method with no event
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        final StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedState(new double[1]);
        assertFalse(eventState.evaluateStep(interpolator));
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testEventStateEvaluateStepWithEvent() throws Exception {
        // Test the evaluateStep method with an event
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        final StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedState(new double[1]);
        interpolator.setStateFunction(new UnivariateRealFunction() {
            @Override
            public double value(double t) {
                return Math.sin(t);
            }
        });
        interpolator.setInterpolatedTime(2.0);
        handler.setEventAt(2.5);
        assertTrue(eventState.evaluateStep(interpolator));
        assertTrue(eventState.isPendingEvent());
        assertEquals(2.5, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testEventStateGetEventTime() {
        // Test the getEventTime method
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        eventState.setPendingEventTime(2.5);
        assertEquals(2.5, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testEventStateStepAccepted() throws Exception {
        // Test the stepAccepted method
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        double[] state = new double[1];
        stepAccepted(eventState, 2.0, state);
        assertEquals(2.0, eventState.getT0(), 0.0);
        assertEquals(handler.g(2.0, state), eventState.getG0(), 0.0);
        if (eventState.isPendingEvent()) {
            assertTrue(eventState.isG0Positive());
            assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
        } else {
            assertTrue(eventState.isForward());
            assertTrue(eventState.isIncreasing());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        }
    }

    @Test
    public void testEventStateReset() throws Exception {
        // Test the reset method
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        double t = 2.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertArrayEquals(y, eventState.getInterpolatedState(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        if (eventState.getNextAction() == EventHandler.RESET_STATE) {
            assertEquals(y, handler.getState(), 0.0);
        }
    }

    @Test
    public void testEventStateStop() {
        // Test the stop method
        final EventHandler handler = new TestEventHandler();
        final EventState eventState = new EventState(handler, 1.0, 0.01, 10);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    private void stepAccepted(EventState eventState, double t, double[] y) throws Exception {
        eventState.stepAccepted(t, y);
    }

}

class TestEventHandler implements EventHandler {

    private boolean throwException;

    public void eventOccurred(double t, double[] y, boolean isForward) {
        if (throwException) {
            throw new RuntimeException();
        }
    }

    public void resetState(double t, double[] y) {
        // Do nothing
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    public double g(double t, double[] y) throws DerivativeException, EventException {
        if (throwException) {
            throw new RuntimeException();
        }
        return Math.sin(t);
    }

}