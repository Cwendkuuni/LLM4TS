package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.MockEventHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;

public class EventStateTest {

    @Test
    public void testEventState() {
        // Test with default values
        EventState state = new EventState(new MockEventHandler(), 0.01, 0.001, 10);
        assertEquals(MockEventHandler.class, state.getEventHandler().getClass());
        assertEquals(0.01, state.getMaxCheckInterval(), Precision.tolerance(1e-9));
        assertEquals(0.001, state.getConvergence(), Precision.tolerance(1e-9));
        assertEquals(10, state.getMaxIterationCount());
        assertTrue(Double.isNaN(state.getT0()));
        assertTrue(Double.isNaN(state.getG0()));
        assertTrue(true, state.isG0Positive());
        assertFalse(state.hasPendingEvent());
        assertNull(state.getPendingEventTime());
        assertNull(state.getPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // Test with custom values
        EventState state2 = new EventState(new MockEventHandler(), 0.02, 0.005, 5);
        assertEquals(0.02, state2.getMaxCheckInterval(), Precision.tolerance(1e-9));
        assertEquals(0.005, state2.getConvergence(), Precision.tolerance(1e-9));
        assertEquals(5, state2.getMaxIterationCount());

        // Test reinitializeBegin method
        double[] yStart = new double[]{1.0, 2.0};
        try {
            state.reinitializeBegin(0.1, yStart);
            assertEquals(0.1, state.getT0(), Precision.tolerance(1e-9));
            assertEquals(MockEventHandler.g(0.1, yStart), state.getG0(), Precision.tolerance(1e-9));
            assertTrue(true, state.isG0Positive());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test evaluateStep method
        StepInterpolator interpolator = new StepInterpolator(new MockDerivativeFunction(), 0.1, 0.2);
        try {
            state.evaluateStep(interpolator);
            assertFalse(state.hasPendingEvent());
            assertNull(state.getPendingEventTime());
        } catch (ConvergenceException | DerivativeException | EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test evaluateStep method with an event
        interpolator = new StepInterpolator(new MockDerivativeFunctionWithEvent(0.15), 0.1, 0.2);
        try {
            state.evaluateStep(interpolator);
            assertTrue(state.hasPendingEvent());
            assertEquals(0.15, state.getPendingEventTime(), Precision.tolerance(1e-9));
        } catch (ConvergenceException | DerivativeException | EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test stepAccepted method
        double[] y = new double[]{3.0, 4.0};
        try {
            state.stepAccepted(0.2, y);
            assertEquals(0.2, state.getT0(), Precision.tolerance(1e-9));
            assertEquals(MockEventHandler.g(0.2, y), state.getG0(), Precision.tolerance(1e-9));
            if (state.hasPendingEvent()) {
                assertEquals(EventHandler.ACTION, state.getNextAction());
            } else {
                assertEquals(EventHandler.CONTINUE, state.getNextAction());
            }
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test stop method
        assertTrue(state.stop());

        // Test reset method
        double tReset = 0.3;
        try {
            state.reset(tReset, new double[]{5.0, 6.0});
            assertEquals(tReset, state.getT0(), Precision.tolerance(1e-9));
            assertEquals(MockEventHandler.resetState(tReset, new double[]{5.0, 6.0}), state.getEventHandler().getState(), new double[]{5.0, 6.0});
            if (state.hasPendingEvent()) {
                assertEquals(EventHandler.RESET_STATE, state.getNextAction());
            } else {
                assertEquals(EventHandler.CONTINUE, state.getNextAction());
            }
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test reset method with resetDerivatives
        state = new EventState(new MockEventHandler(), 0.01, 0.001, 10);
        state.hasPendingEvent();
        try {
            state.reset(tReset, new double[]{5.0, 6.0, 0.0, 0.0});
            assertEquals(tReset, state.getT0(), Precision.tolerance(1e-9));
            assertEquals(MockEventHandler.resetState(tReset, new double[]{5.0, 6.0}), state.getEventHandler().getState(), new double[]{5.0, 6.0});
            assertEquals(EventHandler.RESET_STATE, state.getNextAction());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            state.reset(tReset, new double[]{5.0, 6.0, 0.0, 0.1});
            fail("Expected EventException");
        } catch (EventException e) {
        }
    }
}

class MockEventHandler implements EventHandler {

    public double g(double t, double[] y) {
        return 2.0 * Math.sin(t) - 1.0;
    }

    public void eventOccurred(double t, double[] y, boolean forward) throws EventException {
        // Do nothing
    }

    public void resetState(double t, double[] y) {
        // Do nothing
    }
}

class MockDerivativeFunction implements UnivariateRealFunction {

    public double value(double t) throws FunctionEvaluationException {
        return Math.sin(t);
    }
}

class MockDerivativeFunctionWithEvent extends MockDerivativeFunction {

    private final double eventTime;

    public MockDerivativeFunctionWithEvent(double eventTime) {
        this.eventTime = eventTime;
    }

    public double value(double t) throws FunctionEvaluationException {
        if (Double.isNaN(t)) {
            throw new FunctionEvaluationException("Invalid input", t);
        }
        if (t == eventTime) {
            throw new EventException("Event occurred at t = " + eventTime);
        }
        return Math.sin(t);
    }
}