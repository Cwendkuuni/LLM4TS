package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.interpolation.UnivariateSpline;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.junit.Test;

/**
 * Test cases for {@link BisectionSolver}.
 */
public class BisectionSolverTest {
    /**
     * A function with a single root.
     */
    private static final UnivariateRealFunction SIN_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
            return Math.sin(x) - 0.5;
        }
    };

    /**
     * A function with no roots.
     */
    private static final UnivariateRealFunction SQUARE_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
            return x * x;
        }
    };

    /**
     * A function with two roots.
     */
    private static final UnivariateRealFunction CUBIC_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
            return x * x * x - 2 * x + 1;
        }
    };

    /**
     * A function with an infinite number of roots.
     */
    private static final UnivariateRealFunction TAN_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
            return Math.tan(x);
        }
    };

    @Test
    public void testBisectionSolverSingleRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(SIN_FUNCTION, -3.0, 3.0);
        assertEquals(0.0, root, 1e-9);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverNoRoots() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(SQUARE_FUNCTION, -5.0, 5.0);
    }

    @Test
    public void testBisectionSolverTwoRoots() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double root1 = solver.solve(CUBIC_FUNCTION, -2.0, 2.0);
        double root2 = solver.solve(CUBIC_FUNCTION, -3.0, -1.0);
        assertArrayEquals(new double[] { -1.0, 1.0 }, new double[] { root1, root2 });
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverInfiniteRoots() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(TAN_FUNCTION, -3.0, 3.0);
    }

    @Test
    public void testBisectionSolverExpression() throws Exception {
        UnivariateRealFunction xSquaredPlusOne = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
                return x * x + 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(xSquaredPlusOne, -2.0, 2.0);
        assertEquals(1.0, root, 1e-9);
    }

    @Test
    public void testBisectionSolverInitialGuess() throws Exception {
        UnivariateRealFunction sinFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(sinFunction, -2.0, 2.0, 1.5);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testBisectionSolverMaxIterations() throws Exception {
        UnivariateRealFunction sinFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver(10);
        try {
            solver.solve(sinFunction, -3.0, 3.0);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testBisectionSolverAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction sinFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver(100, 1e-10);
        double root = solver.solve(sinFunction, -3.0, 3.0);
        assertEquals(0.0, root, 1e-11);
    }

    @Test
    public void testBisectionSolverFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException, FunctionEvaluationException {
                throw new FunctionEvaluationException("test");
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 1.0);
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testBisectionSolverVerifyInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws PrecisionRequiredException, NumberIsTooSmallException {
                return x * x;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, Double.MIN_VALUE, Double.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            solver.solve(function, -1.0, 1.0);
            assertTrue(FunctionUtils.isZero(function.value(1.0), 1e-9));
            assertTrue(FunctionUtils.isZero(function.value(-1.0), 1e-9));
        } catch (Exception e) {
            fail();
        }

        try {
            solver.solve(function, 0.0, Double.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            solver.solve(function, Double.MIN_VALUE, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBisectionSolverUnivariateSpline() throws Exception {
        UnivariateRealFunction function = new UnivariateSpline(new double[] { -5.0, 0.0, 5.0 }, new double[] { -1.0, 1.0, -1.0 });

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -5.0, 5.0);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testBisectionSolverNullFunction() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(null, -5.0, 5.0);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}