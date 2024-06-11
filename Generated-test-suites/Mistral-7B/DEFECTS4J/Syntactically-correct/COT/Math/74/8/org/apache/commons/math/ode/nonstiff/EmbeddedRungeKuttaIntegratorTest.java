package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test suite for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator_standard() {
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false,
                        new double[] { 0.3, 0.4, 0.1, 0.2 },
                        new double[][] { { 0.2376368261, -0.0535483953 },
                                { 0.4677536525, 0.5066626932 },
                                { 0.3822983619, 0.6127401985 },
                                { 0.1749658693, 0.7315808529 },
                                { 0.0, 1.0 } },
                        new double[] { 0.2376368261, 0.7711960246 },
                        new RungeKuttaStepInterpolator(), 0.01, 10.0,
                        1.0e-12, 1.0e-12) {

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1,
                                        double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        // Test integration of simple equation
        double[] y0 = new double[] { 1.0 };
        double t0 = 0.0;
        double[] y1 = new double[] { 2.0 };
        double t1 = 1.0;
        double[] y2 = new double[y0.length];

        integrator.integrate(new SimpleDifferentialEquations(), t0, y0, t1, y2);

        // Check the result
        Assert.assertArrayEquals(y1, y2, 1e-12);

        // Test integration with an event
        double[] y3 = new double[] { 1.0 };
        double t2 = 1.5;
        double[] y4 = new double[y0.length];
        integrator.reset();
        integrator.setEquations(new SimpleDifferentialEquationsWithEvent());
        integrator.integrate(t0, y0, t2, y4);

        // Check the result
        Assert.assertEquals(2.0, y4[0], 1e-12);

        // Test integration with step handlers
        double[] y5 = new double[] { 1.0 };
        double t3 = 2.0;
        double[] y6 = new double[y0.length];
        integrator.reset();
        integrator.setEquations(new SimpleDifferentialEquationsWithStepHandlers());
        integrator.integrate(t0, y0, t3, y6);

        // Check the result
        Assert.assertEquals(3.0, y6[0], 1e-12);

        // Test integration with events and step handlers
        double[] y7 = new double[] { 1.0 };
        double t4 = 2.5;
        double[] y8 = new double[y0.length];
        integrator.reset();
        integrator.setEquations(new SimpleDifferentialEquationsWithEventAndStepHandlers());
        integrator.integrate(t0, y0, t4, y8);

        // Check the result
        Assert.assertEquals(4.0, y8[0], 1e-12);
    }

    @Test(expected = IntegratorException.class)
    public void testEmbeddedRungeKuttaIntegrator_eventReached() {
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false,
                        new double[] { 0.3, 0.4, 0.1, 0.2 },
                        new double[][] { { 0.2376368261, -0.0535483953 },
                                { 0.4677536525, 0.5066626932 },
                                { 0.3822983619, 0.6127401985 },
                                { 0.1749658693, 0.7315808529 },
                                { 0.0, 1.0 } },
                        new double[] { 0.2376368261, 0.7711960246 },
                        new RungeKuttaStepInterpolator(), 0.01, 10.0,
                        1.0e-12, 1.0e-12) {

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1,
                                        double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        double[] y0_1 = new double[] { 1.0 };
        double t0_1 = 0.0;
        double[] y1_1 = new double[] { 2.0 };
        double t1_1 = 1.0;

        integrator.integrate(new SimpleDifferentialEquations(), t0_1, y0_1, t1_1, y1_1);

        integrator.setEquations(new SimpleDifferentialEquationsWithEvent());
        double t2_1 = 1.5;
        integrator.integrate(t0_1, y0_1, t2_1, null);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_edgeCases() {
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false,
                        new double[] { 0.3, 0.4, 0.1, 0.2 },
                        new double[][] { { 0.2376368261, -0.0535483953 },
                                { 0.4677536525, 0.5066626932 },
                                { 0.3822983619, 0.6127401985 },
                                { 0.1749658693, 0.7315808529 },
                                { 0.0, 1.0 } },
                        new double[] { 0.2376368261, 0.7711960246 },
                        new RungeKuttaStepInterpolator(), 0.01, 10.0,
                        1.0e-12, 1.0e-12) {

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1,
                                        double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[0][i] - yDotK[yDotK.length - 1][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        // Test integration with negative step size
        try {
            integrator.integrate(new SimpleDifferentialEquations(), 0.0, new double[] { 1.0 },
                                1.0, new double[1]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test integration with zero step size
        try {
            integrator.integrate(new SimpleDifferentialEquations(), 0.0, new double[] { 1.0 },
                                0.0, new double[1]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test integration with very small step size
        integrator.integrate(new SimpleDifferentialEquations(), 0.0, new double[] { 1.0 },
                            1.0e-20, new double[1]);

        // Test integration with very large step size
        integrator.integrate(new SimpleDifferentialEquations(), 0.0, new double[] { 1.0 },
                            1.0e20, new double[1]);

        // Test integration with tolerance too small
        integrator.setTolerance(1.0e-25);
        integrator.integrate(new SimpleDifferentialEquations(), 0.0, new double[] { 1.0 },
                            1.0, new double[1]);
    }

    private static class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getNumberOfEquations() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0] * 0.5;
        }
    }

    private static class SimpleDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

        @Override
        public int getNumberOfEquations() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0] * 0.5;
        }

        @Override
        public Event[] getEvents(double t, double[] y, double h) {
            if (Math.abs(t - 1.5) < Math.ulp(1.5)) {
                return new Event[] { new When(1.5, 1.0) };
            }
            return new Event[0];
        }

    }

    private static class SimpleDifferentialEquationsWithStepHandlers implements FirstOrderDifferentialEquations {

        @Override
        public int getNumberOfEquations() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0] * 0.5;
        }

        @Override
        public void handleStep(StepHandler handler, int stepNumber) {
            handler.println("Step " + stepNumber);
        }

    }

    private static class SimpleDifferentialEquationsWithEventAndStepHandlers implements FirstOrderDifferentialEquations {

        @Override
        public int getNumberOfEquations() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0] * 0.5;
        }

        @Override
        public Event[] getEvents(double t, double[] y, double h) {
            if (Math.abs(t - 1.5) < Math.ulp(1.5)) {
                return new Event[] { new When(1.5, 1.0) };
            }
            return new Event[0];
        }

        @Override
        public void handleStep(StepHandler handler, int stepNumber) {
            handler.println("Step " + stepNumber);
        }

    }
}