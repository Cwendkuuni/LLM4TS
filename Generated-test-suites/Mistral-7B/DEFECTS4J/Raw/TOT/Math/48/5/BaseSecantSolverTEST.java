package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.Precision;

/**
 * Tests the {@link BaseSecantSolver} abstract base class for bracketing
 * root-finding methods.
 */
public class BaseSecantSolverTest {

    /**
     * A simple function for testing the root-finding algorithms. This function
     * has a root at x = 1.
     */
    private static class TestFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x - 1;
        }

        @Override
        public double derivative(double x) {
            return 1;
        }

        @Override
        public void checkDerivativeAt(double x, double tolerance) {
            // Not used in this test
        }

    }

    /**
     * Tests the {@link BaseSecantSolver} with the {@link RegulaFalsiSolver}
     * method.
     */
    @Test
    public void testRegulaFalsiSolver() {
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(0.001);
        UnivariateFunction function = new TestFunction();
        double root = 1;
        try {
            double answer = solver.solve(100, function, 0, 2, 0.5);
            assertTrue(Precision.abs(answer, 0.001) <= Precision.EPSILON);
            assertEquals(root, answer, 0.001);
        } catch (MaxEvalExceededException e) {
            fail("The maximum number of iterations should not be reached.");
        } catch (ConvergenceException e) {
            fail("The root should have been found.");
        }
    }

    /**
     * Tests the {@link BaseSecantSolver} with the {@link IllinoisSolver}
     * method.
     */
    @Test
    public void testIllinoisSolver() {
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(0.001);
        UnivariateFunction function = new TestFunction();
        double root = 1;
        try {
            BaseSecantSolver solverWithIllinoisMethod = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS) {
                @Override
                protected double doSolve() {
                    return super.doSolve();
                }
            };
            double answer = solverWithIllinoisMethod.solve(100, function, 0, 2, 0.5);
            assertTrue(Precision.abs(answer, 0.001) <= Precision.EPSILON);
            assertEquals(root, answer, 0.001);
        } catch (MaxEvalExceededException e) {
            fail("The maximum number of iterations should not be reached.");
        } catch (ConvergenceException e) {
            fail("The root should have been found.");
        }
    }

    /**
     * Tests that the {@link BaseSecantSolver} throws a
     * {@link NullArgumentException} when the function is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(0.001);
        solver.solve(100, null, 0, 2, 0.5);
    }

    /**
     * Tests that the {@link BaseSecantSolver} throws a
     * {@link NumberIsTooLargeException} when the maximum number of evaluations
     * is surpassed.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testMaxEvalExceeded() {
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(0.001);
        UnivariateFunction function = new TestFunction();
        solver.solve(0, function, 0, 2, 0.5);
    }

    /**
     * Tests that the {@link BaseSecantSolver} throws a
     * {@link ConvergenceException} when the algorithm does not converge.
     */
    @Test
    public void testNoConvergence() {
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(0.001);
        UnivariateFunction function = (x) -> Math.sin(x) / x;
        try {
            solver.solve(100, function, -10, 10, 0.5);
            fail("The algorithm should not have converged.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

}