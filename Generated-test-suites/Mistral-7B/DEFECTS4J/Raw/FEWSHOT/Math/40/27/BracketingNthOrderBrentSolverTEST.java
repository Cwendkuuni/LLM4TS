package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solverWithMaximalOrderFive = new BracketingNthOrderBrentSolver(5);
        assertEquals(5, solverWithMaximalOrderFive.getMaximalOrder());

        BracketingNthOrderBrentSolver solverWithMaximalOrderTen = new BracketingNthOrderBrentSolver(10);
        assertEquals(10, solverWithMaximalOrderTen.getMaximalOrder());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolveWithFunctionNotBracketingRoot() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(root));
    }

    @Test
    public void testSolveWithFunctionBracketingRoot() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, root, 1e-8);
    }

    @Test
    public void testSolveWithFunctionBracketingRootAndSpecificAllowedSolution() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, root, 1e-8);
    }

    @Test
    public void testSolveWithFunctionBracketingRootAndStartValue() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -2.0, 2.0, -1.5, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, root, 1e-8);
    }

    @Test
    public void testSolveWithDifferentAllowedSolutions() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        double rootBelow = solver.solve(10, f, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, rootBelow, 1e-8);

        double rootAbove = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, rootAbove, 1e-8);

        double rootLeft = solver.solve(10, f, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, rootLeft, 1e-8);
    }

    @Test
    public void testSolveWithFunctionValuesCloseEnough() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 0.001;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, root, 1e-8);
    }

    @Test
    public void testSolveWithFunctionValuesIdentical() {
        UnivariateFunction f = x -> Math.pow(x, 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, root, 1e-8);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithFunctionNotBracketingRootAndAllowedSolution() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(10, f, -1.5, 1.5, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveWithMaxEvalReached() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        try {
            solver.solve(0, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertFalse(true);
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testGuessX() {
        double[] x = {-2.0, -1.5, -1.0, 0.0, 1.0, 2.0};
        double[] y = {-3.5, -2.5, -1.5, 0.0, 1.5, 3.5};
        double targetY = 0.0;
        double expectedX = -1.0;
        double[] tmpX = new double[x.length];

        double result = new BracketingNthOrderBrentSolver().guessX(targetY, x, y, 1, x.length);

        assertEquals(expectedX, result, 1e-8);
    }

    @Test
    public void testGuessXWithIdenticalYValue() {
        double[] x = {-2.0, -1.5, -1.0, -1.0, 1.0, 1.0};
        double[] y = {-3.5, -3.5, -1.5, 0.0, 1.5, 1.5};
        double targetY = 0.0;
        double expectedX = Double.NaN;
        double[] tmpX = new double[x.length];

        double result = new BracketingNthOrderBrentSolver().guessX(targetY, x, y, 1, x.length);

        assertEquals(expectedX, result);
    }
}