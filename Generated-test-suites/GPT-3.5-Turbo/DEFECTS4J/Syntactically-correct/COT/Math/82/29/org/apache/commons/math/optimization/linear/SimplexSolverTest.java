import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.linear.*;
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
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MAXIMIZE, false, 0);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertNull(pivotCol);
        
        tableau.setEntry(0, 0, -1);
        pivotCol = solver.getPivotColumn(tableau);
        assertEquals(0, (int) pivotCol);

        tableau.setEntry(0, 1, -2);
        pivotCol = solver.getPivotColumn(tableau);
        assertEquals(1, (int) pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MAXIMIZE, false, 0);
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertNull(pivotRow);

        tableau.setEntry(1, 0, 0.5);
        pivotRow = solver.getPivotRow(0, tableau);
        assertEquals(1, (int) pivotRow);

        tableau.setEntry(2, 0, 1);
        pivotRow = solver.getPivotRow(0, tableau);
        assertEquals(1, (int) pivotRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MAXIMIZE, false, 0);
        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MAXIMIZE, false, 0);
        tableau.setEntry(0, 0, -1);

        solver.doIteration(tableau);

        assertEquals(1, tableau.getEntry(0, 0), 0);
        assertEquals(0, tableau.getEntry(0, 1), 0);
        assertEquals(0, tableau.getEntry(0, 2), 0);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MAXIMIZE, false, 0);
        assertTrue(solver.isPhase1Solved(tableau));

        tableau.setEntry(0, 0, -1);
        assertFalse(solver.isPhase1Solved(tableau));

        tableau.setEntry(0, 0, 1);
        assertFalse(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[]{1, 2, 3}, new LinearConstraint[0], GoalType.MAXIMIZE, false, 0);
        assertTrue(solver.isOptimal(tableau));

        tableau.setEntry(0, 0, -1);
        assertFalse(solver.isOptimal(tableau));

        tableau.setEntry(0, 0, 1);
        assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2, 3}, 0);
        LinearConstraint constraint = new LinearConstraint(new double[]{1, -1, 1}, Relationship.LEQ, 1);
        SimplexTableau tableau = new SimplexTableau(f, new LinearConstraint[]{constraint}, GoalType.MAXIMIZE, false, 0);

        solver.solvePhase1(tableau);

        assertEquals(-1, tableau.getEntry(0, 0), 0);
        assertEquals(1, tableau.getEntry(0, 1), 0);
        assertEquals(0, tableau.getEntry(0, 2), 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2, 3}, 0);
        LinearConstraint constraint = new LinearConstraint(new double[]{1, 1, 1}, Relationship.LEQ, 1);
        SimplexTableau tableau = new SimplexTableau(f, new LinearConstraint[]{constraint}, GoalType.MAXIMIZE, false, 0);

        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2, 3}, 0);
        LinearConstraint constraint = new LinearConstraint(new double[]{1, 1, 1}, Relationship.LEQ, 1);
        SimplexTableau tableau = new SimplexTableau(f, new LinearConstraint[]{constraint}, GoalType.MAXIMIZE, false, 0);

        RealPointValuePair solution = solver.doOptimize(tableau);

        assertArrayEquals(new double[]{0, 0, 1}, solution.getPoint(), 0);
        assertEquals(3, solution.getValue(), 0);
    }
}