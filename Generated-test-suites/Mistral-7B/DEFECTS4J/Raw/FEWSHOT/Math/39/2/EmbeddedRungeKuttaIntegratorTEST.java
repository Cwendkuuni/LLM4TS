package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.UnivariateFunction;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    /**
     * Test for EmbeddedRungeKuttaIntegrator with a simple function: f(x) = x<sup>2</sup>.
     */
    @Test
    public void testSimpleFunction() throws MathIllegalArgumentException {
        final double a = 0.0;
        final double b = 1.0;
        final double stepSize = 0.01;
        final int order = 5;
        final double[] vectol = {1.0e-9, 1.0e-10};
        final double[] vectolRel = {1.0e-5, 1.0e-6};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(order);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestEmbeddedRungeKuttaIntegratorTest", false, new double[]{0.3, 0.48, 0.3, 0.0},
                new double[][]{{0.68, 0.5, 0.36}, {0.168, 0.6, 0.276}, {0.054, 0.2, 0.846}, {0.344, 0.0, 0.922}},
                new double[]{0.68, 0.168, 0.054, 0.344}, prototype, stepSize, stepSize, vectol, vectolRel) {
            @Override
            public int getOrder() {
                return order;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(y1[i] - y0[i], 2);
                }
                return Math.sqrt(error);
            }
        };

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        final ExpandableStatefulODE equations = new ExpandableStatefulODE(function) {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void setTime(double time) {
                super.setTime(time);
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{getTime()};
            }
        };

        integrator.integrate(equations, 0.0);
        assertEquals(0.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, 2.0);
        assertEquals(4.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, 3.0);
        assertEquals(9.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, 4.0);
        assertEquals(16.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, 5.0);
        assertEquals(25.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, 10.0);
        assertEquals(100.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, 100.0);
        assertEquals(10000.0, equations.getCompleteState()[0], Precision.DOUBLE_EPSILON);
    }

    /**
     * Test for EmbeddedRungeKuttaIntegrator with a more complex function:
     * f(x, y) = y * sin(x).
     */
    @Test
    public void testComplexFunction() throws MathIllegalArgumentException {
        final double a = 0.0;
        final double b = 2.0 * Math.PI;

        final double stepSize = 0.01;
        final int order = 5;
        final double[] vectol = {1.0e-9, 1.0e-10};
        final double[] vectolRel = {1.0e-5, 1.0e-6};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(order);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestEmbeddedRungeKuttaIntegratorTestComplex", false, new double[]{0.3, 0.48, 0.3, 0.0},
                new double[][]{{0.68, 0.5, 0.36}, {0.168, 0.6, 0.276}, {0.054, 0.2, 0.846}, {0.344, 0.0, 0.922}},
                new double[]{0.68, 0.168, 0.054, 0.344}, prototype, stepSize, stepSize, vectol, vectolRel) {
            @Override
            public int getOrder() {
                return order;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(y1[i] - y0[i], 2);
                }
                return Math.sqrt(error);
            }
        };

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        final ExpandableStatefulODE equations = new ExpandableStatefulODE(new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[1] * Math.sin(x[0]);
            }

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void setTime(double time) {
                super.setTime(time);
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{getTime(), getState()[0]};
            }
        }) {
            @Override
            public double[] getState() {
                return new double[]{getTime()};
            }
        };

        integrator.integrate(equations, new double[]{0.0, 1.0});
        assertEquals(1.0, equations.getState()[1], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, new double[]{2.0, 1.5});
        assertEquals(1.5 * Math.sin(2.0), equations.getState()[1], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, new double[]{3.5, 2.0});
        assertEquals(2.0 * Math.sin(3.5), equations.getState()[1], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, new double[]{5.0, 3.0});
        assertEquals(3.0 * Math.sin(5.0), equations.getState()[1], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, new double[]{6.5, 4.0});
        assertEquals(4.0 * Math.sin(6.5), equations.getState()[1], Precision.DOUBLE_EPSILON);

        integrator.integrate(equations, new double[]{8.0, 5.0});
        assertEquals(5.0 * Math.sin(8.0), equations.getState()[1], Precision.DOUBLE_EPSILON);
    }
}