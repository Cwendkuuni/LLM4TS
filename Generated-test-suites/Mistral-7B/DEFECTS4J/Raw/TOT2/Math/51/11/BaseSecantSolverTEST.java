package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        function = new FunctionUnderTest();
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
    }

    /**
     * Tests the solver with a simple function where the root is known.
     */
    @Test
    public void testSimpleRoot() {
        double root = solver.solve(10, function, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Tests the solver with a function where the root is outside the search
     * interval.
     */
    @Test(expected = NoRootsException.class)
    public void testRootOutsideInterval() {
        solver.solve(10, function, -10.0, 0.0, 0.0);
    }

    /**
     * Tests the solver with a function that has multiple roots.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testMultipleRoots() {
        solver.solve(10, new FunctionWithMultipleRoots(), -10.0, 10.0, 0.0);
    }

    /**
     * Tests the solver with a function that has no roots.
     */
    @Test(expected = NoRootsException.class)
    public void testNoRoots() {
        solver.solve(10, new FunctionWithNoRoots(), -10.0, 10.0, 0.0);
    }

    /**
     * Tests the solver with a function where the search interval is inverted.
     */
    @Test
    public void testInvertedSearchInterval() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, 10.0, -10.0, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Tests the solver with a function where the search interval is too small.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTooSmallSearchInterval() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(10, function, 0.0, 0.0, 0.0);
    }

    /**
     * Tests the solver with a function where the relative accuracy is too high.
     */
    @Test
    public void testHighRelativeAccuracy() {
        solver = new RegulaFalsiSolver(10.0, RegulaFalsiSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-2);
    }

    /**
     * Tests the solver with a function where the absolute accuracy is too high.
     */
    @Test
    public void testHighAbsoluteAccuracy() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.setAbsoluteAccuracy(1.0);
        double root = solver.solve(10, function, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-3);
    }

    /**
     * A test function with a known root.
     */
    private static class FunctionUnderTest implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 1.0;
        }

    }

    /**
     * A test function with multiple roots.
     */
    private static class FunctionWithMultipleRoots implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return (x - 1.0) * (x - 3.0);
        }

    }

    /**
     * A test function with no roots.
     */
    private static class FunctionWithNoRoots implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) * Math.sin(x);
        }

    }

}