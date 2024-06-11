package org.junit.platform.commons.annotation.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingExecutable;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.mockito.Mockito;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = Mockito.mock(UnivariateRealFunction.class);
        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithInvalidMinAndMax() {
        UnivariateRealFunction function = Mockito.mock(UnivariateRealFunction.class);
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithFunctionThatReturnsZero() {
        UnivariateRealFunction function = x -> x * x;
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testSolveWithFunctionThatHasRoot() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 2.0);
        assertEquals(-2.0, result, 0.0);
    }

    @Test
    public void testSolveWithMaxIterationsExceeded() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -2.0, 2.0, 0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithLargeAbsolutError() {
        UnivariateRealFunction function = x -> Math.abs(x - 1.0);
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 0.0, 2.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithFunctionMultipleRoots() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4; // Function with two roots
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -2.5, 2.5);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithFunctionNotContinuous() {
        UnivariateRealFunction function = x -> {
            if (x < 0) {
                return 1.0 / x;
            } else {
                return x * x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -2.0, 2.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithFunctionNaN() {
        UnivariateRealFunction function = x -> Math.sqrt(x * x - 1.0);
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 0.0, 2.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }
}