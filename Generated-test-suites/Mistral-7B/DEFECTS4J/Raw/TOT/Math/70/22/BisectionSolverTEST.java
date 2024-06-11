package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.BisectionSolver;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.Pow;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

/**
 * Test cases for {@link BisectionSolver} class.
 */
public class BisectionSolverTest {

    /**
     * Test for solver with simple function.
     */
    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double expected = -1.0;
        double result = solver.solve(f, -2.0, 2.0);
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test for solver with function having roots at the boundaries.
     */
    @Test
    public void testSolveFunctionWithRootAtBoundary() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI) * Math.sin(Math.PI * x);
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double expected = 1.0 / 2.0;
        double result = solver.solve(f, -2.0, 2.0);
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test for solver with function having no roots in the interval.
     */
    @Test
    public void testSolveFunctionNoRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI) * Math.cos(Math.PI * x);
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -2.0, 2.0));
    }

    /**
     * Test for solver with function having a vertical asymptote at the midpoint.
     */
    @Test
    public void testSolveFunctionWithVerticalAsymptote() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    throw new UnsupportedOperationException("function is undefined for positive x");
                }
                return Math.pow(x, 2) - 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -2.0, 0.0));
    }

    /**
     * Test for solver with function having different signs at boundaries but no roots.
     */
    @Test
    public void testSolveFunctionWithDifferentSignsButNoRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI) * (Math.cos(Math.PI * x) > 0 ? 1.0 : -1.0);
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -2.0, 2.0));
    }

    /**
     * Test for solver with function having a horizontal asymptote.
     */
    @Test
    public void testSolveFunctionWithHorizontalAsymptote() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    /**
     * Test for solver with non-finite values at boundaries.
     */
    @Test
    public void testSolveFunctionWithNonFiniteValues() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (Math.log(Math.abs(x)) + 1.0);
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertThrows(ArithmeticException.class, () -> solver.solve(f, -1.0, 1.0));
    }

}