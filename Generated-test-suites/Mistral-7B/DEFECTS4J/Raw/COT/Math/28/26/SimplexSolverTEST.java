import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setup() {
        double[][] function = {{-1.0, 2.0}, {-3.0, -1.0}};
        LinearObjectiveFunction objFunc = new LinearObjectiveFunctionSet(function);

        LinearConstraint[][] constraints = {{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                             {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}};
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);

        solver = new SimplexSolver(1e-6, 10);
        solver.setGoalType(LinearGoalType.MINIMIZE);
        solver.setInput(objFunc, constraintSet);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(solver);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionSet(new double[][]{{-1.0, 2.0}, {-3.0, -1.0}}),
                                                   new LinearConstraintSet(new LinearConstraint[][]{{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                                                                                         {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}}),
                                                   LinearGoalType.MINIMIZE,
                                                   false,
                                                   1e-6,
                                                   10);
        int pivotCol = solver.getPivotColumn(tableau);
        Assert.assertEquals(pivotCol, 0);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionSet(new double[][]{{-1.0, 2.0}, {-3.0, -1.0}}),
                                                   new LinearConstraintSet(new LinearConstraint[][]{{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                                                                                         {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}}),
                                                   LinearGoalType.MINIMIZE,
                                                   false,
                                                   1e-6,
                                                   10);
        int pivotRow = solver.getPivotRow(tableau, 0);
        Assert.assertEquals(pivotRow, 0);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionSet(new double[][]{{-1.0, 2.0}, {-3.0, -1.0}}),
                                                   new LinearConstraintSet(new LinearConstraint[][]{{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                                                                                         {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}}),
                                                   LinearGoalType.MINIMIZE,
                                                   false,
                                                   1e-6,
                                                   10);
        solver.doIteration(tableau);
        Assert.assertEquals(tableau.getEntry(0, 0), 1.0, solver.maxUlps);
        Assert.assertEquals(tableau.getEntry(0, 1), 0.0, solver.maxUlps);
        Assert.assertEquals(tableau.getEntry(1, 0), 0.0, solver.maxUlps);
        Assert.assertEquals(tableau.getEntry(1, 1), 1.0, solver.maxUlps);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionSet(new double[][]{{-1.0, 2.0}, {-3.0, -1.0}}),
                                                   new LinearConstraintSet(new LinearConstraint[][]{{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                                                                                         {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}}),
                                                   LinearGoalType.MINIMIZE,
                                                   false,
                                                   1e-6,
                                                   10);
        solver.solvePhase1(tableau);
        Assert.assertEquals(tableau.getEntry(0, 0), 1.0, solver.maxUlps);
        Assert.assertEquals(tableau.getEntry(0, 1), 0.0, solver.maxUlps);
        Assert.assertEquals(tableau.getEntry(1, 0), 0.0, solver.maxUlps);
        Assert.assertEquals(tableau.getEntry(1, 1), 1.0, solver.maxUlps);
        Assert.assertEquals(tableau.getEntry(0, tableau.getWidth() - 1), 0.0, solver.maxUlps);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        PointValuePair result = solver.doOptimize();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(), -5.0, solver.maxUlps);
        Assert.assertArrayEquals(result.getPoint(), new double[]{2.0 / 3.0, 1.0}, solver.maxUlps);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolveWithUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] function = {{-1.0, 2.0}, {-3.0, -1.0}, {{-5.0, 0.0}}};
        LinearObjectiveFunction objFunc = new LinearObjectiveFunctionSet(function);

        LinearConstraint[][] constraints = {{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                             {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}};
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);

        solver.setInput(objFunc, constraintSet);
        solver.doOptimize();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolveWithMaxIterations() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] function = {{-1.0, 2.0}, {-3.0, -1.0}, {{-5.0, 0.0}}};
        LinearObjectiveFunction objFunc = new LinearObjectiveFunctionSet(function);

        LinearConstraint[][] constraints = {{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                             {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}};
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);

        solver.setInput(objFunc, constraintSet);
        solver.setMaxIterations(0);
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolveWithNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] function = {{0.0, 1.0}, {-3.0, -1.0}};
        LinearObjectiveFunction objFunc = new LinearObjectiveFunctionSet(function);

        LinearConstraint[][] constraints = {{new LinearConstraint(1.0, 1.0, 2.0), new LinearConstraint(1.0, 2.0, 3.0)},
                                             {new LinearConstraint(2.0, 1.0, 4.0), new LinearConstraint(2.0, 2.0, 5.0)}};
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);

        solver.setInput(objFunc, constraintSet);
        solver.doOptimize();
    }
}