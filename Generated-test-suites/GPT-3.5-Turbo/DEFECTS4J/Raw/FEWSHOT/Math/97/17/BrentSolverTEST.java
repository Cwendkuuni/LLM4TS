package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
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
        
        BrentSolver solver = new BrentSolver();
        
        assertTrue(solver.findRoot(function, checker, 100, -10, -96, 10, 96));
        assertEquals(2.0, solver.getRoot(), 1e-6);
        
        assertTrue(solver.findRoot(function, checker, 100, -10, -100, 10, 100));
        assertEquals(-2.0, solver.getRoot(), 1e-6);
        
        assertTrue(solver.findRoot(function, checker, 100, -10, -50, 10, 50));
        assertEquals(2.0, solver.getRoot(), 1e-6);
        
        assertFalse(solver.findRoot(function, checker, 100, -10, 10, -10, 10));
        assertTrue(Double.isNaN(solver.getRoot()));
    }
}