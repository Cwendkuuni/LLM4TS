package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRootSimpleFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4.0;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return ConvergenceChecker.NEITHER;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double fx0 = x0 * x0 - 4.0;
        double x1 = 2.0;
        double fx1 = x1 * x1 - 4.0;

        solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);

        assertEquals(1.0, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRootComplexFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.log(Math.exp(x) - 1.0);
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return ConvergenceChecker.NEITHER;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 0.5;
        double fx0 = Math.log(Math.E - 1.0);
        double x1 = 1.0;
        double fx1 = Math.log(Math.E);

        solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);

        assertEquals(Math.E - 1.0, solver.getRoot(), 0.01);
    }

    @Test(expected = FunctionException.class)
    public void testThrowsFunctionException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new FunctionException("Exception message");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return ConvergenceChecker.NEITHER;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double fx0 = 0.0;
        double x1 = 1.0;
        double fx1 = 0.0;

        solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    }

}