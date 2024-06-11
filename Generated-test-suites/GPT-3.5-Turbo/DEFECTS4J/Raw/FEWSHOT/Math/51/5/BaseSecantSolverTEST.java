package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BaseSecantSolverTest {
    protected static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    protected static class TestSecantSolver extends BaseSecantSolver {
        public TestSecantSolver(final double absoluteAccuracy, final Method method) {
            super(absoluteAccuracy, method);
        }

        public TestSecantSolver(final double relativeAccuracy, final double absoluteAccuracy, final Method method) {
            super(relativeAccuracy, absoluteAccuracy, method);
        }

        public TestSecantSolver(final double relativeAccuracy, final double absoluteAccuracy, final double functionValueAccuracy, final Method method) {
            super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        }

        public double computeObjectiveValue(double x) {
            return x * x - 4;
        }
    }

    @Test
    public void testSolve() {
        TestSecantSolver solver = new TestSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, null, -5, 5, 0, AllowedSolution.ANY_SIDE);
        assertEquals(2, root, DEFAULT_ABSOLUTE_ACCURACY);
    }
    
    @Test
    public void testComputeObjectiveValue() {
        TestSecantSolver solver = new TestSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        double value = solver.computeObjectiveValue(2);
        assertEquals(0, value, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testAllowedSolution() {
        TestSecantSolver solver = new TestSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);

        // Any side
        solver.setAllowedSolution(AllowedSolution.ANY_SIDE);
        double root = solver.solve(100, null, -5, 5, 0);
        assertEquals(2, root, DEFAULT_ABSOLUTE_ACCURACY);

        // Left side
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        root = solver.solve(100, null, -5, 5, 0);
        assertEquals(-2, root, DEFAULT_ABSOLUTE_ACCURACY);

        // Right side
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        root = solver.solve(100, null, -5, 5, 0);
        assertEquals(2, root, DEFAULT_ABSOLUTE_ACCURACY);

        // Below side
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        root = solver.solve(100, null, -5, 5, 0);
        assertEquals(2, root, DEFAULT_ABSOLUTE_ACCURACY);

        // Above side
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        root = solver.solve(100, null, -5, 5, 0);
        assertEquals(-2, root, DEFAULT_ABSOLUTE_ACCURACY);
    }
}