package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws ConvergenceException
     * @throws MaxEvalExceededException
     */
    @Test
    public void testSolve() throws ConvergenceException, MaxEvalExceededException {
        // Test with Regula-Falsi method
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double root = solver.solve(10, function, -3.0, 3.0);
        assertEquals(0.0, root, 1.0e-6);

        // Test with Illinois method
        solver = new IllinoisSolver(1.0e-6);
        root = solver.solve(10, function, -3.0, 3.0);
        assertEquals(0.0, root, 1.0e-6);

        // Test with Pegasus method
        solver = new PegasusSolver(1.0e-6);
        root = solver.solve(10, function, -3.0, 3.0);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws ConvergenceException
     * @throws MaxEvalExceededException
     */
    @Test
    public void testSolveWithAllowedSolution() throws ConvergenceException, MaxEvalExceededException {
        // Test with Regula-Falsi method and any solution
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, AllowedSolution.ANY_SIDE);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertEquals(0.0, root, 1.0e-6);

        // Test with Regula-Falsi method and left solution
        solver = new RegulaFalsiSolver(1.0e-6, AllowedSolution.LEFT_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertLessThan(root, 0.0);

        // Test with Regula-Falsi method and right solution
        solver = new RegulaFalsiSolver(1.0e-6, AllowedSolution.RIGHT_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertGreaterThan(root, 0.0);

        // Test with Illinois method and any solution
        solver = new IllinoisSolver(1.0e-6, AllowedSolution.ANY_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertEquals(0.0, root, 1.0e-6);

        // Test with Illinois method and below solution
        solver = new IllinoisSolver(1.0e-6, AllowedSolution.BELOW_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertLessThan(root, 0.0);

        // Test with Illinois method and above solution
        solver = new IllinoisSolver(1.0e-6, AllowedSolution.ABOVE_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertGreaterThan(root, 0.0);

        // Test with Pegasus method and any solution
        solver = new PegasusSolver(1.0e-6, AllowedSolution.ANY_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertEquals(0.0, root, 1.0e-6);

        // Test with Pegasus method and left solution
        solver = new PegasusSolver(1.0e-6, AllowedSolution.LEFT_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertLessThan(root, 0.0);

        // Test with Pegasus method and right solution
        solver = new PegasusSolver(1.0e-6, AllowedSolution.RIGHT_SIDE);
        root = solver.solve(10, function, -3.0, 3.0, -3.0);
        assertGreaterThan(root, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     * @throws ConvergenceException
     * @throws MaxEvalExceededException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, MaxEvalExceededException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = null;
        double root = solver.solve(10, function, -3.0, 3.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws ConvergenceException
     * @throws MaxEvalExceededException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidMin() throws ConvergenceException, MaxEvalExceededException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double min = Double.NEGATIVE_INFINITY;
        double max = 3.0;
        solver.solve(10, function, min, max);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws ConvergenceException
     * @throws MaxEvalExceededException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidMax() throws ConvergenceException, MaxEvalExceededException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double min = -3.0;
        double max = Double.POSITIVE_INFINITY;
        solver.solve(10, function, min, max);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws ConvergenceException
     * @throws MaxEvalExceededException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullAllowedSolution() throws ConvergenceException, MaxEvalExceededException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, AllowedSolution.ANY_SIDE);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double min = -3.0;
        double max = 3.0;
        AllowedSolution allowedSolution = null;
        solver.solve(10, function, min, max, -3.0, allowedSolution);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws ConvergenceException
     * @throws MaxEvalExceededException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() throws ConvergenceException, MaxEvalExceededException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, AllowedSolution.ANY_SIDE);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double min = -3.0;
        double max = 3.0;
        AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        int allowedSolutionIndex = AllowedSolution.LEFT_SIDE.ordinal() + 1;
        solver.solve(10, function, min, max, -3.0, AllowedSolution.values()[allowedSolutionIndex]);
    }
}

enum AllowedSolution {
    ANY_SIDE,
    LEFT_SIDE,
    RIGHT_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE
}