package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.goaltypes.Maximize;
import org.apache.commons.math3.optimization.nonlinear.SimpleGoalTypeConstraint;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /**
     * Test solver with simple objective function and no constraints.
     */
    @Test
    public void testSimpleOptimization() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[0];
        Maximize goalType = new Maximize();
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunc, constraints, goalType);
        assertEquals(2.0, solution.getPoint()[0], 0.0);
        assertEquals(4.0, solution.getPoint()[1], 0.0);
        assertEquals(5.0, solution.getValue(), 0.0);
    }

    /**
     * Test solver with objective function and one constraint.
     */
    @Test
    public void testOptimizationWithConstraint() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0.0);
        LinearConstraint constraint = new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, 1.0);
        Maximize goalType = new Maximize();
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunc, new LinearConstraint[]{constraint}, goalType);
        assertEquals(1.0 / 2.0, solution.getPoint()[0], 0.0);
        assertEquals(1.0 / 2.0, solution.getPoint()[1], 0.0);
        assertEquals(3.0 / 2.0, solution.getValue(), 0.0);
    }

    /**
     * Test solver with objective function and multiple constraints.
     */
    @Test
    public void testOptimizationWithMultipleConstraints() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}, 0.0);
        Maximize goalType = new Maximize();
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunc, new LinearConstraint[]{constraint1, constraint2}, goalType);
        assertEquals(2.0 / 3.0, solution.getPoint()[0], 0.0);
        assertEquals(2.0 / 3.0, solution.getPoint()[1], 0.0);
        assertEquals(3.0 / 3.0, solution.getValue(), 0.0);
    }

    /**
     * Test solver with non-negative constraints.
     */
    @Test
    public void testOptimizationWithNonNegativeConstraints() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[][]{{0.0, 1.0}, {1.0, 1.0}}, 0.0);
        Maximize goalType = new Maximize();
        SimplexSolver solver = new SimplexSolver(true);
        PointValuePair solution = solver.optimize(objFunc, new LinearConstraint[]{constraint1, constraint2}, goalType);
        assertEquals(1.0, solution.getPoint()[0], 0.0);
        assertEquals(2.0, solution.getPoint()[1], 0.0);
        assertEquals(3.0, solution.getValue(), 0.0);
    }

    /**
     * Test solver with non-bounded objective function.
     */
    @Test
    public void testUnboundedObjectiveFunction() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, Double.POSITIVE_INFINITY);
        LinearConstraint[] constraints = new LinearConstraint[0];
        Maximize goalType = new Maximize();
        assertThrows(UnboundedSolutionException.class, () -> new SimplexSolver().optimize(objFunc, constraints, goalType));
    }

    /**
     * Test solver with no feasible solution.
     */
    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0.0);
        LinearConstraint constraint = new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, Double.NEGATIVE_INFINITY);
        Maximize goalType = new Maximize();
        assertThrows(NoFeasibleSolutionException.class, () -> new SimplexSolver().optimize(objFunc, new LinearConstraint[]{constraint}, goalType));
    }

    /**
     * Test solver with maximal number of iterations exceeded.
     */
    @Test
    public void testMaxIterationsExceeded() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0.0);
        LinearConstraint constraint = new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, 1.0);
        Maximize goalType = new Maximize();
        SimplexSolver solver = new SimplexSolver(1);
        assertThrows(MaxCountExceededException.class, () -> solver.optimize(objFunc, new LinearConstraint[]{constraint}, goalType));
    }

    /**
     * Test solver with GoalTypeConstraint.
     */
    @Test
    public void testGoalTypeConstraint() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0.0);
        SimpleGoalTypeConstraint goalTypeConstraint = new SimpleGoalTypeConstraint(3.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunc, null, goalTypeConstraint);
        assertEquals(1.5, solution.getPoint()[0], 0.0);
        assertEquals(2.5, solution.getPoint()[1], 0.0);
        assertEquals(3.0, solution.getValue(), 0.0);
    }
}