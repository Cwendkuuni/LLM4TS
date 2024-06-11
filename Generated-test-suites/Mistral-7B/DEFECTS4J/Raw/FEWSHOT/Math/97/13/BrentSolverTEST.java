package org.apache.commons.cli.roots; // change the package name if necessary

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction; // replace with your ComputableFunction implementation
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot_simpleExample() throws FunctionException {
        double x0 = 0.0;
        double f0 = 1.0;
        double x1 = 1.0;
        double f1 = 0.0;
        ComputableFunction function = new DummyFunction(0.0); // replace with your ComputableFunction implementation
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int convergenceStatus(double x, double f) {
                if (Math.abs(f) < Double.MIN_VALUE) {
                    return ConvergenceChecker.CONVERGED_LOW;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        double root = solver.getRoot();
        assertTrue(Math.abs(root - 0.5) < 0.01);
        solver.reset(); // you may need to reset the solver if it's not idempotent
    }

    @Test
    public void testFindRoot_invalid_interval() throws FunctionException {
        double x0 = 0.0;
        double f0 = 1.0;
        double x1 = 0.0; // invalid interval
        double f1 = 0.0;
        ComputableFunction function = new DummyFunction(0.0); // replace with your ComputableFunction implementation
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int convergenceStatus(double x, double f) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    }

    @Test
    public void testFindRoot_converged_low() throws FunctionException {
        double x0 = 0.0;
        double f0 = 1.0;
        double x1 = 1.0;
        double f1 = 0.0;
        ComputableFunction function = new DummyFunction(-1.0); // replace with your ComputableFunction implementation
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int convergenceStatus(double x, double f) {
                if (Math.abs(f) < Double.MIN_VALUE) {
                    return ConvergenceChecker.CONVERGED_LOW;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        double root = solver.getRoot();
        assertTrue(Math.abs(root + 1.0) < 0.01);
        solver.reset(); // you may need to reset the solver if it's not idempotent
    }

    @Test
    public void testFindRoot_converged_high() throws FunctionException {
        double x0 = 0.0;
        double f0 = 1.0;
        double x1 = 1.0;
        double f1 = 0.0;
        ComputableFunction function = new DummyFunction(1.0); // replace with your ComputableFunction implementation
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int convergenceStatus(double x, double f) {
                if (Math.abs(f) < Double.MIN_VALUE) {
                    return ConvergenceChecker.CONVERGED_HIGH;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        double root = solver.getRoot();
        assertTrue(Math.abs(root - 1.0) < 0.01);
        solver.reset(); // you may need to reset the solver if it's not idempotent
    }

    @Test
    public void testFindRoot_converged_xm() throws FunctionException {
        double x0 = 0.0;
        double f0 = 1.0;
        double x1 = 2.0;
        double f1 = 0.0;
        ComputableFunction function = new DummyFunction(0.0); // replace with your ComputableFunction implementation
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int convergenceStatus(double x, double f) {
                if (Math.abs(f) < Double.MIN_VALUE) {
                    return ConvergenceChecker.CONVERGED_XM;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        double root = solver.getRoot();
        assertTrue(Math.abs(root - 1.0) < 0.01);
        solver.reset(); // you may need to reset the solver if it's not idempotent
    }

    @Test
    public void testFindRoot_maxIter() throws FunctionException {
        double x0 = 0.0;
        double f0 = 1.0;
        double x1 = 2.0;
        double f1 = 0.0;
        ComputableFunction function = new DummyFunction(0.0); // replace with your ComputableFunction implementation
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int convergenceStatus(double x, double f) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 0, x0, f0, x1, f1)); // maxIter = 0
        assertFalse(solver.findRoot(function, checker, 1, x0, f0, x1, f1)); // maxIter = 1
        assertFalse(solver.findRoot(function, checker, 10, x0, f0, x1, f1)); // maxIter = 10
        assertFalse(solver.findRoot(function, checker, 101, x0, f0, x1, f1)); // maxIter = 101
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_FunctionException() throws FunctionException {
        double x0 = 0.0;
        double f0 = 1.0 / 0.0; // infinite value
        double x1 = 1.0;
        double f1 = 0.0;
        ComputableFunction function = new DummyFunction(0.0); // replace with your ComputableFunction implementation
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int convergenceStatus(double x, double f) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    }

}