package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double f0 = -4.0;
        double x1 = 2.0;
        double f1 = 0.0;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 0.01) {
                    return LOW;
                }
                if (Math.abs(fHigh) < 0.01) {
                    return HIGH;
                }
                return CONTINUE;
            }
        };
        int maxIter = 100;
        
        boolean success = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        assertTrue(success);
        assertEquals(2.0, solver.getRoot(), 0.01);
    }

}