package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method.
     */
    @Test
    public void testSolve() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return x * x - 1;
            }
        };
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        final double root = solver.solve(f, -3.0, 3.0);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method.
     */
    @Test
    public void testSolveAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return x * x - 1;
            }
        };
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS, AllowedSolution.BELOW_SIDE);
        final double root = solver.solve(f, -3.0, 3.0);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with an illegal argument.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveIllegalArgument() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                throw new UnsupportedOperationException();
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with an illegal argument.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveAllowedSolutionIllegalArgument() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                throw new UnsupportedOperationException();
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS, AllowedSolution.BELOW_SIDE);
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a function that does not bracket the root.
     */
    @Test
    public void testSolveNoBracketing() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.pow(x, 2) - 3;
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        final double result = solver.solve(f, -5.0, 0.0);
        Assert.fail("Expected a MathInternalError, but got " + result);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with a function that does not bracket the root.
     */
    @Test
    public void testSolveAllowedSolutionNoBracketing() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.pow(x, 2) - 3;
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS, AllowedSolution.BELOW_SIDE);
        final double result = solver.solve(f, -5.0, 0.0);
        Assert.fail("Expected a MathInternalError, but got " + result);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a function that does not converge.
     */
    @Test
    public void testSolveNoConvergence() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.sin(1 / x);
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        final double result = solver.solve(f, -1.0, 1.0);
        Assert.fail("Expected a MathInternalError, but got " + result);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with a function that does not converge.
     */
    @Test
    public void testSolveAllowedSolutionNoConvergence() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.sin(1 / x);
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS, AllowedSolution.BELOW_SIDE);
        final double result = solver.solve(f, -1.0, 1.0);
        Assert.fail("Expected a MathInternalError, but got " + result);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a function that has a singularity.
     */
    @Test
    public void testSolveSingularity() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                if (x == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return Math.log(x);
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        final double result = solver.solve(f, 0.1, 1.0);
        Assert.fail("Expected a MathInternalError, but got " + result);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with a function that has a singularity.
     */
    @Test
    public void testSolveAllowedSolutionSingularity() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                if (x == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return Math.log(x);
            }
        };
        final IllinoisSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS, AllowedSolution.BELOW_SIDE);
        final double result = solver.solve(f, 0.1, 1.0);
        Assert.fail("Expected a MathInternalError, but got " + result);
    }
}