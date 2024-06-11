
package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                double absFLow = Math.abs(fLow);
                double absFHigh = Math.abs(fHigh);
                double tol = epsilon * Math.max(Math.abs(xLow), Math.abs(xHigh));
                if (absFHigh < tol) {
                    return ConvergenceChecker.HIGH;
                } else if (absFLow < tol) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 0, 0, 2 * Math.PI, 0);
        assertTrue(rootFound);
        assertEquals(0, solver.getRoot(), epsilon);
    }
}
