package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorDoubleDouble() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverConstructorDoubleDoubleDouble() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolve() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertTrue(FastMath.abs(root - 2.0) <= 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolveAllowedSolution() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertTrue(FastMath.abs(root - 2.0) <= 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolveRegulaFalsi() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -5.0, 5.0);
        assertTrue(FastMath.abs(root - 2.0) <= 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolveRegulaFalsiAllowedSolution() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertTrue(FastMath.abs(root - 2.0) <= 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolveIllinois() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(x) - 0.1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, 0.0, 3.0);
        assertTrue(FastMath.abs(Precision.subtract(Math.PI / 2.0, root)) <= 1e-4);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolveIllinoisAllowedSolution() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(x) - 0.1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, 0.0, 3.0, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertTrue(FastMath.abs(Precision.subtract(Math.PI / 2.0, root)) <= 1e-4);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolvePegasus() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, function, -5.0, 5.0);
        assertTrue(FastMath.abs(Math.exp(root) - 2.0) <= 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws ConvergenceException
     */
    @Test
    public void testSolvePegasusAllowedSolution() throws ConvergenceException {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertTrue(FastMath.abs(Math.exp(root) - 2.0) <= 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testVerifyBracketingNotBracketing() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double min = 0.0;
        double max = 0.0;
        solver.verifyBracketing(min, max);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test
    public void testVerifyBracketingBracketing() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double min = -2.0;
        double max = 2.0;
        solver.verifyBracketing(min, max);
    }
}