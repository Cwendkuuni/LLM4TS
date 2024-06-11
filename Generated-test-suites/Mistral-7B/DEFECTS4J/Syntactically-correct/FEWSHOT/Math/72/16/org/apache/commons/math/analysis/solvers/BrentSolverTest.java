package org.apache.commons.cli.roots; // Assuming a new package name for testing

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot_SimpleFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return x < xLow ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, -2.0, 2.0, -1.0, 1.0);
        assertEquals(-1.4142135623730955, solver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRoot_ComplexFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.log(Math.abs(Math.sin(x) - Math.exp(-x)));
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return Math.abs(fx) < 0.01 ? ConvergenceChecker.HIGH : ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, -2.0, 2.0, -1.0, 1.0);
        assertEquals(0.1253773624833348, solver.getRoot(), 0.001);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_InvalidFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new UnsupportedOperationException();
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {};
        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, 100, 0.0, 1.0, 0.0, 1.0);
    }
}