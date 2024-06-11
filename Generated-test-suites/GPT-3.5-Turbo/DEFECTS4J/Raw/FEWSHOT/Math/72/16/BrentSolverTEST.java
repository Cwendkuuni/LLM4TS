package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testSimpleFindRoot() throws FunctionException {
        double x0 = 0.0;
        double f0 = new SimpleFunction().valueAt(x0);
        double x1 = 1.0;
        double f1 = new SimpleFunction().valueAt(x1);

        BrentSolver solver = new BrentSolver();
        boolean foundRoot = solver.findRoot(new SimpleFunction(), new ConvergenceChecker(), 100, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(0.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRootWithNegativeValues() throws FunctionException {
        double x0 = -1.0;
        double f0 = new SimpleFunction().valueAt(x0);
        double x1 = 0.0;
        double f1 = new SimpleFunction().valueAt(x1);

        BrentSolver solver = new BrentSolver();
        boolean foundRoot = solver.findRoot(new SimpleFunction(), new ConvergenceChecker(), 100, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(-0.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testNoRootInInterval() throws FunctionException {
        double x0 = 0.0;
        double f0 = new SimpleFunction().valueAt(x0);
        double x1 = 0.5;
        double f1 = new SimpleFunction().valueAt(x1);

        BrentSolver solver = new BrentSolver();
        boolean foundRoot = solver.findRoot(new SimpleFunction(), new ConvergenceChecker(), 100, x0, f0, x1, f1);

        assertFalse(foundRoot);
    }

    @Test
    public void testConvergenceChecker() throws FunctionException {
        double x0 = 0.0;
        double f0 = new SimpleFunction().valueAt(x0);
        double x1 = 1.0;
        double f1 = new SimpleFunction().valueAt(x1);

        BrentSolver solver = new BrentSolver();
        boolean foundRoot = solver.findRoot(new SimpleFunction(), new CustomConvergenceChecker(), 100, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(0.0, solver.getRoot(), 1e-10);
    }

    private class CustomConvergenceChecker implements ConvergenceChecker {
        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (fLow == fHigh) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-10) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }

}