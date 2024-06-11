package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import junit.framework.TestCase.*;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.initialvalues.InitialValueProblem;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test a simple differential equation with a known solution.
     */
    public void testSimpleEquation() {
        double[][] coefficients = {{0.0, 0.5, 0.5}, {0.0, 0, 0.5, 0.5}, {0.0}};
        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKutta54Integrator(
                "Embedded Runge-Kutta 5(4)", false, coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(), 1e-10, 1e-10, null, null);

        double[] stepsizes = {0.1, 0.2, 0.3, 0.5, 0.8};
        double[] t = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5};
        double[] y0 = {1.0, 2.0};
        double[] y  = new double[2];

        for (int i = 0; i < stepsizes.length; ++i) {
            integrator.integrate(
                new FirstOrderDifferentialEquations() {
                    @Override
                    public int getDimension() {
                        return y0.length;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y, double[] yPrime) {
                        for (int j = 0; j < y.length; ++j) {
                            yPrime[j] = y[j] * (1.0 - y[j]);
                        }
                    }
                }, t[i], y0, t[i + 1], y);
            assertArrayEquals(new double[] {Math.exp(t[i + 1] - t[i]), Math.exp(2 * (t[i + 1] - t[i]))}, y);
        }

        StepInterpolator interpolator = integrator.getInterpolator();
        for (int i = 0; i < stepsizes.length; ++i) {
            double tInterpolated = interpolator.interpolate(t[i], 0);
            double[] yInterpolated = interpolator.interpolate(t[i], y);
            double error = Math.abs(yInterpolated[0] - Math.exp(t[i]));
            assertTrue("Error is too large: " + error, error <= 1e-6);
        }
    }

    /**
     * Test a differential equation with singularities.
     */
    public void testSingularity() {
        double[][] coefficients = {{0.0, 0.5, 0.5, 0.5, 0.5}, {0.0, 0, 0.5, 0.5, 0.5, 0.5},
                {0.0}, {0.0, 1.0 / 3.0, 2.0 / 3.0, 1.0, 1.0 / 3.0, 2.0 / 3.0}, {0.0}};
        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKutta78Integrator(
                "Embedded Runge-Kutta 7(8)", true, coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(), 1e-12, 1e-12, null, null);

        double[] stepsizes = {0.01, 0.02, 0.03, 0.05, 0.08, 0.1, 0.15};
        double[] t = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        double[] y0 = {1.0, 1.0};
        double[] y  = new double[2];
        double[] x  = new double[2];

        for (int i = 0; i < stepsizes.length; ++i) {
            integrator.integrate(
                new FirstOrderDifferentialEquations() {
                    @Override
                    public int getDimension() {
                        return y0.length;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y, double[] yPrime) {
                        x[0] = Math.sqrt(y[0]);
                        x[1] = Math.sqrt(y[1]);
                        for (int j = 0; j < y.length; ++j) {
                            yPrime[j] = -1.0 / (4.0 * Math.pow(x[j], 2))
                                      + Math.log(x[j]) / (2.0 * Math.sqrt(Math.PI));
                        }
                    }
                }, t[i], y0, t[i + 1], y);
            assertArrayEquals(
                new double[] {BesselFunctions.besselI(1.0, Math.sqrt(t[i + 1] - t[i])) / Math.sqrt(t[i + 1] - t[i]),
                               BesselFunctions.besselI(2.0, Math.sqrt(t[i + 1] - t[i])) / Math.sqrt(t[i + 1] - t[i])},
                y);
        }

        StepInterpolator interpolator = integrator.getInterpolator();
        for (int i = 0; i < stepsizes.length; ++i) {
            double tInterpolated = interpolator.interpolate(t[i], 0);
            double[] yInterpolated = interpolator.interpolate(t[i], y);
            double error = Math.abs(yInterpolated[0] - BesselFunctions.besselI(1.0, Math.sqrt(t[i + 1] - t[i])))
                         / Math.sqrt(t[i + 1] - t[i]);
            assertTrue("Error is too large: " + error, error <= 1e-6);
            error = Math.abs(yInterpolated[1] - BesselFunctions.besselI(2.0, Math.sqrt(t[i + 1] - t[i])))
                   / Math.sqrt(t[i + 1] - t[i]);
            assertTrue("Error is too large: " + error, error <= 1e-6);
        }
    }

    /**
     * Test a differential equation with a singular Jacobian.
     */
    public void testSingularJacobian() {
        double[][] coefficients = {{0.0, 0.5, 0.5}, {0.0, 0, 0.5, 0.5}, {0.0}};
        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKutta54Integrator(
                "Embedded Runge-Kutta 5(4)", false, coefficients[0], coefficients[1], coefficients[2],
                new RungeKuttaStepInterpolator(), 1e-10, 1e-10, null, null);

        double[] stepsizes = {0.1, 0.2, 0.3, 0.5, 0.8};
        double[] t = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5};
        double[] y0 = {0.0, 1.0};
        double[] y  = new double[2];

        try {
            integrator.integrate(
                new FirstOrderDifferentialEquations() {
                    @Override
                    public int getDimension() {
                        return y0.length;
                    }

                    @Override
                    public void computeDerivatives(double t, double[] y, double[] yPrime) {
                        if (y[0] > 1e-6 || y[0] < -1e-6) {
                            throw new MathArithmeticException("Singular Jacobian");
                        }
                        for (int j = 0; j < y.length; ++j) {
                            yPrime[j] = Math.sin(y[j]);
                        }
                    }
                }, t[0], y0, t[1], y);
            fail("Exception not thrown");
        } catch (MathArithmeticException expected) {
            // expected
        }

        for (int i = 1; i < stepsizes.length; ++i) {
            try {
                integrator.integrate(
                    new FirstOrderDifferentialEquations() {
                        @Override
                        public int getDimension() {
                            return y0.length;
                        }

                        @Override
                        public void computeDerivatives(double t, double[] y, double[] yPrime) {
                            if (y[0] > 1e-6 || y[0] < -1e-6) {
                                throw new MathArithmeticException("Singular Jacobian");
                            }
                            for (int j = 0; j < y.length; ++j) {
                                yPrime[j] = Math.sin(y[j]);
                            }
                        }
                    }, t[i], y[0] > 0.0 ? y0 : y, t[i + 1], y);
                fail("Exception not thrown");
            } catch (MathArithmeticException expected) {
                // expected
            }
        }
    }

}