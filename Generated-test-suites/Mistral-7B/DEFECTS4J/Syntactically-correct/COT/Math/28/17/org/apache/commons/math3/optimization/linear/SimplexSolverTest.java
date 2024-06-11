import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.goals.MaximizeGoal;
import org.apache.commons.math3.optimization.linear.goals.MinimizeGoal;
import org.apache.commons.math3.optimization.nonlinear.GoalType;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.getEpsilon(), 0.0);
        assertEquals(10, solver.getMaxUlps());
    }

    @Test
    public void testCustomConstructor() {
        double epsilon = 1.0e-4;
        int maxUlps = 5;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        assertEquals(epsilon, solver.getEpsilon(), 0.0);
        assertEquals(maxUlps, solver.getMaxUlps());
    }

    @Test
    public void testGetPivotColumn() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraintSet(new LinearConstraint[]{}), new MaximizeGoal(), true, 1.0e-6, 10);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraintSet(new LinearConstraint[]{}), new MaximizeGoal(), true, 1.0e-6, 10);
        Integer pivotRow = new SimplexSolver().getPivotRow(tableau, 0);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraintSet(new LinearConstraint[]{}), new MaximizeGoal(), true, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        Integer pivotCol = solver.getPivotColumn(tableau);
        Integer pivotRow = solver.getPivotRow(tableau, pivotCol.intValue());

        solver.doIteration(tableau);

        double[] expectedResult = new double[]{1.0 / (-1.0), 1.0 / (-2.0)};
        assertArrayEquals(expectedResult, tableau.getBasicSolution(), 0.0);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraintSet(new LinearConstraint[]{}), new MaximizeGoal(), true, 1.0e-6, 10);

        new SimplexSolver().solvePhase1(tableau);

        assertFalse(tableau.isOptimal());
        assertEquals(0.0, tableau.getEntry(0, tableau.getWidth() - 1), 0.0);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{constraint});
        SimplexTableau tableau = new SimplexTableau(objective, constraints, new MaximizeGoal(), true, 1.0e-6, 10);

        PointValuePair solution = new SimplexSolver().doOptimize();
        double[] expectedSolution = new double[]{1.0 / (-1.0), 1.0 / (-2.0)};
        assertArrayEquals(expectedSolution, solution.getPoint(), 0.0);
        assertEquals(objective.value(expectedSolution), solution.getValue(), 0.0);
    }

    @Test
    public void testNoFeasibleSolutionException() throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraintSet(new LinearConstraint[]{}), new MaximizeGoal(), true, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
        tableau.dropPhase1Objective();

        assertThrows(NoFeasibleSolutionException.class, () -> new SimplexSolver().doIteration(tableau));
    }

    @Test
    public void testMaxCountExceededException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraintSet(new LinearConstraint[]{}), new MaximizeGoal(), true, 1.0e-6, 1);
        new SimplexSolver().solvePhase1(tableau);
        tableau.dropPhase1Objective();

        assertThrows(MaxCountExceededException.class, () -> new SimplexSolver().doIteration(tableau));
    }

    @Test
    public void testMinimizeGoal() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-1.0, -2.0});
        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraintSet(new LinearConstraint[]{}), new MinimizeGoal(), true, 1.0e-6, 10);

        new SimplexSolver().solvePhase1(tableau);
        tableau.dropPhase1Objective();

        PointValuePair solution = new SimplexSolver().doOptimize();
        double[] expectedSolution = new double[]{1.0 / (-1.0), 1.0 / (-2.0)};
        assertArrayEquals(expectedSolution, solution.getPoint(), 0.0);
        assertEquals(objective.value(expectedSolution), solution.getValue(), 0.0);
    }

}