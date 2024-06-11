package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                if (Math.abs(fx) < 0.001) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(x - xOld) < 0.001) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NONE;
            }
        };

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 100, -5.0, function.valueAt(-5.0), -3.0, function.valueAt(-3.0)));
        assertEquals(-2.0, solver.getRoot(), 0.001);

        solver = new BrentSolver();
        try {
            assertTrue(solver.findRoot(function, checker, 100, 3.0, function.valueAt(3.0), 5.0, function.valueAt(5.0)));
            fail("Exception expected");
        } catch (FunctionException e) {
            // expected
        }

        solver = new BrentSolver();
        try {
            assertTrue(solver.findRoot(function, checker, 100, -5.0, Double.NaN, -3.0, Double.NaN));
            fail("Exception expected");
        } catch (FunctionException e) {
            // expected
        }
    }
}