import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Test case 1: Simple tableau with all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        tableau1.setEntry(0, 3, 4);
        Integer expected1 = null;
        Integer actual1 = new SimplexSolver().getPivotColumn(tableau1);
        assertEquals(expected1, actual1);

        // Test case 2: Simple tableau with one negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(0, 1, 2);
        tableau2.setEntry(0, 2, 3);
        tableau2.setEntry(0, 3, 4);
        Integer expected2 = 0;
        Integer actual2 = new SimplexSolver().getPivotColumn(tableau2);
        assertEquals(expected2, actual2);

        // Test case 3: Simple tableau with multiple negative coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(0, 1, -2);
        tableau3.setEntry(0, 2, -3);
        tableau3.setEntry(0, 3, -4);
        Integer expected3 = 3;
        Integer actual3 = new SimplexSolver().getPivotColumn(tableau3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testGetPivotRow() {
        // Test case 1: Simple tableau with all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(1, 0, 2);
        tableau1.setEntry(2, 0, 3);
        tableau1.setEntry(3, 0, 4);
        Integer expected1 = null;
        Integer actual1 = new SimplexSolver().getPivotRow(0, tableau1);
        assertEquals(expected1, actual1);

        // Test case 2: Simple tableau with one negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(1, 0, 2);
        tableau2.setEntry(2, 0, 3);
        tableau2.setEntry(3, 0, 4);
        Integer expected2 = 3;
        Integer actual2 = new SimplexSolver().getPivotRow(0, tableau2);
        assertEquals(expected2, actual2);

        // Test case 3: Simple tableau with multiple negative coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(1, 0, -2);
        tableau3.setEntry(2, 0, -3);
        tableau3.setEntry(3, 0, -4);
        Integer expected3 = 0;
        Integer actual3 = new SimplexSolver().getPivotRow(0, tableau3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testDoIteration() {
        // Test case 1: Simple tableau with all positive entries
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        tableau1.setEntry(0, 3, 4);
        new SimplexSolver().doIteration(tableau1);
        double expected1 = 0;
        double actual1 = tableau1.getEntry(0, 0);
        assertEquals(expected1, actual1, 0.0001);

        // Test case 2: Simple tableau with one negative entry
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(0, 1, 2);
        tableau2.setEntry(0, 2, 3);
        tableau2.setEntry(0, 3, 4);
        new SimplexSolver().doIteration(tableau2);
        double expected2 = 0;
        double actual2 = tableau2.getEntry(0, 0);
        assertEquals(expected2, actual2, 0.0001);

        // Test case 3: Simple tableau with multiple negative entries
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(0, 1, -2);
        tableau3.setEntry(0, 2, -3);
        tableau3.setEntry(0, 3, -4);
        new SimplexSolver().doIteration(tableau3);
        double expected3 = 1;
        double actual3 = tableau3.getEntry(0, 0);
        assertEquals(expected3, actual3, 0.0001);
    }

    @Test
    public void testIsPhase1Solved() {
        // Test case 1: Simple tableau with all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        tableau1.setEntry(0, 3, 4);
        boolean expected1 = true;
        boolean actual1 = new SimplexSolver().isPhase1Solved(tableau1);
        assertEquals(expected1, actual1);

        // Test case 2: Simple tableau with one negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(0, 1, 2);
        tableau2.setEntry(0, 2, 3);
        tableau2.setEntry(0, 3, 4);
        boolean expected2 = false;
        boolean actual2 = new SimplexSolver().isPhase1Solved(tableau2);
        assertEquals(expected2, actual2);

        // Test case 3: Simple tableau with multiple negative coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(0, 1, -2);
        tableau3.setEntry(0, 2, -3);
        tableau3.setEntry(0, 3, -4);
        boolean expected3 = false;
        boolean actual3 = new SimplexSolver().isPhase1Solved(tableau3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testIsOptimal() {
        // Test case 1: Simple tableau with all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        tableau1.setEntry(0, 3, 4);
        boolean expected1 = true;
        boolean actual1 = new SimplexSolver().isOptimal(tableau1);
        assertEquals(expected1, actual1);

        // Test case 2: Simple tableau with one negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(0, 1, 2);
        tableau2.setEntry(0, 2, 3);
        tableau2.setEntry(0, 3, 4);
        boolean expected2 = false;
        boolean actual2 = new SimplexSolver().isOptimal(tableau2);
        assertEquals(expected2, actual2);

        // Test case 3: Simple tableau with multiple negative coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(0, 1, -2);
        tableau3.setEntry(0, 2, -3);
        tableau3.setEntry(0, 3, -4);
        boolean expected3 = false;
        boolean actual3 = new SimplexSolver().isOptimal(tableau3);
        assertEquals(expected3, actual3);
    }

}