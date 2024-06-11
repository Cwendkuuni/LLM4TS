package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest extends AbstractTest {

    /**
     * Test a simple case with a quadratic function.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testQuadraticFunction() throws Exception {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                double result = 0;
                for (int i = 0; i < x.length; i++) {
                    result += x[i] * x[i];
                }
                return result;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-8);
    }

    /**
     * Test a case with a non-quadratic function.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testNonQuadraticFunction() throws Exception {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                double result = 0;
                for (int i = 0; i < x.length; i++) {
                    result += Math.sin(x[i]);
                }
                return result;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3);
        PointValuePair solution = optimizer.optimize(function);
        // The exact solution depends on the starting point.
        // Here we expect a good approximation.
        assertArrayEquals(new double[]{Math.PI / 2, Math.PI / 2}, solution.getPoint(), 1e-3);
    }

    /**
     * Test a case with a function that has multiple local minima.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testMultipleLocalMinima() throws Exception {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                double result = 0;
                for (int i = 0; i < x.length; i++) {
                    result += Math.pow(x[i] - 1, 2);
                }
                return result;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3);
        PointValuePair solution = optimizer.optimize(function);
        // The exact solution depends on the starting point.
        // Here we expect a good approximation.
        assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 1e-3);
    }

    /**
     * Test a case with a function that has no solution.
     *
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoSolution() throws Exception {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI) / Math.sin(Math.PI);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function);
        fail("Expected ConvergenceException");
    }
}