package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)} with
     * default method and relative and absolute accuracies.
     * @throws ConvergenceException if the solver fails to converge.
     */
    @Test
    public void testBaseSecantSolverDefaultConstructor() throws ConvergenceException {
        UnivariateRealFunction function = new UnivariateSin();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.abs(Math.PI / 2.0 - root) < 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double)}
     * with default method and absolute accuracy.
     * @throws ConvergenceException if the solver fails to converge.
     */
    @Test
    public void testBaseSecantSolverAbsoluteAccuracyConstructor() throws ConvergenceException {
        UnivariateRealFunction function = new UnivariateSin();
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.abs(Math.PI / 2.0 - root) < 0.0015);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double)}
     * with default method and relative and function value accuracies.
     * @throws ConvergenceException if the solver fails to converge.
     */
    @Test
    public void testBaseSecantSolverRelativeAndFunctionValueAccuracyConstructor() throws ConvergenceException {
        UnivariateRealFunction function = new UnivariateSin();
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.abs(Math.PI / 2.0 - root) < 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     * with different methods.
     * @throws ConvergenceException if the solver fails to converge.
     */
    @Test
    public void testBaseSecantSolverDifferentMethods() throws ConvergenceException {
        UnivariateRealFunction function = new UnivariateSin();

        // Test Regula Falsi method
        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root1 = solver1.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.abs(Math.PI / 2.0 - root1) < 1e-5);

        // Test Illinois method
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root2 = solver2.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.abs(Math.PI / 2.0 - root2) < 1e-5);

        // Test Pegasus method
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double root3 = solver3.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.abs(Math.PI / 2.0 - root3) < 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with different methods and allowed solutions.
     * @throws ConvergenceException if the solver fails to converge.
     */
    @Test
    public void testBaseSecantSolverDifferentAllowedSolutions() throws ConvergenceException {
        UnivariateRealFunction function = new UnivariateQuadratic(1.0, 0.0, 1.0);

        // Test Regula Falsi method with AllowedSolution.LEFT_SIDE
        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root1 = solver1.solve(10, function, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-3.0, root1, 1e-5);

        // Test Regula Falsi method with AllowedSolution.RIGHT_SIDE
        double root2 = solver1.solve(10, function, -5.0, 5.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(3.0, root2, 1e-5);

        // Test Illinois method with AllowedSolution.BELOW_SIDE
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root3 = solver2.solve(10, function, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-3.0, root3, 1e-5);

        // Test Pegasus method with AllowedSolution.ABOVE_SIDE
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double root4 = solver3.solve(10, function, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(3.0, root4, 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with different methods, allowed solutions, and start values.
     * @throws ConvergenceException if the solver fails to converge.
     */
    @Test
    public void testBaseSecantSolverDifferentMethodsAndStartValues() throws ConvergenceException {
        UnivariateRealFunction function = new UnivariateQuadratic(1.0, 0.0, 1.0);

        // Test Regula Falsi method with AllowedSolution.LEFT_SIDE and start value less than root
        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root1 = solver1.solve(10, function, -5.5, 5.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-3.0, root1, 1e-5);

        // Test Regula Falsi method with AllowedSolution.RIGHT_SIDE and start value greater than root
        double root2 = solver1.solve(10, function, 5.5, 5.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(3.0, root2, 1e-5);

        // Test Illinois method with AllowedSolution.BELOW_SIDE and start value less than root
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root3 = solver2.solve(10, function, -5.5, 5.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-3.0, root3, 1e-5);

        // Test Pegasus method with AllowedSolution.ABOVE_SIDE and start value greater than root
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double root4 = solver3.solve(10, function, 5.5, 5.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(3.0, root4, 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * when the solver fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testBaseSecantSolverConvergenceException() {
        UnivariateRealFunction function = new UnivariateQuartic(1.0, 0.0, 0.0, 0.0, 0.0);
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * when the solver fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testBaseSecantSolverConvergenceExceptionAllowedSolution() {
        UnivariateRealFunction function = new UnivariateQuartic(1.0, 0.0, 0.0, 0.0, 0.0);
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -10.0, 10.0, AllowedSolution.LEFT_SIDE);
    }
}