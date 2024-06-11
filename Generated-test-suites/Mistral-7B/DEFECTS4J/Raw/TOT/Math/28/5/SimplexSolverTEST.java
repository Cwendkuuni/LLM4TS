package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test that the solver returns the correct solution for a simple linear optimization problem.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleOptimization() throws Exception {
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{-1.0, -1.0}, new double[]{0.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0}, new double[]{0.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(
            new LinearObjectiveFunction(new double[]{-5.0, 2.0}, 1.0),
            constraints,
            null,
            null
        );
        Assert.assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), eps);
        Assert.assertEquals(-5.0 / 3.0, solution.getValue(), eps);
    }

    /**
     * Test that the solver throws an {@link UnboundedSolutionException} for an unbounded problem.
     * @throws Exception if an error occurs
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedOptimization() throws Exception {
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{-1.0, -1.0}, new double[]{0.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0}, new double[]{0.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(
            new LinearObjectiveFunction(new double[]{-5.0, 2.0}, Double.POSITIVE_INFINITY),
            constraints,
            null,
            null
        );
    }

    /**
     * Test that the solver throws an {@link NoFeasibleSolutionException} for an infeasible problem.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testInfeasibleOptimization() throws Exception {
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{-1.0, -1.0}, new double[]{1.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(
            new LinearObjectiveFunction(new double[]{-5.0, 2.0}, 1.0),
            constraints,
            null,
            null
        );
    }

    /**
     * Test that the solver throws a {@link MaxCountExceededException} for a maximal iteration count exceeded problem.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationCountExceeded() throws Exception {
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{-1.0, -1.0}, new double[]{0.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0}, new double[]{0.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(
            new LinearObjectiveFunction(new double[]{-5.0, 2.0}, 1.0),
            constraints,
            null,
            null
        );
    }

    /**
     * Test that the solver returns the correct solution for a simple linear optimization problem with bounds.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleOptimizationWithBounds() throws Exception {
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{-1.0, -1.0}, new double[]{1.0, Double.NEGATIVE_INFINITY}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0}, new double[]{Double.NEGATIVE_INFINITY, 1.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(
            new LinearObjectiveFunction(new double[]{-5.0, 2.0}, 1.0),
            constraints,
            null,
            new double[]{-3.0, 3.0}
        );
        Assert.assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), eps);
        Assert.assertEquals(-5.0 / 3.0, solution.getValue(), eps);
    }

    /**
     * Test that the solver throws an {@link IllegalArgumentException} for null inputs.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInputs() throws Exception {
        new SimplexSolver().optimize(null, null);
    }

    /**
     * Test that the solver throws an {@link IllegalArgumentException} for empty constraint lists.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstraintList() throws Exception {
        new SimplexSolver().optimize(
            new LinearObjectiveFunction(new double[]{}, 1.0),
            new LinearConstraint[]{},
            null,
            null
        );
    }

    /**
     * Test that the solver throws an {@link IllegalArgumentException} for non-linear objective functions.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearObjectiveFunction() throws Exception {
        new SimplexSolver().optimize(
            new LinearObjectiveFunction(new double[]{0.0}, x -> Math.pow(x, 2.0)),
            new LinearConstraint[]{},
            null,
            null
        );
    }

    private static final double eps = 1e-9;
}