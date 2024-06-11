package org.apache.commons.math.analysis.solvers;

import org.junit.Test;
import org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_anySide() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -1;
        double max = 1;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(100, f, min, max, allowedSolution);

        assertEquals(result, 0, absoluteAccuracy);
    }

    @Test
    public void testSolve_leftSide() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -1;
        double max = 1;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.LEFT_SIDE;

        double result = solver.solve(100, f, min, max, allowedSolution);

        assertEquals(result, -absoluteAccuracy, absoluteAccuracy);
    }

    @Test
    public void testSolve_rightSide() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -1;
        double max = 1;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.RIGHT_SIDE;

        double result = solver.solve(100, f, min, max, allowedSolution);

        assertEquals(result, absoluteAccuracy, absoluteAccuracy);
    }

    @Test
    public void testSolve_belowSide() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        double min = -1;
        double max = 1;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.BELOW_SIDE;

        double result = solver.solve(100, f, min, max, allowedSolution);

        assertEquals(result, -1, absoluteAccuracy);
    }

    @Test
    public void testSolve_aboveSide() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double min = -1;
        double max = 1;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ABOVE_SIDE;

        double result = solver.solve(100, f, min, max, allowedSolution);

        assertEquals(result, 1, absoluteAccuracy);
    }
}