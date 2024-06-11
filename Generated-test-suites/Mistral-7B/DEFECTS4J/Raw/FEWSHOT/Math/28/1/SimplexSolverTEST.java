package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.GoalType;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_EmptyConstraintAndObjectiveFunction_ReturnsNull() {
        LinearObjectiveFunction objectiveFunction = null;
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = null;
        SimplexSolver simplexSolver = new SimplexSolver();
        PointValuePair solution = simplexSolver.doOptimize();
        assertNull(solution);
    }

    @Test
    public void testDoOptimize_BoundedSolution_ReturnsSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 1.0}, 0.0), new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver();
        PointValuePair solution = simplexSolver.doOptimize();
        assertEquals(2.0, solution.getValue(), 0.0);
        assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.0);
    }

    @Test
    public void testDoOptimize_UnboundedSolution_ThrowsException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 1.0}, 0.0), new LinearConstraint(new double[]{-1.0, 1.0}, Double.POSITIVE_INFINITY)};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> simplexSolver.doOptimize());
    }

    @Test
    public void testDoOptimize_NoFeasibleSolution_ThrowsException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 1.0}, 1.0), new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> simplexSolver.doOptimize());
    }

    @Test
    public void testDoOptimize_MaxIterationLimitExceeded_ThrowsException() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 1.0}, 0.0), new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver(1, 1);
        assertThrows(MaxCountExceededException.class, () -> simplexSolver.doOptimize());
    }

}