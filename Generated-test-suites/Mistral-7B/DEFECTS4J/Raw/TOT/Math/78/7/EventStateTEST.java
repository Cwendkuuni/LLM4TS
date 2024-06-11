package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

public class EventStateTest {

    @org.junit.Test
    public void testEventState() {
        // Create an event handler that always returns a fixed value
        EventHandler handler = new MockEventHandler(1.0);

        // Create an event state with the given parameters
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 10;
        EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        // Test initialization
        assertEquals(handler, state.getEventHandler());
        assertEquals(maxCheckInterval, state.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, state.getConvergence(), 0.0);
        assertEquals(maxIterationCount, state.getMaxIterationCount());
        assertNull(state.getEventHandler().getState());
        assertNull(state.getEventHandler().getPreviousEventTime());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // Test reinitialization
        double tStart = 0.0;
        double[] yStart = new double[1];
        try {
            state.reinitializeBegin(tStart, yStart);
            assertEquals(tStart, state.getT0(), 0.0);
            assertEquals(handler.g(tStart, yStart), state.getG0(), 0.0);
            assertTrue(state.isG0Positive());
            assertNull(state.getPendingEventTime());
            assertFalse(state.isPendingEvent());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test evaluation of step
        double h = 0.01;
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedTime(tStart);
        interpolator.addStep(tStart, h);
        try {
            boolean eventOccurred = state.evaluateStep(interpolator);
            assertFalse(eventOccurred);
            assertEquals(EventHandler.CONTINUE, state.getNextAction());
            assertNull(state.getPendingEventTime());
            assertFalse(state.isPendingEvent());
        } catch (DerivativeException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test evaluation of step with event
        double tEvent = tStart + convergence;
        yStart[0] = 1.0;
        handler.setExpectedEvent(tEvent);
        interpolator.setInterpolatedState(yStart);
        try {
            interpolator.addStep(tEvent, h);
            interpolator.setInterpolatedTime(tEvent);
            state.reinitializeBegin(tStart, yStart);
            boolean eventOccurred = state.evaluateStep(interpolator);
            assertTrue(eventOccurred);
            assertEquals(tEvent, state.getPendingEventTime(), 0.0);
            assertTrue(state.isPendingEvent());
            assertEquals(EventHandler.EVENT_FOUND, state.getNextAction());
        } catch (DerivativeException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test step acceptance
        double tAccepted = tEvent;
        double[] yAccepted = new double[1];
        try {
            state.stepAccepted(tAccepted, yAccepted);
            assertEquals(tAccepted, state.getT0(), 0.0);
            assertEquals(handler.g(tAccepted, yAccepted), state.getG0(), 0.0);
            assertTrue(state.isG0Positive() == handler.isExpectedEventPositive());
            assertFalse(state.isPendingEvent());
            assertNull(state.getPendingEventTime());
            assertEquals(EventHandler.CONTINUE, state.getNextAction());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test step rejection due to event
        handler.setExpectedEvent(tStart + convergence);
        try {
            state.stepAccepted(tAccepted, yAccepted);
            fail("Expected EventException");
        } catch (EventException e) {
            assertEquals(EventHandler.EVENT_FOUND, e.getEventType());
            assertEquals(tStart + convergence, e.getEventTime(), 0.0);
        }

        // Test step rejection due to convergence
        handler.setExpectedEvent(Double.NaN);
        try {
            interpolator.addStep(tAccepted + convergence, h);
            interpolator.setInterpolatedTime(tAccepted + convergence);
            state.reinitializeBegin(tAccepted, yAccepted);
            boolean eventOccurred = state.evaluateStep(interpolator);
            assertTrue(eventOccurred);
            assertEquals(tAccepted + convergence, state.getPendingEventTime(), 0.0);
            assertTrue(state.isPendingEvent());
            assertEquals(EventHandler.EVENT_FOUND, state.getNextAction());
            state.stepAccepted(tAccepted + convergence, yAccepted);
            assertEquals(EventHandler.EVENT_FOUND, state.getNextAction());
            fail("Expected ConvergenceException");
        } catch (DerivativeException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (EventException e) {
            if (e.getEventType() == EventHandler.EVENT_FOUND) {
                assertEquals(tAccepted + convergence, e.getEventTime(), 0.0);
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test reset
        try {
            state.reset(tAccepted + convergence, yAccepted);
            assertNull(state.getPendingEventTime());
            assertFalse(state.isPendingEvent());
            assertEquals(EventHandler.CONTINUE, state.getNextAction());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

    }

    static class MockEventHandler implements EventHandler {

        private double expectedEventTime;
        private boolean expectedEventPositive;

        public MockEventHandler(double expectedEventTime) {
            this.expectedEventTime = expectedEventTime;
            this.expectedEventPositive = true;
        }

        @Override
        public double g(double t, double[] y) throws EventException {
            if (Double.isNaN(expectedEventTime)) {
                return 0.0;
            } else if (t == expectedEventTime) {
                return expectedEventPositive ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            } else {
                return 0.0;
            }
        }

        @Override
        public void resetState(double t, double[] y) throws EventException {
            // empty
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean isLocalEvent) throws EventException {
            if (isLocalEvent != expectedEventPositive) {
                throw new EventException("Unexpected sign of event.");
            }
            expectedEventTime = Double.NaN;
        }

        public void setExpectedEvent(double expectedEventTime) {
            this.expectedEventTime = expectedEventTime;
        }

    }

}