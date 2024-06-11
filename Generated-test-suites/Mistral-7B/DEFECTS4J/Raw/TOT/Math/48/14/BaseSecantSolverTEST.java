package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link RegulaFalsiSolver} method.
     *
     * @throws MaxEvalException if the maximum number of evaluations is exceeded
     */
    @Test
    public void testRegulaFalsi() throws MaxEvalException {
        final double x = BaseSecantSolver.solve(10, new QuadraticFunction(1.0, 2.0, 3.0),
                                               1.0, 2.0, 1.5);
        Assert.assertEquals("RegulaFalsi: wrong root", 1.16159765625, x, 1e-5);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link IllinoisSolver} method.
     *
     * @throws MaxEvalException if the maximum number of evaluations is exceeded
     * @throws PrecisionException if the solution could not be found within the given accuracy
     */
    @Test
    public void testIllinois() throws MaxEvalException, PrecisionException {
        final double x = BaseSecantSolver.solve(10, new QuadraticFunction(1.0, 2.0, 3.0),
                                               1.0, 2.0, 1.5);
        Assert.assertEquals("Illinois: wrong root", 1.16159765625, x, 1e-5);

        final double x2 = BaseSecantSolver.solve(10, new QuadraticFunction(-1.0, -2.0, 1.0),
                                                1.0, 2.0, 1.5);
        Assert.assertEquals("Illinois: wrong root for negative function", -1.16159765625, x2, 1e-5);

        final double x3 = BaseSecantSolver.solve(10, new QuadraticFunction(-1.0, 2.0, 1.0),
                                                1.0, 2.0, 1.5);
        Assert.assertEquals("Illinois: wrong root for inverted function", -1.16159765625, x3, 1e-5);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link PegasusSolver} method.
     *
     * @throws MaxEvalException if the maximum number of evaluations is exceeded
     * @throws PrecisionException if the solution could not be found within the given accuracy
     */
    @Test
    public void testPegasus() throws MaxEvalException, PrecisionException {
        final double x = BaseSecantSolver.solve(10, new QuadraticFunction(1.0, 2.0, 3.0),
                                               1.0, 2.0, 1.5);
        Assert.assertEquals("Pegasus: wrong root", 1.16159765625, x, 1e-5);

        final double x2 = BaseSecantSolver.solve(10, new QuadraticFunction(-1.0, -2.0, 1.0),
                                                1.0, 2.0, 1.5);
        Assert.assertEquals("Pegasus: wrong root for negative function", -1.16159765625, x2, 1e-5);

        final double x3 = BaseSecantSolver.solve(10, new QuadraticFunction(-1.0, 2.0, 1.0),
                                                1.0, 2.0, 1.5);
        Assert.assertEquals("Pegasus: wrong root for inverted function", -1.16159765625, x3, 1e-5);
    }

    /**
     * A simple quadratic function for testing.
     */
    private static class QuadraticFunction implements UnivariateRealFunction {
        private final double a, b, c;

        public QuadraticFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }
    }
}