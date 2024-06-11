package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.RootNotFoundException;

import org.junit.Test;

/**
 * Tests for {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * A simple test function for testing the root finding algorithm.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x - 1;
        }
    };

    /**
     * Tests {@code BaseSecantSolver(double, Method)} constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFunction() {
        new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI, null);
    }

    /**
     * Tests {@code BaseSecantSolver(double, Method)} constructor - successful
     * case.
     */
    @Test
    public void testConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple test function and default bounds.
     */
    @Test
    public void testSolveSimpleFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, TEST_FUNCTION, -5.0, 5.0);
        assertEquals("The root is not found.", -1.0, root, 1e-6);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple test function and custom bounds.
     */
    @Test
    public void testSolveCustomBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, TEST_FUNCTION, -2.5, 2.5);
        assertEquals("The root is not found.", -1.0, root, 1e-6);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple test function and default bounds and solution type.
     */
    @Test
    public void testSolveSimpleFunctionDefaultSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, TEST_FUNCTION, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("The root is not found.", -1.0, root, 1e-6);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple test function and custom bounds and solution type.
     */
    @Test
    public void testSolveCustomBoundsAndSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, TEST_FUNCTION, -2.5, 2.5, AllowedSolution.ANY_SIDE);
        assertEquals("The root is not found.", -1.0, root, 1e-6);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple test function and invalid bounds.
     */
    @Test(expected = NoBracketException.class)
    public void testSolveInvalidBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, TEST_FUNCTION, 5.0, 5.0);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple test function and max evaluation limit.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalLimit() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(1, TEST_FUNCTION, -5.0, 5.0);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple test function and a root inside the interval.
     */
    @Test(expected = RootNotFoundException.class)
    public void testSolveRootInsideInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, TEST_FUNCTION, 0.0, 0.0);
    }

    /**
     * Tests {@code solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple test function and a root outside
     * the interval.
     */
    @Test(expected = RootNotFoundException.class)
    public void testSolveRootOutsideInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, TEST_FUNCTION, -5.0, 2.5);
    }

}