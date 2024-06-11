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
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case where there are negative coefficients in the objective function row
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        
        Integer expected = 2;
        Integer actual = solver.getPivotColumn(tableau);
        assertEquals(expected, actual);
        
        // Test case where all coefficients in the objective function row are non-negative
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        
        expected = null;
        actual = solver.getPivotColumn(tableau);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        int pivotColumn = 1;
        
        // Test case where there are positive ratios
        tableau.setEntry(1, pivotColumn, 2.0);
        tableau.setEntry(1, tableau.getWidth() - 1, 6.0);
        tableau.setEntry(2, pivotColumn, 1.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 3.0);
        
        Integer expected = 2;
        Integer actual = solver.getPivotRow(pivotColumn, tableau);
        assertEquals(expected, actual);
        
        // Test case where all ratios are negative or zero
        tableau.setEntry(1, pivotColumn, -2.0);
        tableau.setEntry(1, tableau.getWidth() - 1, -6.0);
        tableau.setEntry(2, pivotColumn, 0.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 0.0);
        
        expected = null;
        actual = solver.getPivotRow(pivotColumn, tableau);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case with valid pivot column and pivot row
        tableau.setEntry(1, 1, 2.0);
        tableau.setEntry(1, 2, 3.0);
        tableau.setEntry(1, tableau.getWidth() - 1, 6.0);
        
        tableau.setEntry(2, 1, 1.0);
        tableau.setEntry(2, 2, 2.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 3.0);
        
        solver.doIteration(tableau);
        
        // Assert that pivot element is 1
        double expected = 1.0;
        double actual = tableau.getEntry(0, 1);
        assertEquals(expected, actual, 0.0001);
        
        // Assert that rest of pivot column is 0
        expected = 0.0;
        actual = tableau.getEntry(2, 1);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolutionException() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

        // Test case with no pivot row
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(1, 2, -3.0);
        tableau.setEntry(1, tableau.getWidth() - 1, -6.0);
        
        tableau.setEntry(2, 1, -1.0);
        tableau.setEntry(2, 2, -2.0);
        tableau.setEntry(2, tableau.getWidth() - 1, -3.0);
        
        solver.doIteration(tableau);
    }
    
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case where Phase 1 is solved
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, 3.0);
        
        boolean expected = true;
        boolean actual = solver.isPhase1Solved(tableau);
        assertEquals(expected, actual);
        
        // Test case where Phase 1 is not solved
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, -2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, -3.0);
        
        expected = false;
        actual = solver.isPhase1Solved(tableau);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case where problem is at optimal state
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, 3.0);
        
        boolean expected = true;
        boolean actual = solver.isOptimal(tableau);
        assertEquals(expected, actual);
        
        // Test case where problem is not at optimal state
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, -2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, -3.0);
        
        expected = false;
        actual = solver.isOptimal(tableau);
        assertEquals(expected, actual);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case with no feasible solution
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, 3.0);
        
        solver.solvePhase1(tableau);
    }
    
    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case with no artificial variables and problem is at optimal state
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, 3.0);
        
        RealPointValuePair expected = new RealPointValuePair(null, null);
        RealPointValuePair actual = solver.doOptimize(tableau);
        assertEquals(expected, actual);
    }

}