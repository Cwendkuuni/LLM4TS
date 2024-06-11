import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 0, 0);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        tableau.setEntry(0, 3, -4);
        tableau.setEntry(0, 4, -5);

        Integer pivotColumn = solver.getPivotColumn(tableau);

        Assert.assertEquals(4, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 0, 0);
        tableau.setEntry(0, 4, 5);
        tableau.setEntry(1, 4, 4);
        tableau.setEntry(2, 4, 3);
        tableau.setEntry(3, 4, 2);
        tableau.setEntry(4, 4, 1);

        Integer pivotRow = solver.getPivotRow(tableau, 4);

        Assert.assertEquals(4, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 0, 0);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, -5);
        tableau.setEntry(1, 0, -2);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, 3);

        solver.doIteration(tableau);

        Assert.assertEquals(0, tableau.getEntry(0, 0), 0);
        Assert.assertEquals(1, tableau.getEntry(0, 1), 0);
        Assert.assertEquals(4, tableau.getEntry(0, 2), 0);
        Assert.assertEquals(0, tableau.getEntry(1, 0), 0);
        Assert.assertEquals(3, tableau.getEntry(1, 1), 0);
        Assert.assertEquals(9, tableau.getEntry(1, 2), 0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnbounded() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MINIMIZE, false, 0, 0);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        tableau.setEntry(0, 3, -4);
        tableau.setEntry(0, 4, -5);

        solver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MINIMIZE, false, 0, 0);

        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1, 1, 1}, Relationship.LEQ, 1);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, -2, -1}, Relationship.EQ, 2);
        tableau.setConstraints(new LinearConstraint[]{constraint1, constraint2});

        solver.solvePhase1(tableau);

        Assert.assertEquals(1, tableau.getEntry(0, 0), 0);
        Assert.assertEquals(0, tableau.getEntry(0, 1), 0);
        Assert.assertEquals(0, tableau.getEntry(1, 0), 0);
        Assert.assertEquals(1, tableau.getEntry(1, 1), 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MINIMIZE, false, 0, 0);

        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1, 1}, Relationship.LEQ, 1);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, 1, 1}, Relationship.GEQ, 2);
        tableau.setConstraints(new LinearConstraint[]{constraint1, constraint2});

        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(100);
        solver.setConvergenceChecker(null);

        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{3, 4}, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 2}, Relationship.LEQ, 14);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{3, 2}, Relationship.LEQ, 18);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{1, -1}, Relationship.LEQ, 3);
        LinearConstraint[] constraints = new LinearConstraint[]{constraint1, constraint2, constraint3};

        PointValuePair solution = solver.optimize(objectiveFunction, constraints, GoalType.MAXIMIZE, false);

        Assert.assertEquals(8, solution.getPoint()[0], 0);
        Assert.assertEquals(3, solution.getPoint()[1], 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(100);
        solver.setConvergenceChecker(null);

        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 2);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 1);
        LinearConstraint[] constraints = new LinearConstraint[]{constraint1, constraint2};

        solver.optimize(objectiveFunction, constraints, GoalType.MAXIMIZE, false);
    }

}