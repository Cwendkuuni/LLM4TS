import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case with all positive coefficients
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        Integer pivotCol = getPivotColumn(tableau);
        assertNull(pivotCol);

        // Test case with negative coefficient
        tableau.setEntry(0, 0, -1);
        pivotCol = getPivotColumn(tableau);
        assertEquals(0, (int) pivotCol);

        // Test case with more than one negative coefficient
        tableau.setEntry(0, 1, -2);
        pivotCol = getPivotColumn(tableau);
        assertEquals(1, (int) pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case with all positive coefficients
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 2);
        tableau.setEntry(3, 0, 3);
        Integer pivotRow = getPivotRow(tableau, 0);
        assertNull(pivotRow);

        // Test case with negative coefficient
        tableau.setEntry(1, 0, -1);
        pivotRow = getPivotRow(tableau, 0);
        assertEquals(1, (int) pivotRow);

        // Test case with more than one negative coefficient
        tableau.setEntry(2, 0, -2);
        pivotRow = getPivotRow(tableau, 0);
        assertEquals(2, (int) pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case with basic variables
        tableau.setBasicRow(0, 1);
        tableau.setBasicRow(1, 2);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(2, 0, -2);
        try {
            solver.doIteration(tableau);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (UnboundedSolutionException e) {
            fail("UnboundedSolutionException should not be thrown");
        }
        assertEquals(2d, tableau.getEntry(0, 1), 0.0001);
        assertEquals(3d, tableau.getEntry(0, 2), 0.0001);
        assertEquals(0d, tableau.getEntry(1, 0), 0.0001);
        assertEquals(0d, tableau.getEntry(2, 0), 0.0001);
        assertEquals(1d, tableau.getEntry(1, 1), 0.0001);
        assertEquals(2d, tableau.getEntry(2, 2), 0.0001);

        // Test case with no feasible solution
        tableau.setBasicRow(0, 1);
        tableau.setBasicRow(1, 2);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(2, 0, 2);
        try {
            solver.doIteration(tableau);
            fail("UnboundedSolutionException should be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (UnboundedSolutionException e) {
            // success
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case with optimal solution
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(1, 0, 0);
        tableau.setEntry(2, 0, 0);
        try {
            solver.solvePhase1(tableau);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (UnboundedSolutionException e) {
            fail("UnboundedSolutionException should not be thrown");
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException should not be thrown");
        }
        assertEquals(0d, tableau.getEntry(0, 0), 0.0001);
        assertEquals(0d, tableau.getEntry(1, 0), 0.0001);
        assertEquals(0d, tableau.getEntry(2, 0), 0.0001);

        // Test case with no feasible solution
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(1, 0, 0);
        tableau.setEntry(2, 0, 1);
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException should be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (UnboundedSolutionException e) {
            fail("UnboundedSolutionException should not be thrown");
        } catch (NoFeasibleSolutionException e) {
            // success
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        
        // Test case with optimal solution
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(1, 0, 0);
        tableau.setEntry(2, 0, 0);
        try {
            PointValuePair solution = solver.doOptimize(tableau);
            assertEquals(0d, solution.getValue(), 0.0001);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (UnboundedSolutionException e) {
            fail("UnboundedSolutionException should not be thrown");
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException should not be thrown");
        }

        // Test case with no feasible solution
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(1, 0, 0);
        tableau.setEntry(2, 0, 1);
        try {
            solver.doOptimize(tableau);
            fail("NoFeasibleSolutionException should be thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        } catch (UnboundedSolutionException e) {
            fail("UnboundedSolutionException should not be thrown");
        } catch (NoFeasibleSolutionException e) {
            // success
        }
    }

}