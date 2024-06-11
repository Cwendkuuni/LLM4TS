package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 2.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                if (Math.abs(fx) < 0.001) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fxOld) < 0.001) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        BrentSolver brentSolver = new BrentSolver();

        boolean rootFound = brentSolver.findRoot(function, checker, 10, -2.0, -1.0, 1.0, 1.0);

        assertTrue(rootFound);
        assertEquals(-1.0, brentSolver.getRoot(), 0.001);
    }

    @Test
    public void testFindRootWithDivergingFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.log(Math.abs(x - 0.5));
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                if (Math.abs(fx) < 0.001) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        BrentSolver brentSolver = new BrentSolver();

        boolean rootFound = brentSolver.findRoot(function, checker, 10, 0.4, 0.4, 0.6, 0.6);

        assertFalse(rootFound);
    }

    @Test (expected = FunctionException.class)
    public void testFindRootWithFunctionException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                if (x > 0) {
                    throw new FunctionException();
                }
                return Math.pow(x, 2) - 2.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                return ConvergenceChecker.NONE;
            }
        };

        BrentSolver brentSolver = new BrentSolver();

        brentSolver.findRoot(function, checker, 10, -2.0, -1.0, 1.0, 1.0);
    }
}