package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        assertNull(SimplexSolver.getPivotColumn(tableau));
        tableau.setEntry(0, 0, -10);
        tableau.setEntry(0, 1, -5);
        tableau.setEntry(0, 2, -1);
        assertEquals(2, SimplexSolver.getPivotColumn(tableau).intValue());
        tableau.setEntry(0, 2, -10);
        assertEquals(1, SimplexSolver.getPivotColumn(tableau).intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        assertNull(SimplexSolver.getPivotRow(tableau, 0));
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);
        assertEquals(2, SimplexSolver.getPivotRow(tableau, 0).intValue());
        tableau.setEntry(2, 0, 5);
        assertEquals(1, SimplexSolver.getPivotRow(tableau, 0).intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);
        SimplexSolver.doIteration(tableau);
        assertEquals(1, tableau.getEntry(0, 0), 0);
        assertEquals(1, tableau.getEntry(1, 0), 0);
        assertEquals(0, tableau.getEntry(2, 0), 0);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver.solvePhase1(tableau);
        assertFalse(tableau.isOptimal());
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, -1);
        SimplexSolver.solvePhase1(tableau);
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertNotNull(solution);
        assertEquals(0, solution.getPoint().getDimension());
        assertEquals(0, solution.getPoint().getEntry(0), 0);
        assertEquals(0, solution.getValue(), 0);
    }
}