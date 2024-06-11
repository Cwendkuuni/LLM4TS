package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.NoFeasibleSolutionException;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;
import org.junit.Before;

public class SimplexSolverTest {

    SimplexSolver solver;
    SimplexTableau tableau;

    @Before
    public void setup() {
        solver = new SimplexSolver();
        tableau = new SimplexTableau(new double[][]{
            {-5.0, 2.0, 1.0, 3.0, 1.0},
            {1.0, -3.0, 2.0, 1.0, 1.0},
            {1.0, 3.0, -1.0, 2.0, 1.0},
            {0.0, 0.0, 0.0, 0.0, 2.0}
        }, new Object[][]{
            new Object[]{1, 1},
            new Object[]{1, 2},
            new Object[]{1, 3},
            new Object[]{1, 4},
            new Object[]{1, 5}
        }, LinearOptimizer.GOAL_TYPE_MINIMIZE, false, 1.0e-6);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableauCopy = new SimplexTableau(tableau);
        Integer pivotCol = solver.getPivotColumn(tableauCopy);
        assertEquals(2, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableauCopy = new SimplexTableau(tableau);
        Integer pivotRow = solver.getPivotRow(2, tableauCopy);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIterationThrowsExceptionWhenNoSolution() {
        SimplexTableau tableauCopy = new SimplexTableau(tableau);
        assertThrows(UnboundedSolutionException.class, () -> solver.doIteration(tableauCopy));
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableauCopy = new SimplexTableau(tableau);
        solver.doIteration(tableauCopy);
        assertEquals(1.0 / 3.0, tableauCopy.getEntry(1, 2), 1.0e-6);
        assertEquals(1.0, tableauCopy.getEntry(0, 2), 1.0e-6);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableauCopy1 = new SimplexTableau(tableau);
        SimplexTableau tableauCopy2 = new SimplexTableau(tableau);
        solver.doIteration(tableauCopy1);
        assertFalse(solver.isPhase1Solved(tableauCopy1));
        solver.doIteration(tableauCopy2);
        assertTrue(solver.isPhase1Solved(tableauCopy2));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableauCopy1 = new SimplexTableau(tableau);
        SimplexTableau tableauCopy2 = new SimplexTableau(tableau);
        solver.doIteration(tableauCopy1);
        solver.doIteration(tableauCopy2);
        solver.doIteration(tableauCopy1);
        assertFalse(solver.isOptimal(tableauCopy1));
        assertTrue(solver.isOptimal(tableauCopy2));
    }

    @Test
    public void testSolvePhase1ThrowsExceptionWhenUnboundedSolution() {
        SimplexTableau tableauCopy = new SimplexTableau(new double[][]{
            {-5.0, 2.0, 1.0, 3.0, 1.0},
            {1.0, -3.0, 2.0, 1.0, 1.0},
            {1.0, 3.0, -1.0, 2.0, 1.0},
            {0.0, 0.0, 0.0, 0.0, 2.0},
            {0.0, 0.0, 0.0, 0.0, 0.0}
        }, new Object[][]{
            new Object[]{1, 1},
            new Object[]{1, 2},
            new Object[]{1, 3},
            new Object[]{1, 4},
            new Object[]{1, 5},
            new Object[]{1, 6}
        }, LinearOptimizer.GOAL_TYPE_MINIMIZE, false, 1.0e-6);
        assertThrows(UnboundedSolutionException.class, () -> solver.solvePhase1(tableauCopy));
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableauCopy = new SimplexTableau(tableau);
        solver.solvePhase1(tableauCopy);
        assertEquals(1.0 / 3.0, tableauCopy.getEntry(1, 2), 1.0e-6);
        assertEquals(1.0, tableauCopy.getEntry(0, 2), 1.0e-6);
    }

    @Test
    public void testDoOptimize() {
        PointValuePair solution = solver.doOptimize();
        RealPointValuePair expectedSolution = new RealPointValuePair(new double[]{2.0 / 3.0, 1.0, 1.0, 1.0, 1.0}, -5.0 / 3.0);
        assertEquals(expectedSolution.getPoint(), solution.getPoint(), 1.0e-6);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1.0e-6);
    }
}