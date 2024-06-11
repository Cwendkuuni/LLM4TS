package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertDoubleEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.interpolators.CubicSplineInterpolator;
import org.apache.commons.math.analysis.interpolators.Interpolator;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MaxIterationsExceededException;
import org.apache.commons.math.exception.NoRealSolutionException;
import org.apache.commons.math.exception.NullArgumentException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 3.0, 2.0, 1.0});
        double x0 = 1.0;
        double x1 = 2.0;
        double root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertDoubleEquals(1.5, root, 1e-10);

        f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        try {
            root = UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        f = new PolynomialFunction(new double[]{0.0});
        x0 = 0.0;
        x1 = 1.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(0.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{-1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{0.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 0.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, -1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(-1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{2.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.5, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(-1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(-1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(-1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, -1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(-1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, root, 1e-10);

        // Test with interpolated function
        Interpolator interpolator = new CubicSplineInterpolator();
        double[] x = new double[]{0.0, 1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{0.1, 0.2, 0.3, 0.4, 0.5};
        UnivariateRealFunction g = interpolator.interpolate(x, y);
        x0 = 1.5;
        x1 = 2.5;
        root = UnivariateRealSolverUtils.solve(g, x0, x1);
        assertEquals(1.6666666666666666, root, 1e-6);

        // Test with brent solver
        UnivariateRealFunction h = new PolynomialFunction(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        UnivariateRealSolver solver = new BrentSolver();
        x0 = 1.0;
        x1 = 4.0;
        root = UnivariateRealSolverUtils.solve(h, solver, x0, x1);
        assertEquals(2.0, root, 1e-10);

        // Test with secant solver
        UnivariateRealFunction i = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        solver = new SecantSolver();
        x0 = 1.0;
        x1 = 2.0;
        root = UnivariateRealSolverUtils.solve(i, solver, x0, x1);
        assertEquals(-1.0, root, 1e-10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        double x0 = 0.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1, 1e-10, new BrentSolver());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        double x0 = 0.0;
        double x1 = 2.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0});
        double x0 = 2.0;
        double x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        double x0 = 0.0;
        double x1 = 2.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1, 1e-10, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test(expected = NoRealSolutionException.class)
    public void testSolveWithNoRealSolutionException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        double x0 = 0.0;
        double x1 = 2.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1, 1e-10, new SecantSolver());
            fail("Expected NoRealSolutionException");
        } catch (NoRealSolutionException e) {
            // expected
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceededException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        double x0 = 0.0;
        double x1 = 2.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1, 1e-10, new SecantSolver(), 1);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }
}