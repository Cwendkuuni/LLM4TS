package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.PolynomialFunctions;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified arguments.
     */
    @Test
    public void testConstructor() {
        double absoluteAccuracy = 1e-5;
        int maximalOrder = 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidArguments() {
        new BracketingNthOrderBrentSolver(1, 1);
    }

    /**
     * Test getMaximalOrder method.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3);
        assertEquals(3, solver.getMaximalOrder());
    }

    /**
     * Test solving with a quadratic function.
     */
    @Test
    public void testSolveQuadratic() {
        double a = 1.0;
        double b = 2.0;
        double c = 3.0;
        UnivariateFunction f = new PolynomialFunction(new double[] {a, b, c});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -2.0;
        double max = 2.0;
        double expectedSolution = (b * b - 4 * a * c) / (2 * a);
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE);
        assertEquals(1, solutions.length);
        assertEquals(expectedSolution, solutions[0], 1e-6);
    }

    /**
     * Test solving with a function having three real roots.
     */
    @Test
    public void testSolveThreeRealRoots() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, -3.0, 2.0, -2.0, 1.0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -3.0;
        double max = 3.0;
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE);
        assertEquals(3, solutions.length);
        assertArrayEquals(new double[] {-1.0, 0.0, 2.0}, solutions, 1e-6);
    }

    /**
     * Test solving with a function having complex roots.
     */
    @Test
    public void testSolveComplexRoots() {
        UnivariateFunction f = new PolynomialFunction(new double[] {-1.0, 1.0, 1.0, 1.0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -2.0;
        double max = 2.0;
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE);
        assertFalse(solutions.length > 0);
    }

    /**
     * Test solving with a function having no real roots.
     */
    @Test
    public void testSolveNoRealRoots() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, 1.0, -1.0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -2.0;
        double max = 2.0;
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.ANY_SIDE);
        assertFalse(solutions.length > 0);
    }

    /**
     * Test solving with a function having a root on the left side.
     */
    @Test
    public void testSolveLeftSide() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, 2.0, -1.0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -3.0;
        double max = -1.0;
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.LEFT_SIDE);
        assertEquals(1, solutions.length);
        assertEquals(-2.0, solutions[0], 1e-6);
    }

    /**
     * Test solving with a function having a root on the right side.
     */
    @Test
    public void testSolveRightSide() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, -2.0, -1.0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -1.0;
        double max = 3.0;
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.RIGHT_SIDE);
        assertEquals(1, solutions.length);
        assertEquals(0.0, solutions[0], 1e-6);
    }

    /**
     * Test solving with a function having a root below the given interval.
     */
    @Test
    public void testSolveBelow() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -2.0;
        double max = 2.0;
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.BELOW_SIDE);
        assertFalse(solutions.length > 0);
    }

    /**
     * Test solving with a function having a root above the given interval.
     */
    @Test
    public void testSolveAbove() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, -1.0, -1.0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double min = -2.0;
        double max = 2.0;
        double[] solutions = solver.solve(100, f, min, max, AllowedSolution.ABOVE_SIDE);
        assertEquals(1, solutions.length);
        assertEquals(-1.0, solutions[0], 1e-6);
    }

}