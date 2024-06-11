package org.apache.commons.cli.roots; // Assuming the package name of the test class is adjusted accordingly

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        double x0 = 1.0;
        double x1 = 2.0;
        double expectedRoot = 1.0;

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double x2, double f2) {
                if (Math.abs(f) < 1e-9) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(expectedRoot, solver.getRoot(), 1e-9);
        assertTrue(result);
    }

    @Test
    public void testFindRoot_function_throws_FunctionException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function error");
            }
        };

        double x0 = 1.0;
        double x1 = 2.0;
        double expectedRoot = 1.0;

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double x2, double f2) {
                if (Math.abs(f) < 1e-9) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        assertThrows(FunctionException.class, () -> {
            solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        });

        // Ensure the root is not changed
        assertNaN(solver.getRoot());
    }

    @Test
    public void testFindRoot_zero_interval() {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };

        double x0 = 0.0;
        double x1 = 0.0;

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double x2, double f2) {
                if (Math.abs(f) < 1e-9) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertNaN(solver.getRoot());
    }

    @Test
    public void testFindRoot_diverging_function() {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.log(x);
            }
        };

        double x0 = 1.0;
        double x1 = 2.0;

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double x2, double f2) {
                if (Math.abs(f) < 1e-9) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertNaN(solver.getRoot());
    }

}