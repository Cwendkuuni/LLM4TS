import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SimplexSolver solver = new SimplexSolver();

        Integer pivotColumn = solver.getPivotColumn(tableau);

        Assert.assertEquals(Integer.valueOf(0), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SimplexSolver solver = new SimplexSolver();

        Integer pivotRow = solver.getPivotRow(tableau, 0);

        Assert.assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SimplexSolver solver = new SimplexSolver();

        solver.doIteration(tableau);

        Assert.assertArrayEquals(new double[][]{{1, 2, 3}, {0, -3, -6}, {7, 8, 9}}, tableau.getData());
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SimplexSolver solver = new SimplexSolver();

        solver.solvePhase1(tableau);

        Assert.assertEquals(0, tableau.getEntry(0, tableau.getRhsOffset()), 0.0001);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SimplexSolver solver = new SimplexSolver();

        PointValuePair result = solver.doOptimize();

        Assert.assertEquals(0, result.getPoint()[0], 0.0001);
        Assert.assertEquals(0, result.getPoint()[1], 0.0001);
        Assert.assertEquals(0, result.getPoint()[2], 0.0001);
    }

}