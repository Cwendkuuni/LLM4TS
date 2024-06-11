package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 1e-6;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-15;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, RELATIVE_ACCURACY, FUNCTION_VALUE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        final int maxEval = 100;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(maxEval, f, 0, 10);
        assertEquals(2.0, result, ABSOLUTE_ACCURACY);

        result = solver.solve(maxEval, f, 0, 10, 5);
        assertEquals(2.0, result, ABSOLUTE_ACCURACY);

        try {
            solver.solve(maxEval, f, -10, 0);
            fail("Exception expected");
        } catch (MathInternalError e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithAllowedSolution()
    {
        final int maxEval = 100;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(maxEval, f, 0, 10, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, ABSOLUTE_ACCURACY);

        result = solver.solve(maxEval, f, 0, 10, 5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, result, ABSOLUTE_ACCURACY);

        result = solver.solve(maxEval, f, 0, 10, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result, ABSOLUTE_ACCURACY);

        result = solver.solve(maxEval, f, 0, 10, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, result, ABSOLUTE_ACCURACY);

        result = solver.solve(maxEval, f, 0, 10, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, result, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolveWithInvalidAllowedSolution()
    {
        final int maxEval = 100;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            solver.solve(maxEval, f, 0, 10, 5, BaseSecantSolver.AllowedSolution.UNKNOWN);
            fail("Exception expected");
        } catch (MathInternalError e) {
            // Expected
        }
    }
}