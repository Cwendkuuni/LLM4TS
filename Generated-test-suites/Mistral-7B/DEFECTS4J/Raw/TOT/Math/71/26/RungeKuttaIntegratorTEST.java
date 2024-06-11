package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test that the integrator correctly integrates a simple equation.
     */
    @Test
    public void testSimpleIntegration() {
        // create a simple equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        // create a Runge-Kutta integrator
        final double[] c = { 0.5, 0.5 };
        final double[][] a = { { 0.0, 0.5 }, { 0.5, 0.0 } };
        final double[] b = { 0.5, 0.5 };
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), 0.001) {
            @Override
            public void initializeState(double t, double[] y) {
                y[0] = 0.0;
            }
        };

        // perform the integration
        final double tEnd = 10.0;
        final double[] yEnd = { 1.0 };
        try {
            final double result = integrator.integrate(equations, 0.0, null, tEnd, yEnd);
            Assert.assertEquals(tEnd, result, 1e-9);
            Assert.assertArrayEquals(new double[] { 1.0 }, yEnd, 1e-9);
        } catch (IntegratorException | DerivativeException e) {
            Assert.fail("Failed to integrate equation: " + e.getMessage());
        }
    }

    /**
     * Test that the integrator correctly handles an event.
     */
    @Test
    public void testEventIntegration() {
        // create a simple equation with an event
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivative(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }

            @Override
            public void addEvent(double t, double[] value, String type, Object data) {
                if ("my_event".equals(type)) {
                    throw new RelativetimeEvent(t + 2.0);
                }
            }
        };

        // create a Runge-Kutta integrator and add an event handler
        final double[] c = { 0.5, 0.5 };
        final double[][] a = { { 0.0, 0.5 }, { 0.5, 0.0 } };
        final double[] b = { 0.5, 0.5 };
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), 0.001) {
            @Override
            public void initializeState(double t, double[] y) {
                y[0] = 0.0;
            }

            @Override
            public void handleEvent(RelativetimeEvent event) {
                System.out.println("Event encountered at time " + event.getTime());
            }
        };
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                System.out.println("Step " + stepNumber + " accepted, time " + context.getStepSize());
            }
        });

        // perform the integration
        final double tEnd = 5.0;
        final double[] yEnd = { 1.0 };
        try {
            final double result = integrator.integrate(equations, 0.0, null, tEnd, yEnd);
            Assert.assertEquals(tEnd, result, 1e-9);
            Assert.assertArrayEquals(new double[] { 1.0 }, yEnd, 1e-9);
            System.out.println("Integration finished");
        } catch (IntegratorException | DerivativeException | NumberIsTooSmallException e) {
            Assert.fail("Failed to integrate equation: " + e.getMessage());
        }
    }

    /**
     * Test that the integrator correctly handles an illegal argument.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArgument() {
        // create a Runge-Kutta integrator with an illegal argument
        final double[] c = { 0.5, 0.5 };
        final double[][] a = { { 0.0, 0.5 }, { 0.5, 0.0 } };
        final double[] b = { 0.5, 0.5 };
        new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), -0.001);
    }

}