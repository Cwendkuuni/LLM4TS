package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testEmptyConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertFalse(solver.isSolved());
        assertNull(solver.getRoot());
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 1E-8);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithEmptyConstructor() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(null, 0.0, 0.0);
    }

    @Test
    public void testDeprecatedConstructor() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver(f);
        assertTrue(solver.isSolved());
        assertArrayEquals(new double[]{1.0}, new double[]{solver.getRoot()}, 1E-8);
    }

    @Test
    public void testSolveWithInitialGuess() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double[] roots = {1.0, -1.0};

        for (double root : roots) {
            double[] bounds = {-5.0, root};
            double result = solver.solve(f, bounds[0], bounds[1]);
            assertTrue(solver.isSolved());
            assertArrayEquals(new double[]{root}, new double[]{solver.getRoot()}, 1E-8);
        }
    }

    @Test
    public void testSolveWithMaxIterations() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double[] roots = {1.0, -1.0};

        for (double root : roots) {
            double[] bounds = {-5.0, root};
            solver.setMaxIterations(5);
            double result = solver.solve(f, bounds[0], bounds[1]);
            assertTrue(solver.isSolved());
            assertArrayEquals(new double[]{root}, new double[]{solver.getRoot()}, 1E-5);
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double[] roots = {1.0, -1.0};

        for (double root : roots) {
            double[] bounds = {-5.0, root};
            solver.setAbsoluteAccuracy(1E-3);
            double result = solver.solve(f, bounds[0], bounds[1]);
            assertTrue(solver.isSolved());
            assertArrayEquals(new double[]{root}, new double[]{solver.getRoot()}, 1E-3);
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double[] roots = {1.0, -1.0};

        for (double root : roots) {
            double[] bounds = {-5.0, root};
            solver.setMaxIterations(1);
            solver.solve(f, bounds[0], bounds[1]);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithNonContinuousFunction() {
        UnivariateFunction f = new PolynomialFunction(new double[]{0.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-5.0, 5.0};
        solver.solve(f, bounds[0], bounds[1]);
    }

    @Test
    public void testSolveWithSinFunction() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-Math.PI, -Math.PI, 0.0, Math.PI, Math.PI});
        BisectionSolver solver = new BisectionSolver();
        double[] roots = {0.0};
        double[] bounds = {-Math.PI, Math.PI};

        for (double root : roots) {
            assertTrue(solver.solve(f, bounds[0], bounds[1]) instanceof NumberIsTooSmallException);
            assertFalse(solver.isSolved());
            assertNull(solver.getRoot());
        }

        solver.setMaxIterations(1000);
        for (double root : roots) {
            double[] bounds = {-Math.PI + 1E-9, Math.PI - 1E-9};
            double result = solver.solve(f, bounds[0], bounds[1]);
            assertTrue(solver.isSolved());
            assertArrayEquals(new double[]{root}, new double[]{solver.getRoot()}, 1E-9);
        }
    }

    @Test
    public void testSolveWithLogFunction() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-2.0, -1.0, 1.0, 1.0, 2.0});
        BisectionSolver solver = new BisectionSolver();
        double[] roots = {0.0};
        double[] bounds = {-5.0, 0.0};

        for (double root : roots) {
            assertTrue(solver.solve(f, bounds[0], bounds[1]) instanceof NumberIsTooSmallException);
            assertFalse(solver.isSolved());
            assertNull(solver.getRoot());
        }

        solver.setMaxIterations(1000);
        for (double root : roots) {
            double[] bounds = {-Math.log(1E-9), Math.log(1E-9)};
            double result = solver.solve(f, bounds[0], bounds[1]);
            assertTrue(solver.isSolved());
            assertArrayEquals(new double[]{root}, new double[]{solver.getRoot()}, 1E-9);
        }
    }
}