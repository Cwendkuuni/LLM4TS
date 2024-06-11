package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        double epsilon = Math.pow(2.0, -52);

        ComputableFunction function = new MockComputableFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x1, double f1, double x2, double f2) {
                if (Math.abs(x1 - x2) < epsilon || Math.abs(f1) < Double.MIN_VALUE) {
                    return ConvergenceChecker.CONVERGED;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;
        double f0 = function.valueAt(x0);
        double f1 = function.valueAt(x1);

        boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        Assert.assertTrue(result);
        Assert.assertFalse(Double.isNaN(solver.getRoot()));
        Assert.assertEquals(root, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_notConverged() throws FunctionException {
        double epsilon = Math.pow(2.0, -52);

        ComputableFunction function = new MockComputableFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x1, double f1, double x2, double f2) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;
        double f0 = function.valueAt(x0);
        double f1 = function.valueAt(x1);

        boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

        Assert.assertFalse(result);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_FunctionException() throws FunctionException {
        double epsilon = Math.pow(2.0, -52);

        ComputableFunction function = new MockComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Test Exception");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x1, double f1, double x2, double f2) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;
        double f0 = 0.0; // arbitrary value
        double f1 = 0.0; // arbitrary value

        solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    }

}

class MockComputableFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
        return 0.0;
    }
}