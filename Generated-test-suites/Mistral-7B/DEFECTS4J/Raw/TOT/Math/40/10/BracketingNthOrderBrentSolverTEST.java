package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxCountExceededError;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.function.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /** A simple function for testing. */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x - 2;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }

        @Override
        public double root() {
            return Math.sqrt(2.);
        }

        @Override
        public double rootDerivative() {
            return 0.5 * Math.sqrt(2.);
        }
    }

    /** A function with no bracketing interval. */
    private static class NoBracketingFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x >= 0) {
                return Math.pow(x, 3) - Math.pow(2, 3);
            } else {
                return -Math.pow(-x, 3) + Math.pow(2, 3);
            }
        }

        @Override
        public double derivative(double x) {
            if (x >= 0) {
                return 3 * Math.pow(x, 2);
            } else {
                return -3 * Math.pow(-x, 2);
            }
        }

        @Override
        public double root() {
            return 1.0;
        }

        @Override
        public double rootDerivative() {
            throw new UnsupportedOperationException();
        }
    }

    /** A function with a bracketing interval but the root is a NaN. */
    private static class NaNFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.log(Math.sin(Math.PI * x)) / x;
        }

        @Override
        public double derivative(double x) {
            return (Math.pi * (Math.cos(Math.PI * x) / x - 1. / x)) / (Math.sin(Math.PI * x) * Math.sin(Math.PI * x));
        }

        @Override
        public double root() {
            throw new NumberIsTooSmallException(0.0, 0.0);
        }

        @Override
        public double rootDerivative() {
            throw new NumberIsTooSmallException(0.0, 0.0);
        }
    }

    /** A function with a bracketing interval but the root is not finite. */
    private static class InfiniteFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) / Math.log(Math.abs(x) + 1.);
        }

        @Override
        public double derivative(double x) {
            return (2 * x / (Math.log(Math.abs(x) + 1.) * (Math.abs(x) + 1.)) - x * x * Math.log(Math.abs(x) + 1.) / Math.pow(Math.abs(x) + 1., 2));
        }

        @Override
        public double root() {
            throw new NumberIsTooSmallException(-1.);
        }

        @Override
        public double rootDerivative() {
            throw new NumberIsTooSmallException(-1.);
        }
    }

    /** A function with a very wide bracketing interval. */
    private static class WideIntervalFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) - Math.sin(Math.PI * x / 2.);
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x) + Math.cos(Math.PI * x / 2.) * Math.PI / 2.;
        }

        @Override
        public double root() {
            return Math.PI / 4.;
        }

        @Override
        public double rootDerivative() {
            return Math.sqrt(2.) / 2.;
        }
    }

    /** A function with a very narrow bracketing interval. */
    private static class NarrowIntervalFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2);
        }

        @Override
        public double root() {
            return 1.;
        }

        @Override
        public double rootDerivative() {
            return 0.;
        }
    }

    /** A function with a bracketing interval but with some arbitrary precision. */
    private static class PreciseFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) + Math.pow(Math.sin(10 * Math.PI * x), 2) / Math.pow(1000, 10);
        }

        @Override
        public double derivative(double x) {
            return 2 * x + 20 * Math.cos(10 * Math.PI * x) * Math.sin(10 * Math.PI * x) / Math.pow(1000, 10);
        }

        @Override
        public double root() {
            return Math.sqrt(1.);
        }

        @Override
        public double rootDerivative() {
            return 0.5;
        }
    }

    /** Tests the solver with a simple function. */
    @Test
    public void testSimpleFunction() {
        try {
            final TestFunction f = new TestFunction();
            final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 3);
            final double root = solver.solve(100, f, -10., 10.);
            assertEquals(Math.sqrt(2.), root, 1e-5);
        } catch (NumberIsTooSmallException e) {
            throw new RuntimeException(e);
        }
    }

    /** Tests the solver with a function having no bracketing interval. */
    @Test
    public void testNoBracketingFunction() {
        final NoBracketingFunction f = new NoBracketingFunction();
        assertThrows(NoBracketingException.class, () -> {
            new BracketingNthOrderBrentSolver(1e-4, 3).solve(100, f, -10., 10.);
        });
    }

    /** Tests the solver with a function having a NaN root. */
    @Test
    public void testNaNFunction() {
        final NaNFunction f = new NaNFunction();
        assertThrows(NumberIsTooSmallException.class, () -> {
            new BracketingNthOrderBrentSolver(1e-4, 3).solve(100, f, -10., 10.);
        });
    }

    /** Tests the solver with a function having a root that is not finite. */
    @Test(expected = MathInternalError.class)
    public void testInfiniteFunction() {
        final InfiniteFunction f = new InfiniteFunction();
        new BracketingNthOrderBrentSolver(1e-4, 3).solve(100, f, -10., 10.);
    }

    /** Tests the solver with a function having a very wide bracketing interval. */
    @Test
    public void testWideIntervalFunction() {
        final WideIntervalFunction f = new WideIntervalFunction();
        try {
            final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 3);
            final double root = solver.solve(100, f, -5., 5.);
            assertEquals(Math.PI / 4., root, 1e-3);
        } catch (NumberIsTooSmallException e) {
            throw new RuntimeException(e);
        }
    }

    /** Tests the solver with a function having a very narrow bracketing interval. */
    @Test
    public void testNarrowIntervalFunction() {
        final NarrowIntervalFunction f = new NarrowIntervalFunction();
        try {
            final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 3);
            final double root = solver.solve(100, f, -1., 1.);
            assertEquals(1., root, 1e-5);
        } catch (NumberIsTooSmallException e) {
            throw new RuntimeException(e);
        }
    }

    /** Tests the solver with a function having a bracketing interval but with arbitrary precision. */
    @Test
    public void testPreciseFunction() {
        try {
            final PreciseFunction f = new PreciseFunction();
            final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-12, 3);
            final double root = solver.solve(100, f, -10., 10.);
            assertEquals(Math.sqrt(1.), root, 1e-12);
        } catch (NumberIsTooSmallException | MaxCountExceededError e) {
            throw new RuntimeException(e);
        }
    }

    /** Tests the solver with an invalid maximal order. */
    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(-1, 3);
    }

    /** Tests the solver with an invalid allowed solution. */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAllowedSolution() {
        new BracketingNthOrderBrentSolver(1e-4, 3, AllowedSolution.values()[3]);
    }
}