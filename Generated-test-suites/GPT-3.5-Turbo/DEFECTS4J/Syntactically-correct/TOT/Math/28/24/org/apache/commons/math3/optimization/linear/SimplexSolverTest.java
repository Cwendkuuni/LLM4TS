import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Test case 1: Simple tableau with negative coefficient
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, -5);
        tableau1.setEntry(0, 1, -3);
        tableau1.setEntry(0, 2, -2);
        tableau1.setEntry(0, 3, -1);
        Integer expected1 = 3;
        Integer actual1 = SimplexSolver.getPivotColumn(tableau1);
        assertEquals(expected1, actual1);

        // Test case 2: Simple tableau with positive coefficients only
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, 2);
        tableau2.setEntry(0, 1, 3);
        tableau2.setEntry(0, 2, 4);
        tableau2.setEntry(0, 3, 5);
        Integer expected2 = null;
        Integer actual2 = SimplexSolver.getPivotColumn(tableau2);
        assertEquals(expected2, actual2);

        // Test case 3: Simple tableau with zero coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, 0);
        tableau3.setEntry(0, 1, 0);
        tableau3.setEntry(0, 2, 0);
        tableau3.setEntry(0, 3, 0);
        Integer expected3 = null;
        Integer actual3 = SimplexSolver.getPivotColumn(tableau3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testGetPivotRow() {
        // Test case 1: Simple tableau with positive coefficients only
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(1, 0, 2);
        tableau1.setEntry(1, 1, 3);
        tableau1.setEntry(1, 2, 4);
        tableau1.setEntry(1, 3, 5);
        Integer expected1 = null;
        Integer actual1 = SimplexSolver.getPivotRow(tableau1, 2);
        assertEquals(expected1, actual1);

        // Test case 2: Simple tableau with negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(1, 0, -5);
        tableau2.setEntry(1, 1, -3);
        tableau2.setEntry(1, 2, -2);
        tableau2.setEntry(1, 3, -1);
        Integer expected2 = 1;
        Integer actual2 = SimplexSolver.getPivotRow(tableau2, 3);
        assertEquals(expected2, actual2);

        // Test case 3: Simple tableau with zero coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(1, 0, 0);
        tableau3.setEntry(1, 1, 0);
        tableau3.setEntry(1, 2, 0);
        tableau3.setEntry(1, 3, 0);
        Integer expected3 = null;
        Integer actual3 = SimplexSolver.getPivotRow(tableau3, 3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testDoIteration() {
        // Test case 1: Simple tableau with positive coefficients only
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 2);
        tableau1.setEntry(0, 1, 3);
        tableau1.setEntry(0, 2, 4);
        tableau1.setEntry(0, 3, 5);
        SimplexSolver.doIteration(tableau1);
        double[] expected1 = {1, 3d/2, 2d/3, 5d/4};
        double[] actual1 = tableau1.getRow(0);
        assertArrayEquals(expected1, actual1, 0.0001);

        // Test case 2: Simple tableau with negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -5);
        tableau2.setEntry(0, 1, -3);
        tableau2.setEntry(0, 2, -2);
        tableau2.setEntry(1, 0, 2);
        tableau2.setEntry(1, 1, 3);
        tableau2.setEntry(1, 2, 4);
        tableau2.setEntry(1, 3, 5);
        SimplexSolver.doIteration(tableau2);
        double[] expected2 = {0, 0, 0, 0};
        double[] actual2 = tableau2.getRow(1);
        assertArrayEquals(expected2, actual2, 0.0001);
    }

    @Test
    public void testSolvePhase1() {
        // Test case 1: Simple tableau with no artificial variables
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 2);
        tableau1.setEntry(0, 1, 3);
        tableau1.setEntry(0, 2, 4);
        tableau1.setEntry(0, 3, 5);
        SimplexSolver.solvePhase1(tableau1);
        assertFalse(tableau1.isOptimal());

        // Test case 2: Simple tableau with artificial variables
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -5);
        tableau2.setEntry(0, 1, -3);
        tableau2.setEntry(0, 2, -2);
        tableau2.setEntry(1, 0, 2);
        tableau2.setEntry(1, 1, 3);
        tableau2.setEntry(1, 2, 4);
        tableau2.setEntry(1, 3, 5);
        SimplexSolver.solvePhase1(tableau2);
        assertFalse(tableau2.isOptimal());
    }

    @Test
    public void testDoOptimize() {
        // Test case 1: Simple tableau with no feasible solution
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, -3);
        tableau1.setEntry(0, 1, -4);
        tableau1.setEntry(1, 1, 2);
        tableau1.setEntry(1, 2, 3);
        tableau1.setEntry(1, 3, 4);
        tableau1.setEntry(1, 4, 5);
        boolean exceptionThrown1 = false;
        try {
            SimplexSolver.doOptimize(tableau1);
        } catch (NoFeasibleSolutionException e) {
            exceptionThrown1 = true;
        }
        assertTrue(exceptionThrown1);

        // Test case 2: Simple tableau with feasible solution
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -3);
        tableau2.setEntry(0, 1, -4);
        tableau2.setEntry(1, 0, 2);
        tableau2.setEntry(1, 1, 3);
        tableau2.setEntry(1, 2, 4);
        tableau2.setEntry(1, 3, 5);
        SimplexSolver.doOptimize(tableau2);
        assertTrue(tableau2.isOptimal());
    }

    @Test
    public void testDoOptimizeWithMaxCountExceeded() {
        // Test case 1: Simple tableau with no feasible solution
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, -3);
        tableau1.setEntry(0, 1, -4);
        tableau1.setEntry(1, 1, 2);
        tableau1.setEntry(1, 2, 3);
        tableau1.setEntry(1, 3, 4);
        tableau1.setEntry(1, 4, 5);
        boolean exceptionThrown1 = false;
        try {
            SimplexSolver.doOptimizeWithMaxCountExceeded(tableau1, 100);
        } catch (MaxCountExceededException e) {
            exceptionThrown1 = true;
        }
        assertTrue(exceptionThrown1);

        // Test case 2: Simple tableau with feasible solution
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -3);
        tableau2.setEntry(0, 1, -4);
        tableau2.setEntry(1, 0, 2);
        tableau2.setEntry(1, 1, 3);
        tableau2.setEntry(1, 2, 4);
        tableau2.setEntry(1, 3, 5);
        SimplexSolver.doOptimizeWithMaxCountExceeded(tableau2, 10);
        assertTrue(tableau2.isOptimal());
    }

}