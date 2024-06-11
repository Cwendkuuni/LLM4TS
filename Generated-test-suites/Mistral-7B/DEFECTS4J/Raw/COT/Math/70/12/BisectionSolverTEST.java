package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for the BisectionSolver class.
 */
public class BisectionSolverTest {

    /**
     * Test constructor with UnivariateRealFunction as argument.
     */
    @Test
    public void testConstructorFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver.getMaximalIterationCount());
        assertNotNull(solver.getAbsoluteAccuracy());
        assertEquals(100, solver.getMaximalIterationCount().intValue());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test constructor without UnivariateRealFunction as argument.
     */
    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver.getMaximalIterationCount());
        assertNotNull(solver.getAbsoluteAccuracy());
        assertEquals(100, solver.getMaximalIterationCount().intValue());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test solve method with initial value.
     */
    @Test
    public void testSolveInitialValue() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.0, 1.0, 0.5);
        assertEquals(0.841470985008243, root, 1E-6);
    }

    /**
     * Test solve method without initial value.
     */
    @Test
    public void testSolveNoInitialValue() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.841470985008243, root, 1E-6);
    }

    /**
     * Test solve method with minimum and maximum values out of order.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveMinMaxOutOfOrder() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 1.0, 0.0);
    }

    /**
     * Test solve method with function that does not bracket the root.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionDoesNotBracketRoot() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.5, 0.5);
    }

    /**
     * Test solve method with function that exceeds maximum iterations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(1);
        solver.solve(function, 0.0, 1.0);
    }

    /**
     * Test solve method with function that returns NaN.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveFunctionReturnsNaN() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Double.NaN;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 1.0);
    }

    /**
     * Test solve method with function that throws an exception.
     */
    @Test(expected = RuntimeException.class)
    public void testSolveFunctionThrowsException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new RuntimeException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 1.0);
    }

    /**
     * Test verifyInterval method with minimum and maximum values out of order.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalMinMaxOutOfOrder() {
        BisectionSolver solver = new BisectionSolver();
        solver.verifyInterval(1.0, 0.0);
    }

    /**
     * Test verifyInterval method with minimum and maximum values too close.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalMinMaxTooClose() {
        BisectionSolver solver = new BisectionSolver();
        solver.verifyInterval(0.0, 0.0);
    }

    /**
     * Test verifyInterval method with minimum and maximum values far apart.
     */
    @Test
    public void testVerifyIntervalMinMaxFarApart() {
        BisectionSolver solver = new BisectionSolver();
        solver.verifyInterval(0.0, 1.0);
    }

    /**
     * Test midpoint method with double values.
     */
    @Test
    public void testMidpointDoubleValues() {
        double min = 0.0;
        double max = 1.0;
        double expected = 0.5;
        double actual = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, actual, 1E-15);
    }

    /**
     * Test midpoint method with long values.
     */
    @Test
    public void testMidpointLongValues() {
        long min = 0L;
        long max = 1L;
        long expected = (min + max) / 2L;
        long actual = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, actual);
    }

    /**
     * Test midpoint method with int values.
     */
    @Test
    public void testMidpointIntValues() {
        int min = 0;
        int max = 1;
        int expected = (min + max) / 2;
        int actual = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, actual);
    }
}