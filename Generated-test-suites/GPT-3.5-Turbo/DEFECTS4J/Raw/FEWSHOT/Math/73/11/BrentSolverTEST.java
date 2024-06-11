package org.spaceroots.mantissa.roots;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        double x0 = 0.0, f0 = 1.0;
        double x1 = 1.0, f1 = -1.0;

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x - 1.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return 1;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return -1;
                }
                return 0;
            }
        };

        int maxIter = 100;

        assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));

        assertEquals(1.0, solver.getRoot(), 1e-6);
    }
}