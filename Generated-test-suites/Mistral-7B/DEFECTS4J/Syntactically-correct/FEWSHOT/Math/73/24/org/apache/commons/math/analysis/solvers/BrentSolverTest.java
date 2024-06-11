package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for {@link BrentSolver} class
 */
public class BrentSolverTest {

    @Test
    public void testFindRoot_simple() {
        ComputableFunction function = (x) -> x * x - 2.0;
        double x0 = 1.0;
        double x1 = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.UNKNOWN;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertTrue(Double.isFinite(solver.getRoot()));
        assertEquals(Math.sqrt(2.0), solver.getRoot(), 1e-5);
    }

    @Test
    public void testFindRoot_rootAtEnd() {
        ComputableFunction function = (x) -> x * x - 2.0;
        double x0 = 1.0;
        double x1 = 3.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.UNKNOWN;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertTrue(Double.isFinite(solver.getRoot()));
        assertEquals(Math.sqrt(2.0), solver.getRoot(), 1e-5);
    }

    @Test
    public void testFindRoot_rootAtBeginning() {
        ComputableFunction function = (x) -> x * x - 2.0;
        double x0 = 0.0;
        double x1 = 1.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.UNKNOWN;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertTrue(Double.isFinite(solver.getRoot()));
        assertEquals(Math.sqrt(2.0), solver.getRoot(), 1e-5);
    }

    @Test
    public void testFindRoot_rootInMiddle() {
        ComputableFunction function = (x) -> x * x - 2.0;
        double x0 = 0.5;
        double x1 = 1.5;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.UNKNOWN;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertTrue(Double.isFinite(solver.getRoot()));
        assertEquals(Math.sqrt(2.0), solver.getRoot(), 1e-5);
    }

    @Test
    public void testFindRoot_functionThrowsException() {
        ComputableFunction function = (x) -> {
            throw new RuntimeException("Function error");
        };
        double x0 = 1.0;
        double x1 = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.UNKNOWN;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertNull(solver.getRoot());
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_functionException() {
        ComputableFunction function = (x) -> {
            throw new FunctionException("Function exception");
        };
        double x0 = 1.0;
        double x1 = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.UNKNOWN;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    }

    @Test
    public void testFindRoot_convergenceChecker() {
        ComputableFunction function = (x) -> Math.pow(x, 3) - 2.0;
        double x0 = 1.0;
        double x1 = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return (fx * fxp < 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertTrue(Double.isFinite(solver.getRoot()));
        assertEquals(1.0, solver.getRoot(), 1e-5);
    }

    @Test
    public void testFindRoot_convergenceChecker_high() {
        ComputableFunction function = (x) -> Math.pow(x, 3) - 2.0;
        double x0 = 1.0;
        double x1 = 3.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.HIGH;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertTrue(Double.isFinite(solver.getRoot()));
        assertEquals(1.0, solver.getRoot(), 1e-5);
    }

    @Test
    public void testFindRoot_convergenceChecker_low() {
        ComputableFunction function = (x) -> Math.pow(x, 3) - 2.0;
        double x0 = 3.0;
        double x1 = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.LOW;
            }
        };
        int maxIter = 100;

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertTrue(Double.isFinite(solver.getRoot()));
        assertEquals(1.0, solver.getRoot(), 1e-5);
    }

    @Test
    public void testFindRoot_maxIter() {
        ComputableFunction function = (x) -> Math.pow(x, 3) - 2.0;
        double x0 = 1.0;
        double x1 = 2.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xp, double fxp) {
                return ConvergenceChecker.UNKNOWN;
            }
        };
        int maxIter = 0;

        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertNull(solver.getRoot());
    }

}