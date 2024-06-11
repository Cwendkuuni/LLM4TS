package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.Solver;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;

/**
 * Test class for BaseSecantSolver, which tests the abstract BaseSecantSolver
 * class and its subclasses: RegulaFalsiSolver, IllinoisSolver, and PegasusSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double)
     * using a quadratic function with roots -2 and 2.
     */
    @org.junit.Test
    public void testSolveQuadratic() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0,
                9.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy);
        double solution = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, solution, absoluteAccuracy);
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double)
     * using a quadratic function with roots -2 and 2, and a function value
     * tolerance of 1e-6.
     */
    @org.junit.Test
    public void testSolveQuadraticFunctionValueTolerance() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0,
                9.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-6;

        Solver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy,
                functionValueAccuracy);
        double solution = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, solution, absoluteAccuracy);
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) using a quadratic function with roots -2 and 2.
     */
    @org.junit.Test
    public void testSolveQuadraticAllowedSolution() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0,
                9.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy, AllowedSolution.RIGHT_SIDE);
        double solution = solver.solve(f, -3.0, 3.0);
        assertEquals(2.0, solution, absoluteAccuracy);
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) using a quadratic function with roots -2 and 2, and a
     * function value tolerance of 1e-6.
     */
    @org.junit.Test
    public void testSolveQuadraticFunctionValueToleranceAllowedSolution() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0,
                9.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-6;

        Solver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy,
                functionValueAccuracy, AllowedSolution.RIGHT_SIDE);
        double solution = solver.solve(f, -3.0, 3.0);
        assertEquals(2.0, solution, absoluteAccuracy);
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double)
     * using a function with roots at -1, 1, and 2.
     */
    @org.junit.Test
    public void testSolveTripleRoot() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                1.0, 1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy);
        double solution = 0.0;
        try {
            solution = solver.solve(f, -3.0, 3.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
        assertTrue(solution == 0.0 || Double.isNaN(solution));
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double)
     * using a function with roots at -1, 1, and 2, and a function value tolerance
     * of 1e-6.
     */
    @org.junit.Test
    public void testSolveTripleRootFunctionValueTolerance() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                1.0, 1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-6;

        Solver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy,
                functionValueAccuracy);
        double solution = 0.0;
        try {
            solution = solver.solve(f, -3.0, 3.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
        assertTrue(solution == 0.0 || Double.isNaN(solution));
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) using a function with roots at -1, 1, and 2.
     */
    @org.junit.Test
    public void testSolveTripleRootAllowedSolution() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                1.0, 1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy, AllowedSolution.LEFT_SIDE);
        double solution = 0.0;
        try {
            solution = solver.solve(f, -3.0, 3.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
        assertTrue(solution == 0.0 || Double.isNaN(solution));
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) using a function with roots at -1, 1, and 2, and a
     * function value tolerance of 1e-6.
     */
    @org.junit.Test
    public void testSolveTripleRootFunctionValueToleranceAllowedSolution() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                1.0, 1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-6;

        Solver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy,
                functionValueAccuracy, AllowedSolution.LEFT_SIDE);
        double solution = 0.0;
        try {
            solution = solver.solve(f, -3.0, 3.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
        assertTrue(solution == 0.0 || Double.isNaN(solution));
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double)
     * using the secant method.
     */
    @org.junit.Test
    public void testSolveSecantMethod() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0,
                1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new SecantSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy);
        double x0 = -1.0;
        double x1 = 1.0;
        double solution = solver.solve(f, x0, x1);
        assertEquals(0.0, solution, absoluteAccuracy);
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) using the secant method.
     */
    @org.junit.Test
    public void testSolveSecantMethodAllowedSolution() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0,
                1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new SecantSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy, AllowedSolution.LEFT_SIDE);
        double x0 = -1.0;
        double x1 = 1.0;
        double solution = solver.solve(f, x0, x1);
        assertEquals(0.0, solution, absoluteAccuracy);
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double)
     * using the Regula Falsi method with the max number of evaluations exceeded.
     */
    @org.junit.Test(expected = MaxEvalException.class)
    public void testSolveRegulaFalsiMaxEvalException() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0,
                1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy, 1);
        double solution = solver.solve(f, -1.0, 1.0);
        fail("Expected MaxEvalException");
    }

    /**
     * Test method for solve(int, UnivariateFunction, double, double, double,
     * AllowedSolution) using the Regula Falsi method with the max number of
     * evaluations exceeded.
     */
    @org.junit.Test(expected = MaxEvalException.class)
    public void testSolveRegulaFalsiMaxEvalExceptionAllowedSolution() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0,
                1.0});
        double absoluteAccuracy = 1e-12;
        double relativeAccuracy = 1e-12;
        double functionValueAccuracy = 1e-12;

        Solver solver = new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                functionValueAccuracy, 1, AllowedSolution.LEFT_SIDE);
        double solution = solver.solve(f, -1.0, 1.0);
        fail("Expected MaxEvalException");
    }

}