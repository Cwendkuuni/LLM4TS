package org.spaceroots.mantissa.roots;

import org.junit.jupiter.api.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.jupiter.api.Assertions.*;

class BrentSolverTest {

    @Test
    void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // Add the implementation of the function for testing
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // Add the implementation of the convergence checker for testing
                if (Math.abs(xHigh - xLow) < 0.0001) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.HIGH;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 5, 21);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 0.0001);
    }

    @Test
    void testFindRoot_NoRootInInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // Add the implementation of the function for testing
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // Add the implementation of the convergence checker for testing
                return ConvergenceChecker.NONE;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 5, 21);
        assertFalse(rootFound);
    }

    @Test
    void testFindRoot_ZeroIterationsAllowed() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // Add the implementation of the function for testing
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // Add the implementation of the convergence checker for testing
                if (Math.abs(xHigh - xLow) < 0.0001) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.HIGH;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 0, 0, -4, 5, 21);
        assertFalse(rootFound);
    }

    @Test
    void testFindRoot_LowConvergence() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // Add the implementation of the function for testing
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // Add the implementation of the convergence checker for testing
                if (Math.abs(xHigh - xLow) < 0.0001) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NONE;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 5, 21);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 0.0001);
    }
}