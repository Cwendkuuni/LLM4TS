package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noexception.NoExceptionStepsizeControl;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethod;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the basic functionality of the EmbeddedRungeKuttaIntegrator.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Given
        final String name = "testEmbeddedRungeKuttaIntegrator";
        final boolean fsal = true;
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.27, 0.45, 0.62 },
                                { 0.0, 0.45, 0.62, 0.78 },
                                { 0.0, 0.62, 0.78, 0.9 } };
        final double[] b = { 0.238, 0.678, 0.962 };
        final double minStep = 1.0e-6;
        final double maxStep = 1.0e3;
        final double scalAbsoluteTolerance = 1.0e-8;
        final double scalRelativeTolerance = 1.0e-6;
        final int order = 3;
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaMethods.RK4Integrator(name, fsal, c, a, b,
                new RungeKuttaStepInterpolator(new NoExceptionStepsizeControl(minStep, maxStep)), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        // When
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -5.0 * y[0];
            }
        };
        double t0 = 0.0;
        double[] y0 = { 1.0 };
        double t = 1.0;
        double[] y = new double[1];
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, result, 1.0e-8);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Then
        assertTrue(true);
    }

    /**
     * Test the behavior when the equations return NaN.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorNaN() {
        // Given
        final String name = "testEmbeddedRungeKuttaIntegratorNaN";
        final boolean fsal = true;
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.27, 0.45, 0.62 },
                                { 0.0, 0.45, 0.62, 0.78 },
                                { 0.0, 0.62, 0.78, 0.9 } };
        final double[] b = { 0.238, 0.678, 0.962 };
        final double minStep = 1.0e-6;
        final double maxStep = 1.0e3;
        final double scalAbsoluteTolerance = 1.0e-8;
        final double scalRelativeTolerance = 1.0e-6;
        final int order = 3;
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaMethods.RK4Integrator(name, fsal, c, a, b,
                new RungeKuttaStepInterpolator(new NoExceptionStepsizeControl(minStep, maxStep)), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        // When
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -5.0 * Math.sqrt(y[0]);
            }
        };
        double t0 = 0.0;
        double[] y0 = { 1.0 };
        double t = 1.0;
        double[] y = new double[1];
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Then
            assertTrue(e.getMessage().contains("Encountered NaN"));
        }

        // Test with initial NaN
        FirstOrderDifferentialEquations equations2 = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -5.0 * Math.sqrt(y[0]);
            }
        };
        double[] y0NaN = { Double.NaN };
        try {
            integrator.integrate(equations2, t0, y0NaN, t, y);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Then
            assertTrue(e.getMessage().contains("Initial value is NaN"));
        }

        // Test with NaN in the solution
        FirstOrderDifferentialEquations equations3 = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -5.0 * Math.sqrt(y[0]);
            }
        };
        double[] yNaN = new double[] { 1.0 / 0.0 };
        try {
            integrator.integrate(equations3, t0, yNaN, t, y);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Then
            assertTrue(e.getMessage().contains("Encountered NaN"));
        }

        // Test with NaN in the target
        double tNaN = Double.NaN;
        try {
            integrator.integrate(equations, t0, y0, tNaN, y);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Then
            assertTrue(e.getMessage().contains("Target time is NaN"));
        }

        // Then
        assertTrue(true);
    }

    /**
     * Test the behavior when the equations throw an exception.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorException() {
        // Given
        final String name = "testEmbeddedRungeKuttaIntegratorException";
        final boolean fsal = true;
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.27, 0.45, 0.62 },
                                { 0.0, 0.45, 0.62, 0.78 },
                                { 0.0, 0.62, 0.78, 0.9 } };
        final double[] b = { 0.238, 0.678, 0.962 };
        final double minStep = 1.0e-6;
        final double maxStep = 1.0e3;
        final double scalAbsoluteTolerance = 1.0e-8;
        final double scalRelativeTolerance = 1.0e-6;
        final int order = 3;
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaMethods.RK4Integrator(name, fsal, c, a, b,
                new RungeKuttaStepInterpolator(new NoExceptionStepsizeControl(minStep, maxStep)), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        // When
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new RuntimeException("Test exception");
            }
        };
        double t0 = 0.0;
        double[] y0 = { 1.0 };
        double t = 1.0;
        double[] y = new double[1];
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Expected DerivativeException");
        } catch (DerivativeException e) {
            // Then
            assertTrue(e.getMessage().contains("Test exception"));
        }

        // Then
        assertTrue(true);
    }

    /**
     * Test the behavior with a discontinuous function.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorDiscontinuous() {
        // Given
        final String name = "testEmbeddedRungeKuttaIntegratorDiscontinuous";
        final boolean fsal = true;
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.27, 0.45, 0.62 },
                                { 0.0, 0.45, 0.62, 0.78 },
                                { 0.0, 0.62, 0.78, 0.9 } };
        final double[] b = { 0.238, 0.678, 0.962 };
        final double minStep = 1.0e-6;
        final double maxStep = 1.0e3;
        final double scalAbsoluteTolerance = 1.0e-8;
        final double scalRelativeTolerance = 1.0e-6;
        final int order = 3;
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaMethods.RK4Integrator(name, fsal, c, a, b,
                new RungeKuttaStepInterpolator(new NoExceptionStepsizeControl(minStep, maxStep)), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        // When
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = (t > 0.5) ? 2.0 * y[0] : -2.0 * y[0];
            }
        };
        double t0 = 0.0;
        double[] y0 = { 1.0 };
        double t = 1.0;
        double[] y = new double[1];
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(1.0, result, 1.0e-6);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Then
        assertTrue(true);
    }

    /**
     * Test the behavior with a function with large derivatives.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorLargeDerivatives() {
        // Given
        final String name = "testEmbeddedRungeKuttaIntegratorLargeDerivatives";
        final boolean fsal = true;
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.27, 0.45, 0.62 },
                                { 0.0, 0.45, 0.62, 0.78 },
                                { 0.0, 0.62, 0.78, 0.9 } };
        final double[] b = { 0.238, 0.678, 0.962 };
        final double minStep = 1.0e-6;
        final double maxStep = 1.0e3;
        final double scalAbsoluteTolerance = 1.0e-8;
        final double scalRelativeTolerance = 1.0e-6;
        final int order = 3;
        EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaMethods.RK4Integrator(name, fsal, c, a, b,
                new RungeKuttaStepInterpolator(new NoExceptionStepsizeControl(minStep, maxStep)), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        // When
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0e10 * y[0];
            }
        };
        double t0 = 0.0;
        double[] y0 = { 1.0 };
        double t = 1.0;
        double[] y = new double[1];
        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Then
            assertTrue(e.getMessage().contains("Derivative is too large"));
        }

        // Then
        assertTrue(true);
    }

}