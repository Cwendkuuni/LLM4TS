import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {
    
    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {
            {1.0, -2.0, 3.0, 0.0},
            {4.0, 5.0, -6.0, 0.0},
            {7.0, 8.0, 9.0, 0.0}
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(2), pivotColumn);
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {
            {1.0, -2.0, 3.0, 0.0},
            {4.0, 5.0, -6.0, 0.0},
            {7.0, 8.0, 9.0, 0.0}
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        Integer pivotRow = solver.getPivotRow(2, tableau);
        assertEquals(Integer.valueOf(2), pivotRow);
    }
    
    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {
            {1.0, -2.0, 3.0, 0.0},
            {4.0, 5.0, -6.0, 0.0},
            {7.0, 8.0, 9.0, 0.0}
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        boolean isPhase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(isPhase1Solved);
    }
    
    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {
            {1.0, -2.0, 3.0, 0.0},
            {4.0, 5.0, -6.0, 0.0},
            {7.0, 8.0, 9.0, 0.0}
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        boolean isOptimal = solver.isOptimal(tableau);
        assertFalse(isOptimal);
    }
    
    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {
            {1.0, -2.0, 3.0, 0.0},
            {4.0, 5.0, -6.0, 0.0},
            {7.0, 8.0, 9.0, 0.0}
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        try {
            solver.solvePhase1(tableau);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {
            {1.0, -2.0, 3.0, 0.0},
            {4.0, 5.0, -6.0, 0.0},
            {7.0, 8.0, 9.0, 0.0}
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        try {
            RealPointValuePair result = solver.doOptimize();
            assertNotNull(result);
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
    }
    
}