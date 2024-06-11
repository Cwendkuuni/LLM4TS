package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1.0e-8, 5);
    }

    /**
     * Tests the solver with a simple polynomial function.
     */
    @Test
    public void testPolynomialFunction() {
        UnivariateFunction function = new PolynomialFunction(2.0, 3.0, -1.0);
        double solution = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, solution, Precision.SAFE_EPSILON);
    }

    /**
     * Tests the solver with a function that has no bracketing.
     */
    @Test
    public void testNoBracketing() {
        UnivariateFunction function = new PolynomialFunction(-1.0, 0.0, 1.0);
        assertThrows(NoBracketingException.class, () -> solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Tests the solver with a function that has a single root but no bracketing.
     */
    @Test
    public void testSingleRootNoBracketing() {
        UnivariateFunction function = new PolynomialFunction(0.0, 0.0, 1.0);
        assertThrows(NoBracketingException.class, () -> solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Tests the solver with a function that has a single root and a bracketing interval.
     */
    @Test
    public void testSingleRootBracketing() {
        UnivariateFunction function = new PolynomialFunction(0.0, 0.0, 1.0);
        double solution = solver.solve(10, function, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, solution, Precision.SAFE_EPSILON);
    }

    /**
     * Tests the solver with a function that has multiple roots but no bracketing.
     */
    @Test
    public void testMultipleRootsNoBracketing() {
        UnivariateFunction function = new PolynomialFunction(1.0, -1.0, 1.0);
        assertThrows(NoBracketingException.class, () -> solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Tests the solver with a function that has multiple roots and a bracketing interval.
     */
    @Test
    public void testMultipleRootsBracketing() {
        UnivariateFunction function = new PolynomialFunction(1.0, -1.0, 1.0);
        double[] solutions = {-3.0, 2.0};
        for (double solution : solutions) {
            double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(solution, result, Precision.SAFE_EPSILON);
        }
    }

    /**
     * Tests the solver with a function that has a root outside of the allowed interval.
     */
    @Test
    public void testRootOutsideAllowedInterval() {
        UnivariateFunction function = new PolynomialFunction(0.0, 0.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, function, -5.0, -5.0, AllowedSolution.LEFT_SIDE));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, function, 5.0, 5.0, AllowedSolution.RIGHT_SIDE));
    }

    /**
     * Tests the solver with a function that has a root very close to the minimum.
     */
    @Test
    public void testRootVeryCloseToMinimum() {
        UnivariateFunction function = new PolynomialFunction(0.0, 1.0e-8, 1.0);
        double solution = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(PrecisionUtils.round(1.0 / Math.sqrt(2.0), 5), solution, 1e-8);
    }

    /**
     * Tests the solver with a function that has a root very close to the maximum.
     */
    @Test
    public void testRootVeryCloseToMaximum() {
        UnivariateFunction function = new PolynomialFunction(-1.0, 0.0, 1.0);
        double solution = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(PrecisionUtils.round(-1.0 / Math.sqrt(2.0), 5), solution, 1e-8);
    }

    /**
     * Tests the solver with a function that has a root very close to the start value.
     */
    @Test
    public void testRootVeryCloseToStartValue() {
        UnivariateFunction function = new PolynomialFunction(0.0, 0.0, 1.0);
        double startValue = 0.9999999999;
        double solution = solver.solve(10, function, -5.0, 5.0, startValue, AllowedSolution.ANY_SIDE);
        assertEquals(PrecisionUtils.round(1.0 / Math.sqrt(2.0), 5), solution, 1e-8);
    }

    /**
     * Tests the solver with a function that has a root very close to the maximum evaluation.
     */
    @Test
    public void testRootVeryCloseToMaxEval() {
        UnivariateFunction function = new PolynomialFunction(0.0, 1.0, 1.0);
        double solution = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(PrecisionUtils.round(1.0 / Math.sqrt(2.0), 5), solution, 1e-8);
    }

    /**
     * Tests the solver with a function that has an extremely large root.
     */
    @Test
    public void testExtremelyLargeRoot() {
        UnivariateFunction function = new PolynomialFunction(1.0e15, 0.0, 1.0);
        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Tests the solver with a function that has an extremely small root.
     */
    @Test
    public void testExtremelySmallRoot() {
        UnivariateFunction function = new PolynomialFunction(1.0e-15, 0.0, 1.0);
        assertThrows(ArithmeticException.class, () -> solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE));
    }

}