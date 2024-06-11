package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    /**
     * Test constructor with UnivariateRealFunction.
     */
    @Test
    public void testBisectionSolverWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 2 * x - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(0.0, 1.0);
        assertEquals(0.5, root, 1e-6);
    }

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testBisectionSolverDefault() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        try {
            double root = solver.solve(function, 0.0, 1.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxIterationsExceededException e) {
            // expected as the default number of iterations is 100
        }
    }

    /**
     * Test solve method with minimum and maximum values outside the interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverInvalidInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Test solve method with minimum and maximum values too close.
     */
    @Test(expected = IllegalStateException.class)
    public void testBisectionSolverTooClose() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 0.0);
    }

    /**
     * Test solve method with function returning infinite value.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.5) {
                    throw new ArithmeticException("Function Evaluation Exception");
                }
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 1.0);
    }

    /**
     * Test solve method with function returning zero value.
     */
    @Test(expected = ZeroException.class)
    public void testBisectionSolverZeroException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
                // This function has roots at x = n * PI for integer n
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 2 * Math.PI);
    }

    /**
     * Test solve method with function having a very small value.
     */
    @Test(expected = PrecisionExceededException.class)
    public void testBisectionSolverPrecisionExceededException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 0.5) - 0.1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.0, 1.0);
        fail("Expected PrecisionExceededException but got " + root);
    }

    /**
     * Test solve method with function having a root between minimum and initial.
     */
    @Test
    public void testBisectionSolverRootBetweenMinAndInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.1, 0.5);
        assertEquals(0.3416025360827973, root, 1e-6);
    }

    /**
     * Test solve method with function having a root between maximum and initial.
     */
    @Test
    public void testBisectionSolverRootBetweenMaxAndInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.1, 1.5);
        assertEquals(1.3416025360827973, root, 1e-6);
    }

    /**
     * Test solve method with function having a root close to minimum.
     */
    @Test
    public void testBisectionSolverRootCloseToMin() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.0, 0.01);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test solve method with function having a root close to maximum.
     */
    @Test
    public void testBisectionSolverRootCloseToMax() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.0, 1.1);
        assertEquals(1.0, root, 1e-6);
    }

    /**
     * Test solve method with function having a root between minimum and maximum.
     */
    @Test
    public void testBisectionSolverRootBetweenMinAndMax() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.1, 0.9);
        assertEquals(0.5416025360827973, root, 1e-6);
    }

}