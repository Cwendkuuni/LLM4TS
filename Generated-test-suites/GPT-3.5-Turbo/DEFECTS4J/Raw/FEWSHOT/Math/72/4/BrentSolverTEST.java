package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver.ConvergenceChecker;
import org.junit.Test;

public class BrentSolverTest {
    
    private static final double EPSILON = Math.pow(2.0, -52);
      
    private static class TestFunction implements ComputableFunction {
        public double valueAt(double x)
            throws FunctionException {
            return Math.sin(x);
        }
    }
    
    private static class TestConvergenceChecker implements ConvergenceChecker {
        public static final int LOW = -1;
        public static final int HIGH = 1;
        
        public int converged(double xLow, double fLow,
                             double xHigh, double fHigh) {
            if (Math.abs(fLow) < EPSILON) {
                return LOW;
            }
            
            if (Math.abs(fHigh) < EPSILON) {
                return HIGH;
            }
            
            return 0;
        }
    }
    
    @Test
    public void testFindRoot()
        throws FunctionException {
        BrentSolver solver = new BrentSolver();
        TestFunction function = new TestFunction();
        TestConvergenceChecker checker = new TestConvergenceChecker();
          
        double x0 = -Math.PI;
        double f0 = function.valueAt(x0);
        
        double x1 = Math.PI;
        double f1 = function.valueAt(x1);
        
        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(0.0, solver.getRoot(), EPSILON);
    }
    
    @Test
    public void testFindRootNoRoot()
        throws FunctionException {
        BrentSolver solver = new BrentSolver();
        TestFunction function = new TestFunction();
        TestConvergenceChecker checker = new TestConvergenceChecker();
          
        double x0 = -2 * Math.PI;
        double f0 = function.valueAt(x0);
        
        double x1 = -Math.PI;
        double f1 = function.valueAt(x1);
        
        assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    }
}