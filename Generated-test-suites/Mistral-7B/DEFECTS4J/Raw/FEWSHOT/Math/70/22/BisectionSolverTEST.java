package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstruction() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testSolverWithFunction() {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        when(f.value(0.0)).thenReturn(1.0);
        when(f.value(1.0)).thenReturn(-1.0);

        BisectionSolver solver = new BisectionSolver(f);
        double result = solver.solve(0.0, 1.0);
        assertEquals(0.5, result, 1E-6);
    }

    @Test
    public void testSolverWithoutFunction() {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        BisectionSolver solver = new BisectionSolver();

        double[] testCases = {
                new TestCase(0.0, 1.0, 0.5),
                new TestCase(-1.0, 1.0, 0.0),
                new TestCase(-1.0, 0.0, 0.0),
                new TestCase(0.0, -1.0, 0.0),
                new TestCase(1.0, 1.0, 1.0),
                new TestCase(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN),
                new TestCase(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN)
        };

        for (TestCase testCase : testCases) {
            when(f.value(testCase.min)).thenReturn(testCase.fmin);
            when(f.value(testCase.midPoint)).thenReturn(testCase.fm);

            solver.solve(testCase.min, testCase.max);
            double result = solver.getResult();
            assertEquals(testCase.root, result, testCase.tolerance);
        }
    }

    @Test
    public void testSolverWithMaxIterations() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double[] testCases = {
                new TestCase(0.0, 1.0, 0.5, 10),
                new TestCase(0.0, 1.0, 0.5, 5),
                new TestCase(0.0, 1.0, 0.5, 1)
        };

        for (TestCase testCase : testCases) {
            when(f.value(testCase.min)).thenReturn(testCase.fmin);
            when(f.value(testCase.midPoint)).thenReturn(testCase.fm);

            solver.setMaxIterations(testCase.maxIterations);
            assertThrows(MaxIterationsExceededException.class, () -> solver.solve(testCase.min, testCase.max));
        }
    }

    @Test
    public void testSolverWithRelativeAccuracy() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double[] testCases = {
                new TestCase(0.0, 1.0, 0.5, 1E-3),
                new TestCase(0.0, 1.0, 0.5, 1E-6),
                new TestCase(0.0, 1.0, 0.5, 1E-9)
        };

        for (TestCase testCase : testCases) {
            when(f.value(testCase.min)).thenReturn(testCase.fmin);
            when(f.value(testCase.midPoint)).thenReturn(testCase.fm);

            solver.setRelativeAccuracy(testCase.relativeAccuracy);
            assertThrows(FunctionEvaluationException.class, () -> solver.solve(testCase.min, testCase.max));
        }
    }

    @Test
    public void testSolverWithDivergingFunction() {
        UnivariateRealFunction f = x -> Math.log(x) * x;
        BisectionSolver solver = new BisectionSolver();

        double min = 1.0;
        double max = 2.0;

        when(f.value(min)).thenReturn(Double.NEGATIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);

        assertThrows(FunctionEvaluationException.class, () -> solver.solve(min, max));
    }

    @Test
    public void testSolverWithZeroFunction() {
        UnivariateRealFunction f = x -> 0.0;
        BisectionSolver solver = new BisectionSolver();

        double min = 1.0;
        double max = 0.0;

        when(f.value(min)).thenReturn(Double.POSITIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);

        assertThrows(FunctionEvaluationException.class, () -> solver.solve(min, max));
    }

    @Test
    public void testSolverWithFunctionNotBracketingTheRoot() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double min = 0.0;
        double max = 0.0;

        when(f.value(min)).thenReturn(Double.POSITIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);

        assertThrows(FunctionEvaluationException.class, () -> solver.solve(min, max));
    }

    @Test
    public void testSolverWithFunctionNotDecreasing() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) + 1;
        BisectionSolver solver = new BisectionSolver();

        double min = 0.0;
        double max = 1.0;

        when(f.value(min)).thenReturn(Double.NEGATIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.POSITIVE_INFINITY);

        assertThrows(FunctionEvaluationException.class, () -> solver.solve(min, max));
    }

    @Test
    public void testSolverWithFunctionBracketingTheRootTwice() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4.0;
        BisectionSolver solver = new BisectionSolver();

        double min = -2.0;
        double max = 2.0;

        when(f.value(min)).thenReturn(Double.NEGATIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.POSITIVE_INFINITY);

        assertThrows(FunctionEvaluationException.class, () -> solver.solve(min, max));
    }

    @Test
    public void testSolverWithFunctionBracketingTheRootOnce() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double min = 0.0;
        double max = 1.0;

        when(f.value(min)).thenReturn(Double.POSITIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);

        solver.solve(min, max);
        double result = solver.getResult();
        assertEquals(0.5, result, 1E-6);
    }

    @Test
    public void testSolverWithFunctionWithInitialGuess() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double min = 0.0;
        double max = 1.0;
        double initial = 0.5;

        when(f.value(min)).thenReturn(Double.POSITIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);
        when(f.value(initial)).thenReturn(-0.25);

        solver.solve(min, max, initial);
        double result = solver.getResult();
        assertEquals(0.5, result, 1E-6);
    }

    @Test
    public void testSolverWithFunctionTolerance() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double min = 0.0;
        double max = 1.0;
        double tolerance = 1E-4;

        when(f.value(min)).thenReturn(Double.POSITIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);

        solver.setAbsoluteAccuracy(tolerance);
        solver.solve(min, max);
        double result = solver.getResult();
        assertEquals(0.5, result, tolerance);
    }

    @Test
    public void testSolverWithFunctionMaxIterations() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double min = 0.0;
        double max = 1.0;
        int maxIterations = 5;

        when(f.value(min)).thenReturn(Double.POSITIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);

        solver.setMaxIterations(maxIterations);
        solver.solve(min, max);
        assertTrue(solver.isResultSet());
    }

    @Test
    public void testSolverWithFunctionMaxIterationsAndTolerance() {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BisectionSolver solver = new BisectionSolver();

        double min = 0.0;
        double max = 1.0;
        int maxIterations = 5;
        double tolerance = 1E-4;

        when(f.value(min)).thenReturn(Double.POSITIVE_INFINITY);
        when(f.value(max)).thenReturn(Double.NEGATIVE_INFINITY);

        solver.setMaxIterations(maxIterations);
        solver.setAbsoluteAccuracy(tolerance);
        solver.solve(min, max);
        assertTrue(solver.isResultSet());
    }

    private static class TestCase {
        double min;
        double midPoint;
        double fmin;
        double fm;
        double root;
        double tolerance;
        int maxIterations;

        TestCase(double min, double max, double root) {
            this.min = min;
            this.max = max;
            this.root = root;
        }

        TestCase(double min, double max, double root, int maxIterations) {
            this(min, max, root);
            this.maxIterations = maxIterations;
        }

        TestCase(double min, double max, double root, double tolerance) {
            this(min, max, root);
            this.tolerance = tolerance;
        }
    }
}