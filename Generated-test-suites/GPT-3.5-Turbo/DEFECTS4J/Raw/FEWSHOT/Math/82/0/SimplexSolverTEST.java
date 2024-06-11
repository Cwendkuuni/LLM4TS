package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;

public class SimplexSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, 2); // objective function row
        tableau.setEntry(1, 0, -5); // constraint row 1
        tableau.setEntry(2, 0, 4); // constraint row 2
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(0), pivotColumn);

        tableau.setEntry(0, 1, -4);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(1), pivotColumn);

        tableau.setEntry(0, 2, 6);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(1), pivotColumn);
    }

    @Test
    public void testPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(1, 0, -2);
        tableau.setEntry(1, 1, 4);
        tableau.setEntry(1, 2, 3);
        tableau.setEntry(1, 3, 2);

        tableau.setEntry(2, 1, -4);
        tableau.setEntry(2, 2, 2);
        tableau.setEntry(2, 3, -2);

        tableau.setEntry(3, 1, -3);
        tableau.setEntry(3, 2, 2);
        tableau.setEntry(3, 3, 6);

        tableau.setEntry(4, 1, -1);
        tableau.setEntry(4, 2, -1);
        tableau.setEntry(4, 3, 1);

        Integer pivotRow = solver.getPivotRow(1, tableau);
        assertEquals(Integer.valueOf(3), pivotRow);

        pivotRow = solver.getPivotRow(2, tableau);
        assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);

        // No artificial variables
        assertTrue(solver.isPhase1Solved(tableau));

        // All artificial variables are zero
        tableau.setEntry(0, 0, -1); // artificial variable
        assertTrue(solver.isPhase1Solved(tableau));

        // Some artificial variables are non-zero
        tableau.setEntry(0, 1, -1); // artificial variable
        assertFalse(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);

        // All entries in objective function row are non-negative
        assertTrue(solver.isOptimal(tableau));

        // Some entries in objective function row are negative
        tableau.setEntry(0, 0, -1); // negative entry
        assertFalse(solver.isOptimal(tableau));

        // All entries in objective function row are non-negative except for the last entry
        tableau.setEntry(0, 1, -1); // negative entry
        assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);

        // With artificial variables and no feasible solution
        tableau.setEntry(0, 0, -1); // artificial variable
        try {
            solver.solvePhase1(tableau);
            fail("OptimizationException expected");
        } catch (OptimizationException e) {
            // Expected
        }

        // With artificial variables and feasible solution
        tableau.setEntry(0, 1, -1); // artificial variable
        tableau.setEntry(0, 2, 1); // artificial variable
        try {
            solver.solvePhase1(tableau);
            assertEquals(1, tableau.getWidth()); // Artificial variables should be removed
        } catch (OptimizationException e) {
            fail("No exception expected");
        }

        // No artificial variables
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("No exception expected");
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);

        // No feasible solution
        tableau.setEntry(0, 0, -1); // objective function row
        try {
            solver.doOptimize();
            fail("OptimizationException expected");
        } catch (OptimizationException e) {
            // Expected
        }

        // Feasible solution
        tableau.setEntry(1, 0, -1); // constraint row
        try {
            RealPointValuePair solution = solver.doOptimize();
            assertEquals(0.0, solution.getValue(), EPSILON);
        } catch (OptimizationException e) {
            fail("No exception expected");
        }
    }
}