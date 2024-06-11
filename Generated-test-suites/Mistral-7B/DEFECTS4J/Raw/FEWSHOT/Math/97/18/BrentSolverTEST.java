package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = TestFunctions.SIN;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double fx0 = Math.sin(x0);
        double x1 = 1.0;
        double fx1 = Math.sin(x1);
        try {
            assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
            double root = solver.getRoot();
            assertTrue(Math.abs(root - 0.8414) < 0.001);
        } catch (FunctionException e) {
            fail(e.getMessage());
        }

        x0 = 1.0;
        x1 = 2.0;
        fx0 = Math.sin(x0);
        fx1 = Math.sin(x1);
        try {
            assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
            double root = solver.getRoot();
            assertTrue(Math.abs(root + 1) < 0.001);
        } catch (FunctionException e) {
            fail(e.getMessage());
        }

        x0 = -1.0;
        x1 = 0.0;
        fx0 = Math.sin(x0);
        fx1 = Math.sin(x1);
        try {
            assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
            double root = solver.getRoot();
            assertTrue(Math.abs(root) < 0.001);
        } catch (FunctionException e) {
            fail(e.getMessage());
        }

        x0 = 0.0;
        x1 = 0.0;
        fx0 = 0.0;
        fx1 = 0.0;
        try {
            assertFalse(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
            fail("Expected function exception");
        } catch (FunctionException e) {
            // expected
        }

        function = FunctionUtils.QUADRATIC_FUNCTION;
        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                double error = Math.abs(x - xPrev);
                return error < 0.001 ? ConvergenceChecker.CONVERGED_X
                        : Math.abs(fx) < 0.00001 ? ConvergenceChecker.CONVERGED_F
                        : ConvergenceChecker.NOT_CONVERGED;
            }
        };
        x0 = 1.0;
        fx0 = FunctionUtils.QUADRATIC_FUNCTION.valueAt(x0);
        x1 = -1.0;
        fx1 = FunctionUtils.QUADRATIC_FUNCTION.valueAt(x1);
        try {
            assertTrue(solver.findRoot(FunctionUtils.QUADRATIC_FUNCTION, checker, 100, x0, fx0, x1, fx1));
            double root = solver.getRoot();
            assertTrue(Math.abs(root - 1.5) < 0.01);
        } catch (FunctionException e) {
            fail(e.getMessage());
        }
    }

}