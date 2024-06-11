package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.BisectionSolver;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

/**
 * Tests for the {@link BisectionSolver} class.
 */
public class BisectionSolverTest {

    /**
     * Test solving a simple function with known root.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateFunction fun = new PolynomialFunction(x -> x * x - 4.0);
        double root = 2.0;
        double[] expected = {root};
        BisectionSolver solver = new BisectionSolver();
        double[] result = solver.solve(fun, -3.0, 3.0);
        assertArrayEquals(expected, result);
    }

    /**
     * Test solving a function with roots at the bounds.
     */
    @Test
    public void testRootsAtBounds() {
        UnivariateFunction fun = new PolynomialFunction(x -> x * x - 4.0);
        double rootMin = 2.0;
        double rootMax = 2.0;
        double[] expected = {rootMin, rootMax};
        BisectionSolver solver = new BisectionSolver();
        double[] result = solver.solve(fun, rootMin, rootMax);
        assertArrayEquals(expected, result);
    }

    /**
     * Test solving a function with roots at the beginning of the interval.
     */
    @Test
    public void testRootsAtBeginning() {
        UnivariateFunction fun = new PolynomialFunction(x -> x * x - 4.0);
        double rootMin = 1.5;
        double rootMax = 2.0;
        double root = 1.732;
        double[] expected = {root};
        BisectionSolver solver = new BisectionSolver();
        double[] result = solver.solve(fun, rootMin, rootMax);
        assertArrayEquals(expected, result);
    }

    /**
     * Test solving a function with roots at the end of the interval.
     */
    @Test
    public void testRootsAtEnd() {
        UnivariateFunction fun = new PolynomialFunction(x -> x * x - 4.0);
        double rootMin = 2.0;
        double rootMax = 3.0;
        double root = 1.732;
        double[] expected = {root};
        BisectionSolver solver = new BisectionSolver();
        double[] result = solver.solve(fun, rootMin, rootMax);
        assertArrayEquals(expected, result);
    }

    /**
     * Test solving a function with roots outside the interval.
     */
    @Test
    public void testRootsOutsideInterval() {
        UnivariateFunction fun = new PolynomialFunction(x -> x * x - 4.0);
        double rootMin = 1.0;
        double rootMax = 1.5;
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(fun, rootMin, rootMax));
    }

    /**
     * Test solving a function with roots at the midpoint of the interval.
     */
    @Test
    public void testRootsAtMidpoint() {
        UnivariateFunction fun = new PolynomialFunction(x -> x * x - 4.0);
        double rootMin = 1.5;
        double rootMax = 2.5;
        double root = 2.0;
        double[] expected = {root};
        BisectionSolver solver = new BisectionSolver();
        double[] result = solver.solve(fun, rootMin, rootMax);
        assertArrayEquals(expected, result);
    }

    /**
     * Test solving a discontinuous function.
     */
    @Test
    public void testDiscontinuousFunction() {
        UnivariateFunction fun = new PolynomialFunction(x -> {
            if (x > 0.0) {
                return FunctionUtils.sqrt(Math.abs(x)) - 1.0;
            } else {
                throw new NumberIsTooSmallException();
            }
        });
        double rootMin = -1.5;
        double rootMax = 1.5;
        double root = 0.0;
        double[] expected = {root};
        BisectionSolver solver = new BisectionSolver();
        double[] result = solver.solve(fun, rootMin, rootMax);
        assertArrayEquals(expected, result);
    }
}