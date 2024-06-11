package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.spaceroots.mantissa.roots.ConvergenceChecker.*;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

    /**
     * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
     * with a simple function and default checker.
     */
    @Test
    public void testFindRootSimpleFunction() {
        SimpleFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 2;
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new DefaultConvergenceChecker();

        double x0 = 1.0;
        double x1 = 3.0;
        double root = 1.5;

        boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(root, solver.getRoot(), epsilon);
        assertTrue(result);
    }

    /**
     * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
     * with a simple function and custom checker.
     */
    @Test
    public void testFindRootCustomChecker() {
        SimpleFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        BrentSolver solver = new BrentSolver();
        CustomConvergenceChecker checker = new CustomConvergenceChecker();

        double x0 = 1.0;
        double x1 = 2.0;
        double root = 1.5;

        boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(root, solver.getRoot(), epsilon);
        assertTrue(result);
    }

    /**
     * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
     * with a simple function and failure case.
     */
    @Test(expected = FunctionException.class)
    public void testFindRootFailureCase() {
        SimpleFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                throw new ArithmeticException();
            }
        };

        BrentSolver solver = new BrentSolver();
        ConvergenceChecker checker = new DefaultConvergenceChecker();

        double x0 = 1.0;
        double x1 = 3.0;

        solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    }

}

class DefaultConvergenceChecker implements ConvergenceChecker {
    @Override
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow * fHigh < 0) {
            return LOW;
        } else if (fHigh * fLow < 0) {
            return HIGH;
        } else {
            return NOT_CONVERGED;
        }
    }
}

class CustomConvergenceChecker implements ConvergenceChecker {
    @Override
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double tolerance = 0.01;
        if (Math.abs(xHigh - xLow) < tolerance || Math.abs(fHigh) < Math.abs(fLow)) {
            return CONVERGED;
        } else {
            return NOT_CONVERGED;
        }
    }
}