import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        // Test when SimplexTableau is null
        assertNull(simplexSolver.getPivotColumn(null));

        // Test when SimplexTableau has no negative coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        assertNull(simplexSolver.getPivotColumn(tableau1));

        // Test when SimplexTableau has a negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, 1);
        tableau2.setEntry(0, 1, -2);
        tableau2.setEntry(0, 2, 3);
        assertEquals(1, (int) simplexSolver.getPivotColumn(tableau2));
    }

    @Test
    public void testGetPivotRow() {
        // Test when SimplexTableau does not have any rows for the given column
        SimplexTableau tableau1 = new SimplexTableau();
        assertNull(simplexSolver.getPivotRow(tableau1, 0));

        // Test when SimplexTableau has one row for the given column
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, 1);
        tableau2.setEntry(0, 1, 2);
        assertEquals(0, (int) simplexSolver.getPivotRow(tableau2, 1));

        // Test when SimplexTableau has multiple rows for the given column
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, 1);
        tableau3.setEntry(0, 1, -2);
        tableau3.setEntry(1, 0, 3);
        tableau3.setEntry(1, 1, -2);
        assertEquals(1, (int) simplexSolver.getPivotRow(tableau3, 1));
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        // Test when pivotRow is null
        SimplexTableau tableau1 = new SimplexTableau();
        assertFalse(tableau1.isOptimal());
        try {
            simplexSolver.doIteration(tableau1);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Exception expected
        }

        // Test when pivotRow is not null
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, 1);
        tableau2.setEntry(0, 1, 2);
        tableau2.setEntry(1, 0, 3);
        tableau2.setEntry(1, 1, 4);
        simplexSolver.doIteration(tableau2);
        assertTrue(tableau2.isOptimal());
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test when W is not zero
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        try {
            simplexSolver.solvePhase1(tableau1);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Exception expected
        }

        // Test when W is zero
        SimplexTableau tableau2 = new SimplexTableau();
        simplexSolver.solvePhase1(tableau2);
        assertFalse(tableau2.isOptimal());
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test when there is no feasible solution
        SimplexSolver simplexSolver1 = new SimplexSolver();
        simplexSolver1.setFunction(new LinearObjectiveFunction(new double[]{1, 2}, 0));
        simplexSolver1.setConstraints(new LinearConstraint[]{new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 1)});
        try {
            simplexSolver1.doOptimize();
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Exception expected
        }

        // Test when there is a feasible solution
        SimplexSolver simplexSolver2 = new SimplexSolver();
        simplexSolver2.setFunction(new LinearObjectiveFunction(new double[]{1, 2}, 0));
        simplexSolver2.setConstraints(new LinearConstraint[]{new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 0)});
        PointValuePair solution = simplexSolver2.doOptimize();
        assertNotNull(solution);
    }

}