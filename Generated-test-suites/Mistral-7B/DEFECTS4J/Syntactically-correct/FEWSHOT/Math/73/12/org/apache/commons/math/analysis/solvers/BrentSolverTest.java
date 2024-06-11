package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

    @Test
    public void testFindRootSimpleFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 2;
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return (fx > 0) ? ConvergenceChecker.LOW : (fx < 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.ITERATION;
            }
        };

        double x0 = 1.0;
        double fx0 = x0 * x0 - 2;
        double x1 = 2.0;
        double fx1 = x1 * x1 - 2;

        boolean result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);

        assertTrue(result);
        assertEquals(-1.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootComplexFunction() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.log(Math.abs(Math.sin(x))) * Math.exp(Math.abs(x));
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return (Math.abs(fx) < 1e-6) ? ConvergenceChecker.LOW : ConvergenceChecker.ITERATION;
            }
        };

        double x0 = 0.0;
        double fx0 = Double.NaN;
        double x1 = 1.0;
        double fx1 = Math.log(Math.abs(Math.sin(x1))) * Math.exp(Math.abs(x1));

        boolean result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);

        assertTrue(result);
        // The exact root is not known, but we can check that the result is close enough to 0.5
        assertEquals(0.5, solver.getRoot(), 0.1);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new ArithmeticException("Division by zero");
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.ITERATION;
            }
        };

        double x0 = 0.0;
        double fx0 = Double.NaN;
        double x1 = 1.0;
        double fx1 = Double.NaN;

        solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    }
}