package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertNotNull(pivotCol);
        assertEquals(2, pivotCol.intValue());
    }

    @Test
    public void testGetPivotColumn_AllPositiveCoefficients() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertNotNull(pivotCol);
        assertEquals(0, pivotCol.intValue());
    }

    @Test
    public void testGetPivotColumn_NoNegativeCoefficients() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, 4.0);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertNotNull(pivotCol);
        assertEquals(0, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 4.0);
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertNotNull(pivotRow);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testGetPivotRow_AllNonPositiveRatio() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 0, -2.0);
        tableau.setEntry(2, 0, -4.0);
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertTrue(pivotRow == null);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, -1.0);
        tableau.setEntry(1, 1, 3.0);
        tableau.setEntry(2, 1, -2.0);
        tableau.setEntry(0, 2, 0.0);
        solver.doIteration(tableau);
        assertEquals(0, Double.compare(1.0, tableau.getEntry(0, 0)));
        assertEquals(0, Double.compare(3.0, tableau.getEntry(0, 1)));
        assertEquals(0, Double.compare(-1.0, tableau.getEntry(1, 0)));
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Set up tableau to be solved, e.g., the objective function row contains all 0s
        tableau.setEntry(0, 0, 0.0);
        tableau.setEntry(0, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);
    }

    @Test
    public void testIsPhase1Solved_NotSolved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Set up tableau to not be solved, e.g., the objective function row contains negative coefficients
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(!phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Set up tableau to be solved, e.g., the objective function row contains all 0s
        tableau.setEntry(0, 0, 0.0);
        tableau.setEntry(0, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        boolean optimal = solver.isOptimal(tableau);
        assertTrue(optimal);
    }

    @Test
    public void testIsOptimal_NotOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Set up tableau to not be solved, e.g., the objective function row contains negative coefficients
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        boolean optimal = solver.isOptimal(tableau);
        assertTrue(!optimal);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // Set up tableau with artificial variables
        tableau.setNumArtificialVariables(3);
        // Set up the tableau with artificial variables
        solver.solvePhase1(tableau);
        assertEquals(0, Double.compare(0.0, tableau.getEntry(0, 0)));
        // Set up tableau with infeasible solution (non-zero value for W)
        tableau.setEntry(0, tableau.getRhsOffset(), -1.0);
        solver.solvePhase1(tableau); // Expecting NoFeasibleSolutionException
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        RealPointValuePair solution = solver.doOptimize();
        assertNotNull(solution);
    }
}