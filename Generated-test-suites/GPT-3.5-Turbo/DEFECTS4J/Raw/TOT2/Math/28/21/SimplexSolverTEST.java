import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Test with a simple tableau
        SimplexTableau tableau = new SimplexTableau(2, 2);
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, 2);

        SimplexSolver solver = new SimplexSolver();
        Integer pivotCol = solver.getPivotColumn(tableau);

        assertEquals((Integer) 1, pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        // Test with a simple tableau
        SimplexTableau tableau = new SimplexTableau(2, 2);
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(0, 1, 3);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, 2);

        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(tableau, 1);

        assertEquals((Integer) 0, pivotRow);
    }

    @Test
    public void testDoIteration() {
        // Test with a simple tableau
        SimplexTableau tableau = new SimplexTableau(2, 2);
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(0, 1, 3);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, 2);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doIteration(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            fail("Exception thrown");
        }

        assertEquals(1.0, tableau.getEntry(0, 0), 0.01);
    }

    // Add more test cases for other methods as needed

}