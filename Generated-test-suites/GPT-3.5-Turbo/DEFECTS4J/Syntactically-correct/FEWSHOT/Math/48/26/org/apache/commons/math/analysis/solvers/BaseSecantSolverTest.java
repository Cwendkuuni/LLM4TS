package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x*x - 4;
        }
    }

    @Test
    public void testSolve() {
        TestFunction f = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 1, 4);
        Assert.assertEquals(2, result, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveInvalidMethod() {
        TestFunction f = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, null);
        double result = solver.solve(100, f, 1, 4);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveNoRoot() {
        TestFunction f = new TestFunction();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 4, 5);
    }
}