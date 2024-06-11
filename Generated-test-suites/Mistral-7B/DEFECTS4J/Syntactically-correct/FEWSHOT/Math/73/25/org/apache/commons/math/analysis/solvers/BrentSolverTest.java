package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot_SimpleFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 2;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xNext, double fxNext) {
                return (fx > 0 && fxNext <= 0) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();

        solver.findRoot(function, checker, 100, -5.0, function.valueAt(-5.0), -3.0, function.valueAt(-3.0));

        assertEquals(-2.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_ComplexFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                double result = Math.sin(x) / x;
                return result;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xNext, double fxNext) {
                return (Math.abs(fx) < epsilon) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();

        solver.findRoot(function, checker, 100, 1.0, function.valueAt(1.0), 2.0, function.valueAt(2.0));

        // Approximate value of the root is 1.84775997
        assertEquals(1.84775997, solver.getRoot(), 0.001);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_FunctionThrowsException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new ArithmeticException("Divide by zero");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xNext, double fxNext) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();

        try {
            solver.findRoot(function, checker, 100, -5.0, function.valueAt(-5.0), -3.0, function.valueAt(-3.0));
            fail("FunctionException expected");
        } catch (FunctionException e) {
            // Expected
        }
    }
}