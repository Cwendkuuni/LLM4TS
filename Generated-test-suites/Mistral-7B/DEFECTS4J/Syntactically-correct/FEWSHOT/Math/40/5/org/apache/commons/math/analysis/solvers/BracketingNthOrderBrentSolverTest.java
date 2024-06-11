package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BracketingNthOrderBrentSolver}
 */
public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private double[] xValues;
    private double[] yValues;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-8, 8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}
     */
    @Test
    public void testConstructor() {
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(8, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}
     */
    @Test
    public void testConstructorWithRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 8);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(8, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 1e-10, 8);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(1e-10, solver.getFunctionValueAccuracy(), 1e-12);
        assertEquals(8, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}
     */
    @Test
    public void testGetMaximalOrder() {
        assertEquals(8, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}
     */
    @Test
    public void testSolveWithDefaultStartValue() throws NoBracketingException, MaxCountExceededException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 0, 0});
        double root = 0;
        xValues = new double[16];
        yValues = new double[16];
        solver.solve(100, f, -2, 2, AllowedSolution.ANY_SIDE);
        assertArrayEquals(new double[]{root, root, root, root, root, root, root, root, root, root, root, root, root, root, root, root}, xValues, 0.00001);
        assertArrayEquals(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, yValues, 0.00001);
        assertEquals(root, solver.solve(f, AllowedSolution.ANY_SIDE), 0.00001);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     */
    @Test
    public void testSolveWithSpecifiedStartValue() throws NoBracketingException, MaxCountExceededException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 0, 0});
        xValues = new double[16];
        yValues = new double[16];
        double startValue = 1.5;
        solver.solve(100, f, -2, 2, startValue, AllowedSolution.ANY_SIDE);
        assertArrayEquals(new double[]{startValue, root, root, root, root, root, root, root, root, root, root, root, root, root, root, root}, xValues, 0.00001);
        assertArrayEquals(new double[]{startValue, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, yValues, 0.00001);
        assertEquals(root, solver.solve(f, startValue, AllowedSolution.ANY_SIDE), 0.00001);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     */
    @Test
    public void testSolveWithSpecifiedStartValueAndAllowedSolution() throws NoBracketingException, MaxCountExceededException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 0, 0});
        xValues = new double[16];
        yValues = new double[16];
        double startValue = 1.5;
        solver.solve(100, f, -2, 2, startValue, AllowedSolution.LEFT_SIDE);
        assertArrayEquals(new double[]{startValue, root, root, root, root, root, root, root, root, root, root, root, root, root, root, root}, xValues, 0.00001);
        assertArrayEquals(new double[]{startValue, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, yValues, 0.00001);
        assertEquals(root, solver.solve(f, startValue, AllowedSolution.LEFT_SIDE), 0.00001);
    }

}