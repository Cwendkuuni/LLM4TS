package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.TestUtils;
import org.apache.commons.math3.util.math.NumberTheoryUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver.AllowedSolution;

/**
 * Tests for the {@link BracketingNthOrderBrentSolver} class.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test solver with default accuracy and maximal order.
     */
    @Test
    public void testDefault() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final UnivariateFunction f = new PolynomialFunction(NumberTheoryUtils.random(10));
        final double min = PrecisionUtils.nextDown(f.value(NumberTheoryUtils.random(1.0, 10.0)));
        final double max = PrecisionUtils.nextUp(f.value(NumberTheoryUtils.random(1.0, 10.0)));
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final double solution = solver.solve(100, f, min, max, allowedSolution);
        final double absoluteError = Precision.abs(solution - f.value(solution));
        final double relativeError = Precision.relative(solution, f.value(solution));
        assertTrue("Error is too large: " + absoluteError, absoluteError <= 1e-6);
        assertTrue("Error is too large: " + relativeError, relativeError <= 1e-6);
    }

    /**
     * Test solver with specified accuracy and maximal order.
     */
    @Test
    public void testSpecified() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        final UnivariateFunction f = new PolynomialFunction(NumberTheoryUtils.random(10));
        final double min = PrecisionUtils.nextDown(f.value(NumberTheoryUtils.random(1.0, 10.0)));
        final double max = PrecisionUtils.nextUp(f.value(NumberTheoryUtils.random(1.0, 10.0)));
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final double solution = solver.solve(100, f, min, max, allowedSolution);
        final double absoluteError = Precision.abs(solution - f.value(solution));
        final double relativeError = Precision.relative(solution, f.value(solution));
        assertTrue("Error is too large: " + absoluteError, absoluteError <= 1e-8);
        assertTrue("Error is too large: " + relativeError, relativeError <= 1e-8);
    }

    /**
     * Test solver with specified relative and absolute accuracy.
     */
    @Test
    public void testSpecifiedAccuracy() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 1e-9, 1e-9);
        final UnivariateFunction f = new PolynomialFunction(NumberTheoryUtils.random(10));
        final double min = PrecisionUtils.nextDown(f.value(NumberTheoryUtils.random(1.0, 10.0)));
        final double max = PrecisionUtils.nextUp(f.value(NumberTheoryUtils.random(1.0, 10.0)));
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final double solution = solver.solve(100, f, min, max, allowedSolution);
        final double absoluteError = Precision.abs(solution - f.value(solution));
        final double relativeError = Precision.relative(solution, f.value(solution));
        assertTrue("Error is too large: " + absoluteError, absoluteError <= 1e-9);
        assertTrue("Error is too large: " + relativeError, relativeError <= 1e-7);
    }

    /**
     * Test non-bracketing interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testNonBracketing() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final UnivariateFunction f = new PolynomialFunction(NumberTheoryUtils.random(10));
        final double min = PrecisionUtils.nextDown(f.value(NumberTheoryUtils.random(1.0, 10.0)));
        final double max = min;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        solver.solve(100, f, min, max, allowedSolution);
    }

    /**
     * Test maximal order too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testMaximalOrderTooSmall() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0);
    }

}