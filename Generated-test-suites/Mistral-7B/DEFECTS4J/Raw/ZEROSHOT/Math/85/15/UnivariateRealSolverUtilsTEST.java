package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.CubicSolver;
import org.apache.commons.math3.solvers.LaguerreSolver;
import org.apache.commons.math3.solvers.SecantSolver;
import org.apache.commons.math3.solvers.BisectionSolver;

import org.junit.Test;

/**
 * Tests for the UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Tests the solve method with a simple polynomial function.
     */
    @Test
    public void testSolvePolynomial() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double root = UnivariateRealSolverUtils.solve(function, -1.0, 0.0);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Tests the solve method with a function that has no roots in the given interval.
     */
    @Test
    public void testSolveNoRoots() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 0.0, 1.0});
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.solve(function, -1.0, 1.0));
    }

    /**
     * Tests the solve method with a function that throws an evaluation exception.
     */
    @Test
    public void testSolveEvaluationException() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation exception");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 0.0));
    }

    /**
     * Tests the solve method with a function that throws a convergence exception.
     */
    @Test
    public void testSolveConvergenceException() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.log(x);
                } else {
                    throw new ConvergenceException("Convergence exception");
                }
            }
        };
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(function, -1.0, 0.0));
    }

    /**
     * Tests the bracket method with a simple polynomial function.
     */
    @Test
    public void testBracketPolynomial() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double[] bracket = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        assertEquals(0.0, bracket[0], 1.0e-6);
        assertEquals(0.0, bracket[1], 1.0e-6);
    }

    /**
     * Tests the bracket method with a function that has no roots in the given interval.
     */
    @Test
    public void testBracketNoRoots() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 0.0, 1.0});
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.bracket(function, -1.0, 1.0));
    }

    /**
     * Tests the bracket method with a function that throws an evaluation exception.
     */
    @Test
    public void testBracketEvaluationException() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation exception");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(function, -1.0, 1.0));
    }

    /**
     * Tests the bracket method with a function that throws a convergence exception.
     */
    @Test
    public void testBracketConvergenceException() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.log(x);
                } else {
                    throw new ConvergenceException("Convergence exception");
                }
            }
        };
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(function, -1.0, 0.0));
    }

    /**
     * Tests the bracket method with a function that throws a maximum iterations exception.
     */
    @Test
    public void testBracketMaxIterations() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1.0 - x * x);
            }
        };
        assertThrows(MaxIterationsExceededException.class, () -> UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 1.0, 10));
    }

    /**
     * Tests the solve method with a secant solver.
     */
    @Test
    public void testSolveSecant() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double root = UnivariateRealSolverUtils.solve(new SecantSolver(), function, -1.0, 0.0);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Tests the solve method with a brent solver.
     */
    @Test
    public void testSolveBrent() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double root = UnivariateRealSolverUtils.solve(new BrentSolver(), function, -1.0, 0.0);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Tests the solve method with a laguerre solver.
     */
    @Test
    public void testSolveLaguerre() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double root = UnivariateRealSolverUtils.solve(new LaguerreSolver(), function, -1.0, 0.0);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Tests the solve method with a cubic solver.
     */
    @Test
    public void testSolveCubic() {
        UnivariateRealFunction function = x -> x * x * x - 2.0 * x * x + 1.0;
        double root = UnivariateRealSolverUtils.solve(new CubicSolver(), function, -1.0, 1.0);
        assertEquals(0.0, root, 1.0e-6);
    }

    /**
     * Tests the bracket method with a brent solver.
     */
    @Test
    public void testBracketBrent() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double[] bracket = UnivariateRealSolverUtils.bracket(new BrentSolver(), function, 0.0, -1.0, 1.0);
        assertEquals(0.0, bracket[0], 1.0e-6);
        assertEquals(0.0, bracket[1], 1.0e-6);
    }

    /**
     * Tests the bracket method with a secant solver.
     */
    @Test
    public void testBracketSecant() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double[] bracket = UnivariateRealSolverUtils.bracket(new SecantSolver(), function, 0.0, -1.0, 1.0);
        assertEquals(0.0, bracket[0], 1.0e-6);
        assertEquals(0.0, bracket[1], 1.0e-6);
    }

    /**
     * Tests the bracket method with a laguerre solver.
     */
    @Test
    public void testBracketLaguerre() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double[] bracket = UnivariateRealSolverUtils.bracket(new LaguerreSolver(), function, 0.0, -1.0, 1.0);
        assertEquals(0.0, bracket[0], 1.0e-6);
        assertEquals(0.0, bracket[1], 1.0e-6);
    }

    /**
     * Tests the bracket method with a cubic solver.
     */
    @Test
    public void testBracketCubic() {
        UnivariateRealFunction function = x -> x * x * x - 2.0 * x * x + 1.0;
        double[] bracket = UnivariateRealSolverUtils.bracket(new CubicSolver(), function, -1.0, 1.0, 1.0);
        assertEquals(-1.0, bracket[0], 1.0e-6);
        assertEquals(1.0, bracket[1], 1.0e-6);
    }

}