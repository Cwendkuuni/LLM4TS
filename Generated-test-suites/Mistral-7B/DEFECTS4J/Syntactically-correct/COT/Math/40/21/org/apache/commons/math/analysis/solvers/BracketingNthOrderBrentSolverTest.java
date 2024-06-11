package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.special.BesselFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link BracketingNthOrderBrentSolver} class.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default accuracy and maximal order.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, brentSolver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, brentSolver.getMaximalOrder());
    }

    /**
     * Test constructor with provided accuracy and maximal order.
     */
    @Test
    public void testCustomConstructor() {
        double absoluteAccuracy = 1e-8;
        int maximalOrder = 10;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        assertEquals(absoluteAccuracy, brentSolver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(maximalOrder, brentSolver.getMaximalOrder());
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} method with a simple polynomial function.
     */
    @Test
    public void testSolveWithPolynomialFunction() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0}, 3);
        double min = -5.0;
        double max = 5.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        double root = 1.0;
        try {
            double solution = brentSolver.solve(10, function, min, max);
            assertEquals(root, solution, 1e-8);
        } catch (NoBracketingException e) {
            fail("Expected bracketing of the root.");
        }
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)} method with a simple polynomial function.
     */
    @Test
    public void testSolveWithPolynomialFunctionAndStartValue() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0}, 3);
        double min = -5.0;
        double max = 5.0;
        double startValue = 2.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        double root = 1.0;
        try {
            double solution = brentSolver.solve(10, function, min, max, startValue);
            assertEquals(root, solution, 1e-8);
        } catch (NoBracketingException | MaxCountExceededException e) {
            fail("Expected bracketing of the root.");
        }
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} method with a function with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketingFunction() {
        UnivariateFunction function = f -> Math.sin(f) * Math.sin(f);
        double min = -5.0;
        double max = 5.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        brentSolver.solve(10, function, min, max);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)} method with a function with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketingFunctionAndStartValue() {
        UnivariateFunction function = f -> Math.sin(f) * Math.sin(f);
        double min = -5.0;
        double max = 5.0;
        double startValue = 0.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        brentSolver.solve(10, function, min, max, startValue);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} method with a complex function.
     */
    @Test
    public void testSolveWithComplexFunction() {
        UnivariateFunction function = f -> Math.pow(BesselFunction.I0(f), 2);
        double min = 0.0;
        double max = 5.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        double solution = brentSolver.solve(10, function, min, max);
        double expectedSolution = 1.5707963267948966;
        assertEquals(expectedSolution, solution, 1e-8);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)} method with a complex function.
     */
    @Test
    public void testSolveWithComplexFunctionAndStartValue() {
        UnivariateFunction function = f -> Math.pow(BesselFunction.I0(f), 2);
        double min = 0.0;
        double max = 5.0;
        double startValue = 2.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        double solution = brentSolver.solve(10, function, min, max, startValue);
        double expectedSolution = 3.6268603962269395;
        assertEquals(expectedSolution, solution, 1e-8);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} method with a function that returns NaN.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithFunctionThatReturnsNaN() {
        UnivariateFunction function = f -> Double.NaN;
        double min = -5.0;
        double max = 5.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        brentSolver.solve(10, function, min, max);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)} method with a function that returns NaN.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithFunctionThatReturnsNaNAndStartValue() {
        UnivariateFunction function = f -> Double.NaN;
        double min = -5.0;
        double max = 5.0;
        double startValue = 2.0;
        BracketingNthOrderBrentSolver brentSolver = new BracketingNthOrderBrentSolver();
        brentSolver.solve(10, function, min, max, startValue);
    }

}