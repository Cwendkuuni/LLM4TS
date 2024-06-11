package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

public class BaseSecantSolverTest {

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 2;
        }
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 2);
        assertEquals(FastMath.sqrt(2), result, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_withAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(FastMath.sqrt(2), result, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_withMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(10, new TestFunction(), 0, 2);
        assertNull(result);
    }

    @Test
    public void testSolve_withDifferentMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new TestFunction(), 0, 2);
        assertEquals(FastMath.sqrt(2), result, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }
}