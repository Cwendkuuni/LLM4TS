package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.analysis.PiecewisePolynomialFunction;
import org.apache.commons.math.analysis.PolynomialFunctions;
import org.apache.commons.math.analysis.UnivariateFunction;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is bracketed.
     */
    @Test
    public void testSolveBracketing() {
        final UnivariateFunction f = new PiecewisePolynomialFunction(
            new double[][] { { 1.0, 2.0, 3.0 }, { 1.0, 1.0, -1.0 } },
            new double[] { 0.0, 2.0 }, new double[] { -1.0, 0.0 });

        final double sol = new BracketingNthOrderBrentSolver(1e-6, 5).solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, sol, 1e-6);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is not bracketed.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        final UnivariateFunction f = new PolynomialFunctions.Sine();
        new BracketingNthOrderBrentSolver(1e-6, 5).solve(100, f, 1.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is bracketed but has low precision.
     */
    @Test
    public void testSolveLowPrecision() {
        final UnivariateFunction f = new PolynomialFunctions.SquareRoot(100.0);

        final double sol = new BracketingNthOrderBrentSolver(1e-2, 5).solve(100, f, 0.0, 11.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.sqrt(100.0), sol, 0.01);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is bracketed but with a large maximal order.
     */
    @Test
    public void testSolveLargeOrder() {
        final UnivariateFunction f = new PolynomialFunctions.Sine();
        new BracketingNthOrderBrentSolver(1e-6, 100).solve(100, f, 0.0, Math.PI, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is bracketed but with a large maximal ageing.
     */
    @Test
    public void testSolveLargeAging() {
        final UnivariateFunction f = new PolynomialFunctions.Cubic(1.0, 1.0, 1.0, 1.0);

        final double sol = new BracketingNthOrderBrentSolver(1e-6, 5, 10).solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, sol, 1e-6);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is bracketed but with a large absolute accuracy.
     */
    @Test
    public void testSolveLargeAbsAccuracy() {
        final UnivariateFunction f = new PolynomialFunctions.Cubic(1.0, 1.0, 1.0, 1.0);

        final double sol = new BracketingNthOrderBrentSolver(1e-2, 5).solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, sol, 1e-2);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is bracketed but with a large relative accuracy.
     */
    @Test
    public void testSolveLargeRelAccuracy() {
        final UnivariateFunction f = new PolynomialFunctions.Cubic(1.0, 1.0, 1.0, 1.0);

        final double sol = new BracketingNthOrderBrentSolver(1e-2, 1e-6, 1e-2).solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, sol, 1e-2);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is bracketed but with a large function value accuracy.
     */
    @Test
    public void testSolveLargeFuncValueAccuracy() {
        final UnivariateFunction f = new PolynomialFunctions.Cubic(1.0, 1.0, 1.0, 1.0);

        final double sol = new BracketingNthOrderBrentSolver(1e-6, 5, 1e-2, 1e-2).solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, sol, 1e-6);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a constant function.
     */
    @Test
    public void testConstantFunction() {
        final UnivariateFunction f = new PolynomialFunctions.Constant(1.0);
        try {
            new BracketingNthOrderBrentSolver(1e-6, 5).solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} when given a function with a root that is not found within the given maximum number of evaluations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxEval() {
        final UnivariateFunction f = new PolynomialFunctions.Sine();
        new BracketingNthOrderBrentSolver(1e-6, 5).solve(1, f, 0.0, Math.PI, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the {@link BracketingNthOrderBrentSolver} with a function that does not implement the {@code UnivariateFunction} interface.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNotUnivariateFunction() {
        final Object f = new Object() {};
        new BracketingNthOrderBrentSolver(1e-6, 5).solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
    }

}