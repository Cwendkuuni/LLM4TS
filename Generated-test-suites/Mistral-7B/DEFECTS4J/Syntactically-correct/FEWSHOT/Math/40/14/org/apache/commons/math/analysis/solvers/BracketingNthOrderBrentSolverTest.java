package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.TestDoubleArrays;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, 3);
        double[] expected = {0.33999999999999993, 0.33999999999999993};
        double[] actual = solver.solve(10, function, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        TestDoubleArrays.assertArraysEqualWithinEpsilon(expected, actual, 1e-8);
    }

    @Test
    public void testSolveWithCustomAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 8);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, 3);
        double[] expected = {0.33999999999999993, 0.33999999999999993};
        double[] actual = solver.solve(10, function, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        TestDoubleArrays.assertArraysEqualWithinEpsilon(expected, actual, 1e-9);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInitWithLowerThanTwoMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1, 1);
    }

    @Test
    public void testSolveWithCustomFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 8, 1e-10, 8);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, 3);
        double[] expected = {0.33999999999999993, 0.33999999999999993};
        double[] actual = solver.solve(10, function, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        TestDoubleArrays.assertArraysEqualWithinEpsilon(expected, actual, 1e-9);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNonBracketingInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 8);
        UnivariateFunction function = new PolynomialFunction(new double[]{1, 1, 1}, 3);
        double[] actual = solver.solve(10, function, 0, 0, AllowedSolution.ANY_SIDE);
        assertNull(actual);
    }

    @Test
    public void testAllowedSolutions() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 8);
        UnivariateFunction function = new PolynomialFunction(new double[]{1, 1, -1}, 3);
        double[] expectedLeft = {-1.0};
        double[] actualLeft = solver.solve(10, function, -2, 0, AllowedSolution.LEFT_SIDE);
        assertArrayEquals(expectedLeft, actualLeft);

        double[] expectedRight = {1.0};
        double[] actualRight = solver.solve(10, function, 0, 2, AllowedSolution.RIGHT_SIDE);
        assertArrayEquals(expectedRight, actualRight);

        double[] expectedBelow = {-1.5};
        double[] actualBelow = solver.solve(10, function, -2, 0, AllowedSolution.BELOW_SIDE);
        assertArrayEquals(expectedBelow, actualBelow);

        double[] expectedAbove = {1.5};
        double[] actualAbove = solver.solve(10, function, 0, 2, AllowedSolution.ABOVE_SIDE);
        assertArrayEquals(expectedAbove, actualAbove);

        double[] expectedAnySide = {0.33999999999999993, 0.33999999999999993};
        double[] actualAnySide = solver.solve(10, function, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        TestDoubleArrays.assertArraysEqualWithinEpsilon(expectedAnySide, actualAnySide, 1e-9);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 8);
        UnivariateFunction function = new PolynomialFunction(new double[]{1, 1, -1}, 3);
        solver.solve(10, function, -1.5, 1.5, AllowedSolution.NEGATIVE_INFINITY);
    }

}