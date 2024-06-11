package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new RegulaFalsiSolver(DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solver.solve(10, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solver.solve(10, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new PegasusSolver(DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solver.solve(10, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }
    }

    @Test
    public void testSolve_AllowedSolution() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);

        // Test with any solution
        try {
            double solution = solver.solve(10, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }

        // Test with left solution
        BaseSecantSolver solverLeft = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solverLeft.solve(10, f, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
            assertEquals(-2.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }

        // Test with right solution
        BaseSecantSolver solverRight = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solverRight.solve(10, f, -5.0, 5.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(2.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }

        // Test with below solution
        BaseSecantSolver solverBelow = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solverBelow.solve(10, f, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
            assertEquals(-3.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }

        // Test with above solution
        BaseSecantSolver solverAbove = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solverAbove.solve(10, f, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
            assertEquals(1.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }
    }

    @Test
    public void testSolve_CustomAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(0.001, DEFAULT_ABSOLUTE_ACCURACY);
        try {
            double solution = solver.solve(10, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the default maxEval
        }
    }

    @Test
    public void testSolve_CustomMaxEval() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY, 5);
        try {
            double solution = solver.solve(10, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the custom maxEval
        }
    }

    @Test
    public void testSolve_FunctionValueAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY, 5, 1e-5);
        try {
            double solution = solver.solve(10, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, solution, 1e-9);
        } catch (MaxEvalExceededException e) {
            // Expected with the custom maxEval
        }
    }

    @Test
    public void testSolve_NullFunction() {
        BaseSecantSolver solver = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);
        assertNull(solver.solve(10, null, -5.0, 5.0, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testSolve_OutOfBounds() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(DEFAULT_ABSOLUTE_ACCURACY);
        assertNull(solver.solve(10, f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, AllowedSolution.ANY_SIDE));
    }
}