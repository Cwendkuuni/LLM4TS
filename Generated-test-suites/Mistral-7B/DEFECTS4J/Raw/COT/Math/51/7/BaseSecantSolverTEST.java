package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testCustomConstructor() {
        double relativeAccuracy = 1e-3;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solver);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0);
        assertEquals(BaseSecantSolver.Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test solve method with default arguments.
     */
    @Test
    public void testSolveDefaultArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        double min = -2;
        double max = 2;
        double root = 2.0;
        try {
            double solution = solver.solve(100, f, min, max);
            assertEquals(root, solution, Precision.SAFE_EPSILON);
        } catch (MaxEvalException | NumberIsTooSmallException | TooManyIterationsException e) {
            // Expected exception
        }
    }

    /**
     * Test solve method with custom arguments.
     */
    @Test
    public void testSolveCustomArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        double min = -2;
        double max = 2;
        double root = 2.0;
        try {
            double solution = solver.solve(100, f, min, max);
            assertEquals(root, solution, Precision.SAFE_EPSILON);
        } catch (MaxEvalException | NumberIsTooSmallException | TooManyIterationsException e) {
            // Expected exception
        }
    }

    /**
     * Test solve method with maxEval = 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithMaxEvalZero() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        double min = -2;
        double max = 2;
        solver.solve(0, f, min, max);
    }

    /**
     * Test solve method with null function.
     */
    @Test(expected = NullPointerException.class)
    public void testSolveWithNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double min = -2;
        double max = 2;
        solver.solve(100, null, min, max);
    }

    /**
     * Test solve method with invalid allowed solution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        double min = -2;
        double max = 2;
        solver.solve(100, f, min, max, BaseSecantSolver.AllowedSolution.INVALID);
    }

    /**
     * Test solve method with bracketing failure.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithBracketingFailure() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        double min = 2;
        double max = 2;
        solver.solve(100, f, min, max);
    }

    /**
     * Test verifyBracketing method.
     */
    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        double min = -2;
        double max = 2;
        double x0 = -1.5;
        double x1 = 0.5;
        double f0 = f.value(x0);
        double f1 = f.value(x1);
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Test solve method with MultivariateFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithMultivariateFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        MultivariateFunction f = x -> x[0] * x[0] * x[0] - 4 * x[0] * x[1] + x[1] * x[1];
        double[] min = {-2, 0};
        double[] max = {2, 2};
        solver.solve(100, f, min, max);
    }
}