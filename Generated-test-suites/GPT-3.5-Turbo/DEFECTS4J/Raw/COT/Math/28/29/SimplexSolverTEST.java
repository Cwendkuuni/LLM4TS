import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Test case 1
        tableau.setEntry(0, 0, 1.0);
        assertEquals(Integer.valueOf(0), solver.getPivotColumn(tableau));
        // Test case 2
        tableau.setEntry(0, 1, -2.0);
        assertEquals(Integer.valueOf(1), solver.getPivotColumn(tableau));
        // Test case 3
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(0, 1, -1.0);
        assertEquals(Integer.valueOf(1), solver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Test case 1
        tableau.setEntry(1, 0, 5.0);
        tableau.setEntry(2, 0, 4.0);
        assertEquals(Integer.valueOf(1), solver.getPivotRow(tableau, 0));
        // Test case 2
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(2, 1, 1.0);
        assertEquals(Integer.valueOf(1), solver.getPivotRow(tableau, 1));
        // Test case 3
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(1, 1, -1.0);
        tableau.setEntry(2, 0, 1.0);
        tableau.setEntry(2, 1, -2.0);
        assertEquals(Integer.valueOf(2), solver.getPivotRow(tableau, 1));
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Test case 1
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(1, 0, 3.0);
        tableau.divideRow(1, 3.0);
        assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
        // Test case 2
        tableau.subtractRow(0, 1, 2.0);
        assertEquals(0.0, tableau.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Test case 1
        tableau.setEntry(0, 0, 1.0);
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException not thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
        // Test case 2
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, -2.0);
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException not thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
        // Test case 3
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, -1.0);
        try {
            solver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException thrown");
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        // Test case 1
        PointValuePair result1 = solver.doOptimize();
        assertNull(result1);
        // Test case 2
        solver.setFunction(new LinearObjectiveFunction(new double[]{1.0, 2.0}, 0.0));
        solver.setConstraints(new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0)});
        solver.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result2 = solver.doOptimize();
        assertNotNull(result2);
    }

}