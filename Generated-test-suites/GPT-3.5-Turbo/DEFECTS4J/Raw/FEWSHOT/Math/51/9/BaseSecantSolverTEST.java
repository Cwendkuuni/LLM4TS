package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) throws MathInternalError {
                return 0.0;
            }
        };

        double root = solver.solve(100, null, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolve_AccuracyAchieved() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            private boolean returnZero = true;

            @Override
            protected double computeObjectiveValue(double x) throws MathInternalError {
                if (returnZero) {
                    return 0.0;
                } else {
                    return 1.0;
                }
            }
        };

        double root = solver.solve(100, null, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolve_IntervalWithinAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            private double epsilon = 1e-7;

            @Override
            protected double computeObjectiveValue(double x) throws MathInternalError {
                // x is the expected root
                if (Math.abs(x - 0.5) < epsilon) {
                    return 0.0;
                }
                return 1.0;
            }
        };

        double root = solver.solve(100, null, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.5, root, 1e-7);
    }
}