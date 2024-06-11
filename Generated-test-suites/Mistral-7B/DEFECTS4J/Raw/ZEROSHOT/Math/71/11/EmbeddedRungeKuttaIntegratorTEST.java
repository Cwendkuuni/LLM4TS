package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RateEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the Embedded Runge Kutta method of order 4.
     */
    @Test
    public void testRK4() {
        final double a = 1.0;
        final double b = 0.0;
        final double c = -1.0;
        final double[] c1 = { 0.0, 0.5, 0.5, 1.0 };
        final double[][] a1 = { { 0.0, 0.5, 0.5, 1.0 }, { 0.25, 0.6, 0.6, 1.0 },
                               { 0.25, 0.5, 0.75, 1.0 }, { 0.25, 0.41666666666666667, 0.8333333333333333, 1.0 } };
        final double[] b1 = { 0.25, 0.4, 0.6, 0.8 };
        final double minStep = 0.00001;
        final double maxStep = 10.0;
        final double scalAbsoluteTolerance = 1e-12;
        final double scalRelativeTolerance = 1e-12;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorFactory()
                .newInstance("RK4", true, c1, a1, b1, new RungeKuttaStepInterpolator(), minStep, maxStep,
                        scalAbsoluteTolerance, scalRelativeTolerance);

        // test the integrator with a simple differential equation
        double[] y0 = { 1.0 };
        double[] y = new double[y0.length];
        double t = 1.0;
        double[] expected = new double[] { 1.0, 1.0 };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i];
                }
            }
        };

        double step = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Integration step size is not correct", step, 1.0, 1e-12);
        assertArrayEquals("Initial state not correctly computed", expected, y, 1e-12);

        // test the integrator with an event
        RateEvent event = new RateEvent(1.5, 2.0);
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertEquals("Unexpected event time", 1.5, context.getStepSize(), 1e-12);
                assertEquals("Unexpected event index", 0, stepNumber);
            }
        };
        integrator.addStepHandler(handler);

        t = 1.5;
        y = new double[y0.length];
        y0 = y.clone();
        integrator.reset();
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected event not triggered", 1, handler.getStepNumber());

        t = 2.0;
        y = new double[y0.length];
        y0 = y.clone();
        integrator.reset();
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected event not triggered", 2, handler.getStepNumber());

        // test the integrator with a second order differential equation
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * (a - b * y[i]);
                }
            }
        };

        t = 2.0;
        y = new double[y0.length];
        y0 = y.clone();
        integrator.reset();
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                BesselFunctions.iacv(1, a), y[0], 1e-12);

        // test the integrator with a differential equation that has no solution
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = 1.0 / (y[i] * y[i] + 1.0);
                }
            }
        };

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have thrown an exception");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        // test the integrator with a differential equation that has a singularity
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    if (y[i] == 0.0) {
                        throw new IllegalArgumentException("y[" + i + "] cannot be zero");
                    }
                    yPrime[i] = y[i] / (y[i] * y[i] + 1.0);
                }
            }
        };

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test the integrator with a differential equation that has a singular point
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    if (y[i] == 0.0) {
                        yPrime[i] = Double.NaN;
                    } else {
                        yPrime[i] = y[i] / (y[i] * y[i] + 1.0);
                    }
                }
            }
        };

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // integrate with a large step size
        integrator = new EmbeddedRungeKuttaIntegratorFactory()
                .newInstance("RK4", true, c1, a1, b1, new RungeKuttaStepInterpolator(), minStep, maxStep,
                        scalAbsoluteTolerance, scalRelativeTolerance);

        integrator.setMaxGrowth(100.0);

        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        // integrate with a small step size
        integrator.setMaxGrowth(1.0);

        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                BesselFunctions.iacv(1, a), y[0], 1e-12);

        // test the integrator with a differential equation that has a singularity at infinity
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = 1.0 / Math.sqrt(y[i]);
                }
            }
        };

        try {
            integrator.integrate(equations, -1.0, y0, t, y);
            fail("Integration should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            integrator.integrate(equations, 0.0, y0, Double.POSITIVE_INFINITY, y);
            fail("Integration should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            integrator.integrate(equations, 0.0, y0, Double.NEGATIVE_INFINITY, y);
            fail("Integration should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test the integrator with a differential equation that has a singularity at a finite value
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    if (y[i] == 1.0) {
                        throw new IllegalArgumentException("y[" + i + "] cannot be 1.0");
                    }
                    yPrime[i] = 1.0 / (y[i] * y[i] + 1.0);
                }
            }
        };

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test the integrator with a differential equation that has a singularity at a finite value but is allowed
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    if (y[i] == 1.0) {
                        yPrime[i] = Double.NaN;
                    } else {
                        yPrime[i] = 1.0 / (y[i] * y[i] + 1.0);
                    }
                }
            }
        };

        integrator.setMaxGrowth(100.0);

        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        integrator.reset();
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        integrator.setMinReduction(0.0);

        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        integrator.reset();
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        integrator.setMinReduction(1.0e-12);

        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        integrator.reset();
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        integrator.setMinReduction(1.0e-24);

        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

        integrator.reset();
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals("Unexpected state at end of integration",
                Double.NaN, y[0], 1e-12);

    }

}