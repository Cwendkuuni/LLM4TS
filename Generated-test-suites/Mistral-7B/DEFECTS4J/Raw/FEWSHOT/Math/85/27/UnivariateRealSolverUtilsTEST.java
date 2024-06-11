package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction function = x -> x * x - 2;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, 1.0, -2.0, 2.0);
        assertEquals(1.0, bracketingValues[0], 0.001);
        assertEquals(1.5, bracketingValues[1], 0.001);
        
        try {
            UnivariateRealSolverUtils.solve(function, -1.0, 0.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.solve(function, 2.0, 3.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.solve(null, 0.0, 0.0);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction function = x -> Math.pow(Math.E, x) - 2;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 2.0);
        double absoluteAccuracy = 0.001;

        try {
            UnivariateRealSolverUtils.solve(function, bracketingValues[0], bracketingValues[1], absoluteAccuracy);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.solve(function, bracketingValues[0], bracketingValues[1], -absoluteAccuracy);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.solve(null, bracketingValues[0], bracketingValues[1], absoluteAccuracy);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = x -> x * x - 2;
        double initial = 1.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, 0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, Integer.MIN_VALUE);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.bracket(null, initial, lowerBound, upperBound);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, 1);
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, 100000);
            assertEquals(2, result.length);
            assertArrayEquals(new double[]{1.0, 1.5}, result, 0.001);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException");
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction function = x -> Math.pow(Math.E, x) - 2;
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        int maxIterations = 10;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, -maxIterations);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations + 1);
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations);
            assertEquals(2, result.length);
            assertArrayEquals(new double[]{0.0, 1.215}, result, 0.001);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException");
        }
    }
}