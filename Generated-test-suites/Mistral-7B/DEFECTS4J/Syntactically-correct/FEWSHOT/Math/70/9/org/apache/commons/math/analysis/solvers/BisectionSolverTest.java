package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.exceptions.MaxEvalException;
import org.apache.commons.math3.analysis.intervals.Interval;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x);
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double[] expected = {-1.1872089115938531, -0.841519280339111, -0.3785288332336134, 0.0, 0.3785288332336134, 0.841519280339111, 1.1872089115938531};
        double[] result = new double[expected.length];

        Interval interval = new Interval(-5, 5);

        for (int i = 0; i < expected.length; i++) {
            try {
                double root = solver.solve(f, interval);
                result[i] = root;
            } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
                // ignore and try next iteration
            }
        }

        assertEquals("Roots not found", expected.length, result.length);
        assertArrayEquals("Roots not found", expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 0.0);
    }

    @Test
    public void testBisectionSolverWithFunctionAndInitPoint() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double expected = 2.0;
        double result;

        result = solver.solve(f, 1.0, 3.0);
        assertEquals("Root not found", expected, result, 1e-6);
    }

    @Test
    public void testBisectionSolverWithFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double expected = 2.0;
        double result;

        result = solver.solve(f, new Interval(1.0, 3.0));
        assertEquals("Root not found", expected, result, 1e-6);
    }

    @Test
    public void testBisectionSolverWithMaxIterations() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver(10);

        try {
            solver.solve(f, 0.0, 1.0);
            assertNull("Root not found with max iterations", solver.getResult());
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testBisectionSolverWithFunctionAndMaxIterations() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(5);

        double expected = 2.0;
        double result;

        try {
            result = solver.solve(f, 1.0, 3.0);
            assertNotNull("Root not found with max iterations", result);
            assertEquals("Root not found with max iterations", expected, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // ignore and try next iteration
        }
    }
}