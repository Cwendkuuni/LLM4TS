package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math3.test.AbstractMathTestCase;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Tests for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest extends AbstractMathTestCase {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using default solver.
     */
    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                0.0, 1.0, -1.0}, new double[]{0.0, 1.0, 2.0, 1.0, 0.0});
        double expectedRoot = 1.0;
        double x0 = 0.0;
        double x1 = 2.0;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(expectedRoot, result, 1e-14);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using default solver.
     */
    @Test
    public void testSolveDefaultWithAccuracy() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                0.0, 1.0, -1.0}, new double[]{0.0, 1.0, 2.0, 1.0, 0.0});
        double expectedRoot = 1.0;
        double x0 = 0.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-12;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(expectedRoot, result, absoluteAccuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with function for which no root exists.
     */
    @Test
    public void testSolveNoRoot() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{0.0, 1.0,
                0.0}, new double[]{1.0, 1.0, 1.0});
        double x0 = 0.0;
        double x1 = 1.0;
        
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.solve(f, x0, x1);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with invalid endpoints.
     */
    @Test
    public void testSolveInvalidEndpoints() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 0.0,
                1.0}, new double[]{0.0, 1.0, 1.0});
        double x0 = 1.0;
        double x1 = 0.0;
        
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(f, x0, x1);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                0.0, 1.0, -1.0}, new double[]{0.0, 1.0, 2.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] expectedBracket = {0.0, 1.0};
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound,
                    upperBound);
            assertArrayEquals(expectedBracket, result);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     */
    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0,
                0.0, 1.0, -1.0}, new double[]{0.0, 1.0, 2.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 1;
        double[] expectedBracket = null;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound,
                    upperBound, maxIterations);
            assertEquals(expectedBracket, result);
            assertNull(result);
        } catch (ConvergenceException | FunctionEvaluationException |
                IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with function for which no root exists.
     */
    @Test
    public void testBracketNoRoot() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{0.0, 1.0,
                0.0}, new double[]{1.0, 1.0, 1.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 10;
        
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound,
                    maxIterations);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with invalid endpoints.
     */
    @Test
    public void testBracketInvalidEndpoints() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 0.0,
                1.0}, new double[]{0.0, 1.0, 1.0});
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 0.0;
        int maxIterations = 10;
        
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound,
                    maxIterations);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}
     */
    @Test
    public void testSetup() {
        UnivariateRealFunction f = null;
        
        assertThrows(MathRuntimeException.class, () -> {
            UnivariateRealSolverUtils.setup(f);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expectedMidpoint = 1.5;

        assertEquals(expectedMidpoint, UnivariateRealSolverUtils.midpoint(a, b), 1e-14);
    }
}