package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {
    
    private BrentSolver solver;
    
    @Before
    public void setUp() {
        solver = new BrentSolver();
    }
    
    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < 1e-5) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-5) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NONE;
            }
        };
        
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = Math.PI;
        double f1 = function.valueAt(x1);
        
        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        assertTrue(rootFound);
        assertEquals(Math.PI / 2, solver.getRoot(), 1e-5);
    }
    
    @Test
    public void testFindRoot_NoRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < 1e-5) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-5) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NONE;
            }
        };
        
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);
        
        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        
        assertFalse(rootFound);
    }
    
}