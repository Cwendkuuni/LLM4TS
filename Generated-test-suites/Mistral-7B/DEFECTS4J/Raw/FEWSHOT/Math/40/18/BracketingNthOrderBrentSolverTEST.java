package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    // Helper function for creating a test function
    private static UnivariateFunction createTestFunction(double a, double b) {
        return new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - a, 2) - b;
            }

            @Override
            public double derivative(double x) {
                return 2 * (x - a);
            }
        };
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1);
    }

    @Test
    public void testSolveDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(BracketingNthOrderBrentSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(BracketingNthOrderBrentSolver.DEFAULT_MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @Test
    public void testSolveExplicitConstructor() {
        double absoluteAccuracy = 1e-4;
        double relativeAccuracy = 1e-6;
        int maximalOrder = 7;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveConstructorWithInvalidMaximalOrderForFunctionValueAccuracy() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        int maximalOrder = 1;
        new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, maximalOrder);
    }

    @Test
    public void testSolveWithDefaultValues() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(10, f, -1.0, 5.0);
        assertEquals(root, solution, 5e-5);
    }

    @Test
    public void testSolveWithStartValue() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double startValue = 1.5;
        double solution = solver.solve(10, f, -1.0, 5.0, startValue);
        assertEquals(root, solution, 5e-5);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithoutBracketing() {
        UnivariateFunction f = createTestFunction(1, 1);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(10, f, -1.0, 1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolveMaxEvalExceeded() {
        UnivariateFunction f = createTestFunction(1, 4);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(0, f, -1.0, 5.0);
    }

    @Test
    public void testSolveWithAllowedSolutions() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        AllowedSolution allowed = AllowedSolution.BELOW_SIDE;
        double solution = solver.solve(10, f, -1.0, 5.0, allowed);
        assertEquals(root, solution, 5e-5);
    }

    @Test
    public void testSolveWithAllowedSolutionsAbove() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        AllowedSolution allowed = AllowedSolution.ABOVE_SIDE;
        double solution = solver.solve(10, f, -1.0, 5.0, allowed);
        assertEquals(root, solution, 5e-5);
    }

    @Test
    public void testSolveWithAllowedSolutionsLeft() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        AllowedSolution allowed = AllowedSolution.LEFT_SIDE;
        double solution = solver.solve(10, f, -1.0, 5.0, allowed);
        assertEquals(root, solution, 5e-5);
    }

    @Test
    public void testSolveWithAllowedSolutionsRight() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        AllowedSolution allowed = AllowedSolution.RIGHT_SIDE;
        double solution = solver.solve(10, f, -1.0, 5.0, allowed);
        assertEquals(root, solution, 5e-5);
    }

    @Test
    public void testSolveWithAllowedSolutionsAnySide() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        AllowedSolution allowed = AllowedSolution.ANY_SIDE;
        double solution = solver.solve(10, f, -1.0, 5.0, allowed);
        assertEquals(root, solution, 5e-5);
    }

    @Test
    public void testSolveMaximalOrder() {
        UnivariateFunction f = createTestFunction(1, 4);
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1e-8, 10);
        double solution = solver.solve(10, f, -1.0, 5.0);
        assertEquals(root, solution, 1e-8);
    }

    @Test
    public void testGuessX() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{1.0, 4.0, 9.0};
        double targetY = 6.0;
        double expectedGuessedRoot = 1.5;
        double guessedRoot = new BracketingNthOrderBrentSolver().guessX(targetY, x, y, 0, x.length);
        assertEquals(expectedGuessedRoot, guessedRoot, 1e-6);
    }

}