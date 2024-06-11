package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

/**
 * Test case for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /**
     * Test solver with a simple polynomial function.
     */
    @org.junit.Test
    public void testSolvePolynomial() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root not found", 1.0, Precision.round(root, 3));
    }

    /**
     * Test solver with a function that has no roots.
     */
    @org.junit.Test(expected = NoRootsException.class)
    public void testSolveNoRoots() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has infinite roots.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testSolveInfiniteRoots() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has roots at the bounds.
     */
    @org.junit.Test
    public void testSolveRootsAtBounds() {
        UnivariateFunction f = new PolynomialFunction(new double[]{0.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root not found", 0.0, Precision.round(root, 3));
    }

    /**
     * Test solver with a function that has roots at the initial guess.
     */
    @org.junit.Test
    public void testSolveRootsAtInitialGuess() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root not found", 1.0, Precision.round(root, 3));
    }

    /**
     * Test solver with a function that has roots bracketing the initial guess.
     */
    @org.junit.Test
    public void testSolveRootsBracketingInitialGuess() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root not found", 1.0, Precision.round(root, 3));
    }

    /**
     * Test solver with a function that has roots outside the interval.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveRootsOutsideInterval() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 4.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has an infinite number of roots.
     */
    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolveInfinitelyManyRoots() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has roots at infinity.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testSolveRootsAtInfinity() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, -1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has a root with infinite slope.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testSolveRootsWithInfiniteSlope() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has a root with infinite slope at the
     * bounds.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testSolveRootsWithInfiniteSlopeAtBound() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has a root with infinite slope at the
     * initial guess.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testSolveRootsWithInfiniteSlopeAtInitialGuess() {
        UnivariateFunction f = new PolynomialFunction(new double[]{0.0, -1.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function that has roots at the end of the iteration
     * count.
     */
    @org.junit.Test
    public void testSolveRootsAtEndOfIterationCount() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, 1.0});
        BisectionSolver solver = new BisectionSolver(1000);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals("Root not found", 1.0, Precision.round(root, 3));
    }

}