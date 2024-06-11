package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test for a simple linear optimization problem.
     * @throws Exception if any error occurs
     */
    @Test
    public void testSimpleLinearOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {3.0, 1.0}}, 4.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, 0.0}, "x <= 2.0"),
            new LinearConstraint(new double[][]{{2.0, -1.0}, 0.0}, "x >= 0.0")
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(new double[]{-2.0, 2.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);
    }

    /**
     * Test for an unbounded linear optimization problem.
     * @throws Exception if any error occurs
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedLinearOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {3.0, 1.0}}, 4.0);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints);
    }

    /**
     * Test for a linearly infeasible linear optimization problem.
     * @throws Exception if any error occurs
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testInfeasibleLinearOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {3.0, 1.0}}, 4.0);
        LinearConstraint constraint = new LinearConstraint(new double[][]{{0.0, 1.0}, 1.0}, "x <= 1.0");
        LinearConstraint[] constraints = new LinearConstraint[] {constraint};
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints);
    }

    /**
     * Test for a simple linear optimization problem with maximum iteration count.
     * @throws Exception if any error occurs
     */
    @Test
    public void testSimpleLinearOptimizationWithMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {3.0, 1.0}}, 4.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, 0.0}, "x <= 2.0"),
            new LinearConstraint(new double[][]{{2.0, -1.0}, 0.0}, "x >= 0.0")
        };
        SimplexSolver solver = new SimplexSolver(10);
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(new double[]{-2.0, 2.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);
    }

    /**
     * Test for a simple linear optimization problem with epsilon.
     * @throws Exception if any error occurs
     */
    @Test
    public void testSimpleLinearOptimizationWithEpsilon() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {3.0, 1.0}}, 4.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, 0.0}, "x <= 2.0"),
            new LinearConstraint(new double[][]{{2.0, -1.0}, 0.0}, "x >= 0.0")
        };
        SimplexSolver solver = new SimplexSolver(1.0e-3, 10);
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(new double[]{-2.0, 2.0}, solution.getPoint(), 1.0e-3);
        assertEquals(0.0, solution.getValue(), 1.0e-3);
    }

    /**
     * Test for a simple linear optimization problem with max ulps.
     * @throws Exception if any error occurs
     */
    @Test
    public void testSimpleLinearOptimizationWithMaxUlps() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {3.0, 1.0}}, 4.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, 0.0}, "x <= 2.0"),
            new LinearConstraint(new double[][]{{2.0, -1.0}, 0.0}, "x >= 0.0")
        };
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10, 5);
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(new double[]{-2.0, 2.0}, solution.getPoint(), 1.0e-6);
        assertEquals(0.0, solution.getValue(), 1.0e-6);
    }
}