package org.apache.commons.cli.optimization.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraints;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.SimpleLinearObjectiveFunction;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        LinearObjectiveFunction obj = new SimpleLinearObjectiveFunction(new double[]{-1.0, 3.0}, 5.0);
        LinearConstraints constraints = new LinearConstraints(new double[][]{new double[]{0.0, 0.0}, new double[]{1.0, 1.0}}, new GoalType[]{SimpleGoalType.MINIMIZE, SimpleGoalType.EQUALS}, new double[]{0.0, 4.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(5.0, solution.getValue(), 0.0);
        assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        LinearObjectiveFunction obj = new SimpleLinearObjectiveFunction(new double[]{-1.0, 3.0}, 5.0);
        LinearConstraints constraints = new LinearConstraints(new double[][]{new double[]{0.0, 0.0}, new double[]{1.0, 1.0}}, new GoalType[]{SimpleGoalType.MINIMIZE, SimpleGoalType.EQUALS}, new double[]{0.0, 4.0});
        SimplexSolver solver = new SimplexSolver(0.0, 0);
        solver.setMaxIterations(0);
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        LinearObjectiveFunction obj = new SimpleLinearObjectiveFunction(new double[]{-1.0, 3.0}, Double.POSITIVE_INFINITY);
        LinearConstraints constraints = new LinearConstraints(new double[][]{new double[]{0.0, 0.0}, new double[]{1.0, 1.0}}, new GoalType[]{SimpleGoalType.MINIMIZE, SimpleGoalType.EQUALS}, new double[]{0.0, 4.0});
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws Exception {
        LinearObjectiveFunction obj = new SimpleLinearObjectiveFunction(new double[]{-1.0, 3.0}, 5.0);
        LinearConstraints constraints = new LinearConstraints(new double[][]{new double[]{0.0, 0.0}, new double[]{1.0, 1.0}}, new GoalType[]{SimpleGoalType.MINIMIZE, SimpleGoalType.EQUALS}, new double[]{Double.NEGATIVE_INFINITY, 4.0});
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }
}