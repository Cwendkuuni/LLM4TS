import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    @Test
    public void testCustomConstructor() throws Exception {
        double epsilon = 1.0e-8;
        int maxUlps = 20;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    @Test
    public void testGetPivotColumn() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0, 4.0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 10);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(0, pivotColumn.intValue());

        tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0, 3.0)}, null, false, 1.0e-6, 10);
        pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0, 4.0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 10);
        Integer pivotRow = new SimplexSolver().getPivotRow(tableau, 0);
        Assert.assertEquals(1, pivotRow.intValue());

        tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0, 3.0)}, null, false, 1.0e-6, 10);
        pivotRow = new SimplexSolver().getPivotRow(tableau, 1);
        Assert.assertEquals(2, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0, 4.0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 10);
        new SimplexSolver().doIteration(tableau);
        double[][] expectedTableau = new double[][]{{1.0 / 3.0, 1.0 / 3.0, 0.0, 2.0 / 3.0, 1.0 / 3.0, 0.0},
                {0.0, 0.0, 1.0, -1.0 / 3.0, -1.0 / 3.0, 1.0},
                {1.0, 0.0, 0.0, 1.0, -1.0 / 3.0, -1.0 / 3.0},
                {0.0, 0.0, 0.0, 0.0, 2.0 / 3.0, 3.0 / 3.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, -1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 2.0}};
        Assert.assertArrayEquals(expectedTableau, tableau.getTableau(), 0.0);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}}, new double[]{2.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
        double[][] expectedTableau = new double[][]{{1.0 / 3.0, 1.0 / 3.0, 0.0, 2.0 / 3.0, 1.0 / 3.0, 0.0},
                {0.0, 0.0, 1.0, -1.0 / 3.0, -1.0 / 3.0, 1.0},
                {1.0, 0.0, 0.0, 1.0, -1.0 / 3.0, -1.0 / 3.0},
                {0.0, 0.0, 0.0, 0.0, 2.0 / 3.0, 3.0 / 3.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, -1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 2.0}};
        Assert.assertArrayEquals(expectedTableau, tableau.getTableau(), 0.0);
    }

    @Test
    public void testDoOptimize() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}}, new double[]{2.0, 2.0});
        PointValuePair expectedSolution = new PointValuePair(new double[]{3.0 / 3.0, 3.0 / 3.0}, 2.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
        tableau.dropPhase1Objective();
        PointValuePair solution = new SimplexSolver().doOptimize();
        Assert.assertEquals(expectedSolution.getValue(), solution.getValue(), 0.0);
        Assert.assertArrayEquals(expectedSolution.getPoint(), solution.getPoint(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}}, new double[]{2.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 1);
        new SimplexSolver().solvePhase1(tableau);
        tableau.dropPhase1Objective();
        new SimplexSolver().doIteration(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}}, new double[]{2.0, 2.0, 1.0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
        tableau.dropPhase1Objective();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws Exception {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}}, new double[]{2.0, 2.0, -1.0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[0], null, false, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
        tableau.dropPhase1Objective();
    }

}