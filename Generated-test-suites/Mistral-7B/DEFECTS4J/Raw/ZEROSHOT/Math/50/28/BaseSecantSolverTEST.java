package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.precision.AbsolutionFailedException;
import org.apache.commons.math3.exception.precision.BracketEmptyException;
import org.apache.commons.math3.exception.precision.BracketFailedException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
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
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        try {
            new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
        new BaseSecantSolver(1e-4, 3, BaseSecantSolver.Method.ILLINOIS);
        new BaseSecantSolver(1e-5, 5, 1e-6, BaseSecantSolver.Method.PEGASUS);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() throws BracketEmptyException, MaxEvalExceededException,
            TooManyIterationsException, AbsolutionFailedException, BracketFailedException {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double[] roots = {-1.0, 1.0};
        for (int i = 0; i < 2; i++) {
            final double min = roots[i] - 0.1;
            final double max = roots[i] + 0.1;
            final BaseSecantSolver solver;
            switch (i) {
            case 0:
                solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
                break;
            case 1:
                solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
                break;
            default:
                throw new AssertionError();
            }
            final double solution = solver.solve(100, f, min, max);
            assertEquals(roots[i], solution, 1e-6);
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() throws BracketEmptyException,
            MaxEvalExceededException, TooManyIterationsException,
            AbsolutionFailedException, BracketFailedException {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double[] roots = {-1.0, 1.0};
        for (int i = 0; i < 2; i++) {
            final double min = roots[i] - 0.1;
            final double max = roots[i] + 0.1;
            final double startValue = roots[i] + 0.5;
            final BaseSecantSolver solver;
            switch (i) {
            case 0:
                solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
                break;
            case 1:
                solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
                break;
            default:
                throw new AssertionError();
            }
            final double solution = solver.solve(100, f, min, max, startValue);
            assertEquals(roots[i], solution, 1e-6);
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() throws BracketEmptyException,
            MaxEvalExceededException, TooManyIterationsException,
            AbsolutionFailedException, BracketFailedException {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final double min = 0.0;
        final double max = 1.0;
        final double[] roots = {0.5235987756, 1.872664651};
        for (int i = 0; i < 2; i++) {
            final BaseSecantSolver solver;
            switch (i) {
            case 0:
                solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
                break;
            case 1:
                solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
                break;
            default:
                throw new AssertionError();
            }
            final double solution = solver.solve(100, f, min, max, AllowedSolution.RIGHT_SIDE);
            assertEquals(roots[i], solution, 1e-6);
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution, double)}.
     */
    @Test
    public void testSolveWithAllowedSolutionAndStartValue()
            throws BracketEmptyException, MaxEvalExceededException,
            TooManyIterationsException, AbsolutionFailedException,
            BracketFailedException {
        final UnivariateRealFunction f = Polynomials.fitQuintic(1.0, new double[] {0.1, 0.2, 0.3, 0.4, 0.5});
        final double[] roots = {-0.8063744124, -0.4384389407, -0.3700716511, 0.0561381356, 1.024476398};
        final double min = -1.5;
        final double max = 1.5;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        for (int i = 0; i < 5; i++) {
            final double solution = solver.solve(100, f, min, max, roots[i], AllowedSolution.LEFT_SIDE);
            assertEquals(roots[i], solution, 1e-6);
        }
    }
}