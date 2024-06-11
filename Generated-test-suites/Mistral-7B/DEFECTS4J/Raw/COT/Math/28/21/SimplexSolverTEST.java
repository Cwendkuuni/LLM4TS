import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SimpleCase() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
        Assert.assertEquals(0.0, solution.getPoint()[0], solver.epsilon);
        Assert.assertEquals(0.0, solution.getPoint()[1], solver.epsilon);
        Assert.assertEquals(3.0, solution.getValue(), solver.epsilon);
    }

    @Test
    public void testDoOptimize_FeasibleSolution() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint("x >= 0", new double[]{1.0}, null),
                new LinearConstraint("y >= 0", new double[]{1.0}, null)};
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), solver.epsilon);
        Assert.assertEquals(3.0, solution.getValue(), solver.epsilon);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint("x <= 0", new double[]{1.0}, null),
                new LinearConstraint("y <= 0", new double[]{1.0}, null)};
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxIterationsExceeded() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.doOptimize();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, null, 0.0, 0);
        int pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(-1, pivotColumn);

        tableau = new SimplexTableau(null, null, null, null, 0.0, 1);
        pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(-1, pivotColumn);

        tableau = new SimplexTableau(null, null, null, null, 0.0, 2);
        pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(-1, pivotColumn);

        tableau = new SimplexTableau(new double[][]{
                new double[]{Double.NEGATIVE_INFINITY, 3.0},
                new double[]{1.0, 1.0},
                new double[]{2.0, 2.0}},
                null,
                null,
                0.0,
                3);
        pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(0, pivotColumn);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0},
                new double[]{1.0, 1.0},
                new double[]{2.0, 2.0}},
                null,
                null,
                0.0,
                3);
        pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(1, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, null, 0.0, 1);
        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertEquals(-1, pivotRow);

        tableau = new SimplexTableau(null, null, null, null, 0.0, 2);
        pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertEquals(-1, pivotRow);

        tableau = new SimplexTableau(new double[][]{
                new double[]{Double.NEGATIVE_INFINITY, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 3.0, 3.0}},
                null,
                3,
                3);
        pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertEquals(0, pivotRow);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                null,
                3,
                3);
        pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertEquals(1, pivotRow);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                null,
                3,
                3);
        pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 1);
        Assert.assertEquals(2, pivotRow);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                new LinearConstraint[0],
                4,
                3);
        pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 1);
        Assert.assertEquals(3, pivotRow);
    }

    @Test
    public void testDoIteration() throws Exception {
        SimplexTableau tableau = new SimplexTableau(null, null, null, null, 0.0, 1);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);

        tableau = new SimplexTableau(null, null, null, null, 0.0, 2);
        solver = new SimplexSolver();
        solver.doIteration(tableau);

        tableau = new SimplexTableau(new double[][]{
                new double[]{Double.NEGATIVE_INFINITY, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 3.0, 3.0}},
                null,
                3,
                3);

        solver = new SimplexSolver();
        solver.doIteration(tableau);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                null,
                3,
                3);

        solver = new SimplexSolver();
        solver.doIteration(tableau);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                null,
                3,
                3);

        solver = new SimplexSolver();
        solver.doIteration(tableau);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                null,
                3,
                3);

        solver = new SimplexSolver();
        try {
            solver.doIteration(tableau);
            Assert.fail();
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = new SimplexTableau(null, null, null, null, 0.0, 1);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        tableau = new SimplexTableau(null, null, null, null, 0.0, 2);
        solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        tableau = new SimplexTableau(new double[][]{
                new double[]{Double.NEGATIVE_INFINITY, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 3.0, 3.0}},
                null,
                3,
                3);

        solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                null,
                3,
                3);

        solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        tableau = new SimplexTableau(new double[][]{
                new double[]{0.0, 3.0, 3.0},
                new double[]{1.0, 1.0, 1.0},
                new double[]{2.0, 2.0, 2.0},
                new double[]{3.0, 1.0, 1.0}},
                null,
                3,
                3);

        solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
            Assert.fail();
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }
}