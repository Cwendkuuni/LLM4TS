package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class BaseSecantSolverTest {
    @Test
    public void testConstructor() {
        BaseSecantSolver solver1 = new DummySecantSolver(0.1, 0.01, 0.001);
        assertEquals(0.1, solver1.getRelativeAccuracy(), 0.0);
        assertEquals(0.01, solver1.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.001, solver1.getFunctionValueAccuracy(), 0.0);

        BaseSecantSolver solver2 = new DummySecantSolver(0.01, 0.001);
        assertEquals(0.01, solver2.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solver2.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-14, solver2.getFunctionValueAccuracy(), 0.0);

        BaseSecantSolver solver3 = new DummySecantSolver(0.001);
        assertEquals(1e-14, solver3.getRelativeAccuracy(), 0.0);
        assertEquals(1e-14, solver3.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-14, solver3.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new DummySecantSolver(0.01, 0.001);

        // Test solving a linear function
        UnivariateRealFunction f1 = new DummyFunction(2, 3);  // f(x) = 2x + 3
        double root1 = solver.solve(100, f1, -10, 10);
        assertEquals(-1.5, root1, 0.01);

        // Test solving a quadratic function
        UnivariateRealFunction f2 = new DummyFunction(1, -5, 6); // f(x) = x^2 - 5x + 6
        double root2 = solver.solve(100, f2, 0, 5);
        assertEquals(2, root2, 0.01);

        // Test solving a cubic function
        UnivariateRealFunction f3 = new DummyFunction(1, -3, 3, -1); // f(x) = x^3 - 3x^2 + 3x - 1
        double root3 = solver.solve(100, f3, -1, 2);
        assertEquals(1, root3, 0.01);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMaxIterationsExceeded() {
        BaseSecantSolver solver = new DummySecantSolver(0.01, 0.001);

        // Test solving an unbounded function
        // Using a function that has no root in the given interval [0, 1]
        UnivariateRealFunction f = new DummyFunction(1, 1); // f(x) = x + 1
        solver.solve(5, f, 0, 1);
    }

    private class DummySecantSolver extends BaseSecantSolver {
        public DummySecantSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
            super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
        }

        public DummySecantSolver(double relativeAccuracy, double absoluteAccuracy) {
            super(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI);
        }

        public DummySecantSolver(double relativeAccuracy) {
            super(relativeAccuracy, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            return x;  // Dummy function f(x) = x
        }
    }

    private class DummyFunction implements UnivariateRealFunction {
        private double[] coefficients;

        public DummyFunction(double... coefficients) {
            this.coefficients = coefficients;
        }

        @Override
        public double value(double x) {
            double result = 0.0;
            for (int i = 0; i < coefficients.length; i++) {
                result += coefficients[i] * Math.pow(x, i);
            }
            return result;
        }
    }
}