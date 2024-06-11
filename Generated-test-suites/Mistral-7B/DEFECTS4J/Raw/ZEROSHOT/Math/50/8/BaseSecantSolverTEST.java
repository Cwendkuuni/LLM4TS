package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = f -> Math.pow(Math.E, f) - Math.E;
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0.0);
            }
        };
    }

    @Test
    public void testSolverWithDefaults() {
        try {
            double root = solver.solve(10, function, -5.0, 5.0);
            assertEquals("Root not found", 0.0, root, 1e-6);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNullFunction() {
        solver.solve(10, (UnivariateRealFunction) null, -5.0, 5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidMinimum() {
        solver.solve(10, function, Double.NEGATIVE_INFINITY, 5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidMaximum() {
        solver.solve(10, function, -5.0, Double.POSITIVE_INFINITY);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithNonBracketingFunction() {
        function = f -> Math.sin(f);
        solver.solve(10, function, -3.0, 3.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverWithFunctionValueTooSmall() {
        solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0.0);
            }
        };
        try {
            double root = solver.solve(10, function, -5.0, 5.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithRelativeAndAbsoluteAccuracy() {
        solver = new BaseSecantSolver(1e-5, 1e-7, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0.0);
            }
        };
        try {
            double root = solver.solve(10, function, -5.0, 5.0);
            double actualRelError = Precision.relativeError(root, 0.0);
            double actualAbsError = Precision.absError(root, 0.0);
            assertLessThan("Relative error is too large", 1e-5, actualRelError);
            assertLessThan("Absolute error is too large", 1e-7, actualAbsError);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() {
        solver = new BaseSecantSolver(1e-6, 1e-6, 1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0.0);
            }
        };
        try {
            double root = solver.solve(10, function, -5.0, 5.0);
            double actualFvError = PrecisionUtils.relative
                    (Math.E, function.value(root), 1e-6);
            assertLessThan("Function value error is too large", 1e-6, actualFvError);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}