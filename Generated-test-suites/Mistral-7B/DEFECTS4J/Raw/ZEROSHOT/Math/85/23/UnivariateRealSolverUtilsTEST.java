package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double x0 = 1.0;
        double x1 = 2.0;
        double expected = -1.0;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(expected, result, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        UnivariateRealFunction f2 = (x -> Math.log(x) - Math.log(2));
        double x2 = 1.0;
        double x3 = 2.0;
        double expected2 = Math.log(2) + 0.69314718056;
        try {
            double result2 = UnivariateRealSolverUtils.solve(f2, x2, x3, 1e-6);
            assertEquals(expected2, result2, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        UnivariateRealFunction f3 = (x -> Math.sin(x) - x);
        double x4 = 0.0;
        double x5 = Math.PI;
        double expected3 = Math.PI / 2;
        try {
            double result3 = UnivariateRealSolverUtils.solve(f3, x4, x5, 1e-6);
            assertEquals(expected3, result3, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithNonValidFunction() {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction f = (x -> x * x);
        double x0 = 2.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithExhaustedIterations() {
        UnivariateRealFunction f = (x -> x * x);
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1, 0);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double initial = 1.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        double[] expected = {-1.2, -0.8};

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertArrayEquals(expected, result, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        UnivariateRealFunction f2 = (x -> Math.log(x) - Math.log(2));
        double initial2 = 1.0;
        double lowerBound2 = Math.log(1.5);
        double upperBound2 = Math.log(2.5);
        double[] expected2 = {Math.log(1.5), Math.log(2.5)};

        try {
            double[] result2 = UnivariateRealSolverUtils.bracket(f2, initial2, lowerBound2, upperBound2);
            assertArrayEquals(expected2, result2, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        UnivariateRealFunction f3 = (x -> Math.sin(x) - x);
        double initial3 = 0.0;
        double lowerBound3 = 0.5;
        double upperBound3 = 1.5;
        double[] expected3 = {Math.PI / 4, Math.PI / 2};

        try {
            double[] result3 = UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3);
            assertArrayEquals(expected3, result3, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        UnivariateRealFunction f4 = (x -> x * x);
        double initial4 = 1.0;
        double lowerBound4 = 1.0;
        double upperBound4 = 1.0;

        assertFalse(UnivariateRealSolverUtils.bracket(f4, initial4, lowerBound4, upperBound4, 1).isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidFunction() {
        UnivariateRealFunction f = null;
        double initial = 1.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() {
        UnivariateRealFunction f = (x -> x * x);
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = NoBracketingException.class)
    public void testBracketWithNoBracketing() {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) + 1);
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBracketWithNegativeMaxIterations() {
        UnivariateRealFunction f = (x -> 1 / x);
        double initial = 1.0;
        double lowerBound = 0.1;
        double upperBound = 2.0;
        int maxIterations = -1;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = TooManyIterationsException.class)
    public void testBracketWithMaxIterationsExceeded() {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double initial = 1.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        int maxIterations = 0;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 1e-6);
    }
}