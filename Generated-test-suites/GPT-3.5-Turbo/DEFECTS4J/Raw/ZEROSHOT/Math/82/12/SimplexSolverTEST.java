import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;
    
    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
        
        tableau.setWidth(5);
        tableau.setNumObjectiveFunctions(3);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 3);
        tableau.setEntry(0, 4, 1);
        
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(2, pivotColumn.intValue());
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = solver.getPivotRow(2, tableau);
        assertNull(pivotRow);
        
        tableau.setHeight(4);
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 3);
        tableau.setEntry(0, 4, 1);
        
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(1, 3, 1);
        tableau.setEntry(1, 4, 0);
        
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(2, 3, 2);
        tableau.setEntry(2, 4, 0);
        
        tableau.setEntry(3, 0, 0);
        tableau.setEntry(3, 1, 0);
        tableau.setEntry(3, 2, 1);
        tableau.setEntry(3, 3, 1);
        tableau.setEntry(3, 4, 1);
        
        pivotRow = solver.getPivotRow(2, tableau);
        assertEquals(3, pivotRow.intValue());
    }
    
    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        solver.doIteration(tableau);
        
        tableau.setHeight(4);
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 3);
        tableau.setEntry(0, 4, 1);
        
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(1, 3, 1);
        tableau.setEntry(1, 4, 0);
        
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(2, 3, 2);
        tableau.setEntry(2, 4, 0);
        
        tableau.setEntry(3, 0, 0);
        tableau.setEntry(3, 1, 0);
        tableau.setEntry(3, 2, 1);
        tableau.setEntry(3, 3, 1);
        tableau.setEntry(3, 4, 1);
        
        solver.doIteration(tableau);
        
        assertEquals(0.0, tableau.getEntry(0, 2), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 2), 0.0);
        assertEquals(0.0, tableau.getEntry(2, 2), 0.0);
        assertEquals(1.0, tableau.getEntry(3, 2), 0.0);
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setHeight(4);
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 3);
        tableau.setEntry(0, 4, 1);
        
        solver.doIteration(tableau);
    }
    
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);
        
        tableau.setHeight(4);
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 3);
        tableau.setEntry(0, 4, 1);
        
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(1, 3, 1);
        tableau.setEntry(1, 4, 0);
        
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(2, 3, 2);
        tableau.setEntry(2, 4, 0);
        
        tableau.setEntry(3, 0, 0);
        tableau.setEntry(3, 1, 0);
        tableau.setEntry(3, 2, 1);
        tableau.setEntry(3, 3, 1);
        tableau.setEntry(3, 4, 1);
        
        phase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
    }
    
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        boolean optimal = solver.isOptimal(tableau);
        assertFalse(optimal);
        
        tableau.setHeight(4);
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 3);
        tableau.setEntry(0, 4, 1);
        
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(1, 3, 1);
        tableau.setEntry(1, 4, 0);
        
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(2, 3, 2);
        tableau.setEntry(2, 4, 0);
        
        tableau.setEntry(3, 0, 0);
        tableau.setEntry(3, 1, 0);
        tableau.setEntry(3, 2, 1);
        tableau.setEntry(3, 3, 1);
        tableau.setEntry(3, 4, 1);
        
        optimal = solver.isOptimal(tableau);
        assertFalse(optimal);
        
        tableau.setEntry(0, 2, -0.1);
        
        optimal = solver.isOptimal(tableau);
        assertTrue(optimal);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setHeight(4);
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 3);
        tableau.setEntry(0, 4, 1);
        
        solver.solvePhase1(tableau);
    }
    
    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();
        assertNotNull(solution);
    }
}