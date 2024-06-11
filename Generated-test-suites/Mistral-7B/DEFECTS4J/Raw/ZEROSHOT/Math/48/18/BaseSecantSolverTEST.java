package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstruction() {
        final double accuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(accuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructionRelative() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-7;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructionWithFunctionValueAccuracy() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-7;
        final double functionValueAccuracy = 1e-8;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-12);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        final double accuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        final UnivariateFunction f = new PolynomialFunction(x -> x * x - 2);
        final double root = Math.sqrt(2.0) - 1.0;
        assertEquals(root, solver.solve(10, f, -1.5, 1.5), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        final double accuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        final UnivariateFunction f = new PolynomialFunction(x -> x * x - 2);
        final double root = Math.sqrt(2.0) - 1.0;
        assertEquals(root, solver.solve(10, f, -1.5, 1.5, 0.0), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final double accuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        final UnivariateFunction f = new PolynomialFunction(x -> x * x - 2);
        final double root = Math.sqrt(2.0) - 1.0;
        assertEquals(root, solver.solve(10, f, -1.5, 1.5, 0.0, AllowedSolution.LEFT_SIDE), Precision.SAFE_EPSILON);
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(10, f, -1.5, 1.5, 0.0, AllowedSolution.ABOVE_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithInvertedIntervals() {
        final double accuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        final UnivariateFunction f = new PolynomialFunction(x -> x * x - 2);
        final double root = -Math.sqrt(2.0) - 1.0;
        final double min = -3.0;
        final double max = 1.0;
        assertEquals(root, solver.solve(10, f, min, max, 0.0, AllowedSolution.LEFT_SIDE), Precision.SAFE_EPSILON);
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(10, f, min, max, 0.0, AllowedSolution.RIGHT_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        final double accuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        final UnivariateFunction f = new PolynomialFunction(x -> 1.0 / (x * x));
        final double min = -5.0;
        final double max = 5.0;
        solver.solve(10, f, min, max, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        final int maxEval = 1;
        final double accuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        final UnivariateFunction f = new PolynomialFunction(x -> Math.sin(x) / x);
        final double min = -FastMath.PI;
        final double max = FastMath.PI;
        solver.solve(maxEval, f, min, max, 0.0, AllowedSolution.ANY_SIDE);
    }

}