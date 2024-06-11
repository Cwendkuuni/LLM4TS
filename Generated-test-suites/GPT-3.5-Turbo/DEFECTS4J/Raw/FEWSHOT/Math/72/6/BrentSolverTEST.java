package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        
        // Define a simple linear function y = 2x + 1
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return 2*x + 1;
            }
        };
        
        // Test finding the root in the interval [0, 5]
        boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, 0, 1, 5, 11);
        assertTrue(rootFound);
        assertEquals(-0.5, solver.getRoot(), 1e-6);
        
        // Test finding the root in the interval [-5, 5]
        rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, -5, -9, 5, 11);
        assertTrue(rootFound);
        assertEquals(-0.5, solver.getRoot(), 1e-6);
    }
}