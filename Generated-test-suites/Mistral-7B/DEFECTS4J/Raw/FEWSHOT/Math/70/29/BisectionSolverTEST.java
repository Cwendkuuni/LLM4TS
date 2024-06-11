package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1.0E-6, solver.getRelativeAccuracy(), 1.0E-9);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructorWithFunction() {
        // Constructor with function argument is deprecated.
        new BisectionSolver(FunctionUtils.sin);
    }

    @Test
    public void testSolverWithPolynomialFunction() {
        double[] coefficients = {1.0, -3.0, 2.0, -1.0};
        UnivariateRealFunction function = new PolynomialFunction(coefficients);

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.0, 3.0);
        assertArrayEquals(new double[]{1.0, 2.0}, FunctionUtils.roots(function), 1.0E-8);
        assertEquals(root, FunctionUtils.value(function, 2.0), 1.0E-8);
    }

    @Test
    public void testSolverWithNegativeInitialGuess() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 1.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithFunctionThrowsMaxIterationsExceededException() {
        UnivariateRealFunction function = x -> {
            if (x > 0.0) {
                return Math.log(x) / x;
            } else {
                throw new MaxIterationsExceededException(1);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 0.0, 1.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithFunctionThrowsNumberIsTooSmallException() {
        UnivariateRealFunction function = x -> Math.exp(-Math.pow(x, 2)) - 0.1;
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 0.0, 1.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithSinFunction() {
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(FunctionUtils.sin, 0.0, Math.PI / 2.0);
            assertEquals(Math.sin(1.0), root, 1.0E-8);
        } catch (FunctionEvaluationException e) {
            fail("Expected the solver to find the root within the interval.");
        }
    }

    @Test
    public void testSolverWithLogFunction() {
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(x -> Math.log(1.0 + x), 0.0, 1.0);
            assertEquals(0.5, root, 1.0E-8);
        } catch (FunctionEvaluationException e) {
            fail("Expected the solver to find the root within the interval.");
        }
    }

    @Test
    public void testSolverWithSimpleFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        try {
            double root = solver.solve(function, -2.0, 2.0);
            assertEquals(2.0, root, 1.0E-8);
        } catch (FunctionEvaluationException e) {
            fail("Expected the solver to find the root within the interval.");
        }
    }

    @Test
    public void testSolverWithFunctionWithNegativeRoot() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = x -> Math.pow(x, 2) + 4.0;
        try {
            double root = solver.solve(function, -2.0, 2.0);
            assertFalse("Expected the solver to throw an exception, as the function does not have a negative root within the interval.", root < 0.0);
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithFunctionWithRootAtEndpoints() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        try {
            double root = solver.solve(function, 4.0, 4.0);
            assertEquals(4.0, root, 1.0E-8);
        } catch (FunctionEvaluationException e) {
            fail("Expected the solver to find the root at the endpoint.");
        }
    }

    @Test
    public void testSolverWithFunctionWithRootInMiddle() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        try {
            double root = solver.solve(function, 1.0, 3.0);
            assertEquals(2.0, root, 1.0E-8);
        } catch (FunctionEvaluationException e) {
            fail("Expected the solver to find the root within the interval.");
        }
    }

    @Test
    public void testSolverWithFunctionWithRootVeryCloseToEndpoint() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 0.001;
        try {
            double root = solver.solve(function, 0.0, 1.0);
            assertEquals(0.0, root, 1.0E-8);
        } catch (FunctionEvaluationException e) {
            fail("Expected the solver to find the root within the interval.");
        }
    }

    @Test
    public void testSolverWithFunctionWithRootFarFromEndpoint() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 100.0;
        try {
            double root = solver.solve(function, -50.0, 50.0);
            assertEquals(10.0, root, 1.0E-8);
        } catch (FunctionEvaluationException e) {
            fail("Expected the solver to find the root within the interval.");
        }
    }

    @Test
    public void testSolverWithFunctionThrowsNullPointerException() {
        UnivariateRealFunction function = null;
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 0.0, 1.0);
            fail("Expected NPE when the function is null.");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}