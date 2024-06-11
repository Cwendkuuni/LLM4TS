package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test Solver with simple function.
     */
    @Test
    public void testSolveSimpleFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.0001, 0.01, Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10, f, -3.0, 3.0, 0.0), 4), 0.0);
    }

    /**
     * Test Solver with function that has no roots.
     */
    @Test(expected = MathRuntimeError.class)
    public void testSolveNoRootFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.0001, 0.01, Method.REGULA_FALSI);
        solver.solve(10, f, -5.0, 5.0, 0.0);
    }

    /**
     * Test Solver with function that has multiple roots.
     */
    @Test
    public void testSolveMultipleRootFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 2.0) * (x - 3.0) * (x - 4.0);
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.0001, 0.01, Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10, f, -5.0, 5.0, 2.5), 4), 0.0);
        assertEquals(3.0, Precision.round(solver.solve(10, f, -5.0, 5.0, 3.5), 4), 0.0);
        assertEquals(4.0, Precision.round(solver.solve(10, f, -5.0, 5.0, 4.5), 4), 0.0);
    }

    /**
     * Test Solver with function that is undefined at the interval bounds.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveUndefinedFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 0) {
                    throw new ArithmeticException("Function undefined at x=" + x);
                }
                return Math.log(x);
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.0001, 0.01, Method.REGULA_FALSI);
        solver.solve(10, f, -1.0, 1.0, 0.0);
    }
}