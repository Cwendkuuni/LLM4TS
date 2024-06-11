package org.spaceroots.mantissa.roots;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testConstructor() {
        BrentSolver solver = new BrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        assertTrue(solver.findRoot(function, checker, 100, -10, -96, 10, 96));
        assertEquals(2, solver.getRoot(), epsilon);
    }
}