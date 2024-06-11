package org.spaceroots.mantissa.roots;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;
    private ComputableFunction mockFunction;
    private ConvergenceChecker mockChecker;
    private int maxIter;

    @Before
    public void setUp() {
        solver = new BrentSolver();
        mockFunction = new MockFunction();
        mockChecker = new MockChecker();
        maxIter = 100;
    }

    @After
    public void tearDown() {
        solver = null;
        mockFunction = null;
        mockChecker = null;
    }

    @Test
    public void testFindRoot() throws FunctionException {
        double x0 = -1.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 0.0;
        boolean result = solver.findRoot(mockFunction, mockChecker, maxIter, x0, f0, x1, f1);
        assertTrue(result);
    }

    @Test
    public void testGetRoot() throws FunctionException {
        double x0 = -1.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 0.0;
        solver.findRoot(mockFunction, mockChecker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();
        assertEquals(0.0, root, 0.0);
    }

    private class MockFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) throws FunctionException {
            return 0.0;
        }
    }

    private class MockChecker implements ConvergenceChecker {
        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (fHigh == 0.0) {
                return ConvergenceChecker.HIGH;
            } else if (fLow == 0.0) {
                return ConvergenceChecker.LOW;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }

}