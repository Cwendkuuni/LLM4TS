package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.IMathConstants;
import org.junit.Test;

/**
 * Tests for the UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test of solve method, with default solver and default accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveDefault() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 8, -15, 12});
        double x0 = 0.0;
        double x1 = 3.0;
        double root = 1.0;
        assertEquals(root, UnivariateRealSolverUtils.solve(f, x0, x1), 1e-6);
    }

    /**
     * Test of solve method, with default solver and given accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveDefaultAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 8, -15, 12});
        double x0 = 0.0;
        double x1 = 3.0;
        double root = 1.0;
        double accuracy = 1e-9;
        assertEquals(root, UnivariateRealSolverUtils.solve(f, x0, x1, accuracy), accuracy);
    }

    /**
     * Test of solve method, with BrentSolver and default accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveBrent() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 8, -15, 12});
        double x0 = 0.0;
        double x1 = 3.0;
        double root = 1.0;
        assertEquals(root, UnivariateRealSolverUtils.solve(f, x0, x1,
                UnivariateRealSolverFactory.BRENT), 1e-6);
    }

    /**
     * Test of solve method, with BrentSolver and given accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveBrentAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 8, -15, 12});
        double x0 = 0.0;
        double x1 = 3.0;
        double root = 1.0;
        double accuracy = 1e-9;
        assertEquals(root, UnivariateRealSolverUtils.solve(f, x0, x1,
                UnivariateRealSolverFactory.BRENT, accuracy), accuracy);
    }

    /**
     * Test of solve method, with SecantSolver and default accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveSecant() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 8, -15, 12});
        double x0 = 0.0;
        double x1 = 3.0;
        double root = 1.0;
        assertEquals(root, UnivariateRealSolverUtils.solve(f, x0, x1,
                UnivariateRealSolverFactory.SECANT), 1e-6);
    }

    /**
     * Test of solve method, with SecantSolver and given accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveSecantAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 8, -15, 12});
        double x0 = 0.0;
        double x1 = 3.0;
        double root = 1.0;
        double accuracy = 1e-9;
        assertEquals(root, UnivariateRealSolverUtils.solve(f, x0, x1,
                UnivariateRealSolverFactory.SECANT, accuracy), accuracy);
    }

    /**
     * Test of solve method, with failure case.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveFailure() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 8, -15, 12});
        double x0 = 0.0;
        double x1 = 1.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    /**
     * Test of bracket method, with default solver and default accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketDefault() throws Exception {
        UnivariateRealFunction f = (x) -> Math.sin(x) - x;
        double initial = 1.5;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        double a = result[0];
        double b = result[1];
        assertTrue(a < b);
        assertTrue(f.value(a) * f.value(b) < 0);
    }

    /**
     * Test of bracket method, with BrentSolver and default accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketBrent() throws Exception {
        UnivariateRealFunction f = (x) -> Math.sin(x) - x;
        double initial = 1.5;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound,
                UnivariateRealSolverFactory.BRENT);
        double a = result[0];
        double b = result[1];
        assertTrue(a < b);
        assertTrue(f.value(a) * f.value(b) < 0);
    }

    /**
     * Test of bracket method, with failure case.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketFailure() throws Exception {
        UnivariateRealFunction f = (x) -> Math.sin(x) - x;
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 1.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    /**
     * Test of bracket method, with large interval.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketLargeInterval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, -1});
        double initial = 1000000.0;
        double lowerBound = -1000000.0;
        double upperBound = 1000000.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    /**
     * Test of bracket method, with failure case: crossing bounds.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketBoundsCrossing() throws Exception {
        UnivariateRealFunction f = (x) -> Math.sin(x) - x;
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 1.5;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    /**
     * Test of bracket method, with failure case: non-continuous function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketFunctionNoncontinuous() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0.5) {
                    return x * x;
                }
                return 0.0;
            }
        };
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 0.5;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    /**
     * Test of midpoint method.
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 1e-10);
    }

}