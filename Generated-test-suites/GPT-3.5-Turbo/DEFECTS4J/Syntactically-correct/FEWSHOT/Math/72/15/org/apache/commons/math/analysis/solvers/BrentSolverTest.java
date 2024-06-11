package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < 0.01) {
                    return ConvergenceChecker.CONVERGED;
                } else if (fLow * fHigh > 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.LOW;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 1, -3, 3, 7);

        assertEquals(true, rootFound);
        assertEquals(2, solver.getRoot(), 0.01);
    }
}