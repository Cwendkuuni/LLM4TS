import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case for empty tableau
        assertNull(solver.getPivotColumn(tableau));
        
        // Add objective function
        tableau.setEntry(0, 1, -5);
        assertEquals(1, solver.getPivotColumn(tableau).intValue());
        assertEquals(-5.0, tableau.getEntry(0, 1), 0.0);
        
        // Add more columns
        tableau.setEntry(0, 2, -10);
        assertEquals(2, solver.getPivotColumn(tableau).intValue());
        assertEquals(-10.0, tableau.getEntry(0, 2), 0.0);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case for empty tableau
        assertNull(solver.getPivotRow(tableau, 0));
        
        // Add equations
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, -2);
        assertEquals(1, solver.getPivotRow(tableau, 1).intValue());
        
        // Add more rows
        tableau.setEntry(2, 1, 4);
        assertEquals(2, solver.getPivotRow(tableau, 1).intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test case for empty tableau
        try {
            solver.doIteration(tableau);
        } catch (Exception e) {
            assertTrue(e instanceof UnboundedSolutionException);
        }
        
        // Add objective function
        tableau.setEntry(0, 1, -5);
        assertEquals(1, solver.getPivotColumn(tableau).intValue());
        
        // Add more columns
        tableau.setEntry(0, 2, -10);
        assertEquals(2, solver.getPivotColumn(tableau).intValue());
        
        // Add rows
        tableau.setEntry(1, 1, 2);
        try {
            solver.doIteration(tableau);
        } catch (Exception e) {
            assertTrue(e instanceof UnboundedSolutionException);
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case for empty tableau
        try {
            solver.solvePhase1(tableau);
        } catch (Exception e) {
            assertTrue(e instanceof NoFeasibleSolutionException);
        }

        // Add artificial variables
        tableau.setEntry(1, 3, 1);
        tableau.setEntry(2, 4, 1);
        try {
            solver.solvePhase1(tableau);
        } catch (Exception e) {
            assertTrue(e instanceof NoFeasibleSolutionException);
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        
        // Test with no constraints
        solver.setFunction(new LinearObjectiveFunction(new double[] {1, 2, 3}, 5));
        PointValuePair solution = solver.doOptimize();
        assertEquals(-1.0, solution.getValue(), 0.0);
        assertArrayEquals(new double[] {1.0, 0.0, 0.0}, solution.getPoint(), 0.0);
        
        // Test with constraints
        LinearConstraintSet constraints = new LinearConstraintSet(
            new LinearConstraint(new double[] {2, 1, 1}, Relationship.LEQ, 7),
            new LinearConstraint(new double[] {-1, 1, 4}, Relationship.LEQ, 4)
        );
        solver.setConstraints(constraints);
        solution = solver.doOptimize();
        assertEquals(3.5, solution.getValue(), 0.0);
        assertArrayEquals(new double[] {2.0, 1.0, 0.0}, solution.getPoint(), 0.0);
    }

}