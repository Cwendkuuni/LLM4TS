package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    private static final double EPSILON = Math.pow(2.0, -52);
    
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        TestFunction function = new TestFunction();
        
        // Test when root is between x0 and x1
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);
        boolean rootFound = solver.findRoot(function, new TestConvergenceChecker(), 100, x0, f0, x1, f1);
        assertEquals(true, rootFound);
        assertEquals(0.6823278038280192, solver.getRoot(), EPSILON);
        
        // Test when root is not between x0 and x1
        x0 = -1.0;
        f0 = function.valueAt(x0);
        x1 = -0.5;
        f1 = function.valueAt(x1);
        rootFound = solver.findRoot(function, new TestConvergenceChecker(), 100, x0, f0, x1, f1);
        assertEquals(false, rootFound);
    }
    
    private static class TestFunction implements ComputableFunction {

        @Override
        public double valueAt(double x) throws FunctionException {
            return Math.exp(x) - x;
        }
        
    }
    
    private static class TestConvergenceChecker implements ConvergenceChecker {

        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            // Check if a root is found within the given tolerance
            if (Math.abs(fLow) < EPSILON) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < EPSILON) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        }
        
    }
}