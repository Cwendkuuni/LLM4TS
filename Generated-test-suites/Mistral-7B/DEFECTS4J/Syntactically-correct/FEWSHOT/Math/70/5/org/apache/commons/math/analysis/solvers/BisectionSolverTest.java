package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstruction() {
        BisectionSolver solver = new BisectionSolver();
        assertTrue(solver instanceof BisectionSolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverNullFunction() {
        new BisectionSolver(null);
    }

    @Test
    public void testSolveBisectionSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -3.0, 3.0);
        assertTrue(Math.abs(root - Math.PI / 2) < 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveBisectionSolverWithMaxIterations() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver(10);
        solver.solve(function, -3.0, 3.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveBisectionSolverWithFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new NumberIsTooSmallException("x is too small: " + x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -3.0, 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveBisectionSolverWithInvalidMaxIterations() {
        BisectionSolver solver = new BisectionSolver(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveBisectionSolverWithInvalidAbsoluteAccuracy() {
        BisectionSolver solver = new BisectionSolver(0.0);
    }

    @Test
    public void testSolveBisectionSolverWithSerialBracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils. Pow(x, 2) - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -3.0, 3.0);
        double expected = 1.0;
        assertTrue(Math.abs(result - expected) < 1e-6);
    }

    @Test
    public void testSolveBisectionSolverWithSerialBracketingAndDifferentInitialGuess() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils. Pow(x, 2) - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 2.0, 4.0);
        double expected = 3.0;
        assertTrue(Math.abs(result - expected) < 1e-6);
    }

    @Test
    public void testSolveBisectionSolverWithSerialBracketingDifferentFunction() {
        UnivariateRealFunction function1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils. Pow(x, 2) - 2.0;
            }
        };

        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result1 = solver.solve(function1, -3.0, 3.0);
        double expected1 = 1.0;
        assertTrue(Math.abs(result1 - expected1) < 1e-6);

        double result2 = solver.solve(function2, -3.0, 3.0);
        double expected2 = Math.PI / 2;
        assertTrue(Math.abs(result2 - expected2) < 1e-6);
    }

    @Test
    public void testSolveBisectionSolverWithSerialBracketingAndDifferentMaxIterations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils. Pow(x, 2) - 2.0;
            }
        };

        BisectionSolver solver1 = new BisectionSolver(100);
        double result1 = solver1.solve(function, -3.0, 3.0);
        double expected1 = 1.0;
        assertTrue(Math.abs(result1 - expected1) < 1e-6);

        BisectionSolver solver2 = new BisectionSolver(50);
        double result2 = solver2.solve(function, -3.0, 3.0);
        double expected2 = 1.0;
        assertTrue(Math.abs(result2 - expected2) < 1e-6);

        assertFalse(result1 == result2);
    }

    @Test
    public void testSolveBisectionSolverWithSerialBracketingAndDifferentAbsoluteAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils. Pow(x, 2) - 2.0;
            }
        };

        BisectionSolver solver1 = new BisectionSolver(100, 1e-10);
        double result1 = solver1.solve(function, -3.0, 3.0);
        double expected1 = 1.0;
        assertTrue(Math.abs(result1 - expected1) < 1e-10);

        BisectionSolver solver2 = new BisectionSolver(100, 1e-8);
        double result2 = solver2.solve(function, -3.0, 3.0);
        double expected2 = 1.0;
        assertTrue(Math.abs(result2 - expected2) < 1e-8);

        assertArrayEquals(new double[] { result1, result2 }, new double[] { 1.0, 1.0 }, 0.001);
    }
}