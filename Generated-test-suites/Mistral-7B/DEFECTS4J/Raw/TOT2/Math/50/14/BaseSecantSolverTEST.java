package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 1e-4;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-7;

    private BaseSecantSolver solver;
    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, Method.REGULA_FALSI);
    }

    /**
     * Test method solve with given accuracy and method.
     */
    @Test
    public void testSolve() {
        double root = solver.solve(100, f, -10.0, 10.0);
        assertTrue(FastMath.abs(root - 1.0) <= ABSOLUTE_ACCURACY);
    }

    /**
     * Test method solve with given accuracy and method, starting from a value
     * close to the root.
     */
    @Test
    public void testSolveStartCloseToRoot() {
        double root = solver.solve(100, f, 0.9, 1.1);
        assertTrue(FastMath.abs(root - 1.0) <= ABSOLUTE_ACCURACY);
    }

    /**
     * Test method solve with given accuracy and method, starting from a value
     * further from the root.
     */
    @Test
    public void testSolveStartFarFromRoot() {
        double root = solver.solve(100, f, -5.0, 5.0);
        assertTrue(FastMath.abs(root - 1.0) <= ABSOLUTE_ACCURACY);
    }

    /**
     * Test method solve with given accuracy and method, when the function has
     * a local minimum or maximum at the root.
     */
    @Test
    public void testSolveMinimumOrMaximum() {
        UnivariateRealFunction fMin = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solverMin = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, Method.REGULA_FALSI);
        double rootMin = solverMin.solve(100, fMin, -2.0, 2.0);
        assertTrue(FastMath.abs(rootMin - 0.0) <= ABSOLUTE_ACCURACY);

        UnivariateRealFunction fMax = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2) + 1;
            }
        };
        BaseSecantSolver solverMax = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, Method.REGULA_FALSI);
        double rootMax = solverMax.solve(100, fMax, -2.0, 2.0);
        assertTrue(FastMath.abs(rootMax - 0.0) <= ABSOLUTE_ACCURACY);
    }

    /**
     * Test method solve with given accuracy and method, when the function has
     * an inflection point at the root.
     */
    @Test
    public void testSolveInflectionPoint() {
        UnivariateRealFunction fInflection = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 3 * x;
            }
        };
        BaseSecantSolver solverInflection = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, Method.REGULA_FALSI);
        double rootInflection = solverInflection.solve(100, fInflection, -2.0, 2.0);
        assertTrue(FastMath.abs(rootInflection - 1.0) <= ABSOLUTE_ACCURACY);
    }

    /**
     * Test method solve with given accuracy and method, when the function is
     * not bracketed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveNotBracketed() {
        UnivariateRealFunction fNotBracketed = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return x * x;
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };
        BaseSecantSolver solverNotBracketed = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, Method.REGULA_FALSI);
        solverNotBracketed.solve(100, fNotBracketed, -1.0, 0.0);
    }

    /**
     * Test method solve with given accuracy and method, when the function value
     * at the current approximation is too small.
     */
    @Test
    public void testSolveFunctionValueTooSmall() {
        UnivariateRealFunction fFunctionValueTooSmall = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1e-10;
            }
        };
        BaseSecantSolver solverFunctionValueTooSmall = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, Method.REGULA_FALSI);
        double xFunctionValueTooSmall = solverFunctionValueTooSmall.solve(100, fFunctionValueTooSmall, -1.0, 1.0);
        assertNotNull(solverFunctionValueTooSmall.getLastThrowable());
        assertTrue(solverFunctionValueTooSmall.getLastThrowable() instanceof MaxIterationsExceededException);
    }

    /**
     * Test method solve with given accuracy and method, when the function value
     * is too large.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSolveFunctionValueTooLarge() {
        UnivariateRealFunction fFunctionValueTooLarge = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1e10;
            }
        };
        BaseSecantSolver solverFunctionValueTooLarge = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, Method.REGULA_FALSI);
        solverFunctionValueTooLarge.solve(100, fFunctionValueTooLarge, -1.0, 1.0);
    }

}