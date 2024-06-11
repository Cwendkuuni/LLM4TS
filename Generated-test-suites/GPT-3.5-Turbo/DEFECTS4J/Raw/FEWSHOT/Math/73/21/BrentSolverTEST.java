package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver brentSolver;

    @Before
    public void setup() {
        brentSolver = new BrentSolver();
    }

    @Test
    public void testFindRoot() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                if (Math.abs(f2) < 1e-6) {
                    return LOW;
                } else if (Math.abs(f1) < 1e-6) {
                    return HIGH;
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };

        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);

        assertTrue(brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(2.0, brentSolver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRootWithNoConvergence() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                if (Math.abs(f2) < 1e-6) {
                    return LOW;
                } else if (Math.abs(f1) < 1e-6) {
                    return HIGH;
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };

        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 1;
        double f1 = function.valueAt(x1);

        assertFalse(brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1));
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithFunctionException() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                throw new FunctionException();
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                if (Math.abs(f2) < 1e-6) {
                    return LOW;
                } else if (Math.abs(f1) < 1e-6) {
                    return HIGH;
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };

        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 1;
        double f1 = function.valueAt(x1);

        brentSolver.findRoot(function, checker, 100, x0, f0, x1, f1);
    }
}