import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.linear.*;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.5);
        assertEquals(2, simplexSolver.getPivotColumn(tableau).intValue());
    }

    @Test
    public void testGetPivotColumn_NoNegativeCoefficient() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.5);
        assertNull(simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 3.5);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(1, 1, 3.0);
        tableau.setEntry(2, 1, 4.5);
        tableau.setEntry(0, 2, 1.0);
        tableau.setEntry(1, 2, -1.0);
        tableau.setEntry(2, 2, 1.0);
        assertEquals(2, simplexSolver.getPivotRow(1, tableau).intValue());
    }

    @Test
    public void testGetPivotRow_NoPositiveEntry() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(1, 0, -2.0);
        tableau.setEntry(2, 0, -3.5);
        assertNull(simplexSolver.getPivotRow(0, tableau));
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, -2.0);
        tableau.setEntry(2, 0, 3.5);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(1, 1, -3.0);
        tableau.setEntry(2, 1, 4.5);
        tableau.setEntry(0, 2, 1.0);
        tableau.setEntry(1, 2, -1.0);
        tableau.setEntry(2, 2, 1.0);
        simplexSolver.doIteration(tableau);
        assertEquals(1.0, tableau.getEntry(0, 0), 0.0001);
        assertEquals(0.0, tableau.getEntry(0, 1), 0.0001);
        assertEquals(1.5, tableau.getEntry(0, 2), 0.0001);
        assertEquals(-7.0, tableau.getEntry(1, 0), 0.0001);
        assertEquals(5.0, tableau.getEntry(1, 1), 0.0001);
        assertEquals(-10.5, tableau.getEntry(1, 2), 0.0001);
        assertEquals(-1.0, tableau.getEntry(2, 0), 0.0001);
        assertEquals(1.0, tableau.getEntry(2, 1), 0.0001);
        assertEquals(-1.0, tableau.getEntry(2, 2), 0.0001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.5);
        tableau.setEntry(1, 0, 2.0);
        assertTrue(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsPhase1Solved_NotSolved() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, 3.5);
        tableau.setEntry(1, 0, 2.0);
        assertFalse(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.5);
        tableau.setEntry(1, 0, 2.0);
        assertFalse(simplexSolver.isOptimal(tableau));
    }

    @Test
    public void testIsOptimal_Optimal() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.5);
        tableau.setEntry(1, 0, -2.0);
        assertTrue(simplexSolver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(1, 0, -2.0);
        tableau.setEntry(1, 1, 3.0);
        tableau.setEntry(2, 0, 3.5);
        tableau.setEntry(2, 1, 4.5);
        tableau.setEntry(0, 2, 1.0);
        tableau.setEntry(1, 2, -1.0);
        tableau.setEntry(2, 2, 1.0);

        simplexSolver.solvePhase1(tableau);
        assertEquals(1.0, tableau.getEntry(0, 0), 0.0001);
        assertEquals(0.0, tableau.getEntry(0, 1), 0.0001);
        assertEquals(-0.5, tableau.getEntry(0, 2), 0.0001);
        assertEquals(0.0, tableau.getEntry(1, 0), 0.0001);
        assertEquals(1.0, tableau.getEntry(1, 1), 0.0001);
        assertEquals(-0.5, tableau.getEntry(1, 2), 0.0001);
        assertEquals(0.0, tableau.getEntry(2, 0), 0.0001);
        assertEquals(0.0, tableau.getEntry(2, 1), 0.0001);
        assertEquals(1.0, tableau.getEntry(2, 2), 0.0001);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(1, 0, 0.0);
        tableau.setEntry(1, 1, 0.0);
        tableau.setEntry(2, 0, 0.0);
        tableau.setEntry(2, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        tableau.setEntry(1, 2, 0.0);
        tableau.setEntry(2, 2, 0.0);

        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        double[] f = {1, -3};
        double[][] constraints = {{3, 2}, {2, 5}};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 0.0001;

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

        RealPointValuePair solution = simplexSolver.doOptimize();

        assertEquals(17.0, solution.getValue(), epsilon);
        assertEquals(1.0, solution.getPoint()[0], epsilon);
        assertEquals(4.0, solution.getPoint()[1], epsilon);
    }

}