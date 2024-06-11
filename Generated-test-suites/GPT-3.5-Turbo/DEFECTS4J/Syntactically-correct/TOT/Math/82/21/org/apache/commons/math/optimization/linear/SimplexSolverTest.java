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
        // Typical use case - there is a column with a negative coefficient
        SimplexTableau tableau = createSimplexTableau();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(2), pivotColumn);
        
        // Edge case - all coefficients are non-negative
        tableau.setEntry(0, 2, 0.0);
        pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
        
        // Error scenario - tableau is empty
        tableau = new SimplexTableau();
        pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }
    
    @Test
    public void testGetPivotRow() {
        // Typical use case - there is a row with a minimum ratio
        SimplexTableau tableau = createSimplexTableau();
        Integer pivotRow = solver.getPivotRow(2, tableau);
        assertEquals(Integer.valueOf(1), pivotRow);
        
        // Edge case - all ratios are non-positive
        tableau.setEntry(1, 2, 0.0);
        pivotRow = solver.getPivotRow(2, tableau);
        assertNull(pivotRow);
        
        // Error scenario - tableau is empty
        tableau = new SimplexTableau();
        pivotRow = solver.getPivotRow(2, tableau);
        assertNull(pivotRow);
    }
    
    @Test
    public void testDoIteration() {
        // Typical use case - perform one iteration
        SimplexTableau tableau = createSimplexTableau();
        solver.doIteration(tableau);
        double expectedPivotVal = 3.0;
        double actualPivotVal = tableau.getEntry(1, 1);
        assertEquals(expectedPivotVal, actualPivotVal, 0.0);
        
        // Error scenario - tableau is empty
        tableau = new SimplexTableau();
        try {
            solver.doIteration(tableau);
            fail("OptimizationException should be thrown");
        } catch (OptimizationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsPhase1Solved() {
        // Typical use case - Phase 1 is solved
        SimplexTableau tableau = createSimplexTableau();
        boolean isPhase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(isPhase1Solved);
        
        // Edge case - there are still artificial variables
        tableau.setEntry(0, 3, -2.0);
        isPhase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(isPhase1Solved);
        
        // Error scenario - tableau is empty
        tableau = new SimplexTableau();
        isPhase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(isPhase1Solved);
    }
    
    @Test
    public void testIsOptimal() {
        // Typical use case - problem is optimal
        SimplexTableau tableau = createSimplexTableau();
        boolean isOptimal = solver.isOptimal(tableau);
        assertFalse(isOptimal);
        
        // Edge case - all objective function coefficients are non-negative
        tableau.setEntry(0, 1, 0.0);
        isOptimal = solver.isOptimal(tableau);
        assertTrue(isOptimal);
        
        // Error scenario - tableau is empty
        tableau = new SimplexTableau();
        isOptimal = solver.isOptimal(tableau);
        assertTrue(isOptimal);
    }
    
    @Test
    public void testSolvePhase1() {
        // Typical use case - Phase 1 is solved
        SimplexTableau tableau = createSimplexTableau();
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
        double expectedEntry = 0.0;
        double actualEntry = tableau.getEntry(0, tableau.getRhsOffset());
        assertEquals(expectedEntry, actualEntry, 0.0);
        
        // Error scenario - W is not zero
        tableau.setEntry(0, tableau.getRhsOffset(), 2.0);
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException should be thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testDoOptimize() {
        // Typical use case - solve the linear problem
        SimplexTableau tableau = createSimplexTableau();
        try {
            RealPointValuePair solution = solver.doOptimize(tableau);
            
            // Check if the solution is correct
            double[] expectedPoint = new double[]{1.0, 2.0, 3.0};
            double[] actualPoint = solution.getPoint();
            assertArrayEquals(expectedPoint, actualPoint, 0.0);
            
            double expectedValue = 4.0;
            double actualValue = solution.getValue();
            assertEquals(expectedValue, actualValue, 0.0);
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
    }
    
    private SimplexTableau createSimplexTableau() {
        double[][] coefficients = {
            {1.0, -1.0, 2.0, 0.0},
            {2.0, 1.0, 1.0, 1.0}
        };
        return new SimplexTableau(coefficients);
    }
    
}