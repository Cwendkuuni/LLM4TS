
package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < 1e-5) {
                    return ConvergenceChecker.LOW;
                }
                return ConvergenceChecker.NONE;
            }
        };
        int maxIter = 100;
        double x0 = 1.0;
        double f0 = 1.0;
        double x1 = 3.0;
        double f1 = 5.0;

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-5);
    }
}
