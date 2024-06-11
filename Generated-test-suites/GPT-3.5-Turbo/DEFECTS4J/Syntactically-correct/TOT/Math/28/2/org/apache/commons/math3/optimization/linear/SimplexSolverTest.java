import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @After
    public void tearDown() {
        simplexSolver = null;
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        tableau.setEntry(0, 3, -4.0);

        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);

        assertNotNull(pivotColumn);
        assertEquals(3, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(2, 0, 2.0);
        tableau.setEntry(3, 0, 3.0);
        tableau.setEntry(4, 0, 4.0);

        Integer pivotRow = simplexSolver.getPivotRow(tableau, 0);

        assertNotNull(pivotRow);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(2, 0, 2.0);
        tableau.setEntry(3, 0, 3.0);
        tableau.setEntry(4, 0, 4.0);

        try {
            simplexSolver.doIteration(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(2, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(3, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(4, 0), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 3.0);
        tableau.setEntry(3, 0, 4.0);

        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);

        try {
            simplexSolver.solvePhase1(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Exception should not be thrown");
        }

        assertTrue(tableau.isOptimal());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1UnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);

        simplexSolver.solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 3.0);

        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);

        try {
            PointValuePair solution = simplexSolver.doOptimize();
            assertNotNull(solution);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Exception should not be thrown");
        }
    }

}