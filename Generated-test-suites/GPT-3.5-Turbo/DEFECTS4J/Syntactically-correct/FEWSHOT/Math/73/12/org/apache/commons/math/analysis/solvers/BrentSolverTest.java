package org.spaceroots.mantissa.roots;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

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
                return x * x;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon && Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.ROOT;
                } else if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        int maxIter = 100;
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(0.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootNoRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon && Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.ROOT;
                } else if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        int maxIter = 100;
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);

        assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }
}