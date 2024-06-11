package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.junit.Test;

/**
 * JUnit tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    @Test
    public void testConstructor() {
        // Test the default constructor
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());

        // Test the constructor with custom absolute accuracy
        solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);

        // Test the constructor with custom relative accuracy and absolute accuracy
        solver = new BaseSecantSolver(1e-5, 1e-7, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 0.0);

        // Test the constructor with function value accuracy
        solver = new BaseSecantSolver(1e-5, 1e-7, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-8, solver.getFunctionValueAccuracy(), 0.0);
    }

    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithMaxEval() throws MaxEvalException {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -Math.log(Math.abs(x - 1.0)));
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(10, f, 0.0, 3.0, 1.5);
        assertEquals(1.0, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionWithInvalidMin() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, f, -Math.PI, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionWithInvalidMax() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, f, 0.0, Math.PI, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionWithInvalidStartValue() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, f, 0.0, 0.0, -1.0);
    }

    @Test
    public void testSolveFunctionWithInvertedInterval() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, f, 2.0, 0.0, 0.0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithAllowedSolutionLeft() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, AllowedSolution.LEFT_SIDE);
        double result = solver.solve(100, f, -2.0, 2.0, 0.0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithAllowedSolutionRight() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, AllowedSolution.RIGHT_SIDE);
        double result = solver.solve(100, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithAllowedSolutionBelow() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        double result = solver.solve(100, f, 2.0, 0.0, 0.0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithAllowedSolutionAbove() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        double result = solver.solve(100, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithException() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Test exception");
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        assertThrows(MaxEvalException.class, () -> solver.solve(1, f, 0.0, 0.0, 0.0));
    }
}