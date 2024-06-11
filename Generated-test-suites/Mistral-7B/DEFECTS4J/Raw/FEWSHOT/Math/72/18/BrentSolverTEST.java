package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        double epsilon = Math.pow(2.0, -52);

        SimpleFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4.0;
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xOld, double fOld) {
                if (Math.abs(f) < epsilon) {
                    return ConvergenceChecker.CONVERGED;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;
        double root = 2.0;

        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(root, solver.getRoot());
        assertTrue(result);
    }

    @Test
    public void testFindRoot_Divergent() {
        double epsilon = Math.pow(2.0, -52);

        SimpleFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                throw new RuntimeException("Function is not defined for x = " + x);
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xOld, double fOld) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;

        assertFalse(solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN));
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_FunctionException() {
        double epsilon = Math.pow(2.0, -52);

        SimpleFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                throw new FunctionException("Function Error");
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xOld, double fOld) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;

        solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
    }
}