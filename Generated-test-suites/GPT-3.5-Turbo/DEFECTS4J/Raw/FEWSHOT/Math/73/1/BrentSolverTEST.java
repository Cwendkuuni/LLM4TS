
package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow * fHigh < 0) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fLow) < 1e-12) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-12) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean foundRoot = solver.findRoot(function, checker, 100, 0.0, -4.0, 3.0, 5.0);
        assertTrue(foundRoot);
        assertEquals(2.0, solver.getRoot(), 1e-12);

        foundRoot = solver.findRoot(function, checker, 100, 0.0, -7.0, 9.0, 13.0);
        assertFalse(foundRoot);
        assertEquals(Double.NaN, solver.getRoot(), 1e-12);
    }
}
