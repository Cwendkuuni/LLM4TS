package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKutta78Integrator;
import org.apache.commons.math3.ode.sampling.StepHandling;
import org.junit.Test;

public class EventStateTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        EventHandler dummyHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                // Dummy implementation
                return 0.0;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forwardCrossing) {
                // Dummy implementation
            }

            @Override
            public void resetState(double t, double[] y) {
                // Dummy implementation
            }
        };
        EventState state = new EventState(dummyHandler, 1.0, 1.0e-6, 10);
        assertNotNull(state.getEventHandler());
        assertEquals(1.0, state.getMaxCheckInterval(), 0.0);
        assertEquals(1.0e-6, state.getConvergence(), 0.0);
        assertEquals(10, state.getMaxIterationCount());
        assertNull(state.getEventTime());
        assertFalse(state.stop());
    }

    /**
     * Test reinitializeBegin.
     * @throws EventException if an error occurs during the test.
     */
    @Test
    public void testReinitializeBegin()
        throws EventException {
        EventHandler dummyHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return y[0];
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forwardCrossing) {
                // Dummy implementation
            }

            @Override
            public void resetState(double t, double[] y) {
                // Dummy implementation
            }
        };
        EventState state = new EventState(dummyHandler, 1.0, 1.0e-6, 10);
        double[] y = new double[] {1.0};
        state.reinitializeBegin(0.0, y);
        assertEquals(0.0, state.getEventHandler().g(0.0, y), 0.0);
        assertEquals(true, state.g0Positive);
    }

    /**
     * Test evaluateStep.
     * @throws EventException if an error occurs during the test.
     * @throws ConvergenceException if an error occurs during the test.
     * @throws DerivativeException if an error occurs during the test.
     */
    @Test
    public void testEvaluateStep()
        throws EventException, ConvergenceException, DerivativeException {
        EventHandler dummyHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return y[0] * (t - 1.0);
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forwardCrossing) {
                // Dummy implementation
            }

            @Override
            public void resetState(double t, double[] y) {
                // Dummy implementation
            }
        };
        EventState state = new EventState(dummyHandler, 1.0, 1.0e-6, 10);
        double[] y = new double[] {1.0};
        RungeKutta78Integrator integrator = new RungeKutta78Integrator();
        StepInterpolator interpolator = new StepInterpolator(integrator, StepHandling.EVENT_DRIVEN);
        interpolator.addStep(0.0, y);
        interpolator.addStep(1.0, y);
        interpolator.setInterpolatedTime(1.0);
        assertFalse(state.evaluateStep(interpolator));
        assertEquals(1.0, state.getEventHandler().g(1.0, y), 0.0);
        assertFalse(state.pendingEvent);
        assertNull(state.getEventTime());

        // Test with an event
        integrator = new RungeKutta78Integrator();
        interpolator = new StepInterpolator(integrator, StepHandling.EVENT_DRIVEN);
        interpolator.addStep(0.0, y);
        interpolator.addStep(1.5, y);
        interpolator.addStep(2.0, y);
        interpolator.setInterpolatedTime(1.5);
        state = new EventState(dummyHandler, 1.0, 1.0e-6, 10);
        state.reinitializeBegin(0.0, y);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getEventTime(), 0.0);
        assertTrue(state.stop());
    }

    /**
     * Test stepAccepted.
     * @throws EventException if an error occurs during the test.
     */
    @Test
    public void testStepAccepted()
        throws EventException {
        EventHandler dummyHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return y[0];
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forwardCrossing) {
                // Dummy implementation
            }

            @Override
            public void resetState(double t, double[] y) {
                // Dummy implementation
            }
        };
        EventState state = new EventState(dummyHandler, 1.0, 1.0e-6, 10);
        double[] y = new double[] {1.0};
        state.reinitializeBegin(0.0, y);
        // Test with no event
        state.stepAccepted(0.5, y);
        assertEquals(0.5, state.getEventHandler().g(0.5, y), 0.0);
        assertEquals(true, state.g0Positive);
        assertFalse(state.pendingEvent);
        assertNull(state.getEventTime());

        // Test with an event
        state.pendingEvent = true;
        state.pendingEventTime = 1.5;
        state.stepAccepted(1.5, y);
        assertEquals(1.5, state.getEventHandler().g(1.5, y), 0.0);
        assertEquals(false, state.g0Positive);
        assertEquals(1.5, state.getEventTime(), 0.0);
        assertFalse(state.stop());

        // Test with a reset event
        state.nextAction = EventHandler.RESET_STATE;
        state.pendingEvent = true;
        state.pendingEventTime = 1.5;
        state.stepAccepted(1.5, y);
        assertEquals(1.5, state.getEventHandler().g(1.5, y), 0.0);
        assertEquals(false, state.g0Positive);
        assertEquals(1.5, state.getEventTime(), 0.0);
        assertTrue(state.stop());
    }

}