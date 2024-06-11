package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        double x0 = 1.0, x1 = 2.0, expectedRoot = 1.5;
        ComputableFunction function = new MockFunction(expectedRoot);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrevious, double fxPrevious) {
                return ConvergenceChecker.CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(expectedRoot, solver.getRoot(), epsilon);
        assertTrue(result);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithException() throws FunctionException {
        double x0 = 1.0, x1 = 2.0, expectedRoot = 1.5;
        ComputableFunction function = new MockFunctionThrowingException();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrevious, double fxPrevious) {
                return ConvergenceChecker.CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    }

    @Test
    public void testFindRootWithDivergence() throws FunctionException {
        double x0 = 1.0, x1 = 2.0, expectedRoot = Double.NaN;
        ComputableFunction function = new MockFunctionDiverging();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrevious, double fxPrevious) {
                return ConvergenceChecker.DIVERGING;
            }
        };
        BrentSolver solver = new BrentSolver();
        double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
        assertEquals(expectedRoot, root);
        assertNull(root);
    }

}

class MockFunction implements ComputableFunction {
    private double value;

    public MockFunction(double value) {
        this.value = value;
    }

    @Override
    public double valueAt(double x) {
        return value;
    }
}

class MockFunctionThrowingException implements ComputableFunction {
    @Override
    public double valueAt(double x) {
        throw new RuntimeException();
    }
}

class MockFunctionDiverging implements ComputableFunction {
    @Override
    public double valueAt(double x) {
        throw new IllegalArgumentException();
    }
}

interface ConvergenceChecker {
    int converged(double x, double fx, double xPrevious, double fxPrevious);

    static final int CONVERGED = 0;
    static final int DIVERGING = 1;
    static final int LOW = 2;
    static final int HIGH = 3;
}