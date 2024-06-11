package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Create a test double function
        FirstOrderDifferentialEquations equations = new AbstractDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(t) + 0.1 * y[0];
            }
        };

        // Create a test integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.25, 0 }, { 0.422580625, 0.25 }, { 0.34482619047619 }, { 0.201151385220256 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 }, new DummyStepInterpolator()) {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        // Test the integrator with default settings
        double[] y0 = new double[] { 0.1 };
        double t0 = 0.0;
        double tEnd = 10.0;
        double stepSize = 0.01;
        double[] y = new double[y0.length];
        double result = integrator.integrate(equations, t0, y0, tEnd, y);
        assertEquals(tEnd, result, 1e-8);
        assertArrayEquals(y0, y, 1e-8);

        // Test the integrator with specified tolerance
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.25, 0 }, { 0.422580625, 0.25 }, { 0.34482619047619 }, { 0.201151385220256 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 }, new DummyStepInterpolator(), 0.001, 0.01);
        integrator.integrate(equations, t0, y0, tEnd, y);
        assertEquals(tEnd, result, 1e-9);
        assertArrayEquals(y0, y, 1e-9);

        // Test the integrator with events handler
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.25, 0 }, { 0.422580625, 0.25 }, { 0.34482619047619 }, { 0.201151385220256 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 }, new DummyStepInterpolator(), 0.0, 10.0, null);
        StepInterpolator interpolator = integrator.getStepInterpolator();
        integrator.addStepHandler(t -> System.out.println("Step at: " + t));
        integrator.integrate(equations, t0, y0, tEnd, y);
        assertTrue(interpolator.size() > 0);

        // Test the integrator with fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true, new double[] { 0.2, 0.3, 0.3, 0.2, 0.1, 0.1 },
                new double[][] { { 0.2, 0 }, { 0.275, 0.45 }, { 0.045, 0.36 }, { 0.0, 0.45 }, { 0.132, 0.4986124883122 },
                        { 0.0, 0.4366881463493 } }, new double[] { 0.2, 0.3, 0.3, 0.2, 0.1, 0.1 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(int index, double t) {
                        return super.interpolate(index, t) * Math.exp(Math.log(2.0));
                    }
                });
        integrator.integrate(equations, t0, y0, tEnd, y);
        assertEquals(Math.exp(Math.sin(tEnd) + 0.1 * y[0]), y[0], 1e-8);
    }

}