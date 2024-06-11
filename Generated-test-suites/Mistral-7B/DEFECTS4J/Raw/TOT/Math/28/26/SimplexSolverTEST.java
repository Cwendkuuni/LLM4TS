package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test case for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test that a simple case with a solution is solved correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleCase() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {-3, -1}}, new double[]{0, 3});
        LinearConstraint[] g = new LinearConstraint[]{{new LinearConstraint(new double[][]{{-1, 1}, {1, 1}}, 5), null},
                                                       {new LinearConstraint(new double[][]{{-1, -1}, {1, 1}}, 4), null}};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = solver.optimize(f, g, GoalType.MINIMIZE, null);
        assertEquals("Unexpected solution: ", new PointValuePair(new double[]{2.0 / 3.0, 1.0}, 5.0 / 3.0), solution);
    }

    /**
     * Test that a simple case with no solution is detected correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNoSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {-3, -1}}, new double[]{0, 3});
        LinearConstraint[] g = new LinearConstraint[]{{new LinearConstraint(new double[][]{{-1, 1}, {1, 1}}, 5), null},
                                                       {new LinearConstraint(new double[][]{{-1, -1}, {1, 1}}, 4), null},
                                                       {new LinearConstraint(new double[][]{{-1, 0}, {1, 0}}, 3), null}};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        assertThrows("Expected no solution: ", NoFeasibleSolutionException.class, () -> solver.optimize(f, g, GoalType.MINIMIZE, null));
    }

    /**
     * Test that a simple case with an unbounded solution is detected correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {-3, -1}}, new double[]{0, 3});
        LinearConstraint[] g = new LinearConstraint[]{{new LinearConstraint(new double[][]{{-1, 1}, {1, 1}}, 5), null}};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        assertThrows("Expected unbounded solution: ", UnboundedSolutionException.class, () -> solver.optimize(f, g, GoalType.MINIMIZE, null));
    }

    /**
     * Test that a simple case with a solution is solved correctly with a goal of MAXIMIZING.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleMaxCase() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {-3, -1}}, new double[]{0, 3});
        LinearConstraint[] g = new LinearConstraint[]{{new LinearConstraint(new double[][]{{-1, 1}, {1, 1}}, 5), null},
                                                       {new LinearConstraint(new double[][]{{-1, -1}, {1, 1}}, 4), null}};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = solver.optimize(f, g, GoalType.MAXIMIZE, null);
        assertEquals("Unexpected solution: ", new PointValuePair(new double[]{2.0 / 3.0, 1.0}, 5.0 / 3.0), solution);
    }

    /**
     * Test that an empty constraint list is handled correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEmptyConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {-3, -1}}, new double[]{0, 3});
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = solver.optimize(f, null, GoalType.MINIMIZE, null);
        assertEquals("Unexpected solution: ", new PointValuePair(new double[]{}, 0), solution);
    }

    /**
     * Test that a constraint list with one constraint is handled correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void testOneConstraint() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {-3, -1}}, new double[]{0, 3});
        LinearConstraint g = new LinearConstraint(new double[][]{{-1, 1}, {1, 1}}, 5);
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = solver.optimize(f, new LinearConstraint[] {g}, GoalType.MINIMIZE, null);
        assertEquals("Unexpected solution: ", new PointValuePair(new double[]{2.0 / 3.0, 1.0}, 5.0 / 3.0), solution);
    }

    /**
     * Test that a constraint list with conflicting goals is handled correctly.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGoalConflict() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {-3, -1}}, new double[]{0, 3});
        LinearConstraint[] g = new LinearConstraint[]{{new LinearConstraint(new double[][]{{-1, 1}, {1, 1}}, 5), null},
                                                       {new LinearConstraint(new double[][]{{-1, -1}, {1, 1}}, 4), GoalType.MINIMIZE}};
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        assertFalse("Expected no solution: ", solver.optimize(f, g, null, null) instanceof PointValuePair);
    }
}