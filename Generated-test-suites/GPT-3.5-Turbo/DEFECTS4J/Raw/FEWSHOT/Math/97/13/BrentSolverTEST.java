
package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        // Create a simple linear function: f(x) = x + 1
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x + 1;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // Always return NOT_CONVERGED
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, -10, -9, 0, 1);
        
        assertTrue(rootFound);
        assertEquals(-1.0, solver.getRoot(), 0.0001);
    }
    
    @Test
    public void testFindRootNoRoot() throws FunctionException {
        // Create a simple linear function: f(x) = x + 1
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x + 1;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // Always return NOT_CONVERGED
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 1, -10, -9, 0, 1);
        
        assertFalse(rootFound);
    }
}
