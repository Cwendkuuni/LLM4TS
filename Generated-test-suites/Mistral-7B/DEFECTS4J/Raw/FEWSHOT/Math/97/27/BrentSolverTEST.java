package org.apache.commons.cli.roots; // Assuming you have created a package named org.apache.commons.cli.roots for testing the BrentSolver class

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.LinearFunction;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        // Test with a simple linear function
        ComputableFunction function = new LinearFunction(1.0, 2.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return ConvergenceChecker.CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 2.0;
        double root = 1.0;
        assertTrue(solver.findRoot(function, checker, 100, x0, 2.0, x1, function.valueAt(x1)));
        assertEquals(root, solver.getRoot(), epsilon);

        // Test with a non-linear function
        ComputableFunction nonLinearFunction = new NonLinearFunction(1.0, 1.0);
        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        solver = new BrentSolver();
        x0 = 0.5;
        x1 = 1.5;
        root = 1.0;
        assertFalse(solver.findRoot(nonLinearFunction, checker, 100, x0, nonLinearFunction.valueAt(x0), x1, nonLinearFunction.valueAt(x1)));
    }

    @Test(expected = FunctionException.class)
    public void testFunctionException() throws FunctionException {
        ComputableFunction function = new ThrowsFunctionExceptionFunction(1.0, 1.0);
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 1.0;
        double root = 0.5;
        solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    }

    @Test
    public void testConvergenceChecker() {
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xLow, double fxLow) {
                if (fx < 0.0 && fxLow > 0.0) {
                    return ConvergenceChecker.LOW;
                } else if (fx > 0.0 && fxLow < 0.0) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        ComputableFunction function = new LinearFunction(1.0, 2.0);
        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double x1 = 2.0;
        double root = 1.0;
        assertTrue(solver.findRoot(function, checker, 100, x0, 2.0, x1, function.valueAt(x1)));
        assertEquals(root, solver.getRoot(), epsilon);
    }

    private static interface ConvergenceChecker {
        int converged(double x, double fx, double xLow, double fxLow);

        static final int CONVERGED = 0;
        static final int NOT_CONVERGED = 1;
        static final int LOW = 2;
        static final int HIGH = 3;
    }

    private static class NonLinearFunction implements ComputableFunction {
        private final double a;
        private final double b;

        public NonLinearFunction(double a, double b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double valueAt(double x) {
            return a * Math.pow(x, 2) + b;
        }
    }

    private static class ThrowsFunctionExceptionFunction implements ComputableFunction {
        private final double a;
        private final double b;

        public ThrowsFunctionExceptionFunction(double a, double b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double valueAt(double x) throws FunctionException {
            throw new FunctionException("FunctionException occurred");
        }

        @Override
        public double valueAt(double x) {
            return a * Math.pow(x, 2) + b;
        }
    }

}