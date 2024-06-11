package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;

import org.apache.commons.math.analysis.solvers.BracketingBrentSolver;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;

/**
 * Test class for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @org.junit.Test
    public void testSolve() {
        UnivariateRealFunction function = new PolynomialFunction(3.0, -1.0, 1.0, 1.0, 0.0);
        double x0 = 0.0;
        double x1 = 1.0;
        
        try {
            double root = UnivariateRealSolverUtils.solve(function, x0, x1);
            assertEquals(Math.sqrt(3.0), root, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Solver failed", e);
        }
        
        UnivariateRealFunction functionWithDoubleRoot = new PolynomialFunction(2.0, -2.0, 2.0, 1.0, 0.0);
        double x0WithDoubleRoot = -1.0;
        double x1WithDoubleRoot = 1.0;
        
        try {
            double rootWithDoubleRoot = UnivariateRealSolverUtils.solve(functionWithDoubleRoot, x0WithDoubleRoot, x1WithDoubleRoot);
            assertEquals(-Math.sqrt(2.0), rootWithDoubleRoot, 0.0001);
            assertEquals(-Math.sqrt(2.0), UnivariateRealSolverUtils.solve(functionWithDoubleRoot, x1WithDoubleRoot, x0WithDoubleRoot), 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Solver failed", e);
        }
        
        UnivariateRealFunction functionWithRootOutsideInterval = new PolynomialFunction(3.0, -5.0, 5.0, 1.0, 0.0);
        double x0WithRootOutsideInterval = 2.0;
        double x1WithRootOutsideInterval = 3.0;
        
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(functionWithRootOutsideInterval, x0WithRootOutsideInterval, x1WithRootOutsideInterval);
        });
        
        UnivariateRealFunction functionWithNegativeInterval = new PolynomialFunction(3.0, 1.0, 2.0, 1.0, 0.0);
        double x0WithNegativeInterval = 2.0;
        double x1WithNegativeInterval = 1.0;
        
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(functionWithNegativeInterval, x0WithNegativeInterval, x1WithNegativeInterval);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @org.junit.Test
    public void testBracket() {
        UnivariateRealFunction function = new PolynomialFunction(3.0, -1.0, 1.0, 1.0, 0.0);
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        
        try {
            double[] bracketedRoot = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, 100);
            assertEquals(lowerBound, bracketedRoot[0], 0.0001);
            assertEquals(upperBound, bracketedRoot[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Solver failed", e);
        }
        
        UnivariateRealFunction functionWithDoubleRoot = new PolynomialFunction(2.0, -2.0, 2.0, 1.0, 0.0);
        double initialWithDoubleRoot = 0.0;
        double lowerBoundWithDoubleRoot = -3.0;
        double upperBoundWithDoubleRoot = 3.0;
        
        try {
            double[] bracketedRootWithDoubleRoot = UnivariateRealSolverUtils.bracket(functionWithDoubleRoot, initialWithDoubleRoot, lowerBoundWithDoubleRoot, upperBoundWithDoubleRoot, 100);
            assertEquals(lowerBoundWithDoubleRoot, bracketedRootWithDoubleRoot[0], 0.0001);
            assertEquals(upperBoundWithDoubleRoot, bracketedRootWithDoubleRoot[1], 0.0001);
            assertEquals(-Math.sqrt(2.0), UnivivariateRealSolverUtils.solve(functionWithDoubleRoot, bracketedRootWithDoubleRoot[0], bracketedRootWithDoubleRoot[1]), 0.0001);
            assertEquals(-Math.sqrt(2.0), UnivariateRealSolverUtils.solve(functionWithDoubleRoot, bracketedRootWithDoubleRoot[1], bracketedRootWithDoubleRoot[0]), 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Solver failed", e);
        }
        
        UnivariateRealFunction functionWithRootOutsideInterval = new PolynomialFunction(3.0, -5.0, 5.0, 1.0, 0.0);
        double initialWithRootOutsideInterval = 2.0;
        double lowerBoundWithRootOutsideInterval = 2.0;
        double upperBoundWithRootOutsideInterval = 3.0;
        
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(functionWithRootOutsideInterval, initialWithRootOutsideInterval, lowerBoundWithRootOutsideInterval, upperBoundWithRootOutsideInterval, 100);
        });
        
        UnivariateRealFunction functionWithNegativeInterval = new PolynomialFunction(3.0, 1.0, 2.0, 1.0, 0.0);
        double initialWithNegativeInterval = 2.0;
        double lowerBoundWithNegativeInterval = 2.0;
        double upperBoundWithNegativeInterval = 1.0;
        
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(functionWithNegativeInterval, initialWithNegativeInterval, lowerBoundWithNegativeInterval, upperBoundWithNegativeInterval, 100);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using Brent's method.
     */
    @org.junit.Test
    public void testSolveWithBrentSolver() {
        UnivariateRealFunction function = new PolynomialFunction(3.0, -1.0, 1.0, 1.0, 0.0);
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-6;
        
        try {
            UnivariateRealSolver solver = new BracketingBrentSolver();
            double root = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy, solver);
            assertEquals(Math.sqrt(3.0), root, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Solver failed", e);
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using bisection method.
     */
    @org.junit.Test
    public void testSolveWithBisectionSolver() {
        UnivariateRealFunction function = new PolynomialFunction(3.0, -1.0, 1.0, 1.0, 0.0);
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-6;
        
        try {
            UnivariateRealSolver solver = new BisectionSolver();
            double root = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy, solver);
            assertEquals(Math.sqrt(3.0), root, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Solver failed", e);
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using secant method.
     */
    @org.junit.Test
    public void testSolveWithSecantSolver() {
        UnivariateRealFunction function = new PolynomialFunction(3.0, -1.0, 1.0, 1.0, 0.0);
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-6;
        
        try {
            UnivariateRealSolver solver = new SecantSolver();
            double root = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy, solver);
            assertEquals(Math.sqrt(3.0), root, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Solver failed", e);
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using default solver that fails.
     */
    @org.junit.Test
    public void testSolveWithFallingBackToDefaultSolver() {
        UnivariateRealFunction function = new PolynomialFunction(-1.0, -1.0, 1.0, 1.0, 0.0);
        double x0 = 0.0;
        double x1 = 1.0;
        
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x0, x1, 1e-6, null);
        });
    }
}