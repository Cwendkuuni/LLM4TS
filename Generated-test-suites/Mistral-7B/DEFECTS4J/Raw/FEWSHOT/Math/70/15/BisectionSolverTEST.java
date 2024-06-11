package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

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
     * Test solving a simple function.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testSimpleFunction() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 0);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -1, 1);
        assertEquals(0.0, root, 1e-12);
    }

    /**
     * Test solving a function with initial guess.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testFunctionWithInitialGuess() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -2, 0);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -3, 1, 0);
        assertEquals(0.0, root, 1e-12);
    }

    /**
     * Test solving a function with different intervals.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testDifferentIntervals() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 1);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, 0, 2);
        assertEquals(1.0, root, 1e-12);
    }

    /**
     * Test solving a function with maximal iterations.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterations() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 0);
        BisectionSolver solver = new BisectionSolver(10);
        solver.solve(f, -1, 1);
    }

    /**
     * Test solving a function with minimal accuracy.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testMinimalAccuracy() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 0);
        BisectionSolver solver = new BisectionSolver(1);
        solver.solve(f, -1, 1);
    }

    /**
     * Test solving a function with null interval.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInterval() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 0);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, null, 1);
    }

    /**
     * Test solving a function with a function that evaluates to zero at both boundaries.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionZeroAtBothBoundaries() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, 0, 0);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1, 1);
    }

    /**
     * Test solving a function with a function that is not continuous.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFunctionNotContinuous() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 0) {
            @Override
            public double value(double x) {
                if (x == 0.5) {
                    throw new UnsupportedOperationException("Function is not continuous at x = 0.5");
                }
                return super.value(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1, 1);
    }
}