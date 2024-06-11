package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestTools;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstruction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructionWithAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructionWithAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, 1e-8, Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double x = solver.solve(new FTestFunction(), 0.1, 1.0, 1.5);
        assertEquals(1.0, x, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double x = solver.solve(new FTestFunction(), 0.1, 1.0, 1.5, AllowedSolution.LEFT_SIDE);
        assertEquals(1.0, x, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with function value tolerance only.
     */
    @Test
    public void testSolveFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        try {
            solver.solve(new FTestFunctionWithSmallFunctionValue(), 0.1, 1.0, 1.5);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with function value tolerance only.
     */
    @Test
    public void testSolveAllowedSolutionFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        try {
            solver.solve(new FTestFunctionWithSmallFunctionValue(), 0.1, 1.0, 1.5, AllowedSolution.LEFT_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with absolute tolerance only.
     */
    @Test
    public void testSolveAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, Method.ILLINOIS);
        try {
            solver.solve(new FTestFunctionWithSmallAbsoluteError(), 0.1, 1.0, 1.5);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with absolute tolerance only.
     */
    @Test
    public void testSolveAllowedSolutionAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, Method.ILLINOIS);
        try {
            solver.solve(new FTestFunctionWithSmallAbsoluteError(), 0.1, 1.0, 1.5, AllowedSolution.LEFT_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with relative tolerance only.
     */
    @Test
    public void testSolveRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, Method.ILLINOIS);
        try {
            solver.solve(new FTestFunctionWithSmallRelativeError(), 0.1, 1.0, 1.5);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with relative tolerance only.
     */
    @Test
    public void testSolveAllowedSolutionRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, Method.ILLINOIS);
        try {
            solver.solve(new FTestFunctionWithSmallRelativeError(), 0.1, 1.0, 1.5, AllowedSolution.LEFT_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with invalid function.
     */
    @Test(expected = NullArgumentException.class)
    public void testSolveNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(null, 0.1, 1.0, 1.5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with invalid function.
     */
    @Test(expected = NullArgumentException.class)
    public void testSolveAllowedSolutionNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(null, 0.1, 1.0, 1.5, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with invalid bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(new FTestFunction(), Double.NaN, Double.NaN, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with invalid bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveAllowedSolutionInvalidBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(new FTestFunction(), Double.NaN, Double.NaN, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with invalid initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInitialGuess() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(new FTestFunction(), 1.5, 0.1, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with invalid initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveAllowedSolutionInvalidInitialGuess() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(new FTestFunction(), 1.5, 0.1, 1.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with invalid method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(new FTestFunction(), 0.1, 1.0, 1.5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with invalid method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveAllowedSolutionInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(new FTestFunction(), 0.1, 1.0, 1.5, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Test with the function that returns infinity.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveFunctionReturnsInfinity() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(new FTestFunctionReturnsInfinity(), 0.1, 1.0, 1.5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Test with the function that returns infinity.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveAllowedSolutionFunctionReturnsInfinity() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        solver.solve(new FTestFunctionReturnsInfinity(), 0.1, 1.0, 1.5, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test function for testing {@link BaseSecantSolver}.
     */
    static class FTestFunction implements UnivariateRealFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return x * x - 2.0;
        }

        /** {@inheritDoc} */
        @Override
        public double derivative(double x) {
             return 2.0 * x;
        }
    }

    /**
     * Test function for testing {@link BaseSecantSolver} with small function value.
     */
    static class FTestFunctionWithSmallFunctionValue implements UnivariateRealFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return Precision.SAFE_MIN;
        }

        /** {@inheritDoc} */
        @Override
        public double derivative(double x) {
            return 2.0;
        }
    }

    /**
     * Test function for testing {@link BaseSecantSolver} with small absolute error.
     */
    static class FTestFunctionWithSmallAbsoluteError implements UnivariateRealFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return Precision.SAFE_MIN + 1.0e-9;
        }

        /** {@inheritDoc} */
        @Override
        public double derivative(double x) {
            return 2.0;
        }
    }

    /**
     * Test function for testing {@link BaseSecantSolver} with small relative error.
     */
    static class FTestFunctionWithSmallRelativeError implements UnivariateRealFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return Precision.SAFE_MIN + 1.0e-9 * Precision.SAFE_MIN;
        }

        /** {@inheritDoc} */
        @Override
        public double derivative(double x) {
            return 2.0;
        }
    }

    /**
     * Test function for testing {@link BaseSecantSolver} that returns infinity.
     */
    static class FTestFunctionReturnsInfinity implements UnivariateRealFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return Double.POSITIVE_INFINITY;
        }

        /** {@inheritDoc} */
        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }
}