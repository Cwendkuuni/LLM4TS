package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };
        double solution = solver.solve(100, null, 0, 10);

        assertEquals(FastMath.sqrt(4), solution, 1e-6);
    }

    @Test
    public void testSolveWithNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };
        try {
            solver.solve(100, null, 0, 10);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}