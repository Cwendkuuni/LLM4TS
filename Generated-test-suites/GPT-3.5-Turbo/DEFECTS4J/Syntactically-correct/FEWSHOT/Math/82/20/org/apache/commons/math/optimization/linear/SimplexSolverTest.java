import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimplexSolverTest {
    // Test getPivotColumn() method
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        tableau.setEntry(0, 0, -2.3);
        tableau.setEntry(0, 1, -4.1);
        tableau.setEntry(0, 2, -6.7);
        tableau.setEntry(0, 3, -1.9);
        
        assertEquals(3, SimplexSolver.getPivotColumn(tableau).intValue());
        
        tableau.setEntry(0, 0, -2.3);
        tableau.setEntry(0, 1, -4.1);
        tableau.setEntry(0, 2, 6.7);
        tableau.setEntry(0, 3, 1.9);
        
        assertEquals(1, SimplexSolver.getPivotColumn(tableau).intValue());
    }
    
    // Test getPivotRow() method
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        tableau.setEntry(0, 0, 5.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 4.0);
        tableau.setEntry(3, 0, 8.0);
        tableau.setEntry(4, 0, 1.0);
        
        tableau.setEntry(0, 1, 3.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(3, 1, 5.0);
        tableau.setEntry(4, 1, 6.0);
        
        assertEquals(1, SimplexSolver.getPivotRow(1, tableau).intValue());
        
        tableau.setEntry(0, 1, 3.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(3, 1, 0.0);
        tableau.setEntry(4, 1, 6.0);
        
        assertEquals(4, SimplexSolver.getPivotRow(1, tableau).intValue());
    }
    
    // Test doIteration() method
    
    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(1, 0, 4.0);
        tableau.setEntry(2, 0, 8.0);
        
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(1, 1, 2.0);
        tableau.setEntry(2, 1, 5.0);
        
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(1, 2, 6.0);
        tableau.setEntry(2, 2, 9.0);
        
        tableau.doIteration(tableau);
        
        assertEquals(0.0, tableau.getEntry(1, 2), epsilon);
        assertEquals(0.0, tableau.getEntry(2, 2), epsilon);
    }
    
    // Test isPhase1Solved() method
    
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        tableau.setEntry(0, 0, -3.0);
        tableau.setEntry(0, 1, -4.0);
        tableau.setEntry(0, 2, -5.0);
        
        tableau.setEntry(0, 3, 2.0);
        tableau.setEntry(0, 4, 3.0);
        tableau.setEntry(0, 5, 4.0);
        
        assertEquals(true, SimplexSolver.isPhase1Solved(tableau));
        
        tableau.setEntry(0, 0, -3.0);
        tableau.setEntry(0, 1, -4.0);
        tableau.setEntry(0, 2, 5.0);
        
        tableau.setEntry(0, 3, 2.0);
        tableau.setEntry(0, 4, 3.0);
        tableau.setEntry(0, 5, 4.0);
        
        assertEquals(false, SimplexSolver.isPhase1Solved(tableau));
    }
    
    // Test isOptimal() method
    
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        tableau.setEntry(0, 0, -3.0);
        tableau.setEntry(0, 1, -4.0);
        tableau.setEntry(0, 2, -5.0);
        
        tableau.setEntry(0, 3, 2.0);
        tableau.setEntry(0, 4, 3.0);
        tableau.setEntry(0, 5, 4.0);
        
        assertEquals(false, SimplexSolver.isOptimal(tableau));
        
        tableau.setEntry(0, 0, 3.0);
        tableau.setEntry(0, 1, 4.0);
        tableau.setEntry(0, 2, 5.0);
        
        tableau.setEntry(0, 3, -2.0);
        tableau.setEntry(0, 4, -3.0);
        tableau.setEntry(0, 5, -4.0);
        
        assertEquals(true, SimplexSolver.isOptimal(tableau));
    }
    
    // Test doOptimize() method
    
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        
        assertEquals(expectedMaxValue, solver.doOptimize().getValue(), epsilon);
        
        solver = new SimplexSolver(0.1);
        assertEquals(expectedMaxValue, solver.doOptimize().getValue(), epsilon);
    }
}