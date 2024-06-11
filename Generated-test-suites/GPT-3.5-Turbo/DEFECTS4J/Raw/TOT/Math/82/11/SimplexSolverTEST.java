import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case 1: Tableau with all positive values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        Integer result1 = solver.getPivotColumn(tableau);
        assertEquals(null, result1);
        
        // Test case 2: Tableau with negative and positive values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        Integer result2 = solver.getPivotColumn(tableau);
        assertEquals(1, (int)result2);
        
        // Test case 3: Tableau with all negative values
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        Integer result3 = solver.getPivotColumn(tableau);
        assertEquals(null, result3);
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        int col = 1;
        
        // Test case 1: Tableau with positive and negative values
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 2);
        tableau.setEntry(3, 0, -3);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 1, 2);
        tableau.setEntry(3, 1, -3);
        Integer result1 = solver.getPivotRow(col, tableau);
        assertEquals(3, (int)result1);
        
        // Test case 2: Tableau with all positive values
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 2);
        tableau.setEntry(3, 0, 3);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 1, 2);
        tableau.setEntry(3, 1, 3);
        Integer result2 = solver.getPivotRow(col, tableau);
        assertEquals(null, result2);
        
        // Test case 3: Tableau with all negative values
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(2, 0, -2);
        tableau.setEntry(3, 0, -3);
        tableau.setEntry(1, 1, -1);
        tableau.setEntry(2, 1, -2);
        tableau.setEntry(3, 1, -3);
        Integer result3 = solver.getPivotRow(col, tableau);
        assertEquals(null, result3);
    }
    
    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case 1: Tableau with positive and negative values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(1, 2, -3);
        solver.doIteration(tableau);
        double result1 = tableau.getEntry(1, 0);
        assertEquals(0, result1, 0.00001);
        
        // Test case 2: Tableau with all positive values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        solver.doIteration(tableau);
        double result2 = tableau.getEntry(0, 0);
        assertEquals(1, result2, 0.00001);
        
        // Test case 3: Tableau with all negative values
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        solver.doIteration(tableau);
        double result3 = tableau.getEntry(0, 0);
        assertEquals(-1, result3, 0.00001);
    }
    
    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case 1: Tableau with all positive values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        boolean result1 = solver.isPhase1Solved(tableau);
        assertEquals(true, result1);
        
        // Test case 2: Tableau with negative and positive values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        boolean result2 = solver.isPhase1Solved(tableau);
        assertEquals(false, result2);
        
        // Test case 3: Tableau with all negative values
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        boolean result3 = solver.isPhase1Solved(tableau);
        assertEquals(true, result3);
    }
    
    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case 1: Tableau with all positive values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        boolean result1 = solver.isOptimal(tableau);
        assertEquals(true, result1);
        
        // Test case 2: Tableau with negative and positive values
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        boolean result2 = solver.isOptimal(tableau);
        assertEquals(false, result2);
        
        // Test case 3: Tableau with all negative values
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        boolean result3 = solver.isOptimal(tableau);
        assertEquals(true, result3);
    }
    
    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case 1: Feasible solution
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(1, 2, -3);
        assertDoesNotThrow(() -> solver.solvePhase1(tableau));
        
        // Test case 2: Infeasible solution
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(1, 2, 3);
        assertThrows(NoFeasibleSolutionException.class, () -> solver.solvePhase1(tableau));
    }
    
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        
        // Test case 1: Solvable problem
        double[] f = {1, 2, 3};
        double[] goalType = {1};
        boolean[] restrictToNonNegative = {true, true, true};
        SimplexTableau tableau1 = new SimplexTableau(f, null, goalType, restrictToNonNegative, 0.00001);
        assertDoesNotThrow(() -> solver.doOptimize(tableau1));
        
        // Test case 2: Unsolvable problem
        double[] f = {-1, -2, -3};
        double[] goalType = {0};
        boolean[] restrictToNonNegative = {true, true, true};
        SimplexTableau tableau2 = new SimplexTableau(f, null, goalType, restrictToNonNegative, 0.00001);
        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize(tableau2));
    }
}