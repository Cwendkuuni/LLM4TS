package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator_String_boolean_doubleArray_doubleArray_doubleArray_RungeKuttaStepInterpolator_double_double_doubleArray_doubleArray() {
        final String name = "RK4";
        final boolean fsal = false;
        final double[] c = { 0, 0.25, 0.25, 0.5, 0.5, 0.5, 0.875, 0.875 };
        final double[][] a = { { 0.0, 0, 0, 1/6, 1/2, 1/2, 1, 1 },
                               { 1/6, 0, 0, 1/3, 1/4, 1/2, 1/2, 1/6 },
                               { 1/2, 1/2, 0, 1/4, 1/2, 1/2, 1/6, 1/3 },
                               { 1, 0.5, 0.5, 0, 0, 0, 0, 0 },
                               { 0.5, 1/2, 1/2, 0, 0, 0, 0, 0 },
                               { 1/2, 1/2, 1/2, 0, 0, 0, 0, 0 },
                               { 1, 1, 1, 0, 0, 0, 0, 0 },
                               { 1, 0, 0, 0, 0, 0, 0, 0 } };
        final double[] b = { 1/6, 1/3, 1/2, 1/2, 1, 1, 1, 1 };
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(name, 4, 2, c, a, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, fsal, c, a, b, prototype, 0.01, 0.04,
                                                                                 new double[] { 1e-8, 1e-8 },
                                                                                 new double[] { 1e-9, 1e-9 });

        // Test with a simple differential equation
        final ExpandableStatefulODE equations = new ExpandableStatefulODE("simpleODE") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };

        // Test with initial conditions
        double[] y0 = { 0.1 };
        double tEnd = 10;
        integrator.integrate(equations, tEnd);
        double[] yEnd = equations.getCompleteState();
        Assert.assertArrayEquals(yEnd, new double[] { Math.sin(y0[0] + 1) }, 1e-9);

        // Test with step size control
        y0 = new double[] { 0.5 };
        tEnd = 5;
        integrator.integrate(equations, tEnd);
        yEnd = equations.getCompleteState();
        Assert.assertArrayEquals(yEnd, new double[] { Math.sin(y0[0] + 0.5) }, 1e-9);

        // Test with error handling
        equations = new ExpandableStatefulODE("simpleODE_error") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                throw new MaxCountExceededException("Maximum number of iterations exceeded.");
            }
        };

        try {
            integrator.integrate(equations, 1);
            Assert.fail("Expected MathIllegalArgumentException.");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test with illegal arguments
        equations = new ExpandableStatefulODE("simpleODE_illegalArgs") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                throw new IllegalArgumentException("Illegal argument.");
            }
        };

        try {
            integrator.integrate(equations, 1);
            Assert.fail("Expected MathIllegalArgumentException.");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }
}