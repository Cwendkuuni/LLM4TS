package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() {
        // Create test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        // Create test object
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }

            @Override
            protected void verifyBracketing(double x0, double x1) throws IllegalArgumentException, MathInternalError {}

            @Override
            protected double getFunctionValueAccuracy() {
                return 1e-6;
            }

            @Override
            protected double getAbsoluteAccuracy() {
                return 1e-10;
            }

            @Override
            protected double getRelativeAccuracy() {
                return 1e-10;
            }
        };

        double root = solver.solve(100, function, 0, 10);

        assertEquals(2, root, EPSILON);
    }

    @Test
    public void testSolve_maxEval() {
        // Create test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        // Create test object
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }

            @Override
            protected void verifyBracketing(double x0, double x1) throws IllegalArgumentException, MathInternalError {}

            @Override
            protected double getFunctionValueAccuracy() {
                return 1e-6;
            }

            @Override
            protected double getAbsoluteAccuracy() {
                return 1e-10;
            }

            @Override
            protected double getRelativeAccuracy() {
                return 1e-10;
            }
        };

        double root = solver.solve(5, function, 0, 10);

        assertEquals(-1, root, EPSILON);
    }

    @Test
    public void testSolve_allowedSolution() {
        // Create test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        // Create test object
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }

            @Override
            protected void verifyBracketing(double x0, double x1) throws IllegalArgumentException, MathInternalError {}

            @Override
            protected double getFunctionValueAccuracy() {
                return 1e-6;
            }

            @Override
            protected double getAbsoluteAccuracy() {
                return 1e-10;
            }

            @Override
            protected double getRelativeAccuracy() {
                return 1e-10;
            }
        };

        double root = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        assertEquals(0, root, EPSILON);
    }

    @Test
    public void testSolve_invertedInterval() {
        // Create test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        // Create test object
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }

            @Override
            protected void verifyBracketing(double x0, double x1) throws IllegalArgumentException, MathInternalError {}

            @Override
            protected double getFunctionValueAccuracy() {
                return 1e-6;
            }

            @Override
            protected double getAbsoluteAccuracy() {
                return 1e-10;
            }

            @Override
            protected double getRelativeAccuracy() {
                return 1e-10;
            }
        };

        double root = solver.solve(100, function, 10, 0);

        assertEquals(2, root, EPSILON);
    }

    @Test
    public void testSolve_smallAccuracy() {
        // Create test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        // Create test object
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }

            @Override
            protected void verifyBracketing(double x0, double x1) throws IllegalArgumentException, MathInternalError {}

            @Override
            protected double getFunctionValueAccuracy() {
                return 1e-6;
            }

            @Override
            protected double getAbsoluteAccuracy() {
                return 1e-10;
            }

            @Override
            protected double getRelativeAccuracy() {
                return 1e-10;
            }
        };

        double root = solver.solve(100, function, 0, 10);

        assertEquals(2, root, EPSILON);
    }

    @Test
    public void testSolve_zeroFunctionValue() {
        // Create test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return 0;
            }
        };

        // Create test object
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }

            @Override
            protected void verifyBracketing(double x0, double x1) throws IllegalArgumentException, MathInternalError {}

            @Override
            protected double getFunctionValueAccuracy() {
                return 1e-6;
            }

            @Override
            protected double getAbsoluteAccuracy() {
                return 1e-10;
            }

            @Override
            protected double getRelativeAccuracy() {
                return 1e-10;
            }
        };

        double root = solver.solve(100, function, 0, 10);

        assertEquals(0, root, EPSILON);
    }
}