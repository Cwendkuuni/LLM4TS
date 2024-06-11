package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** A simple polynomial function for testing. */
    private static final UnivariateRealFunction FUNCTION = new PolynomialFunction(x -> x * x - 4.0);

    /** A solver instance for testing. */
    private BaseSecantSolver solver;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
    }

    /**
     * Tests the constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        solver = new IllinoisSolver();
        assertEquals(IllinoisSolver.ILLINOIS, solver.getMethod());
        assertEquals(DefaultSolverBehavior.DEFAULT, solver.getBehavior());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-12);
    }

    /**
     * Tests the constructor with specified arguments.
     */
    @Test
    public void testConstructor() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-8;
        Method method = PegasusSolver.PEGASUS;
        solver = new IllinoisSolver(relativeAccuracy, absoluteAccuracy, method);
        assertEquals(method, solver.getMethod());
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(DefaultSolverBehavior.DEFAULT, solver.getBehavior());
    }

    /**
     * Tests the constructor with specified arguments and behavior.
     */
    @Test
    public void testConstructorWithBehavior() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-8;
        Method method = PegasusSolver.PEGASUS;
        SolverBehavior behavior = SolverBehavior.SAFE;
        solver = new IllinoisSolver(relativeAccuracy, absoluteAccuracy, method, behavior);
        assertEquals(method, solver.getMethod());
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-12);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(behavior, solver.getBehavior());
    }

    /**
     * Tests the {@code solve} method with default arguments.
     */
    @Test
    public void testSolveDefault() {
        double root = solver.solve(FUNCTION, -10.0, 10.0);
        assertTrue( root > 2.0 && root < 3.0 );
    }

    /**
     * Tests the {@code solve} method with specified arguments.
     */
    @Test
    public void testSolve() {
        double root = solver.solve(FUNCTION, -10.0, 10.0, 3.0);
        assertTrue( root > 2.0 && root < 3.0 );
    }

    /**
     * Tests the {@code solve} method with specified starting value.
     */
    @Test
    public void testSolveStartValue() {
        double root = solver.solve(FUNCTION, -10.0, 10.0, 5.0);
        assertTrue( root > 2.0 && root < 3.0 );
    }

    /**
     * Tests the {@code solve} method with {@code MaxEval} exception.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveMaxEval() {
        solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS, 1);
        solver.solve(FUNCTION, -10.0, 10.0);
    }

    /**
     * Tests the {@code solve} method with {@code NumberIsTooSmallException}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveNumberTooSmall() {
        solver = new IllinoisSolver(1e-12, IllinoisSolver.ILLINOIS);
        solver.solve(FUNCTION, -10.0, 10.0);
    }

    /**
     * Tests the {@code solve} method with {@code UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveUnsupportedOperation() {
        solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS, Method.REGULA_FALSI);
        solver.solve(FUNCTION, -10.0, 10.0);
    }

    /**
     * Tests the {@code doSolve} method.
     */
    @Test
    public void testDoSolve() {
        double x0 = getMin();
        double x1 = getMax();
        double root = solver.doSolve();
        assertTrue( Precision.isClose(root, solver.solve(FUNCTION, x0, x1), 1e-6));
    }

    /**
     * Tests the {@code verifyBracketing} method.
     */
    @Test
    public void testVerifyBracketing() {
        double x0 = getMin();
        double x1 = getMax();
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Tests the {@code verifyBracketing} method with inverted interval.
     */
    @Test
    public void testVerifyBracketingInverted() {
        double x0 = getMax();
        double x1 = getMin();
        solver.verifyBracketing(x0, x1);
        assertTrue(true); // Should not throw an exception
    }

    /**
     * Helper method to get the minimum value of a function.
     *
     * @return The minimum value of the function.
     */
    private double getMin() {
        double min = Double.POSITIVE_INFINITY;
        double x = -10.0;
        double y;
        do {
            y = FUNCTION.value(x);
            if (y < min) {
                min = y;
            }
            x = x - 1.0;
        } while (x > -100.0);
        return min;
    }

    /**
     * Helper method to get the maximum value of a function.
     *
     * @return The maximum value of the function.
     */
    private double getMax() {
        double max = Double.NEGATIVE_INFINITY;
        double x = 10.0;
        double y;
        do {
            y = FUNCTION.value(x);
            if (y > max) {
                max = y;
            }
            x = x + 1.0;
        } while (x < 100.0);
        return max;
    }
}