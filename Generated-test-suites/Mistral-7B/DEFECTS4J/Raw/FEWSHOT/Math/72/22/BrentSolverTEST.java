package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = new MockComputableFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };
        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double fx0 = function.valueAt(x0);
        double x1 = 1.0;
        double fx1 = function.valueAt(x1);

        try {
            boolean rootFound = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
            assertTrue(rootFound);
            double root = solver.getRoot();
            // replace with the expected root value
            assertEquals(0.3626865, root, 0.00001);
        } catch (FunctionException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testFindRootConvergenceFailed() {
        ComputableFunction function = new MockComputableFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.HIGH;
            }
        };
        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double fx0 = function.valueAt(x0);
        double x1 = 1.0;
        double fx1 = function.valueAt(x1);

        try {
            boolean rootFound = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
            assertFalse(rootFound);
        } catch (FunctionException e) {
            // expected
        }
    }

}

class MockComputableFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
    }
}