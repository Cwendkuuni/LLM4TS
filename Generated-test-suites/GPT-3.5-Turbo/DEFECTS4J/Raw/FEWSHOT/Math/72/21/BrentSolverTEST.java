package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return Math.pow(x, 2) - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
                if (Math.abs(xHigh - xLow) < 0.001) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = -4.0;
        double x1 = 3.0;
        double f1 = 5.0;
        
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        
        assertTrue(rootFound);
        assertEquals(2.000, solver.getRoot(), 0.001);
    }
    
    @Test
    public void testFindRoot_noRootInInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return Math.pow(x, 2) + 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
                if (Math.abs(xHigh - xLow) < 0.001) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = 4.0;
        double x1 = 3.0;
        double f1 = 13.0;
        
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        
        assertFalse(rootFound);
    }

}