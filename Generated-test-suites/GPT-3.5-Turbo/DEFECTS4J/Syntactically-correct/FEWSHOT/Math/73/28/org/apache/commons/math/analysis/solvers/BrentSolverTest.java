package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {
    private BrentSolver solver;
    private ComputableFunction function;
    private ConvergenceChecker checker;
    private int maxIter;

    @Before
    public void setup() {
        solver = new BrentSolver();
        function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * Math.sin(x);
            }
        };
        checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        maxIter = 100;
    }

    @Test
    public void testFindRoot() throws FunctionException {
        boolean found = solver.findRoot(function, checker, maxIter, 0, 0, 1, 1);
        assertTrue(found);
        assertEquals(0, solver.getRoot(), 1e-6);

        found = solver.findRoot(function, checker, maxIter, 0, 0, 10, 10);
        assertTrue(found);
        assertEquals(9.42477, solver.getRoot(), 1e-6);

        found = solver.findRoot(function, checker, maxIter, 3, -2, 5, 3);
        assertTrue(found);
        assertEquals(4.88692, solver.getRoot(), 1e-6);
    }

}