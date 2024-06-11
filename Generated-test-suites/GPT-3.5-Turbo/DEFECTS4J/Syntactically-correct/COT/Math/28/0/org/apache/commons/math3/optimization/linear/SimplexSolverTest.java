import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        // Test with negative coefficients
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        Assert.assertEquals(1, pivotColumn.intValue());
        // Test with positive coefficients
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        pivotColumn = solver.getPivotColumn(tableau);
        Assert.assertEquals(null, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(3, 0, 4);

        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 1, -4);
        tableau.setEntry(3, 1, 6);

        tableau.setEntry(1, tableau.getWidth() - 1, 8);
        tableau.setEntry(2, tableau.getWidth() - 1, -3);
        tableau.setEntry(3, tableau.getWidth() - 1, 2);

        Integer pivotRow = solver.getPivotRow(tableau, 1);
        Assert.assertEquals(3, pivotRow.intValue());

        pivotRow = solver.getPivotRow(tableau, 2);
        Assert.assertEquals(2, pivotRow.intValue());

        tableau.setEntry(3, 1, 0); // creating a tie
        pivotRow = solver.getPivotRow(tableau, 1);
        Assert.assertEquals(2, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnbounded() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(3, 0, 4);

        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 1, -4);
        tableau.setEntry(3, 1, 6);

        tableau.setEntry(1, 2, 7);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(3, 2, 5);

        tableau.setEntry(1, tableau.getWidth() - 1, 8);
        tableau.setEntry(2, tableau.getWidth() - 1, -3);
        tableau.setEntry(3, tableau.getWidth() - 1, 2);

        solver.doIteration(tableau);

        Assert.assertEquals(0, tableau.getEntry(0, 0), 0);
        Assert.assertEquals(0, tableau.getEntry(0, 1), 0);
        Assert.assertEquals(0, tableau.getEntry(0, 2), 0);

        Assert.assertEquals(1, tableau.getEntry(1, 0), 0);
        Assert.assertEquals(0, tableau.getEntry(1, 1), 0);
        Assert.assertEquals(0, tableau.getEntry(1, 2), 0);

        Assert.assertEquals(7, tableau.getEntry(2, 0), 0);
        Assert.assertEquals(0, tableau.getEntry(2, 1), 0);
        Assert.assertEquals(-21, tableau.getEntry(2, 2), 0);

        Assert.assertEquals(0, tableau.getEntry(3, 0), 0);
        Assert.assertEquals(0, tableau.getEntry(3, 1), 0);
        Assert.assertEquals(0, tableau.getEntry(3, 2), 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasible() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(0, 3, 1);
        tableau.setEntry(1, 3, 2);
        tableau.setEntry(2, 3, 1);

        tableau.setEntry(1, tableau.getWidth() - 1, 4);
        tableau.setEntry(2, tableau.getWidth() - 1, 1);

        solver.solvePhase1(tableau);

        Assert.assertEquals(1, tableau.getEntry(0, 0), 0);
        Assert.assertEquals(0, tableau.getEntry(0, 1), 0);
        Assert.assertEquals(0, tableau.getEntry(0, 2), 0);
        Assert.assertEquals(0, tableau.getEntry(0, 3), 0);

        Assert.assertEquals(0, tableau.getEntry(1, 0), 0);
        Assert.assertEquals(1, tableau.getEntry(1, 1), 0);
        Assert.assertEquals(0, tableau.getEntry(1, 2), 0);
        Assert.assertEquals(2, tableau.getEntry(1, 3), 0);

        Assert.assertEquals(0, tableau.getEntry(2, 0), 0);
        Assert.assertEquals(0, tableau.getEntry(2, 1), 0);
        Assert.assertEquals(1, tableau.getEntry(2, 2), 0);
        Assert.assertEquals(1, tableau.getEntry(2, 3), 0);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair optimize = solver.doOptimize();
    }

}