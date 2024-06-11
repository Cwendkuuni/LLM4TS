package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRootInSimpleCase() {
        double epsilon = Math.pow(2.0, -52);
        DummyFunction function = new DummyFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xHigh, double fHigh) {
                return ConvergenceChecker.NOT_YET;
            }
        };
        int maxIter = 10;
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 0.0));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootInConvergedCase() {
        double epsilon = Math.pow(2.0, -52);
        DummyFunction function = new DummyFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xHigh, double fHigh) {
                if (Math.abs(f) < epsilon) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NOT_YET;
            }
        };
        int maxIter = 10;
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 0.0));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootInConvergedHighCase() {
        double epsilon = Math.pow(2.0, -52);
        DummyFunction function = new DummyFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xHigh, double fHigh) {
                if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NOT_YET;
            }
        };
        int maxIter = 10;
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 0.0));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootInConvergedNullFunctionCase() {
        double epsilon = Math.pow(2.0, -52);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xHigh, double fHigh) {
                return ConvergenceChecker.NOT_YET;
            }
        };
        int maxIter = 10;
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        BrentSolver solver = new BrentSolver();
        DummyFunction function = null;
        assertFalse(solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 0.0));
        assertNull(solver.getRoot());
    }

    @Test
    public void testFindRootInDivergentCase() {
        double epsilon = Math.pow(2.0, -52);
        DummyFunction function = new DummyFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xHigh, double fHigh) {
                return ConvergenceChecker.NOT_YET;
            }
        };
        int maxIter = 10;
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        BrentSolver solver = new BrentSolver();
        DummyFunction functionDivergent = new DummyFunction(() -> Math.pow(2.0, 100.0));
        assertFalse(solver.findRoot(functionDivergent, checker, maxIter, x0, 0.0, x1, 0.0));
        assertNull(solver.getRoot());
    }

    @Test
    public void testFindRootInMaxIterCase() {
        double epsilon = Math.pow(2.0, -52);
        DummyFunction function = new DummyFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xHigh, double fHigh) {
                return ConvergenceChecker.NOT_YET;
            }
        };
        int maxIter = 0;
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 0.0));
        assertNull(solver.getRoot());
    }
}