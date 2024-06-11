package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test cases for BisectionSolver class
 */
public class BisectionSolverTest {

    /**
     * Test constructor with function
     */
    @Test
    public void testBisectionSolverWithFunction() {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        when(f.value(0.0)).thenReturn(1.0);
        when(f.value(1.0)).thenReturn(-1.0);
        BisectionSolver solver = new BisectionSolver(f);
        assertFalse(solver.isSolved());
    }

    /**
     * Test constructor without function
     */
    @Test
    public void testBisectionSolverWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();
        assertTrue(solver.isSolved());
        assertNull(solver.getSolution());
    }

    /**
     * Test solve method with minimal and maximal bounds
     */
    @Test
    public void testSolveMinimalAndMaximalBounds() throws FunctionEvaluationException,
            MaxIterationsExceededException {
        UnivariateRealFunction f = x -> Math.pow(Math.PI - x, 2.0);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = Math.PI;
        double result = solver.solve(f, min, max);
        assertTrue(solver.isSolved());
        assertEquals(Math.PI / 2.0, result, 1e-8);
    }

    /**
     * Test solve method with initial value
     */
    @Test
    public void testSolveInitialValue() throws FunctionEvaluationException,
            MaxIterationsExceededException {
        UnivariateRealFunction f = x -> Math.pow(E.PI, 2.0) * Math.sin(x);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = Math.PI;
        double initial = Math.PI / 2.0;
        double result = solver.solve(f, min, max, initial);
        assertTrue(solver.isSolved());
        assertEquals(Math.PI / 4.0, result, 1e-8);
    }

    /**
     * Test solve method with minimal and maximal bounds and function evaluation exceptions
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws MaxIterationsExceededException {
        UnivariateRealFunction f = x -> {
            throw new FunctionEvaluationException("Function evaluation error");
        };
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        solver.solve(f, min, max);
    }

    /**
     * Test solve method with maximal iteration count exceeded exception
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceededException() throws FunctionEvaluationException {
        UnivariateRealFunction f = x -> Math.pow(x, 2.0);
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        solver.setMaximalIterationCount(0);
        solver.solve(f, min, max);
    }

    /**
     * Test verifyInterval method
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalWithInvalidMin() {
        BisectionSolver solver = new BisectionSolver();
        double min = Double.NEGATIVE_INFINITY;
        double max = 0.0;
        solver.verifyInterval(min, max);
    }

    /**
     * Test verifyInterval method with minimal and maximal bounds
     */
    @Test
    public void testVerifyIntervalWithValidBounds() {
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        solver.verifyInterval(min, max);
    }

    /**
     * Test verifyInterval method with minimal value greater than maximal value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalWithInvalidMax() {
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = Double.NEGATIVE_INFINITY;
        solver.verifyInterval(min, max);
    }

    /**
     * Test UnivariateRealSolverUtils.midpoint method
     */
    @Test
    public void testMidpoint() {
        double min = 0.0;
        double max = 1.0;
        double expected = 0.5;
        double actual = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, actual, 1e-8);
    }
}