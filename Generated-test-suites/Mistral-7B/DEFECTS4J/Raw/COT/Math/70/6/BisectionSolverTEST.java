package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1e-6, solver.getRelativeAccuracy(), 1e-8);
    }

    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double solution = solver.solve(function, -3.0, 3.0);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveFunctionWithInitialGuess() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double solution = solver.solve(function, -3.0, 3.0, 1.0);
        assertEquals(Math.PI / 4, solution, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(1);
        solver.solve(function, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    throw new UnsupportedOperationException();
                }
                return Math.log(Math.abs(x));
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveNumberIsTooSmallException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 0.001;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -2.0, 2.0);
    }

    @Test
    public void testSolveFunctionWithDegenerateInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, 0.0, 0.0));
    }

    @Test
    public void testSolveFunctionWithLessThanMaxIterations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double solution = solver.solve(function, 0.0, 1.0, 1.0);
        assertEquals(Math.E, solution, 1e-6);
    }

    @Test
    public void testSolveFunctionWithZeroRelativeAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setRelativeAccuracy(0.0);
        double solution = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, solution, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolveFunctionWithLargeAbsoluteAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2.71828;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(100.0);
        double solution = solver.solve(function, -3.0, 3.0);
        assertEquals(1.0, solution, 100.0);
    }
}