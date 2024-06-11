package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test case for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest extends AbstractTest {

    /**
     * Test solver with a simple function.
     */
    @Test
    public void testSimple() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(-1.0, solver.solve(f, -2.0, 2.0, AllowedSolution.ANY_SIDE), 1e-8);
    }

    /**
     * Test solver with a function with a root at the boundary.
     */
    @Test
    public void testRootAtBoundary() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(-1.0, solver.solve(f, -1.0, 1.0, AllowedSolution.LEFT_SIDE), 1e-8);
    }

    /**
     * Test solver with a function with a root at the boundary and a tolerance smaller than the distance to the root.
     */
    @Test
    public void testRootAtBoundaryWithSmallTolerance() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 5);
        assertThrows(NoBracketingException.class, () -> solver.solve(f, -1.0, 1.0, AllowedSolution.LEFT_SIDE));
    }

    /**
     * Test solver with a function that has no real roots.
     */
    @Test
    public void testNoRoots() {
        final UnivariateFunction f = x -> Math.pow(x, 2) + 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertThrows(NoBracketingException.class, () -> solver.solve(f, -2.0, 2.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test solver with a function with a root outside the given interval.
     */
    @Test
    public void testRootOutsideInterval() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertThrows(NoBracketingException.class, () -> solver.solve(f, -2.0, 2.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test solver with a function with a root inside the given interval but too close to one of its endpoints.
     */
    @Test
    public void testRootTooCloseToEndpoint() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1e-12;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(-1.0, solver.solve(f, -1.0, 1.0, AllowedSolution.LEFT_SIDE), 1e-8);
    }

    /**
     * Test solver with a function with a root inside the given interval but too close to the other endpoint.
     */
    @Test
    public void testRootTooCloseToEndpoint2() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1e-12;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(-1.0, solver.solve(f, -1.0, 1.0, AllowedSolution.RIGHT_SIDE), 1e-8);
    }

    /**
     * Test solver with a function with a root inside the given interval but at the middle.
     */
    @Test
    public void testRootInMiddle() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(-1.0, solver.solve(f, -2.0, 0.0, AllowedSolution.ANY_SIDE), 1e-8);
    }

    /**
     * Test solver with a function with a root inside the given interval and a tolerance smaller than the distance to the root.
     */
    @Test
    public void testRootInMiddleWithSmallTolerance() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 5);
        assertThrows(NoBracketingException.class, () -> solver.solve(f, -2.0, 0.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test solver with a function with a root inside the given interval, a tolerance smaller than the distance to the root,
     * and a large maximal order.
     */
    @Test
    public void testRootInMiddleWithLargeMaximalOrder() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 100);
        assertThrows(NoBracketingException.class, () -> solver.solve(f, -2.0, 0.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test solver with a function with roots outside the given interval.
     */
    @Test
    public void testRootsOutsideInterval() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertThrows(NoBracketingException.class, () -> solver.solve(f, -2.0, 2.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test solver with a function with roots very close to each other.
     */
    @Test
    public void testRootsVeryCloseToEachOther() {
        final UnivariateFunction f = x -> 2 * Math.pow(x, 3) - 2;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 5);
        assertEquals(0.5, solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE), 1e-8);
    }

    /**
     * Test solver with a function with complex roots.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testComplexRoots() {
        final UnivariateFunction f = x -> Math.sqrt(Math.pow(x, 2) + 1);
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solver with a function with a very large slope near a root.
     */
    @Test
    public void testLargeSlope() {
        final UnivariateFunction f = x -> 1e12 * (x - 1);
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(1.0, solver.solve(f, 0.0, 2.0, AllowedSolution.ANY_SIDE), 1e-8);
    }

}