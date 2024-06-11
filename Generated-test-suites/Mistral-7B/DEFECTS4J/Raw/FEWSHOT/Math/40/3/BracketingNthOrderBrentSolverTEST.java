package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @Test
    public void testDefaultConstructor() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() {
        final double absoluteAccuracy = 1e-7;
        final int maximalOrder = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-9;
        final int maximalOrder = 15;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder() {
        final double relativeAccuracy = 1e-9;
        final double absoluteAccuracy = 1e-10;
        final double functionValueAccuracy = 1e-11;
        final int maximalOrder = 20;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-12);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithDefaultValues() throws NoBracketingException, MaxCountExceededException {
        final UnivariateFunction f = new PolynomialFunction(new double[] {3, 1, -4}, new double[] {3, 1, 4});
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double root = solver.solve(100, f, -5, 5, allowedSolution);
        assertEquals(-2.0, root, 1e-12);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithStartValue() throws NoBracketingException, MaxCountExceededException {
        final UnivariateFunction f = new PolynomialFunction(new double[] {3, 1, -4}, new double[] {3, 1, 4});
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double root = solver.solve(100, f, -5, 5, 0, allowedSolution);
        assertEquals(-2.0, root, 1e-12);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, AllowedSolution)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalArgument() throws NoBracketingException, MaxCountExceededException {
        final UnivariateFunction f = (x) -> 0;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, f, -5, 5, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double, AllowedSolution)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalArgument2() throws NoBracketingException, MaxCountExceededException {
        final UnivariateFunction f = (x) -> 0;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, f, -5, 5, 0, 0, AllowedSolution.ANY_SIDE);
    }

}