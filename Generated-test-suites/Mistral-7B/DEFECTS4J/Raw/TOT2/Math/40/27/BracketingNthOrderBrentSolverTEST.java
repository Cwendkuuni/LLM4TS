package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test for the {@link BracketingNthOrderBrentSolver} class.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test the solver with a simple function and standard settings.
     */
    @Test
    public void testSimpleFunction() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.0001, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 2;
            }
        };
        final double root = solver.solve(100, f, -10.0, 10.0, 0.0);
        assertEquals(0.25, root, 1e-7);
    }

    /**
     * Test the solver with a simple function and custom settings.
     */
    @Test
    public void testSimpleFunctionCustom() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                0.001, 7, 1e-9, 3);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 2;
            }
        };
        final double root = solver.solve(100, f, -10.0, 10.0, 0.0);
        assertEquals(0.25, root, 1e-9);
    }

    /**
     * Test the solver with a function that does not bracket the root.
     */
    @Test
    public void testNonBracketingFunction() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 7);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, -5.0, 5.0, 0.0));
    }

    /**
     * Test the solver with a function with a large relative accuracy.
     */
    @Test
    public void testLargeRelativeAccuracy() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.1, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 1.0;
            }
        };
        final double root = solver.solve(100, f, -10.0, 10.0, 0.0);
        assertEquals(0.8813759925454338, root, 0.1);
    }

    /**
     * Test the solver with a function with a large absolute accuracy.
     */
    @Test
    public void testLargeAbsoluteAccuracy() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-4, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1.0;
            }
        };
        final double root = solver.solve(100, f, -10.0, 10.0, 0.0);
        assertEquals(1.0471975511965979, root, 1.0e-5);
    }

    /**
     * Test the solver with a function that returns zero at the root.
     */
    @Test
    public void testZeroValueAtRoot() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-4, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }
        };
        final double root = solver.solve(100, f, -10.0, 10.0, 0.0);
        assertEquals(1.0, root, 1.0e-5);
    }

    /**
     * Test the solver with a function that returns NaN at the root.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNaNAtRoot() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-4, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new NumberIsTooSmallException(x, 1.0e-324, true);
                }
                return Math.log(x) - Math.log(2);
            }
        };
        solver.solve(100, f, -10.0, 10.0, 0.0);
    }

    /**
     * Test the solver with a function that throws an exception in computeObjectiveValue.
     */
    @Test(expected = MathInternalError.class)
    public void testExceptionInComputeObjectiveValue() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-4, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new MathInternalError("Internal Error");
            }
        };
        solver.solve(100, f, -10.0, 10.0, 0.0);
    }
}