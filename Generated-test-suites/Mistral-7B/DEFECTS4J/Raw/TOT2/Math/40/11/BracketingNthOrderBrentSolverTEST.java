package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the BracketingNthOrderBrentSolver class.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test solver with simple function f(x) = x^2 - 2.
     * <p>
     * This test checks that the solver correctly returns the root of the function
     * in the given interval.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double result = solver.solve(100, function, -2.5, 2.5);
        assertEquals("The solver should return the root of the function in the given interval",
                     0.0, Precision.round(result, 4));
    }

    /**
     * Test solver with function f(x) = sin(x) and maximal order 3.
     * <p>
     * This test checks that the solver correctly returns the root of the function
     * in the given interval, even if the function has no analytical derivative.
     */
    @Test
    public void testSinFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        double result = solver.solve(100, function, -3.14159, 3.14159);
        assertEquals("The solver should return the root of the function in the given interval",
                     0.0, Precision.round(result, 4));
    }

    /**
     * Test solver with function f(x) = exp(x) - 2 and maximal order 2.
     * <p>
     * This test checks that the solver correctly returns the root of the function
     * in the given interval, even if the function has no explicit root.
     */
    @Test
    public void testExpFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }

            @Override
            public double derivative(double x) {
                return Math.exp(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 2);
        double result = solver.solve(100, function, -3, 2);
        assertEquals("The solver should return the root of the function in the given interval",
                     0.69314718056, Precision.round(result, 6));
    }

    /**
     * Test solver with function f(x) = 1 / (x - 1) and maximal order 4.
     * <p>
     * This test checks that the solver correctly returns the root of the function
     * in the given interval, even if the function has a vertical asymptote.
     */
    @Test
    public void testVerticalAsymptote() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (x - 1);
            }

            @Override
            public double derivative(double x) {
                return -1.0 / (Math.pow(x, 2.0));
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 4);
        double result = solver.solve(100, function, 2, 3);
        assertEquals("The solver should return the root of the function in the given interval",
                     1.0, Precision.round(result, 4));
    }

    /**
     * Test exception handling when the given interval does not bracket the root
     * of the function.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 4 * (x - 2);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        solver.solve(100, function, 1, 1);
    }

    /**
     * Test exception handling when the maximal order is lower than 2.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
        solver.solve(100, function, -2.5, 2.5);
    }

}