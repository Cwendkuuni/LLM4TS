package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        try {
            double result = solver.solve(100, new TestFunction(), 0, 1);
            assertEquals(0.0, result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithMaxEvalReached() {
        try {
            double result = solver.solve(1, new TestFunction(), 0, 1);
            fail("Expected exception: ConvergenceException");
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains("Maximum number of iterations (1) exceeded"));
        }
    }

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x;
        }
    }
}