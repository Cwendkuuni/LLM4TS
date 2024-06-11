package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;

public class BrentSolverTest {

    @Test
    public void testFindRootWithSimpleFunction() {
        ComputableFunction function = new TestFunctions.SinFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double x1 = 2.0;
        double root = Math.PI / 2;

        boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(root, solver.getRoot(), epsilon);
        assertTrue(found);
    }

    @Test
    public void testFindRootWithFunctionWithMultipleRoots() {
        ComputableFunction function = new TestFunctions.QuadraticFunctionWithTwoRoots();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double x1 = 3.0;
        double root1 = 1.0;
        double root2 = 2.0;

        boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(root1, solver.getRoot(), epsilon);
        assertTrue(found);

        solver.reset();
        found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(root2, solver.getRoot(), epsilon);
        assertTrue(found);
    }

    @Test
    public void testFindRootWithFunctionWithNoRoots() {
        ComputableFunction function = new TestFunctions.ConstantFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double x1 = 2.0;

        boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertFalse(found);
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRootWithFunctionWithDivergentBehavior() {
        ComputableFunction function = new TestFunctions.DivisionByZeroFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double x1 = 2.0;

        boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertFalse(found);
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRootWithFunctionWithSingularBehavior() {
        ComputableFunction function = new TestFunctions.SingularFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double x1 = 2.0;

        boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertFalse(found);
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRootWithFunctionWithVerySmallAbsValue() {
        ComputableFunction function = new TestFunctions.SmallValueFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double x1 = 2.0;

        boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertEquals(function.getRoot(), solver.getRoot(), epsilon);
        assertTrue(found);
    }

    @Test
    public void testFindRootWithFunctionWithLargeInterval() {
        ComputableFunction function = new TestFunctions.SineFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 0.0;
        double x1 = 10.0 * Math.PI;

        boolean found = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertTrue(found);
        assertEquals(Math.PI / 2, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRootWithFunctionWithLargeMaxIter() {
        ComputableFunction function = new TestFunctions.SineFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        BrentSolver solver = new BrentSolver();

        double x0 = 0.0;
        double x1 = 10.0 * Math.PI;

        boolean found = solver.findRoot(function, checker, 10000, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertTrue(found);
        assertEquals(Math.PI / 2, solver.getRoot(), epsilon);
    }

}

class TestFunctions {

    static class SinFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return Math.sin(x);
        }

        static double getRoot() {
            return Math.PI / 2;
        }
    }

    static class QuadraticFunctionWithTwoRoots implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return Math.pow(x, 2) - 4.0;
        }
    }

    static class ConstantFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return 0.0;
        }
    }

    static class DivisionByZeroFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            throw new ArithmeticException("Division by zero");
        }
    }

    static class SingularFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            if (x == 0.0) {
                throw new ArithmeticException("Division by zero");
            }
            return 1.0 / x;
        }
    }

    static class SmallValueFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return Math.exp(-1000.0 * x) - 0.000001;
        }

        static double getRoot() {
            return 0.0;
        }
    }

    static class SineFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return Math.sin(x);
        }
    }

}

class TestConvergenceChecker implements ConvergenceChecker {
    @Override
    public int convergenceStatus(double xLow, double fLow, double xHigh, double fHigh) {
        if ((Math.abs(fLow) < Math.abs(fHigh)) && (Math.abs(xLow) < Math.abs(xHigh))) {
            return ConvergenceChecker.LOW;
        } else if ((Math.abs(fLow) > Math.abs(fHigh)) && (Math.abs(xLow) > Math.abs(xHigh))) {
            return ConvergenceChecker.HIGH;
        } else {
            return ConvergenceChecker.NEITHER;
        }
    }
}

enum ConvergenceChecker {
    LOW, HIGH, NEITHER
}