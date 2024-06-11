import org.apache.commons.math.optimization.linear.*;
import org.apache.commons.math.optimization.*;
import org.apache.commons.math.util.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {
    private static final double EPSILON = 1.0e-6;

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, -4);

        Integer pivotCol = tableau.getPivotColumn();
        assertEquals((Integer)3, pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, 4);
        tableau.setEntry(1, 0, 5);
        tableau.setEntry(1, 1, 6);
        tableau.setEntry(1, 2, 7);
        tableau.setEntry(1, 3, 8);

        Integer pivotRow = tableau.getPivotRow(2);
        assertEquals((Integer)1, pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, 4);
        tableau.setEntry(0, 3, -3);
        tableau.setEntry(1, 0, -2);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -4);
        tableau.setEntry(1, 3, 3);

        tableau.doIteration();
        assertEquals(1, tableau.getHeight());
        assertEquals(4, tableau.getWidth());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnbounded() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, 4);
        tableau.setEntry(0, 3, -3);
        tableau.setEntry(1, 0, -2);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -4);
        tableau.setEntry(1, 3, -3);

        tableau.doIteration();
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setNumArtificialVariables(2);
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, -4);

        boolean solved = tableau.isPhase1Solved();
        assertFalse(solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, -4);

        boolean optimal = tableau.isOptimal();
        assertFalse(optimal);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1Unfeasible() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setNumObjectiveFunctions(2);
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, -4);

        tableau.solvePhase1();
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setNumArtificialVariables(1);
        tableau.setNumObjectiveFunctions(2);
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, -4);

        tableau.solvePhase1();
        assertTrue(tableau.isPhase1Solved());
    }

    @Test
    public void testDoOptimize() {
        double[] f = {2, 3, 1, 4};
        double[][] constraints = {
                {1, 2, 3, 4},
                {-1, -2, -3, -4}
        };
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(f, 0);
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.optimize(objectiveFunction, constraints, GoalType.MAXIMIZE, false);

        assertEquals(0, result.getPoint()[0], EPSILON);
        assertEquals(0, result.getPoint()[1], EPSILON);
        assertEquals(0, result.getPoint()[2], EPSILON);
        assertEquals(0, result.getPoint()[3], EPSILON);
    }
}