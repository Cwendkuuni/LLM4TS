package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Test;

/**
 * Test cases for BracketingNthOrderBrentSolver class.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testConstructorWithAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 4);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-8, 6);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(6, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyFunctionValueAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-8, 7);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getAbsoluteAccuracy()}.
     */
    @Test
    public void testGetAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        double[] xData = {-1.0, 0.0, 1.0};
        double[] yData = {2.0, 0.0, 2.0};
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double result = solver.solve(xData.length, function, xData[0], xData[1]);
        assertEquals(xData[1], result, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        double[] xData = {-1.0, 0.0, 1.0};
        double[] yData = {2.0, 0.0, 2.0};
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double result = solver.solve(xData.length, function, xData[0], xData[1], 0.5);
        assertEquals(xData[1], result, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getAllowedSolution()}.
     */
    @Test
    public void testGetAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#setAllowedSolution(AllowedSolution)}.
     */
    @Test
    public void testSetAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#computeObjectiveValue(double)}.
     */
    @Test
    public void testComputeObjectiveValue() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(0.0, solver.computeObjectiveValue(0.0), 1e-8);
        assertEquals(2.0, solver.computeObjectiveValue(1.0), 1e-8);
        assertFalse(Double.isNaN(solver.computeObjectiveValue(-1.0)));
    }
}