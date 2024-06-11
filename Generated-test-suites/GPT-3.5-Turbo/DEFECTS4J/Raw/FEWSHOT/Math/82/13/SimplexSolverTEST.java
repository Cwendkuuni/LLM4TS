package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SimplexSolverTest {

    // Test getPivotColumn()
    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertNotNull(pivotCol);
    }

    // Test getPivotRow()
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Integer pivotCol = 0;
        Integer pivotRow = solver.getPivotRow(pivotCol, tableau);
        assertNotNull(pivotRow);
    }

    // Test doIteration()
    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        try {
            solver.doIteration(tableau);
        } catch (OptimizationException e) {
            assertFalse(true);
        }
    }

    // Test isPhase1Solved()
    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);
    }

    // Test isOptimal()
    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        boolean optimal = solver.isOptimal(tableau);
        assertTrue(optimal);
    }

    // Test solvePhase1()
    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            assertFalse(true);
        }
    }

    // Test doOptimize()
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        try {
            RealPointValuePair result = solver.doOptimize();
            assertNotNull(result);
        } catch (OptimizationException e) {
            assertFalse(true);
        }
    }
}