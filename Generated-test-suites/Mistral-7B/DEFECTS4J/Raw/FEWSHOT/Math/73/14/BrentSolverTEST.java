package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.sin(x) * Math.exp(x) - 2.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                double tolerance = 1e-6;
                return Math.abs(fx) < tolerance ? ConvergenceChecker.LOW :
                       Math.abs(fx2) < tolerance ? ConvergenceChecker.HIGH :
                       ConvergenceChecker.NONE;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.4603166645; // Approximate value of the root

        try {
            boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
            assertEquals(result, true);
            assertEquals(root, solver.getRoot(), 1e-6);
        } catch (FunctionException e) {
            fail("FunctionException should not be thrown.");
        }
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithException() {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                if (x > 0.0) {
                    return Math.sin(x) / x;
                } else {
                    throw new ArithmeticException("Division by zero.");
                }
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                double tolerance = 1e-6;
                return Math.abs(fx) < tolerance ? ConvergenceChecker.LOW :
                       Math.abs(fx2) < tolerance ? ConvergenceChecker.HIGH :
                       ConvergenceChecker.NONE;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 2.0;

        try {
            solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
            fail("FunctionException should be thrown.");
        } catch (FunctionException e) {
            // Expected
        }
    }
}