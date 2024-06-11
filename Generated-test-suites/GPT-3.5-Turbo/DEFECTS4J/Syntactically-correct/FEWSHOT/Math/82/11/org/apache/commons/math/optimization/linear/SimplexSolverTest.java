package org.apache.commons.math.optimization.linear;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    private static final double EPSILON = 1.0E-6;

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotCol = null;

        // Test when the objective function row has all positive coefficients
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, 4.0);
        pivotCol = SimplexSolver.getPivotColumn(tableau);
        assertNull(pivotCol);

        // Test when the objective function row has a negative coefficient
        tableau.setEntry(0, 3, -4.0);
        pivotCol = SimplexSolver.getPivotColumn(tableau);
        assertEquals(3, pivotCol.intValue());

        // Test when the objective function row has multiple negative coefficients
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        pivotCol = SimplexSolver.getPivotColumn(tableau);
        assertEquals(2, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = null;

        // Test when the column has all negative entries
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(2, 0, -2.0);
        tableau.setEntry(3, 0, -3.0);
        pivotRow = SimplexSolver.getPivotRow(0, tableau);
        assertNull(pivotRow);

        // Test when the column has a positive entry
        tableau.setEntry(1, 0, 1.0);
        pivotRow = SimplexSolver.getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());

        // Test when the column has multiple positive entries
        tableau.setEntry(2, 0, 2.0);
        pivotRow = SimplexSolver.getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        boolean phase1Solved = false;

        // Test when no artificial variables exist
        phase1Solved = SimplexSolver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);

        // Test when some artificial variables exist
        tableau.setNumArtificialVariables(2);
        phase1Solved = SimplexSolver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);

        // Test when all artificial variables are non-negative
        tableau.setEntry(0, 2, 1.0);
        tableau.setEntry(0, 3, 2.0);
        tableau.setEntry(0, 4, 3.0);
        tableau.setEntry(0, 5, 4.0);
        phase1Solved = SimplexSolver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);

        // Test when one of the artificial variables is negative
        tableau.setEntry(0, 5, -4.0);
        phase1Solved = SimplexSolver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);

        // Test when all artificial variables are negative
        tableau.setEntry(0, 3, -2.0);
        tableau.setEntry(0, 4, -3.0);
        phase1Solved = SimplexSolver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        boolean optimal = false;

        // Test when no artificial variables exist
        optimal = SimplexSolver.isOptimal(tableau);
        assertTrue(optimal);

        // Test when some artificial variables exist
        tableau.setNumArtificialVariables(2);
        optimal = SimplexSolver.isOptimal(tableau);
        assertFalse(optimal);

        // Test when all entries in the objective function row are non-negative
        tableau.setEntry(0, 2, 1.0);
        tableau.setEntry(0, 3, 2.0);
        tableau.setEntry(0, 4, 3.0);
        tableau.setEntry(0, 5, 4.0);
        optimal = SimplexSolver.isOptimal(tableau);
        assertTrue(optimal);

        // Test when some entries in the objective function row are negative
        tableau.setEntry(0, 3, -2.0);
        optimal = SimplexSolver.isOptimal(tableau);
        assertFalse(optimal);

        // Test when all entries in the objective function row are negative
        tableau.setEntry(0, 2, -1.0);
        tableau.setEntry(0, 4, -3.0);
        tableau.setEntry(0, 5, -4.0);
        optimal = SimplexSolver.isOptimal(tableau);
        assertFalse(optimal);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();

        // Test when the phase 1 problem has a feasible solution
        tableau.setNumArtificialVariables(2);
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(2, 1, 1.0);
        tableau.setEntry(2, 3, 1.0);
        tableau.setEntry(3, 2, -3.0);
        tableau.setEntry(3, 3, -4.0);
        tableau.setEntry(4, 4, 1.0);
        SimplexSolver simplexSolver = new SimplexSolver(EPSILON);
        simplexSolver.solvePhase1(tableau);

        // Test when the phase 1 problem has no feasible solution
        tableau.setEntry(0, 0, 1.0);
        simplexSolver = new SimplexSolver(EPSILON);
        try {
            simplexSolver.solvePhase1(tableau);
            fail("Expected NoFeasibleSolutionException to be thrown");
        } catch (NoFeasibleSolutionException e) {
            // Ignore the exception
        }
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setEpsilon(EPSILON);

        // Test when the optimal solution is found
        RealPointValuePair solution = simplexSolver.doOptimize();
        assertNotNull(solution);

        // Test when the problem does not have a feasible solution
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        simplexSolver = new SimplexSolver();
        simplexSolver.setEpsilon(EPSILON);
        try {
            solution = simplexSolver.doOptimize();
            fail("Expected NoFeasibleSolutionException to be thrown");
        } catch (NoFeasibleSolutionException e) {
            // Ignore the exception
        }
    }
}