package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {
    private BrentSolver brentSolver;
    
    @Before
    public void setup() {
        brentSolver = new BrentSolver();
    }
    
    @Test
    public void testFindRoot_ValidInterval_ReturnsTrue() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                }
                return NONE;
            }
        };
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 3.0;
        double f1 = function.valueAt(x1);
        
        boolean result = brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        assertTrue(result);
        assertEquals(2.0, brentSolver.getRoot(), 1e-6);
    }
    
    @Test
    public void testFindRoot_InvalidInterval_ReturnsFalse() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                }
                return NONE;
            }
        };
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.5;
        double f1 = function.valueAt(x1);
        
        boolean result = brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        assertFalse(result);
    }
}