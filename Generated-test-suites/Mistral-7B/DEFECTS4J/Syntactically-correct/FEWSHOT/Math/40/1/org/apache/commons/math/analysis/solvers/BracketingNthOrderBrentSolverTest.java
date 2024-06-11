package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructorWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 3);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorThrowsException() {
        assertThrows(NumberIsTooSmallException.class, () -> new BracketingNthOrderBrentSolver(1, 1));
    }

    @Test
    public void testConstructorWithAllArgs() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 4, 1e-9, 6);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(4, solver.getMaximalOrder());
        assertEquals(1e-9, solver.getFunctionValueAccuracy());
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 3);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testSolve() throws Exception {
        UnivariateFunction f = x -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 3);
        assertEquals(-1.41421356237, root, 1e-8);
    }

    @Test
    public void testSolveWithStartValue() throws Exception {
        UnivariateFunction f = x -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 3, 1);
        assertEquals(-1.41421356237, root, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithTooSmallMaximalOrder() throws Exception {
        UnivariateFunction f = x -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1);
        solver.solve(100, f, -3, 3);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNotBracketingInterval() throws Exception {
        UnivariateFunction f = x -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        solver.solve(100, f, 5, 5);
    }

    @Test
    public void testAllowedSolutions() throws Exception {
        UnivariateFunction f = x -> x * x - 2;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 3, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.41421356237, root, 1e-8);

        root = solver.solve(100, f, -3, 3, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.41421356237, root, 1e-8);

        root = solver.solve(100, f, -3, 3, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, root, 1e-8);

        root = solver.solve(100, f, -3, 3, AllowedSolution.ABOVE_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testGuessX() {
        double[] x = new double[10];
        double[] y = new double[10];
        double[] tmpX = new double[10];

        for (int i = 0; i < 10; i++) {
            x[i] = i;
            y[i] = i * i;
        }

        double targetY = 0;
        double nextX = BracketingNthOrderBrentSolver.guessX(targetY, x, y, 0, 10);
        assertEquals(1.0, nextX, 1e-8);

        for (int i = 1; i < 10; i++) {
            nextX = BracketingNthOrderBrentSolver.guessX(targetY, x, y, i, 10);
            assertEquals(i, nextX, 1e-8);
        }

        // Test with NaN
        x[5] = Double.NaN;
        nextX = BracketingNthOrderBrentSolver.guessX(targetY, x, y, 5, 10);
        assertEquals(Double.NaN, nextX, 0.0);
    }
}