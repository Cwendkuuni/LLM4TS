
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.linear.*;

public class SimplexSolverTest {

    private SimplexSolver solver;
    
    @Before
    public void setup() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        // Test case for positive values in objective function row
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        Integer expectedColumn = 0;
        assertEquals(expectedColumn, pivotColumn);
        
        // Test case for negative values in objective function row
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        pivotColumn = solver.getPivotColumn(tableau);
        expectedColumn = 2;
        assertEquals(expectedColumn, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        // Test case for positive ratio
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, 2.0);
        tableau.setEntry(1, 2, 3.0);
        tableau.setEntry(1, 3, 10.0);
        Integer pivotRow = solver.getPivotRow(2, tableau);
        Integer expectedRow = 1;
        assertEquals(expectedRow, pivotRow);
        
        // Test case for zero ratio
        tableau.setEntry(2, 0, 1.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(2, 2, 3.0);
        tableau.setEntry(2, 3, 0.0);
        pivotRow = solver.getPivotRow(2, tableau);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        // Test case for a valid iteration
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, 2.0);
        tableau.setEntry(1, 2, 3.0);
        tableau.setEntry(1, 3, 10.0);
        solver.doIteration(tableau);
        double expectedVal = 1.0 / 3.0;
        double actualVal = tableau.getEntry(1, 2);
        assertEquals(expectedVal, actualVal, solver.epsilon);
        
        // Test case for an unbounded solution
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        try {
            solver.doIteration(tableau);
            fail("UnboundedSolutionException should have been thrown");
        } catch (UnboundedSolutionException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsPhase1Solved() {
        // Test case for phase 1 solved
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumArtificialVariables(0);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, 3.0);
        boolean isPhase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(isPhase1Solved);
        
        // Test case for phase 1 not solved
        tableau.setNumArtificialVariables(2);
        isPhase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        // Test case for optimal solution
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumArtificialVariables(0);
        tableau.setEntry(0, 0, 0.0);
        tableau.setEntry(0, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        boolean isOptimal = solver.isOptimal(tableau);
        assertTrue(isOptimal);
        
        // Test case for non-optimal solution
        tableau.setEntry(0, 2, -1.0);
        isOptimal = solver.isOptimal(tableau);
        assertFalse(isOptimal);
    }

    @Test
    public void testSolvePhase1() {
        // Test case for feasible solution
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumArtificialVariables(2);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, -1.0);
        try {
            solver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException should not have been thrown");
        }
        
        // Test case for infeasible solution
        tableau.setEntry(1, 2, 1.0);
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException should have been thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize() {
        // Test case for optimal solution
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumArtificialVariables(0);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(1, 2, 3.0);
        tableau.setEntry(1, 3, 10.0);
        RealPointValuePair solution = solver.doOptimize();
        double expectedVal = 0.0;
        double actualVal = solution.getValue();
        assertEquals(expectedVal, actualVal, solver.epsilon);
        
        // Test case for unbounded solution
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        try {
            solver.doOptimize();
            fail("UnboundedSolutionException should have been thrown");
        } catch (UnboundedSolutionException e) {
            // Expected exception
        }
        
    }

}
