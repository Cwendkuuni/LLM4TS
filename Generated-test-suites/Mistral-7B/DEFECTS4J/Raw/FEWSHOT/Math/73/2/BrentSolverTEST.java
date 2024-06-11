package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = new MockFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xPrev, double fPrev) {
                return ConvergenceChecker.NEITHER;
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        double root = solver.getRoot();
        assertEquals(2.0, root, 0.001);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootFailure() throws FunctionException {
        double x0 = 1.0;
        double x1 = 3.0;
        ComputableFunction function = new MockFunctionWithException();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xPrev, double fPrev) {
                return ConvergenceChecker.NEITHER;
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    }

    @Test
    public void testFindRootConverged() throws FunctionException {
        double x0 = 1.0;
        double x1 = 2.0;
        ComputableFunction function = new MockFunctionConverged();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xPrev, double fPrev) {
                return ConvergenceChecker.LOW;
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        double root = solver.getRoot();
        assertEquals(1.5, root, 0.001);
    }

    @Test
    public void testFindRootConvergedHigh() throws FunctionException {
        double x0 = 1.0;
        double x1 = 2.0;
        ComputableFunction function = new MockFunctionConverged();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xPrev, double fPrev) {
                return ConvergenceChecker.HIGH;
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        double root = solver.getRoot();
        assertEquals(1.5, root, 0.001);
    }

    static class MockFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return Math.sin(x) - x;
        }
    }

    static class MockFunctionWithException implements ComputableFunction {
        @Override
        public double valueAt(double x) throws FunctionException {
            throw new FunctionException("MockFunctionWithException");
        }
    }

    static class MockFunctionConverged implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return Math.sin(x) - x;
        }

        @Override
        public boolean checkDerivative(double x, double fx) {
            return true;
        }
    }

}

class ConvergenceChecker {
    public static final int LOW = 0;
    public static final int HIGH = 1;
    public static final int NEITHER = 2;

    public abstract int converged(double x, double f, double xPrev, double fPrev);
}