package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertNull(pivotCol);

        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -1.5);
        tableau.setEntry(0, 2, 0.0);
        pivotCol = solver.getPivotColumn(tableau);
        assertEquals(0, pivotCol.intValue());

        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -1.5);
        tableau.setEntry(0, 2, -3.0);
        pivotCol = solver.getPivotColumn(tableau);
        assertEquals(2, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertNull(pivotRow);

        tableau.setEntry(1, 0, -4.0);
        tableau.setEntry(1, 1, 3.0);
        tableau.setEntry(1, 2, 0.0);
        pivotRow = solver.getPivotRow(tableau, 0);
        assertEquals(1, pivotRow.intValue());

        tableau.setEntry(1, 0, -4.0);
        tableau.setEntry(1, 1, 3.0);
        tableau.setEntry(1, 2, -2.0);
        pivotRow = solver.getPivotRow(tableau, 0);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        assertThrows(MaxCountExceededException.class, () -> {
            solver.doIteration(tableau);
        });

        tableau.setWidth(3);
        tableau.setHeight(3);
        tableau.setEntry(1, 1, -1.0);
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(2, 1, 1.0);
        tableau.setEntry(2, 2, -1.0);
        solver.doIteration(tableau);
        assertEquals(1.0, tableau.getEntry(1, 1), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 2), 0.0);
        assertEquals(0.0, tableau.getEntry(2, 1), 0.0);
        assertEquals(2.0, tableau.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau();
        assertThrows(MaxCountExceededException.class, () -> {
            solver.solvePhase1(tableau);
        });

        tableau.setWidth(3);
        tableau.setHeight(3);
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(1, 0, -2.0);
        tableau.setEntry(2, 0, -2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(1, 2, -1.0);
        tableau.setEntry(2, 2, 1.0);
        solver.solvePhase1(tableau);
        assertFalse(tableau.isOptimal());
        assertEquals(2, tableau.getPivotColumn().intValue());
        assertEquals(1, tableau.getPivotRow().intValue());

        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, -3.0);
        tableau.setEntry(2, 1, 0.0);
        tableau.setEntry(2, 2, 1.0);
        solver.solvePhase1(tableau);
        assertTrue(tableau.isOptimal());
        assertEquals(2, tableau.getPivotColumn().intValue());
        assertEquals(2, tableau.getPivotRow().intValue());
    }

    @Test
    public void testDoOptimize() {
        assertThrows(MaxCountExceededException.class, () -> {
            solver.doOptimize();
        });

        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[] { 1.0 }, 0.0);
        LinearConstraint constraint = new LinearConstraint(new double[] { 2.0 }, Relationship.LEQ, 1.0);
        LinearConstraint[] constraints = new LinearConstraint[] { constraint };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair optimum = solver.optimize(function, constraints, GoalType.MAXIMIZE, true);
        assertArrayEquals(new double[] { 0.5 }, optimum.getPoint(), 0.001);
        assertEquals(0.5, optimum.getValue(), 0.001);
    }

}