package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {

    private BrentSolver solver;
    private ComputableFunction testFunction;

    @Before
    public void setUp() {
        solver = new BrentSolver();
        testFunction = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4.0;
            }
        };
    }

    @Test
    public void testFindRoot() throws FunctionException {
        boolean found = solver.findRoot(testFunction, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow - 0.0) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh - 0.0) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        }, 100, 1.0, -3.0, 3.0, 7.0);

        assertTrue(found);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootNoRoot() throws FunctionException {
        boolean found = solver.findRoot(testFunction, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow - 0.0) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh - 0.0) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        }, 100, 1.0, 2.0, 3.0, 4.0);

        assertFalse(found);
    }
}