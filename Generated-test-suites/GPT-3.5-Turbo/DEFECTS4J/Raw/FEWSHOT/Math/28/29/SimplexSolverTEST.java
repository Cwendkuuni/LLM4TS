import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, -1.0);
        tableau.setEntry(0, 3, -1.0);

        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);

        assertEquals(Integer.valueOf(2), pivotColumn);
    }

    @Test
    public void testPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.addColumn();
        tableau.addColumn();
        tableau.addColumn();
        tableau.addRow();
        tableau.addRow();
        tableau.addRow();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, -1.0);
        tableau.setEntry(0, 3, -1.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(1, 3, 1.0);
        tableau.setEntry(2, 2, -1.0);
        tableau.setEntry(2, 3, -1.0);

        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(tableau, 2);

        assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.addColumn();
        tableau.addColumn();
        tableau.addColumn();
        tableau.addRow();
        tableau.addRow();
        tableau.addRow();
        tableau.addRow();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, 4.0);
        tableau.setEntry(1, 0, 0.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 2.0);
        tableau.setEntry(1, 3, 3.0);
        tableau.setEntry(2, 1, 0.0);
        tableau.setEntry(2, 2, 3.0);
        tableau.setEntry(2, 3, 4.0);
        tableau.setEntry(3, 1, 1.0);
        tableau.setEntry(3, 2, 4.0);
        tableau.setEntry(3, 3, 5.0);

        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);

        assertEquals(2.0, tableau.getEntry(0, 1), 0.0);
        assertEquals(3.0, tableau.getEntry(0, 2), 0.0);
        assertEquals(4.0, tableau.getEntry(0, 3), 0.0);
        assertEquals(1.0 / 2.0, tableau.getEntry(1, 1), 0.0);
        assertEquals(-1.5, tableau.getEntry(1, 2), 0.0);
        assertEquals(-2.0, tableau.getEntry(1, 3), 0.0);
        assertEquals(2.5, tableau.getEntry(2, 2), 0.0);
        assertEquals(3.5, tableau.getEntry(2, 3), 0.0);
        assertEquals(-1.0 / 2.0, tableau.getEntry(3, 1), 0.0);
        assertEquals(0.5, tableau.getEntry(3, 2), 0.0);
        assertEquals(0.0, tableau.getEntry(3, 3), 0.0);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setData(new double[][]{{1, 3, 2, 1, 0, 0, 5}, {1, 5, 1, 0, 1, 0, 10}, {2, 1, 2, 0, 0, 1, 15}});

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        assertEquals(1, tableau.getWidth());
        assertEquals(2.0, tableau.getEntry(0, 0), 0.0);
        assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
        assertEquals(3.0, tableau.getEntry(2, 0), 0.0);
        assertEquals(37.5, tableau.getEntry(0, 1), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 1), 0.0);
        assertEquals(0.0, tableau.getEntry(2, 1), 0.0);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();

        assertNull(solution);
    }
}