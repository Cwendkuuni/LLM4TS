package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        assertEquals(Method.REGULA_FALSI, solver.method);
        assertEquals(AllowedSolution.ANY_SIDE, solver.allowed);
    }

    @Test
    public void testConstructorWithParams() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.method);
        assertEquals(AllowedSolution.ANY_SIDE, solver.allowed);
    }

    @Test
    public void testConstructorWithAccuraciesAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-7, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.method);
        assertEquals(AllowedSolution.ANY_SIDE, solver.allowed);
    }

    @Test
    public void testConstructorWithAccuraciesFunctionValueAccuracyAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-7, 1e-8, Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.method);
        assertEquals(AllowedSolution.ANY_SIDE, solver.allowed);
    }

    @Test
    public void testSolveWithDefaultParams() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction f = (x) -> x * x - 4.0;
        double result = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveWithParams() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        UnivariateRealFunction f = (x) -> x * x - 4.0;
        double result = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAccuraciesAndParams() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-7, Method.ILLINOIS);
        UnivariateRealFunction f = (x) -> x * x - 4.0;
        double result = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAccuraciesFunctionValueAccuracyAndParams() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-7, 1e-8, Method.PEGASUS);
        UnivariateRealFunction f = (x) -> x * x - 4.0;
        double result = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveWithMaxEvalExceeded() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction f = (x) -> Math.sin(x);
        assertThrows(MaxEvalExceededException.class,
                () -> solver.solve(0, f, -10.0, 10.0, 0.0));
    }

    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        double[] testCases = {-1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 0.0};
        double[] funcValues = {1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0};

        for (int i = 0; i < testCases.length; i++) {
            double min = testCases[i];
            double max = testCases[i] + 0.1;
            double x0 = min;
            double x1 = max;
            double f0 = funcValues[i];
            double f1 = funcValues[i];

            solver.verifyBracketing(min, max);
        }
    }

    @Test
    public void testAllowedSolutions() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        AllowedSolution allowedSolution = AllowedSolution.BELOW_SIDE;
        double[] testCases = {-1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 0.0};
        double[] funcValues = {1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0};

        for (int i = 0; i < testCases.length; i++) {
            double min = testCases[i];
            double max = testCases[i] + 0.1;
            double x0 = min;
            double x1 = max;
            double f0 = funcValues[i];
            double f1 = funcValues[i];

            solver.setAllowedSolution(allowedSolution);
            double result = solver.solve(10, (UnivariateRealFunction) (x) -> x * x - 4.0, min, max, 0.0);

            switch (allowedSolution) {
                case LEFT_SIDE:
                    assertTrue(f0 < 0);
                    break;
                case RIGHT_SIDE:
                    assertTrue(f1 < 0);
                    break;
                case ABOVE_SIDE:
                    assertTrue(f0 > 0);
                    break;
                case BELOW_SIDE:
                    assertTrue(f1 > 0);
                    break;
                default:
                    assertNull(result);
            }
        }
    }

    @Test
    public void testAllowedSolutionsWithInvertedInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        AllowedSolution allowedSolution = AllowedSolution.BELOW_SIDE;
        double[] testCases = {-1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 0.0};
        double[] funcValues = {1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0};

        for (int i = 0; i < testCases.length; i++) {
            double min = testCases[i];
            double max = testCases[i] + 0.1;
            double x0 = min;
            double x1 = max;
            double f0 = funcValues[i];
            double f1 = funcValues[i];

            solver.setAllowedSolution(allowedSolution);
            solver.verifyBracketing(min, max);
            x1 = min;
            x0 = max;

            solver.verifyBracketing(min, max);

            double result = solver.solve(10, (UnivariateRealFunction) (x) -> x * x - 4.0, min, max, 0.0);

            switch (allowedSolution) {
                case LEFT_SIDE:
                    assertTrue(f0 > 0);
                    break;
                case RIGHT_SIDE:
                    assertTrue(f1 > 0);
                    break;
                case ABOVE_SIDE:
                    assertTrue(f0 < 0);
                    break;
                case BELOW_SIDE:
                    assertTrue(f1 < 0);
                    break;
                default:
                    assertNull(result);
            }
        }
    }

    @Test
    public void testAllowedSolutionsWithMinEqualsRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        AllowedSolution allowedSolution = AllowedSolution.ABOVE_SIDE;
        double[] testCases = {-1.0, 1.0, 0.0, 1.0, 1.0, -1.0, 0.0};
        double[] funcValues = {1.0, -1.0, 0.0, -1.0, 1.0, -1.0, 0.0};

        for (int i = 0; i < testCases.length; i++) {
            double min = testCases[i];
            double max = testCases[i] + 0.1;
            double x0 = min;
            double x1 = max;
            double f0 = funcValues[i];
            double f1 = funcValues[i];

            solver.setAllowedSolution(allowedSolution);
            double result = solver.solve(10, (UnivariateRealFunction) (x) -> x * x - 4.0, min, max, 0.0);

            switch (allowedSolution) {
                case LEFT_SIDE:
                    assertTrue(f0 < 0);
                    break;
                case RIGHT_SIDE:
                    assertTrue(f1 < 0);
                    break;
                case ABOVE_SIDE:
                    assertTrue(f0 > 0);
                    break;
                case BELOW_SIDE:
                    assertTrue(f1 > 0);
                    break;
                default:
                    assertNull(result);
            }
        }
    }

    @Test
    public void testAllowedSolutionsWithMaxEqualsRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        AllowedSolution allowedSolution = AllowedSolution.BELOW_SIDE;
        double[] testCases = {-1.0, 1.0, 0.0, 1.0, 1.0, -1.0, 0.0};
        double[] funcValues = {1.0, -1.0, 0.0, -1.0, 1.0, -1.0, 0.0};

        for (int i = 0; i < testCases.length; i++) {
            double min = testCases[i];
            double max = testCases[i] + 0.1;
            double x0 = min;
            double x1 = max;
            double f0 = funcValues[i];
            double f1 = funcValues[i];

            solver.setAllowedSolution(allowedSolution);
            x1 = min;
            x0 = max;

            solver.verifyBracketing(min, max);

            double result = solver.solve(10, (UnivariateRealFunction) (x) -> x * x - 4.0, min, max, 0.0);

            switch (allowedSolution) {
                case LEFT_SIDE:
                    assertTrue(f0 > 0);
                    break;
                case RIGHT_SIDE:
                    assertTrue(f1 > 0);
                    break;
                case ABOVE_SIDE:
                    assertTrue(f0 < 0);
                    break;
                case BELOW_SIDE:
                    assertTrue(f1 < 0);
                    break;
                default:
                    assertNull(result);
            }
        }
    }

    @Test
    public void testAllowedSolutionsWithEqualMinAndMax() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        AllowedSolution allowedSolution = AllowedSolution.BELOW_SIDE;
        double[] testCases = {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        double[] funcValues = {1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0};

        for (int i = 0; i < testCases.length; i++) {
            double min = testCases[i];
            double max = testCases[i];
            double x0 = min;
            double x1 = max;
            double f0 = funcValues[i];
            double f1 = funcValues[i];

            solver.setAllowedSolution(allowedSolution);
            double result = solver.solve(10, (UnivariateRealFunction) (x) -> x * x - 4.0, min, max, 0.0);

            switch (allowedSolution) {
                case LEFT_SIDE:
                    assertTrue(f0 < 0);
                    break;
                case RIGHT_SIDE:
                    assertTrue(f1 < 0);
                    break;
                case ABOVE_SIDE:
                    assertTrue(f0 > 0);
                    break;
                case BELOW_SIDE:
                    assertTrue(f1 > 0);
                    break;
                default:
                    assertNull(result);
            }
        }
    }

    @Test
    public void testCalculateNextX() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double f0 = 1.0;
        double f1 = -1.0;

        double nextX = solver.calculateNextX(x1, f1, f0);

        double expectedX = (x1 * f0 - x0 * f1) / (f1 - f0);
        assertEquals(expectedX, nextX, 1e-6);
    }

    @Test
    public void testCalculateNextXWithInvertedInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double f0 = -1.0;
        double f1 = 1.0;

        double nextX = solver.calculateNextX(x1, f1, f0);

        double expectedX = (x1 * f0 - x0 * f1) / (f1 - f0);
        assertEquals(expectedX, nextX, 1e-6);
    }
}