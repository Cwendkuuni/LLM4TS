package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.UnivariateFunction;
import org.apache.commons.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.TestUtils;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructorDefault() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 4);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(4, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithLowMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-8, 1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithFunction() {
        UnivariateFunction function = (x) -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = TestUtils.sqrt(2, 1e-6);
        double solution = solver.solve(10, function, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(root, solution, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        UnivariateFunction function = (x) -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = TestUtils.sqrt(2, 1e-6);
        double startValue = 1.2;
        double solution = solver.solve(10, function, -1.5, 1.5, startValue, AllowedSolution.ANY_SIDE);
        assertEquals(root, solution, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionLeftSide() {
        UnivariateFunction function = (x) -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = TestUtils.sqrt(2, 1e-6);
        double min = -1.5;
        double max = 1.5;
        double solution = solver.solve(10, function, min, max, AllowedSolution.LEFT_SIDE);
        assertTrue(min <= solution);
    }

    @Test
    public void testSolveWithAllowedSolutionRightSide() {
        UnivariateFunction function = (x) -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = TestUtils.sqrt(2, 1e-6);
        double min = -1.5;
        double max = 1.5;
        double solution = solver.solve(10, function, min, max, AllowedSolution.RIGHT_SIDE);
        assertTrue(solution <= max);
    }

    @Test
    public void testSolveWithAllowedSolutionBelowSide() {
        UnivariateFunction function = (x) -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = TestUtils.sqrt(2, 1e-6);
        double min = -1.5;
        double max = 1.5;
        double solution = solver.solve(10, function, min, max, AllowedSolution.BELOW_SIDE);
        assertTrue(min <= solution && solution >= 0);
    }

    @Test
    public void testSolveWithAllowedSolutionAboveSide() {
        UnivariateFunction function = (x) -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = TestUtils.sqrt(2, 1e-6);
        double min = -1.5;
        double max = 1.5;
        double solution = solver.solve(10, function, min, max, AllowedSolution.ABOVE_SIDE);
        assertTrue(solution >= 0 && solution <= max);
    }

    @Test
    public void testSolveWithZeroFunction() {
        UnivariateFunction function = (x) -> 0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(10, function, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(0, solution, 1e-12);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        UnivariateFunction function = (x) -> Math.cos(x) - 0.5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5, 1e-8, 1e-8);
        double solution = solver.solve(10, function, -3, 3, AllowedSolution.ANY_SIDE);
        double expectedSolution = Math.PI / 4;
        assertEquals(expectedSolution, solution, 1e-6);
    }

    @Test
    public void testSolveWithFunctionAndMaxEval() {
        UnivariateFunction function = (x) -> Math.pow(Math.sin(x), 2) - x;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(5, function, -1, 1, AllowedSolution.ANY_SIDE);
        double expectedSolution = Math.PI / 2;
        assertEquals(expectedSolution, solution, 1e-6);
    }

    @Test
    public void testSolveWithZeroInterval() {
        UnivariateFunction function = (x) -> Math.sin(x);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertThrows(NoBracketingException.class, () -> solver.solve(10, function, 0, 0, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testSolveWithFunctionAndVerificationSequentialPoints() {
        UnivariateFunction function = (x) -> Math.pow(Math.sin(x), 2) - x;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double[] expectedPoints = new double[]{-1.1111111111111112, -0.6, 0.1111111111111111, 0.6, 1.1111111111111112};
        double[] x = new double[expectedPoints.length];
        double[] y = new double[expectedPoints.length];
        solver.solve(10, function, -2, 2, AllowedSolution.ANY_SIDE);
        for (int i = 0; i < expectedPoints.length; i++) {
            x[i] = solver.getSolution();
            y[i] = function.value(x[i]);
        }
        assertArrayEquals(expectedPoints, x, 1e-6);
        assertArrayEquals(new double[]{-0.01851108316587645, -0.003290239521159123, 0.003290239521159123, 0.01851108316587645}, y, 1e-6);
    }

    @Test
    public void testSolveWithFunctionAndVerificationFunctionValues() {
        UnivariateFunction function = (x) -> Math.pow(Math.sin(x), 2) - x;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double xAtZero = 0;
        double xAtMinusOne = -1;
        double xAtOne = 1;
        double[] expectedFunctionValues = new double[]{-1.2819714312433167, -0.01272956423051344, 0.01272956423051344, 1.2819714312433167};
        double[] functionValues = new double[expectedFunctionValues.length];
        solver.solve(10, function, -2, 2, AllowedSolution.ANY_SIDE);
        for (int i = 0; i < expectedFunctionValues.length; i++) {
            functionValues[i] = function.value(solver.getSolution());
        }
        assertArrayEquals(expectedFunctionValues, functionValues, 1e-6);
        assertEquals(-1.2819714312433167, functionValues[0], 1e-6);
        assertEquals(0.01272956423051344, functionValues[2], 1e-6);
    }
}