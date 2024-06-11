package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /** A simple test with two variables and two constraints. */
    @Test
    public void testSimpleExample() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-2.0, 3.0}, {-1.0, -1.0}}, 4.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(-3.0, new double[] {1.0, 1.0}),
                new LinearConstraint(2.0, new double[] {1.0, 1.0})
        };
        LinearOptimizer solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints, null, null, null, null);
        Assert.assertEquals(2.0, solution.getPoint()[0], 1e-8);
        Assert.assertEquals(-1.0, solution.getPoint()[1], 1e-8);
        Assert.assertEquals(4.0, solution.getValue(), 1e-8);
    }

    /** A test with a constraint that has no solution. */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{0.0, 0.0}, {0.0, 0.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(2.0, new double[] {1.0, 1.0})
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, null, null, null, null);
    }

    /** A test with a constraint that has an infinite solution. */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{0.0, 0.0}, {0.0, 0.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(-1.0, new double[] {1.0, 1.0})
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, null, null, null, null);
    }

    /** A test with a simplex tableau that has reached its maximum number of iterations. */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 0.0}, {0.0, -1.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(0.0, new double[] {1.0, 1.0})
        };
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);
        solver.optimize(f, constraints, null, null, null, null);
    }

    /** A test with a simplex tableau that has a goal type of MINIMIZE_INF. */
    @Test
    public void testMinimizeInf() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 0.0}, {0.0, -1.0}}, Double.POSITIVE_INFINITY);
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(0.0, new double[] {1.0, 1.0})
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints, null, null, null, null);
        Assert.assertNull(solution);
    }

    /** A test with a simplex tableau that has a goal type of MAXIMIZE_INF. */
    @Test
    public void testMaximizeInf() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 0.0}, {0.0, -1.0}}, Double.NEGATIVE_INFINITY);
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(0.0, new double[] {1.0, 1.0})
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints, null, null, null, null);
        Assert.assertNull(solution);
    }
}