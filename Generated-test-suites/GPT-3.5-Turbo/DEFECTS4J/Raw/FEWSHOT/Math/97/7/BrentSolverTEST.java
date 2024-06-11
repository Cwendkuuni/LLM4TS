package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

import org.junit.Test;

public class BrentSolverTest {
    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {

            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4; // x^2 - 4 = 0 -> x = 2 or x = -2
            }
        };
        assertTrue(solver.findRoot(function, new ConvergenceChecker() {

            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.ONGOING;
            }
        }, 100, -10, function.valueAt(-10), 10, function.valueAt(10))); // xLow = -10, fLow = -96, xHigh = 10, fHigh = 96
        assertEquals(2, solver.getRoot(), 1e-6);
        
        assertTrue(solver.findRoot(function, new ConvergenceChecker() {

            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.ONGOING;
            }
        }, 100, 0, function.valueAt(0), 5, function.valueAt(5))); // xLow = 0, fLow = -4, xHigh = 5, fHigh = 21
        assertEquals(2, solver.getRoot(), 1e-6);
        
        assertTrue(solver.findRoot(function, new ConvergenceChecker() {

            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.ONGOING;
            }
        }, 100, 0, function.valueAt(0), 2, function.valueAt(2))); // xLow = 0, fLow = -4, xHigh = 2, fHigh = 0
        assertEquals(2, solver.getRoot(), 1e-6);
        
        assertFalse(solver.findRoot(function, new ConvergenceChecker() {

            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.ONGOING;
            }
        }, 100, 0, function.valueAt(0), 1, function.valueAt(1))); // xLow = 0, fLow = -4, xHigh = 1, fHigh = -3
        assertEquals(2, solver.getRoot(), 1e6);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        assertEquals(Double.NaN, solver.getRoot(), 1e-6);
    }

}