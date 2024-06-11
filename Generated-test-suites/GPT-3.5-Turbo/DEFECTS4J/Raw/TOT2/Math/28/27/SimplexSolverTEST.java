package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertEquals(null, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 0);
        assertEquals(null, pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        try {
            simplexSolver.doIteration(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        try {
            simplexSolver.solvePhase1(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        PointValuePair pointValuePair = null;
        try {
            pointValuePair = simplexSolver.doOptimize();
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            assertTrue(false);
        }
        assertTrue(pointValuePair != null);
    }

}