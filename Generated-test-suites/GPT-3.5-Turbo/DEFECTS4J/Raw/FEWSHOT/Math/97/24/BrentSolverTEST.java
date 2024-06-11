package org.spaceroots.mantissa.roots;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1E-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1E-6) {
                    return HIGH;
                } else {
                    return IN_PROGRESS;
                }
            }
        };

        assertTrue(solver.findRoot(function, checker, 100, 0, -4, 5, 9));
        assertEquals(2.0, solver.getRoot(), 1E-6);

        assertFalse(solver.findRoot(function, checker, 100, 0, -8, 5, 9));
    }
}