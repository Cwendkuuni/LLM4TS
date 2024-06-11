package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.exception.ConvergenceException;
import org.apache.commons.commons.math.exception.MaxIterationsExceededException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testUtilClass() {
        // Test static methods of Util class
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("foo bar"));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testBaseSecantSolver() {
        // Test constructor and getters
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-14);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());

        BaseSecantSolver solver2 = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(1e-5, solver2.getMaximumRelativeError(), 1e-14);
        assertEquals(1e-6, solver2.getMaximumAbsoluteError(), 1e-14);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver2.getMethod());

        BaseSecantSolver solver3 = new BaseSecantSolver(1e-5, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        assertEquals(1e-5, solver3.getMaximumRelativeError(), 1e-14);
        assertEquals(1e-6, solver3.getMaximumAbsoluteError(), 1e-14);
        assertEquals(1e-8, solver3.getMaximumFunctionValueError(), 1e-14);
        assertEquals(BaseSecantSolver.Method.PEGASUS, solver3.getMethod());
    }

    @Test
    public void testAllowedSolution() {
        // Test allowed solution values
        assertEquals(AllowedSolution.ANY_SIDE, AllowedSolution.ANY_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.BELOW_SIDE, AllowedSolution.BELOW_SIDE);
        assertEquals(AllowedSolution.ABOVE_SIDE, AllowedSolution.ABOVE_SIDE);
        assertFalse(AllowedSolution.ANY_SIDE.equals(AllowedSolution.LEFT_SIDE));
        assertFalse(AllowedSolution.LEFT_SIDE.equals(AllowedSolution.ABOVE_SIDE));
    }

    @Test
    public void testSolve_PolynomialFunction() {
        // Test solve method with polynomial function
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 4, -4});

        double root = solver.solve(10, function, -10, 10, 0.0);
        assertEquals(0.0, root, 1e-8);

        root = solver.solve(10, function, -10, 10, 5.0);
        assertEquals(2.0, root, 1e-8);

        root = solver.solve(10, function, -10, 10, 8.0);
        assertEquals(4.0, root, 1e-8);
    }

    @Test
    public void testSolve_FunctionUtils() {
        // Test solve method with FunctionUtils function
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        UnivariateRealFunction function = FunctionUtils.pow(x, 2) - 4;

        double root = solver.solve(10, function, -10, 10, 0.0);
        assertEquals(2.0, root, 1e-8);

        root = solver.solve(10, function, -10, 10, 3.0);
        assertEquals(1.0, root, 1e-8);
    }

    @Test
    public void testSolve_ConvergenceException() {
        // Test solve method with ConvergenceException
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        UnivariateRealFunction function = x -> x * x - 2;

        assertThrows(ConvergenceException.class, () -> solver.solve(10, function, -10, 10, 0.0));
    }

    @Test
    public void testSolve_MaxIterationsExceededException() {
        // Test solve method with MaxIterationsExceededException
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> Math.sin(x) / x;

        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(10, function, -10, 10, 0.0));
    }

    @Test
    public void testSolve_MathInternalError() {
        // Test solve method with MathInternalError
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> {
            throw new MathInternalError();
        };

        assertThrows(MathInternalError.class, () -> solver.solve(10, function, -10, 10, 0.0));
    }

    @Test
    public void testAllowedSolution_SolutionNotInInterval() {
        // Test allowed solution with solution not in interval
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> x * x - 4;

        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, function, -10, 10, 3.5));
    }

    @Test
    public void testAllowedSolution_BelowSide() {
        // Test allowed solution with solution below the interval
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> x * x - 4;

        double root = solver.solve(10, function, -10, 10, 2.5);
        assertEquals(2.0, root, 1e-8);
    }

    @Test
    public void testAllowedSolution_AboveSide() {
        // Test allowed solution with solution above the interval
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> x * x - 4;

        double root = solver.solve(10, function, -10, 10, 3.5);
        assertEquals(2.0, root, 1e-8);
    }

    @Test
    public void testAllowedSolution_LeftSide() {
        // Test allowed solution with solution to the left of the interval
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> x * x - 4;

        double root = solver.solve(10, function, -10, 10, 1.5);
        assertEquals(2.0, root, 1e-8);
    }

    @Test
    public void testAllowedSolution_RightSide() {
        // Test allowed solution with solution to the right of the interval
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> x * x - 4;

        double root = solver.solve(10, function, -10, 10, 4.5);
        assertEquals(2.0, root, 1e-8);
    }

    @Test
    public void testAllowedSolution_BelowOrAboveSide() {
        // Test allowed solution with solution below or above the interval
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        UnivariateRealFunction function = x -> x * x - 4;

        double root = solver.solve(10, function, -10, 10, 1.5, AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, root, 1e-8);

        root = solver.solve(10, function, -10, 10, 4.5, AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, root, 1e-8);
    }
}