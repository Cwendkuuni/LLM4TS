package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
        assertEquals(UnivariateRealSolver.BISECTION, solver.getMethod());
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1.0E-6, solver.getAbsoluteAccuracy(), 1.0E-8);
    }

    @Test
    public void testBisectionSolverConstructorWithFunction() {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver(f);
        assertNotNull(solver);
        assertEquals(UnivariateRealSolver.BISECTION, solver.getMethod());
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1.0E-6, solver.getAbsoluteAccuracy(), 1.0E-8);
        assertEquals(f, solver.getFunction());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(1.0, 2.0);
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 1.0, 2.0);
        assertEquals(0.0, Precision.round(result, 12), 1.0E-12);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(0);
        solver.solve(f, 1.0, 2.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{0.0, 0.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 1.0, 2.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveNumberIsTooSmallException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(0.0);
        solver.solve(f, 1.0, 2.0);
    }

    @Test
    public void testSolveWithInitialPoint() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 1.0, 2.0, 1.5);
        assertEquals(0.0, Precision.round(result, 12), 1.0E-12);
    }

    @Test
    public void testVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 0.0});
        try {
            solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            solver.solve(f, 1.0, Double.POSITIVE_INFINITY);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            solver.solve(f, Double.NEGATIVE_INFINITY, 1.0);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            solver.solve(f, 1.0, 1.0);
        } catch (IllegalArgumentException e) {
            assertFalse(true);
        }
    }
}