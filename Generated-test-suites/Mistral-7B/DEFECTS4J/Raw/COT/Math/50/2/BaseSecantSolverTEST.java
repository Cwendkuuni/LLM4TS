package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test of the constructor.
     */
    @Test
    public void testBaseSecantSolverConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test of the constructor.
     */
    @Test
    public void testBaseSecantSolverConstructorWithRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solver);
    }

    /**
     * Test of the constructor.
     */
    @Test
    public void testBaseSecantSolverConstructorWithAllArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-7, 1e-10, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double root = solver.solve(10, f, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double root = solver.solve(10, f, -2.0, 2.0, 0.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithMaxEvalException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        solver.solve(0, f, -2.0, 2.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithNumberIsTooSmallException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.PEGASUS);

        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        solver.solve(10, f, -2.0, 2.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithUnsupportedOperationException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                throw new UnsupportedOperationException("Unsupported operation: " + this.getClass().getName() + ".value(double)");
            }
        };
        solver.solve(10, f, -2.0, 2.0);
    }
}