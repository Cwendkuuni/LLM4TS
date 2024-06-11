package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.Functions;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * using {@link RegulaFalsiSolver}.
     * @throws Exception
     */
    @Test
    public void testSolveRegulaFalsi() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double result = solver.solve(f, -2.5, 2.5);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * using {@link RegulaFalsiSolver}.
     * @throws Exception
     */
    @Test
    public void testSolveRegulaFalsiAllowedSolution() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, AllowedSolution.LEFT_SIDE);
        double result = solver.solve(f, -2.5, 2.5);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
        result = solver.solve(f, -2.5, 2.5, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
        result = solver.solve(f, -2.5, 2.5, AllowedSolution.ABOVE_SIDE);
        assertTrue(Precision.isClose(result, -2.0, 1e-6));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with initial guess outside bracketing interval.
     * @throws Exception
     */
    @Test(expected = RootNotFoundException.class)
    public void testSolveRegulaFalsiOutsideBracketing() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, AllowedSolution.LEFT_SIDE);
        solver.solve(f, 3.0, -2.5, 2.5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * using {@link IllinoisSolver}.
     * @throws Exception
     */
    @Test
    public void testSolveIllinois() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * using {@link IllinoisSolver}.
     * @throws Exception
     */
    @Test
    public void testSolveIllinoisAllowedSolution() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6, AllowedSolution.LEFT_SIDE);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
        result = solver.solve(f, -1.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with initial guess outside bracketing interval.
     * @throws Exception
     */
    @Test(expected = RootNotFoundException.class)
    public void testSolveIllinoisOutsideBracketing() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6, AllowedSolution.LEFT_SIDE);
        solver.solve(f, 1.0, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * using {@link PegasusSolver}.
     * @throws Exception
     */
    @Test
    public void testSolvePegasus() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(Math.cos(x)) - 1;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        solver.setMaxEval(100);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * using {@link PegasusSolver}.
     * @throws Exception
     */
    @Test
    public void testSolvePegasusAllowedSolution() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(Math.cos(x)) - 1;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-6, AllowedSolution.LEFT_SIDE);
        solver.setMaxEval(100);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with initial guess outside bracketing interval and maximum evaluation exceeded.
     * @throws Exception
     */
    @Test(expected = MaxEvalException.class)
    public void testSolvePegasusOutsideBracketingMaxEval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(Math.cos(x)) - 1;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-6, AllowedSolution.LEFT_SIDE);
        solver.setMaxEval(1);
        double x0 = 1.0;
        double x1 = -1.0;
        double result = solver.solve(f, x0, x1);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     * @throws Exception
     */
    @Test
    public void testVerifyBracketing() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double x0 = 1.0;
        double x1 = 0.5;
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}
     * with non-bracketing interval.
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketingNonBracketing() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        double x0 = 1.0;
        double x1 = 2.0;
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Test method for {@link BaseSecantSolver#getMin()}.
     * @throws Exception
     */
    @Test
    public void testGetMin() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertEquals(-2.5, solver.getMin(), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#getMax()}.
     * @throws Exception
     */
    @Test
    public void testGetMax() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertEquals(2.5, solver.getMax(), Precision.SAFE_EPSILON);
    }

}