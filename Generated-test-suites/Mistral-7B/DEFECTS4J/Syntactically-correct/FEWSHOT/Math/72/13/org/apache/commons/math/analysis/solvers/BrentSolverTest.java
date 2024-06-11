package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                if (Math.abs(fx) < 0.0001) {
                    return ConvergenceChecker.CONVERGED;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        BrentSolver solver = new BrentSolver();

        MockFunction function = new MockFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        try {
            boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
            assertEquals(root, solver.getRoot(), 0.001);
            assertTrue(result);
        } catch (FunctionException e) {
            fail("An unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindRootDivergingFunction() {
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                if (Math.abs(fx) < 0.0001) {
                    return ConvergenceChecker.CONVERGED;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        BrentSolver solver = new BrentSolver();

        MockFunction function = new MockFunction() {
            @Override
            public double valueAt(double x) {
                return x * x;
            }
        };

        double x0 = 1.0;
        double x1 = 2.0;

        try {
            solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
            fail("The function is not converging and the method should have thrown an exception.");
        } catch (FunctionException e) {
            // Expected
        }
    }

    @Test
    public void testFindRootMaxIterations() {
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                if (Math.abs(fx) < 0.0001) {
                    return ConvergenceChecker.CONVERGED;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        BrentSolver solver = new BrentSolver();

        MockFunction function = new MockFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        try {
            boolean result = solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
            fail("The method should have thrown an exception.");
        } catch (FunctionException e) {
            // Expected
        }
    }
}