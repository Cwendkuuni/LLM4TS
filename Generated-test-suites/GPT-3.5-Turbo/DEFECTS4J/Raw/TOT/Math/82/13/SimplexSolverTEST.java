import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, -2, 3}, {-4, 5, -6}}, new double[]{7, -8}, GoalType.MINIMIZE, false, 1.0e-6);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        Assert.assertEquals(new Integer(1), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, -2, 3}, {-4, 5, -6}}, new double[]{7, -8}, GoalType.MINIMIZE, false, 1.0e-6);
        Integer pivotRow = solver.getPivotRow(1, tableau);
        Assert.assertEquals(new Integer(1), pivotRow);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, -2, 3}, {-4, 5, -6}}, new double[]{7, -8}, GoalType.MINIMIZE, false, 1.0e-6);
        solver.doIteration(tableau);
        RealPointValuePair solution = tableau.getSolution();
        double[] expected = {0.0, 2.0, 1.0};
        Assert.assertArrayEquals(expected, solution.getPoint(), 1.0e-6);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{-1, -1}, {1, -2}, {3, 2}}, new double[]{1, -2, 5}, GoalType.MINIMIZE, false, 1.0e-6);
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        Assert.assertTrue(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, -2, 3}, {-4, 5, -6}}, new double[]{7, -8}, GoalType.MINIMIZE, false, 1.0e-6);
        boolean isOptimal = solver.isOptimal(tableau);
        Assert.assertTrue(isOptimal);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{-1, -1}, {1, -2}, {3, 2}}, new double[]{1, -2, 5}, GoalType.MINIMIZE, false, 1.0e-6);
        solver.solvePhase1(tableau);
        RealPointValuePair solution = tableau.getSolution();
        double[] expected = {0.666667, 3.333333, 2.666667};
        Assert.assertArrayEquals(expected, solution.getPoint(), 1.0e-6);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        double[] f = {-1, -2};
        double[][] constraints = {{1, -2, 3}, {-4, 5, -6}};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-6;
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        RealPointValuePair solution = solver.doOptimize(tableau);
        double[] expected = {1.0, 0.0, 0.0};
        Assert.assertArrayEquals(expected, solution.getPoint(), 1.0e-6);
    }

}