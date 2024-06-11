package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;

public class BrentSolverTest {
    @Test
    public void testFindRoot() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x - 2;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (xLow < 0 && xHigh > 0) {
                    return ConvergenceChecker.LOW;
                } else if (xLow > 0 && xHigh < 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        
        BrentSolver solver = new BrentSolver();
        boolean rootFound = false;
        try {
            rootFound = solver.findRoot(function, checker, 100, -10, -12, 10, 8);
        } catch (FunctionException e) {
            e.printStackTrace();
        }
        
        assertTrue(rootFound);
        assertEquals(2, solver.getRoot(), 0.0001);
    }
    
    @Test
    public void testFindRootNoRoot() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x + 1;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (xLow < 0 && xHigh > 0) {
                    return ConvergenceChecker.LOW;
                } else if (xLow > 0 && xHigh < 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        
        BrentSolver solver = new BrentSolver();
        boolean rootFound = false;
        try {
            rootFound = solver.findRoot(function, checker, 100, -10, -9, 10, 11);
        } catch (FunctionException e) {
            e.printStackTrace();
        }
        
        assertFalse(rootFound);
    }
}