package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.apache.commons.math3.util.test.OpenMathAssert;
import org.apache.commons.math3.util.test.Values;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {
    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };

        final double root = solver.solve(10, f, -1.0, 1.0);
        OpenMathAssert.assertEquals(TestConstants.DBL_EPSILON, f.value(root), 0.0);
        assertEquals(TestConstants.DBL_EPSILON, Precision.abs(root - 0.5), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };

        try {
            solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE);
            assert false : "Expected ConvergenceException";
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.ABOVE_SIDE);
            assert false : "Expected ConvergenceException";
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
            assert false : "Expected ConvergenceException";
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.RIGHT_SIDE);
            final double root = solver.getSolution();
            OpenMathAssert.assertEquals(TestConstants.DBL_EPSILON, f.value(root), 0.0);
            assertEquals(TestConstants.DBL_EPSILON, Precision.abs(root - Values.PI), 0.0);
        } catch (ConvergenceException e) {
            assert false : "Expected solution";
        }

        try {
            solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.ANY_SIDE);
            final double root = solver.getSolution();
            OpenMathAssert.assertEquals(TestConstants.DBL_EPSILON, f.value(root), 0.0);
            assertEquals(TestConstants.DBL_EPSILON, Precision.abs(root - Values.PI), 0.0);
        } catch (ConvergenceException e) {
            assert false : "Expected solution";
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveNoAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, Method.ILLINOIS);

        try {
            solver.solve(10, f, -1.0, 1.0);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveMaxIterationsExceeded() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(Math.E, -x) - 0.5;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, Method.PEGASUS) {
            @Override
            protected double doSolve() throws MaxIterationsExceededException {
                throw new MaxIterationsExceededException();
            }
        };

        try {
            solver.solve(10, f, -10, 10);
            assert false : "Expected MaxIterationsExceededException";
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveTooManyIterations() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(Math.E, -x) - 0.5;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, Method.PEGASUS) {
            @Override
            protected double doSolve() throws TooManyIterationsException {
                throw new TooManyIterationsException();
            }
        };

        try {
            solver.solve(10, f, -10, 10);
            assert false : "Expected TooManyIterationsException";
        } catch (TooManyIterationsException e) {
            // Expected
        }
    }
}