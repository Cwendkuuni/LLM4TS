package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.LBFGSBrentSolver;
import org.apache.commons.math3.solvers.SecantSolver;
import org.apache.commons.math3.solvers.Solver;
import org.apache.commons.math3.solvers.BrentSolver.BrentSolverOptions;
import org.apache.commons.math3.solvers.LBFGSBrentSolver.LBFGSBrentSolverOptions;
import org.apache.commons.math3.solvers.SecantSolver.SecantSolverOptions;

import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    /**
     * Test solve method with a polynomial function and default settings.
     */
    @Test
    public void testSolvePolynomial() {
        UnivariateFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double root = UnivariateRealSolverUtils.solve(f, -10.0, 10.0);
        assertEquals(0.0, root, 1.0e-12);
    }

    /**
     * Test solve method with a polynomial function and custom precision.
     */
    @Test
    public void testSolvePolynomialPrecision() {
        UnivariateFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double root = UnivariateRealSolverUtils.solve(f, -10.0, 10.0, 1.0e-15);
        assertEquals(0.0, root, 1.0e-15);
    }

    /**
     * Test solve method with a function that has no roots.
     */
    @Test
    public void testSolveNoRoots() {
        UnivariateFunction f = new PolynomialFunction(0.0, 1.0, 0.0, 1.0);
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(f, -10.0, 10.0));
    }

    /**
     * Test solve method with a function that throws an exception during evaluation.
     */
    @Test
    public void testSolveExceptionFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0.0) {
                    throw new FunctionEvaluationException("Function evaluation failed");
                }
                return Math.sqrt(x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, -10.0, 10.0));
    }

    /**
     * Test bracket method with a polynomial function and default settings.
     */
    @Test
    public void testBracketPolynomial() {
        UnivariateFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double[] brackets = UnivariateRealSolverUtils.bracket(f, 0.0, -10.0, 10.0);
        assertEquals(-1.0, brackets[0], 1.0e-12);
        assertEquals(1.0, brackets[1], 1.0e-12);
    }

    /**
     * Test bracket method with a polynomial function and custom precision.
     */
    @Test
    public void testBracketPolynomialPrecision() {
        UnivariateFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        double[] brackets = UnivariateRealSolverUtils.bracket(f, 0.0, -10.0, 10.0, 1.0e-15);
        assertEquals(-1.0, brackets[0], 1.0e-15);
        assertEquals(1.0, brackets[1], 1.0e-15);
    }

    /**
     * Test bracket method with a function that has no roots.
     */
    @Test
    public void testBracketNoRoots() {
        UnivariateFunction f = new PolynomialFunction(0.0, 1.0, 0.0, 1.0);
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, 0.0, -10.0, 10.0));
    }

    /**
     * Test bracket method with a function that throws an exception during evaluation.
     */
    @Test
    public void testBracketExceptionFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0.0) {
                    throw new FunctionEvaluationException("Function evaluation failed");
                }
                return Math.sqrt(x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, 0.0, -10.0, 10.0));
    }

    /**
     * Test solve using different solvers (Brent, Secant, L-BFGS-Brent).
     */
    @Test
    public void testSolver() {
        UnivariateFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        Solver<UnivariateFunction, Double> brentSolver = new BrentSolver();
        Solver<UnivariateFunction, Double> secantSolver = new SecantSolver();
        Solver<UnivariateFunction, Double> lbfsbrentSolver = new LBFGSBrentSolver();

        double rootBrent = UnivariateRealSolverUtils.solve(f, brentSolver, -10.0, 10.0);
        assertEquals(0.0, rootBrent, 1.0e-12);

        double rootSecant = UnivariateRealSolverUtils.solve(f, secantSolver, -10.0, 10.0, 1.0e-15);
        assertEquals(0.0, rootSecant, 1.0e-15);

        double rootLBFGSBrent = UnivariateRealSolverUtils.solve(f, lbfsbrentSolver, -10.0, 10.0, 1.0e-15, 100);
        assertEquals(0.0, rootLBFGSBrent, 1.0e-15);
    }

    /**
     * Test bracket using different solvers (Brent, Secant, L-BFGS-Brent).
     */
    @Test
    public void testBracketSolver() {
        UnivariateFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        Solver<UnivariateFunction, Double> brentSolver = new BrentSolver();
        Solver<UnivariateFunction, Double> secantSolver = new SecantSolver();
        Solver<UnivariateFunction, Double> lbfsbrentSolver = new LBFGSBrentSolver();

        double[] bracketsBrent = UnivariateRealSolverUtils.bracket(f, brentSolver, 0.0, -10.0, 10.0);
        assertEquals(-1.0, bracketsBrent[0], 1.0e-12);
        assertEquals(1.0, bracketsBrent[1], 1.0e-12);

        double[] bracketsSecant = UnivariateRealSolverUtils.bracket(f, secantSolver, 0.0, -10.0, 10.0, 1.0e-15);
        assertEquals(-1.0, bracketsSecant[0], 1.0e-15);
        assertEquals(1.0, bracketsSecant[1], 1.0e-15);

        double[] bracketsLBFGSBrent = UnivariateRealSolverUtils.bracket(f, lbfsbrentSolver, 0.0, -10.0, 10.0, 1.0e-15, 100);
        assertEquals(-1.0, bracketsLBFGSBrent[0], 1.0e-15);
        assertEquals(1.0, bracketsLBFGSBrent[1], 1.0e-15);

        double[] bracketsLBFGSBrentWithIterLimit = UnivariateRealSolverUtils.bracket(f, lbfsbrentSolver, 0.0, -10.0, 10.0, 1.0e-15, 1);
        assertEquals(-1.0, bracketsLBFGSBrentWithIterLimit[0], 1.0e-15);
        assertEquals(1.0, bracketsLBFGSBrentWithIterLimit[1], 1.0e-15);

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, lbfsbrentSolver, 0.0, -10.0, 10.0, 1.0e-15, 0));

        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(null, brentSolver, -10.0, 10.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, null, -10.0, 10.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, brentSolver, -10.0, 10.0, 1.0e-15, -1));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, brentSolver, -10.0, 10.0, 1.0e-15, Integer.MIN_VALUE));
    }

    /**
     * Test options for solving methods.
     */
    @Test
    public void testSolverOptions() {
        UnivariateFunction f = new PolynomialFunction(2.0, -1.0, 1.0, 1.0);
        Solver<UnivariateFunction, Double> brentSolver = new BrentSolver();
        Solver<UnivariateFunction, Double> lbfsbrentSolver = new LBFGSBrentSolver();

        BrentSolverOptions brentOptions = new BrentSolverOptions();
        brentOptions.setMaxIterations(10);
        brentOptions.setTolerance(1.0e-15);

        double rootBrent = UnivariateRealSolverUtils.solve(f, brentSolver, -10.0, 10.0, brentOptions);
        assertEquals(0.0, rootBrent, 1.0e-15);

        LBFGSBrentSolverOptions lbfsbrentOptions = new LBFGSBrentSolverOptions();
        lbfsbrentOptions.setMaxIterations(10);
        lbfsbrentOptions.setTolerance(1.0e-15);

        double rootLBFGSBrent = UnivariateRealSolverUtils.solve(f, lbfsbrentSolver, -10.0, 10.0, lbfsbrentOptions);
        assertEquals(0.0, rootLBFGSBrent, 1.0e-15);
    }
}