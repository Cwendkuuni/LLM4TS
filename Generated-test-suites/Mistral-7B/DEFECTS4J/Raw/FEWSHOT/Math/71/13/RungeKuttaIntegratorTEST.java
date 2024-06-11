package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

/**
 * Test cases for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {
    @Test
    public void testSetup() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(0.5), 0.01) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        assertNotNull(integrator);
        assertEquals("RK2", integrator.getName());
        assertEquals(2, integrator.getOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new RungeKuttaIntegrator(null, null, null, null, null, 0.01);
    }

    @Test
    public void testIntegration() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(0.5), 0.01) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        double[] initialState = new double[] { 1.0, 1.0 };
        double startTime = 0.0;
        double endTime = 3.0;

        StepInterpolator interpolator = integrator.createInterpolator();
        StepHandler stepHandler = new StepHandler() {
            private int stepCount = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double[] y = interpolator.getStepY();
                double[] yDot = interpolator.getStepYPrime();
                assertArrayEquals(new double[] { Math.sin(y[0]), Math.cos(y[0]) }, y, 0.0);
                assertArrayEquals(new double[] { Math.cos(y[0]), -Math.sin(y[0]) }, yDot, 0.0);
                stepCount++;
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, startTime, initialState, endTime);

        assertEquals(10, stepHandler.getStepCount());
    }

    @Test
    public void testEventHandling() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(0.5), 0.01) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        double[] initialState = new double[] { 0.0, 1.0 };
        double startTime = 0.0;
        double endTime = 2.0 * Math.PI;

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        When event = new When() {
            @Override
            public boolean checkEvent(double t, double[] y) {
                return y[0] * y[0] + y[1] * y[1] > 1.0;
            }

            @Override
            public Event nextEvent(double t, double[] y, double h) {
                return new Event(t + h);
            }
        };
        eventsManager.addEvent(event);

        integrator.setEventsManager(eventsManager);

        StepInterpolator interpolator = integrator.createInterpolator();
        StepHandler stepHandler = new StepHandler() {
            private int stepCount = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double[] y = interpolator.getStepY();
                double[] yDot = interpolator.getStepYPrime();
                assertArrayEquals(y, initialState, 0.0);
                stepCount++;
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, startTime, initialState, endTime);

        assertEquals(1, stepHandler.getStepCount());
        assertNull(interpolator.getLastEvent());

        initialState[1] += 1.0;
        integrator.reset();
        integrator.integrate(equations, startTime, initialState, endTime);

        assertEquals(2, stepHandler.getStepCount());
        assertNotNull(interpolator.getLastEvent());
        assertEquals(interpolator.getLastEvent().getTime(), stepHandler.getLastStepTime(), 0.0);
    }

    @Test
    public void testDenseOutput() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK2", new double[] { 0.5 }, new double[][] { { 0.5, 0.5 } }, new double[] { 0.5 }, new RungeKuttaStepInterpolator(0.5), 0.01) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        double[] initialState = new double[] { 0.0, 1.0 };
        double startTime = 0.0;
        double endTime = 2.0 * Math.PI;

        integrator.setRequiresDenseOutput(true);

        StepInterpolator interpolator = integrator.createInterpolator();
        StepHandler stepHandler = new StepHandler() {
            private int stepCount = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double[] y = interpolator.getStepY();
                double[] yDot = interpolator.getStepYPrime();
                double t = interpolator.getStepTime();
                assertArrayEquals(new double[] { Math.sin(y[0]), Math.cos(y[0]) }, y, 0.0);
                assertArrayEquals(new double[] { Math.cos(y[0]), -Math.sin(y[0]) }, yDot, 0.0);
                assertEquals(t, stepHandler.getLastStepTime(), 0.0);
                stepCount++;
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, startTime, initialState, endTime);

        double[] result = interpolator.getStepY();
        double[] expected = new double[result.length * 10];
        System.arraycopy(initialState, 0, expected, 0, initialState.length);
        for (int i = 1; i < result.length * 10; i += result.length) {
            System.arraycopy(result, 0, expected, i, result.length);
        }
        assertArrayEquals(expected, interpolator.getAllStepsY(), 0.0);
    }
}