package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A test function for root finding. */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x * x * x - 2 * x + 1;
        }
    };

    /**
     * Test solver with default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final BaseSecantSolver solver = new BaseSecantSolver(
            BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test solver with constructor with accuracy.
     */
    @Test
    public void testConstructorWithAccuracy() {
        final double absoluteAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(
            absoluteAccuracy, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solver);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test solver with constructor with relative and absolute accuracy.
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-5;
        final BaseSecantSolver solver = new BaseSecantSolver(
            relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test solver with constructor with relative, absolute accuracy and function
     * value accuracy.
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteAndFunctionValueAccuracy() {
        final double relativeAccuracy = 1e-3;
        final double absoluteAccuracy = 1e-5;
        final double functionValueAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy,
            BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
    }

    /**
     * Test solver with bracketing.
     */
    @Test
    public void testSolverWithBracketing() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4,
            BaseSecantSolver.Method.PEGASUS);
        final double[] roots = {-1.0, 1.0};
        final double[] xs = {-2.0, 2.0};
        for (int i = 0; i < roots.length; i++) {
            final double root = roots[i];
            final double x = solver.solve(10, TEST_FUNCTION, xs[i] - 0.1,
                xs[i] + 0.1, root + 0.1, AllowedSolution.ANY_SIDE);
            assertEquals(root, x, 1e-5);
        }
    }

    /**
     * Test solver without bracketing.
     */
    @Test
    public void testSolverWithoutBracketing() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4,
            BaseSecantSolver.Method.PEGASUS);
        final double x = solver.solve(10, TEST_FUNCTION, -10.0, 10.0, 0.0,
            AllowedSolution.ANY_SIDE);
        assertEquals(0.0, x, 1e-5);
    }

    /**
     * Test solver when the function is not bracketing the root.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolverWhenFunctionIsNotBracketingTheRoot() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4,
            BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, TEST_FUNCTION, -10.0, -1.0, 0.0,
            AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solver when the function has multiple roots.
     */
    @Test
    public void testSolverWhenFunctionHasMultipleRoots() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4,
            BaseSecantSolver.Method.PEGASUS);
        final double[] roots = {-1.0, 1.0};
        final double[] xs = {-2.0, 2.0};
        final double[] results = new double[roots.length];
        for (int i = 0; i < roots.length; i++) {
            final double root = roots[i];
            results[i] = solver.solve(10, TEST_FUNCTION, xs[i] - 0.1,
                xs[i] + 0.1, root + 0.1, AllowedSolution.ANY_SIDE);
        }
        assertArrayEquals(roots, results, 1e-5);
    }

    /**
     * Test solver when the function is undefined at the initial bounds.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWhenFunctionIsUndefinedAtInitialBounds() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4,
            BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new ArithmeticException("Division by zero");
                }
                return x * Math.log(x);
            }
        }, -1.0, 1.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solver when the function is not continuous at the initial bounds.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverWhenFunctionIsNotContinuousAtInitialBounds() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-4,
            BaseSecantSolver.Method.PEGASUS);
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (Math.abs(x - 1.0) < 1e-15) {
                    throw new ArithmeticException("Cannot compute value");
                }
                return Math.pow(x, 1.0 / 3.0);
            }
        };
        solver.solve(10, function, -1.0, 1.0, 0.0, AllowedSolution.ANY_SIDE);
    }
}