package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        double x0 = 1.0;
        double x1 = 2.0;
        ComputableFunction function = new MockComputableFunction(0.5, 1.5);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                return ConvergenceChecker.NONE;
            }
        };
        BrentSolver solver = new BrentSolver();
        try {
            boolean result = solver.findRoot(function, checker, 100, x0, 0.5, x1, 1.5);
            assertTrue(result);
            assertEquals(1.718281828, solver.getRoot(), 0.001);
        } catch (FunctionException e) {
            fail("Should not have thrown an exception");
        }

        x0 = 0.0;
        x1 = 1.0;
        function = new MockComputableFunction(-1.0, 0.5);
        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                if (Math.abs(fx) < 0.001) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NONE;
            }
        };
        solver = new BrentSolver();
        try {
            boolean result = solver.findRoot(function, checker, 100, x0, 0.0, x1, 0.5);
            assertTrue(result);
            assertEquals(0.5, solver.getRoot(), 0.001);
        } catch (FunctionException e) {
            fail("Should not have thrown an exception");
        }

        x0 = 1.0;
        x1 = 2.0;
        function = new MockComputableFunction(0.5, 2.5);
        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                if (Math.abs(fx2) < 0.001) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NONE;
            }
        };
        solver = new BrentSolver();
        try {
            boolean result = solver.findRoot(function, checker, 100, x0, 1.5, x1, 2.5);
            assertTrue(result);
            assertEquals(1.718281828, solver.getRoot(), 0.001);
        } catch (FunctionException e) {
            fail("Should not have thrown an exception");
        }

        // Test for maximum number of iterations
        function = new MockComputableFunction(0.0, 1.0);
        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                return ConvergenceChecker.NONE;
            }
        };
        solver = new BrentSolver();
        try {
            boolean result = solver.findRoot(function, checker, 0, x0, 0.0, x1, 0.0);
            assertFalse(result);
        } catch (FunctionException e) {
            // Expected
        }
    }

}

class MockComputableFunction implements ComputableFunction {
    private double xValue;
    private double yValue;

    public MockComputableFunction(double x, double y) {
        xValue = x;
        yValue = y;
    }

    @Override
    public double valueAt(double x) throws FunctionException {
        return yValue + (x - xValue) * 0.1;
    }
}