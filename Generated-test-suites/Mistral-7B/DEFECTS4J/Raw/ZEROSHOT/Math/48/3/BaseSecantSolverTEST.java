package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test that solver correctly returns roots using double precision
     * arithmetic.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testSolverWithDoublePrecisionArithmetic()
            throws Exception {
        final UnivariateFunction f = new PolynomialFunction(3, new double[]{-1.0,
            3.0, 2.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.REGULA_FALSI);

        // Test root at x = 1.0
        double root1 = solver.solve(f, -1.0, 1.0);
        assertEquals(1.0, Precision.round(root1, 10), 1.0);

        // Test root at x = 1.5
        double root2 = solver.solve(f, -1.0, 1.5);
        assertEquals(1.5, Precision.round(root2, 10), 1.5);

        // Test root at x = 2.0
        double root3 = solver.solve(f, 1.5, 2.0);
        assertEquals(2.0, Precision.round(root3, 10), 2.0);
    }

    /**
     * Test that solver correctly handles unbracketed function.
     */
    @Test(expected = UnbracketedException.class)
    public void testUnbracketedFunction() {
        final UnivariateFunction f = new PolynomialFunction(1, new double[]{0.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.REGULA_FALSI);
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test that solver correctly handles maximum number of iterations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterations() {
        final UnivariateFunction f = new PolynomialFunction(1, new double[]{0.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.REGULA_FALSI, 1);
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test that solver correctly handles function value too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testFunctionValueTooSmall() {
        final UnivariateFunction f = new PolynomialFunction(1, new double[]{1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-18, Method.REGULA_FALSI);
        solver.solve(f, -1.0, 1.0);
    }
}