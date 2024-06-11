package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot_SimpleRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 2;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return (fx > 0 && Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double root = 1.5;
        double x0 = 1.0;
        double x1 = 2.0;
        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        Assert.assertTrue(result);
        Assert.assertEquals(root, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRoot_TwoRoots() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(Math.sin(x), 2) - 0.25;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return (Math.abs(fx) < 0.001) ? ConvergenceChecker.BOTH : ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.57;
        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        Assert.assertTrue(result);
        double root1 = -1.0;
        double root2 = Math.PI;
        Assert.assertEquals(root1, solver.getRoot(), 0.001);
        Assert.assertEquals(root2, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRoot_DivergentFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new ArithmeticException("Divide by zero");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        Assert.assertFalse(result);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_ThrowingFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws Exception {
                throw new Exception("Test exception");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        solver.findRoot(function, checker, 100, x0, FunctionUtils.NA, x1, FunctionUtils.NA);
    }
}