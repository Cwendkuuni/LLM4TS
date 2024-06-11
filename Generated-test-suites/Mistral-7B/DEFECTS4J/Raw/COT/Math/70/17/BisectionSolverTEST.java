package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

/**
 * Test class for BisectionSolver.
 */
public class BisectionSolverTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    /**
     * Test constructor with function.
     */
    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {0.0, 1.0, 1.0}, {1.0, 1.0, -1.0}});
        BisectionSolver solver = new BisectionSolver(f);
        assertNotNull(solver);
        assertEquals(f, solver.getFunction());
    }

    /**
     * Test solve method with valid input.
     * @throws Exception
     */
    @Test
    public void testSolveValidInput() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {0.0, 1.0, -1.0}, {1.0, 1.0, 1.0}});
        BisectionSolver solver = new BisectionSolver(f);
        double expectedResult = 0.75;
        double result = solver.solve(f, 0.0, 1.0);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test solve method with minimum and maximum out of order.
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {0.0, 1.0, -1.0}, {1.0, 1.0, 1.0}});
        BisectionSolver solver = new BisectionSolver(f);
        solver.solve(f, 1.0, 0.0);
    }

    /**
     * Test solve method with function that has no root in the interval.
     * @throws Exception
     */
    @Test
    public void testSolveNoRootInInterval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {0.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        BisectionSolver solver = new BisectionSolver(f);
        try {
            solver.solve(f, -1.0, 0.0);
            fail("Expected an exception");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test solve method with absolute accuracy set too low.
     * @throws Exception
     */
    @Test
    public void testSolveAbsoluteAccuracyTooLow() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {0.0, 1.0, -1.0}, {1.0, 1.0, 1.0}});
        BisectionSolver solver = new BisectionSolver(f);
        try {
            solver.setAbsoluteAccuracy(1.0);
            solver.solve(f, 0.0, 1.0);
            fail("Expected an exception");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test solve method with maximal iteration count set too low.
     * @throws Exception
     */
    @Test
    public void testSolveMaxIterationsTooLow() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {0.0, 1.0, -1.0}, {1.0, 1.0, 1.0}});
        BisectionSolver solver = new BisectionSolver(f);
        try {
            solver.setMaxIterations(1);
            solver.solve(f, 0.0, 1.0);
            fail("Expected an exception");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test solve method with function that is not continuous.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveDiscontinuousFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws NumberIsTooSmallException,
                    ZeroException, FunctionEvaluationException {
                if (x > 0.5) {
                    throw new UnsupportedOperationException();
                }
                return Math.pow(x, 2);
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        solver.solve(f, 0.0, 1.0);
    }

    /**
     * Test solve method with function that doesn't cross zero.
     * @throws Exception
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveNoCrossingFunction() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {-1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}});
        BisectionSolver solver = new BisectionSolver(f);
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solve method with function that has multiple roots.
     * @throws Exception
     */
    @Test
    public void testSolveMultipleRoots() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[][]{
            {-1.0, 1.0, 3.0}, {1.0, 1.0, -3.0}});
        BisectionSolver solver = new BisectionSolver(f);
        double[] roots = FunctionUtils.roots(f, -1.0, 1.0);
        double expectedResult = roots[0];
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(expectedResult, result, 1e-8);
    }
}