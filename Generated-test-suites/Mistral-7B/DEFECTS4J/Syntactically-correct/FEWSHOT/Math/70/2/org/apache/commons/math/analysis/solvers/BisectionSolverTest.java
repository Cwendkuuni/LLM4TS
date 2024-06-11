package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver.getMaximalIterationCount());
        assertNotNull(solver.getAbsoluteAccuracy());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithConstructorDeprecated() throws Exception {
        BisectionSolver solver = new BisectionSolver(FunctionUtils.SIN);
    }

    @Test
    public void testSolveSimpleFunction() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double result = new BisectionSolver().solve(f, -2, 2);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testSolveMaxIterations() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) / x;
        try {
            new BisectionSolver().solve(f, -1, 1, 0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionZero() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4;
        double result = new BisectionSolver().solve(f, -2, 2);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testSolveFunctionNegativeZero() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4;
        double result = new BisectionSolver().solve(f, -2, 2);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testSolveFunctionCloseToZero() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 0.000001;
        double result = new BisectionSolver().solve(f, -1, 1);
        assertEquals(0.0, result, 1E-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveIntervalTooNarrow() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        new BisectionSolver().solve(f, 0, 0);
    }

    @Test
    public void testSolveFunctionWithInitialGuess() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4;
        double result = new BisectionSolver().solve(f, -3, 1, 1);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testSolveFunctionRootIsMinOrMax() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4;
        double result = new BisectionSolver().solve(f, 4, 4);
        assertEquals(2.0, result, 1E-6);

        result = new BisectionSolver().solve(f, -4, -4);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testSolveFunctionRootIsInitialGuess() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4;
        double result = new BisectionSolver().solve(f, 2, 2);
        assertNull(result);
    }

    @Test
    public void testSolveFunctionRootIsBetweenMinAndMax() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) / x;
        double result = new BisectionSolver().solve(f, -3.4, 3.4);
        assertTrue(Math.abs(result) < 1);
    }

    @Test
    public void testSolveFunctionRootIsBetweenMinAndMaxWithInitialGuess() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) / x;
        double result = new BisectionSolver().solve(f, -3.4, 3.4, 0);
        assertTrue(Math.abs(result) < 1);
    }

    @Test
    public void testSolveFunctionWithNanMin() throws Exception {
        UnivariateRealFunction f = x -> Math.sqrt(x);
        try {
            new BisectionSolver().solve(f, Double.NaN, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithNanMax() throws Exception {
        UnivariateRealFunction f = x -> Math.sqrt(x);
        try {
            new BisectionSolver().solve(f, 2, Double.NaN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithInfMin() throws Exception {
        UnivariateRealFunction f = x -> Math.log(x);
        try {
            new BisectionSolver().solve(f, Double.POSITIVE_INFINITY, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithInfMax() throws Exception {
        UnivariateRealFunction f = x -> Math.log(x);
        try {
            new BisectionSolver().solve(f, 2, Double.POSITIVE_INFINITY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithInitialGuessOutsideInterval() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) / x;
        try {
            new BisectionSolver().solve(f, -3.4, 3.4, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithInitialGuessCloseToMin() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) / x;
        try {
            new BisectionSolver().solve(f, -3.4, 3.4, -3.4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithInitialGuessCloseToMax() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) / x;
        try {
            new BisectionSolver().solve(f, -3.4, 3.4, 3.4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}