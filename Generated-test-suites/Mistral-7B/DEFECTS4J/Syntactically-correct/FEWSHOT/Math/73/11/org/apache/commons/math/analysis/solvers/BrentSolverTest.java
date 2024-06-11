package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver brentSolver = new BrentSolver();

        ComputableFunction function = TestFunctions.SIN;
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public ConvergenceChecker.Status converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };

        try {
            double root = brentSolver.findRoot(function, checker, 100, -3.0, 3.0)[0];
            assertEquals(0.0, root, 0.001);
        } catch (FunctionException e) {
            fail("Unexpected FunctionException: " + e.getMessage());
        }

        function = TestFunctions.QUADRATIC;
        checker = new ConvergenceChecker() {
            @Override
            public ConvergenceChecker.Status converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.HIGH;
            }
        };

        try {
            brentSolver.findRoot(function, checker, 100, 1.0, 2.0)[0];
            fail("Expected FunctionException");
        } catch (FunctionException e) {
            // expected
        }

        function = TestFunctions.COS;
        checker = new ConvergenceChecker() {
            @Override
            public ConvergenceChecker.Status converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };

        try {
            double[] result = brentSolver.findRoot(function, checker, 100, 0.5, 2.0)[0];
            assertEquals(1.0, result[0], 0.001);
            assertEquals(Math.PI, result[1], 0.001);
        } catch (FunctionException e) {
            fail("Unexpected FunctionException: " + e.getMessage());
        }
    }

}

class TestFunctions {
    static ComputableFunction SIN = new ComputableFunction() {
        @Override
        public double valueAt(double x) {
            return Math.sin(x);
        }
    };

    static ComputableFunction COS = new ComputableFunction() {
        @Override
        public double valueAt(double x) {
            return Math.cos(x);
        }

        @Override
        public double valueAtDerivativeAt(double x) {
            return -Math.sin(x);
        }
    };

    static ComputableFunction QUADRATIC = new ComputableFunction() {
        @Override
        public double valueAt(double x) {
            return x * x - 2.0;
        }

        @Override
        public double valueAtDerivativeAt(double x) {
            return 2.0 * x;
        }
    };
}

interface ConvergenceChecker {
    ConvergenceChecker.Status converged(double x, double fx, double xPrev, double fxPrev);

    enum Status {
        LOW,
        HIGH,
        CONVERGED
    }
}