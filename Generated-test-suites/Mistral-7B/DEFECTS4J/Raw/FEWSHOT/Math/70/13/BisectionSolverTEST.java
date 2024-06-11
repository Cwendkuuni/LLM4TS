package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolverWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(function, -2.0, 2.0);
            assertEquals(-0.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNullFunction() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(null, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidMin() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -Math.PI, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidMax() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, Math.PI);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolverWithMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolverWithZeroFunctionValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.001;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }

    @Test(expected = PrecisionExceededException.class)
    public void testSolverWithPrecisionExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(1);
        try {
            solver.solve(function, -1.0, 1.0);
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithZero() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, 0.0, 0.0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithFunctionCrossingOrigin() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1E-6;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, -1.0, 1.0);
            assertEquals(-0.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionCrossingZeroAtEndpoints() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, 1.0, 2.0);
            assertEquals(-1.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionCrossingZeroInMiddle() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, 0.5, 1.0);
            assertEquals(-0.1, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}