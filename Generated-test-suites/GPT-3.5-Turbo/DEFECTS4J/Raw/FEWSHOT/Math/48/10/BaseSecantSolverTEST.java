package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;
    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 1e-5;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-15;

    private BaseSecantSolver solver;

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.876726, result, EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testMaxEvalExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        solver.solve(2, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        assertEquals(2.876729, result, EPSILON);

        result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(0.876726, result, EPSILON);

        result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        assertEquals(0.876726, result, EPSILON);

        result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        assertEquals(2.876729, result, EPSILON);
    }

    @Test
    public void testMinAndMaxValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        double result = solver.solve(100, f, -1e15, 1e15, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.876726, result, EPSILON);
    }

    @Test
    public void testRelativeAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.876726, result, EPSILON);
    }

    @Test
    public void testAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.876726, result, EPSILON);
    }

    @Test
    public void testFunctionValueAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.876726, result, EPSILON);
    }

    @Test
    public void testDoSolve() {
        solver = new BaseSecantSolver(100, 1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return 0;
            }

            @Override
            protected double getMin() {
                return 0;
            }

            @Override
            protected double getMax() {
                return 10;
            }

            @Override
            protected void verifyBracketing(double min, double max) throws ConvergenceException {
                // Do nothing
            }
        };

        double result = solver.doSolve();

        assertEquals(5, result, EPSILON);
    }
}