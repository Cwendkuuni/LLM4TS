package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        
        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.pow(x, 2) - 4;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        boolean result1 = solver.findRoot(function1, checker1, 100, -10, function1.valueAt(-10), 10, function1.valueAt(10));
        assertTrue(result1);
        assertEquals(2, solver.getRoot(), 1e-6);

        // Test case 2
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x) - 0.5;
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        boolean result2 = solver.findRoot(function2, checker2, 100, 0, function2.valueAt(0), 10, function2.valueAt(10));
        assertTrue(result2);
        assertEquals(Math.PI/6, solver.getRoot(), 1e-6);
    }
}