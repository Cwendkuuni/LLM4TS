package org.apache.commons.cli.roots; //assuming a new package name "roots" for the test class

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = new MockComputableFunction(0.0); // a function that always returns 0
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };
        BrentSolver solver = new BrentSolver();
        boolean found = solver.findRoot(function, checker, 10, 1.0, 2.0);
        assertTrue(found);
        assertEquals(1.5, solver.getRoot(), 0.001); // the root is expected to be around 1.5 for this function
    }

    @Test
    public void testFindRoot_ConvergenceError() {
        ComputableFunction function = new MockComputableFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.HIGH;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 10, 1.0, 2.0));
    }

    @Test
    public void testFindRoot_FunctionException() {
        ComputableFunction function = new MockComputableFunctionThrowingException();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 10, 1.0, 2.0));
    }

    @Test
    public void testFindRoot_MaxIterations() {
        ComputableFunction function = new MockComputableFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };
        BrentSolver solver = new BrentSolver();
        assertFalse(solver.findRoot(function, checker, 0, 1.0, 2.0));
    }

    @Test
    public void testFindRoot_BoundaryValues() {
        ComputableFunction function = new MockComputableFunction(0.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };
        BrentSolver solver = new BrentSolver();
        boolean found = solver.findRoot(function, checker, 10, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
        assertTrue(found);
    }
}

class MockComputableFunction implements ComputableFunction {
    private double result;

    public MockComputableFunction(double result) {
        this.result = result;
    }

    @Override
    public double valueAt(double x) {
        return result;
    }
}

class MockComputableFunctionThrowingException implements ComputableFunction {
    @Override
    public double valueAt(double x) {
        throw new FunctionException("FunctionException");
    }
}

interface ConvergenceChecker {
    int converged(double x, double fx, double xPrev, double fxPrev);

    static final int LOW = 0;
    static final int HIGH = 1;
}