package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    /**
     * Test optimization with empty constraints.
     */
    @Test
    public void testEmptyConstraints() {
        double[][] f = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        PointValuePair solution = solver.optimize(f);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.0);
        Assert.assertEquals(0.0, solution.getValue(), 0.0);
    }

    /**
     * Test optimization with constraints.
     */
    @Test
    public void testConstraints() {
        double[][] f = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        double[][] constraints = new double[][]{{0.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}};
        PointValuePair solution = solver.optimize(f, constraints);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.0);
        Assert.assertEquals(7.0 / 3.0, solution.getValue(), 0.0);
    }

    /**
     * Test optimization with non-negative constraints.
     */
    @Test
    public void testNonNegativeConstraints() {
        double[][] f = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        double[][] constraints = new double[][]{{0.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}};
        Boolean restrictToNonNegative = true;
        PointValuePair solution = solver.optimize(f, constraints, restrictToNonNegative);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.0);
        Assert.assertEquals(7.0 / 3.0, solution.getValue(), 0.0);
    }

    /**
     * Test optimization with goal type.
     */
    @Test
    public void testGoalType() {
        double[][] f = new double[][]{{1.0, 2.0}, {-3.0, -4.0}};
        double[][] constraints = new double[][]{{0.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}};
        PointValuePair solution = solver.optimize(f, constraints, SimplexSolver.GoalType.MINIMIZE);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.0);
        Assert.assertEquals(-11.0 / 3.0, solution.getValue(), 0.0);

        solution = solver.optimize(f, constraints, SimplexSolver.GoalType.MAXIMIZE);
        Assert.assertArrayEquals(new double[]{2.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 0.0);
        Assert.assertEquals(-1.0 / 3.0, solution.getValue(), 0.0);
    }

    /**
     * Test optimization with unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        double[][] f = new double[][]{{1.0, 2.0}, {1.0, 2.0}};
        double[][] constraints = new double[][]{{0.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}};
        solver.optimize(f, constraints);
    }

    /**
     * Test optimization with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        double[][] f = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        double[][] constraints = new double[][]{{-1.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}};
        solver.optimize(f, constraints);
    }

    /**
     * Test optimization with maximal iteration count exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        double[][] f = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        double[][] constraints = new double[][]{{0.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver(0.0);
        solver.optimize(f, constraints);
    }

}