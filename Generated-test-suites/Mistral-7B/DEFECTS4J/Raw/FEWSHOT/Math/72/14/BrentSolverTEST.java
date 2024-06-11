package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        MockFunction function = new MockFunction();

        BrentSolver brentSolver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        assertFalse(brentSolver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0));

        function.setExpectedValues(0.25);

        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.LOW;
            }
        };

        brentSolver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);
        assertEquals(0.25, brentSolver.getRoot(), epsilon);

        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.HIGH;
            }
        };

        function.setExpectedValues(0.5);

        brentSolver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);
        assertEquals(0.5, brentSolver.getRoot(), epsilon);

        checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.CONVERGED;
            }
        };

        function.setExpectedValues(0.1);

        brentSolver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);
        assertEquals(0.1, brentSolver.getRoot(), epsilon);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootFunctionException() {
        MockFunction function = new MockFunction();

        BrentSolver brentSolver = new BrentSolver();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        function.setExpectedThrows(FunctionException.class);

        brentSolver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);
    }
}