package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {
    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return point * point - 4;
            }
        };

        double root = solver.solve(100, -10, 10);
        assertEquals(2, root, EPSILON);
    }

    @Test
    public void testSolve_WithCustomFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double computeObjectiveValue(double point) {
                return Math.sin(point) - 0.5;
            }
        };

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double root = solver.solve(100, function, 0, 10);
        assertEquals(FastMath.PI / 6, root, EPSILON);
    }
}