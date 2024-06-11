package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 1e-6;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-6;

    @Test
    public void testConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());

        solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(RELATIVE_ACCURACY, solver.getRelativeAccuracy(), 0);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());

        solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY,
                FUNCTION_VALUE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        assertEquals(RELATIVE_ACCURACY, solver.getRelativeAccuracy(), 0);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(FUNCTION_VALUE_ACCURACY, solver.getFunctionValueAccuracy(), 0);
        assertEquals(BaseSecantSolver.Method.PEGASUS, solver.getMethod());
    }

    @Test
    public void testSolve_ExactRoot() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, solver.solve(100, f, 1.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE), ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_BracketsRoot() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, solver.solve(100, f, 1.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE), ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_UnderApproximation() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.Illinois);
        assertEquals(0.9, solver.solve(100, f, 0.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE), ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_OverApproximation() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.Illinois);
        assertEquals(3.1, solver.solve(100, f, 2.0, 4.0, BaseSecantSolver.AllowedSolution.ANY_SIDE), ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_MaxEvalExceeded() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.Illinois);
        try {
            solver.solve(5, f, 0.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Maximal number of evaluations not exceeded");
        } catch (MathInternalError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSolve_UnsupportedMethod() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.SECANT);
        try {
            solver.solve(100, f, 0.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Method not supported");
        } catch (MathInternalError e) {
            assertTrue(true);
        }
    }
}