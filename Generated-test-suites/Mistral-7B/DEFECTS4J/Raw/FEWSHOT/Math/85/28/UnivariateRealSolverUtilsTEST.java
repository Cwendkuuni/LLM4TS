package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.precision.ZeroOrVerySmallException;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.analysis.solvers.SimpleBisectSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.analysis.solvers.BracketedBrentSolver;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.cos(2 * x);
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;
        double solution = UnivariateRealSolverUtils.solve(f, x0, x1);

        UnivariateSolver solver = new BrentSolver();
        double solution2 = solver.solve(f, x0, x1);

        assertEquals("Solver and utility solution should be equal", solution, solution2, 1e-12);
    }

    @Test
    public void testSolve_explicitSolver() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.cos(2 * x);
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;
        double solution = UnivariateRealSolverUtils.solve(f, x0, x1, new SimpleBisectSolver());

        UnivariateSolver solver = new SimpleBisectSolver();
        double solution2 = solver.solve(f, x0, x1);

        assertEquals("Solver and utility solution should be equal", solution, solution2, 1e-12);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_convergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / (Math.sin(x) + 1.0);
            }
        };

        double x0 = 0.0;
        double x1 = 0.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test (expected = FunctionEvaluationException.class)
    public void testSolve_functionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSolve_illegalArgumentException() throws Exception {
        UnivariateRealFunction f = null;

        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolve_absoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 1.0;
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;

        double solution = UnivariateRealSolverUtils.solve(f, x0, x1, 1e-5);

        UnivariateSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(1e-5);
        double solution2 = solver.solve(f, x0, x1);

        assertEquals("Solver and utility solution should be equal", solution, solution2, 1e-12);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_functionEvaluationException_absoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-5;

        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_illegalArgumentException_absoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(f, x0, x1, -1.0);
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.cos(2 * x);
            }
        };

        double initial = 2.0;
        double lowerBound = 0.0;
        double upperBound = 4.0;

        double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);

        BracketedBrentSolver solver = new BracketedBrentSolver();
        double[] bracketedValues2 = solver.bracketRoot(f, initial, lowerBound, upperBound, 100);

        assertArrayEquals("Bracketing values should match", bracketedValues, bracketedValues2, 1e-12);
    }

    @Test
    public void testBracket_maxIterations() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.cos(2 * x);
            }
        };

        double initial = 2.0;
        double lowerBound = 0.0;
        double upperBound = 4.0;
        int maxIterations = 5;

        double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);

        BracketedBrentSolver solver = new BracketedBrentSolver();
        solver.setMaxIterations(maxIterations);
        double[] bracketedValues2 = solver.bracketRoot(f, initial, lowerBound, upperBound, maxIterations * 2);

        assertArrayEquals("Bracketing values should match", bracketedValues, bracketedValues2, 1e-12);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_convergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / (Math.sin(x) + 1.0);
            }
        };

        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test (expected = FunctionEvaluationException.class)
    public void testBracket_functionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };

        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBracket_illegalArgumentException() throws Exception {
        UnivariateRealFunction f = null;

        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracket_maxIterations_convergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                double result = Math.sin(x) / (Math.sin(x) + 1.0);
                if (result > 1.0) {
                    throw new NumberIsTooLargeException("Result is too large: " + result);
                }
                if (result < -1.0) {
                    throw new NumberIsTooSmallException("Result is too small: " + result);
                }
                if (Math.abs(result) < 1e-8) {
                    throw new ZeroOrVerySmallException("Result is too close to zero: " + result);
                }
                return result;
            }
        };

        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;
        int maxIterations = 1;

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // pass
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10 * maxIterations);
            assertTrue("Expected bracketing values to be different", Arrays.stream(UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10 * maxIterations))
                    .anyMatch(doubles -> Arrays.stream(UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations))
                            .noneMatch(d -> Arrays.equals(d, doubles))));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}