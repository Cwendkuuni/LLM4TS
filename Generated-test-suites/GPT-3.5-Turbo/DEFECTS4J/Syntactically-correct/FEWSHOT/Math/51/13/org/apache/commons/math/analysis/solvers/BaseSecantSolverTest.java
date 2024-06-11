package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

public class BaseSecantSolverTest {

    class MockSecantSolver extends BaseSecantSolver {

        protected MockSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        @Override
        protected double doSolve() throws MathInternalError {
            return 0;
        }

        @Override
        protected double computeObjectiveValue(double x) throws MathInternalError {
            return 0;
        }

        @Override
        protected void verifyBracketing(double min, double max) throws MathInternalError {
            assertTrue(min < max);
        }
    }

    @Test
    public void testSolve() {
        MockSecantSolver solver = new MockSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min = 1.0;
        double max = 3.0;
        double expected = 2.0;

        assertEquals(expected, solver.solve(maxEval, f, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);
    }

}