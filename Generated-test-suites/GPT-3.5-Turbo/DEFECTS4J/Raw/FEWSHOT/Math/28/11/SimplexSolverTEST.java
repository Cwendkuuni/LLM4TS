package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimplexSolverTest {
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        assertEquals(new Integer(0), SimplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        assertEquals(new Integer(0), SimplexSolver.getPivotRow(tableau, 0));
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        // TODO: Add assertions
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        // TODO: Add assertions
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        // TODO: Add assertions
    }
}