package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot_simpleRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 2;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, 100, 1.5, 1.5, -1.5, -1.5);

        assertTrue(result);
        assertEquals(0.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_multipleRoots() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, 100, -2.0, 4.0, -3.0, 3.0);

        assertTrue(result);
        assertEquals(-2.0, solver.getRoot(), epsilon);

        solver.reset();
        result = solver.findRoot(function, checker, 100, -2.0, 4.0, 3.0, 2.0);

        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_functionWithSingularity() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                if (x == 1) {
                    throw new ArithmeticException("Function has a singularity at x = 1");
                }
                return Math.pow(x, 2) - 1;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, 100, 1.0, 1.0, -1.0, -1.0);

        assertFalse(result);
    }

    @Test
    public void testFindRoot_functionWithLargeInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.sin(Math.PI / x);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, 100, -1000.0, 1000.0, -2000.0, 2000.0);

        assertTrue(result);
        // The exact root is not known for this example, but we can check
        // that the result is within the given interval and has the correct sign
        double root = solver.getRoot();
        assertTrue(root > -1000.0);
        assertTrue(root < 1000.0);
        assertTrue(function.valueAt(root) < 0);
    }
}