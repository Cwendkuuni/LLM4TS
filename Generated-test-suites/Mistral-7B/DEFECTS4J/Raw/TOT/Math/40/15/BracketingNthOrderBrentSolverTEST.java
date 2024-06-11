package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.function.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Test solving a simple function.
     */
    @Test
    public void testSimpleFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals("root not found", 0.0, root, 1e-6);
    }

    /**
     * Test solving a simple function with a known root.
     */
    @Test
    public void testSimpleFunctionWithKnownRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals("root not found", 1.0, root, 1e-6);
    }

    /**
     * Test solving a simple function with a root outside the defined interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testSimpleFunctionWithRootOutsideInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 2.0;
            }
        };
        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solving a simple function with a root very close to the defined interval boundary.
     */
    @Test
    public void testSimpleFunctionWithRootNearIntervalBoundary() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.001;
            }
        };
        double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals("root not found", 1.999999999999996, root, 1e-6);
    }

    /**
     * Test solving a simple function with a very large maximal order.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSimpleFunctionWithVeryLargeMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 128);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solving a simple function with a very large maximum number of evaluations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testSimpleFunctionWithVeryLargeMaxEval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver.solve(Integer.MAX_VALUE, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solving a simple function with a negative relative accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleFunctionWithNegativeRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(-0.01, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solving a simple function with a negative absolute accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleFunctionWithNegativeAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.0, -1e-6);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solving a simple function with a non-positive function value accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleFunctionWithNonPositiveFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver.solve(100, f, -3.0, 3.0, 0.0);
    }

    /**
     * Test solving a simple function with a non-positive maximal order.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleFunctionWithNonPositiveMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, -5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

}