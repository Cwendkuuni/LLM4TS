package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        double[][] f = {{1.0, 2.0}, {-2.0, 3.0}};
        Constraint[] constraints = new Constraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-6;

        SimplexSolver solver = new SimplexSolver(epsilon);
        RealPointValuePair solution = solver.doOptimize();

        assertNotNull(solution);
        PointValuePair expectedSolution = new PointValuePair(new double[]{-1.0 / 3.0, 2.0 / 3.0}, -5.0 / 3.0);
        assertArrayEquals(expectedSolution.getPoint().length, solution.getPoint().length, solution.getPoint());
        assertEquals(expectedSolution.getValue(), solution.getValue(), epsilon);
    }

    @Test(expected = OptimizationException.class)
    public void testDoOptimize_UnboundedSolution() {
        double[][] f = {{1.0, 2.0}, {0.0, 0.0}};
        Constraint[] constraints = new Constraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-6;

        SimplexSolver solver = new SimplexSolver(epsilon);
        solver.doOptimize();
    }

    @Test(expected = OptimizationException.class)
    public void testDoOptimize_NoFeasibleSolution() {
        double[][] f = {{1.0, -1.0}, {-1.0, 1.0}};
        Constraint[] constraints = new Constraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0e-6;

        SimplexSolver solver = new SimplexSolver(epsilon);
        solver.doOptimize();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1.0, 2.0, 3.0, 4.0},
            {-1.0, 0.0, -1.0, 5.0},
            {0.0, -1.0, 1.0, 6.0}
        }, new Constraint[0], GoalType.MINIMIZE, false, 1.0e-6);

        Integer pivotColumn = SimplexSolver.this.getPivotColumn(tableau);
        Integer expectedPivotColumn = 2;
        assertEquals(expectedPivotColumn, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1.0, 2.0, 3.0, 4.0},
            {-1.0, 0.0, -1.0, 5.0},
            {0.0, -1.0, 1.0, 6.0}
        }, new Constraint[0], GoalType.MINIMIZE, false, 1.0e-6);

        Integer pivotRow = SimplexSolver.this.getPivotRow(0, tableau);
        Integer expectedPivotRow = 1;
        assertEquals(expectedPivotRow, pivotRow);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1.0, 2.0, 3.0, 4.0},
            {-1.0, 0.0, -1.0, 5.0},
            {0.0, -1.0, 1.0, 6.0},
            {0.0, 0.0, 0.0, 7.0}
        }, new Constraint[0], GoalType.MINIMIZE, false, 1.0e-6);

        boolean isPhase1Solved = SimplexSolver.this.isPhase1Solved(tableau);
        boolean expectedIsPhase1Solved = false;
        assertEquals(expectedIsPhase1Solved, isPhase1Solved);

        tableau = new SimplexTableau(new double[][]{
            {1.0, 2.0, 3.0, 4.0},
            {-1.0, 0.0, -1.0, 5.0},
            {0.0, -1.0, 1.0, 6.0}
        }, new Constraint[0], GoalType.MINIMIZE, false, 1.0e-6);

        isPhase1Solved = SimplexSolver.this.isPhase1Solved(tableau);
        expectedIsPhase1Solved = true;
        assertTrue(isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1.0, 2.0, 3.0, 4.0},
            {-1.0, 0.0, -1.0, 5.0},
            {0.0, -1.0, 1.0, 6.0},
            {0.0, 0.0, 0.0, 7.0}
        }, new Constraint[0], GoalType.MINIMIZE, false, 1.0e-6);

        boolean isOptimal = SimplexSolver.this.isOptimal(tableau);
        boolean expectedIsOptimal = false;
        assertEquals(expectedIsOptimal, isOptimal);

        tableau = new SimplexTableau(new double[][]{
            {1.0 / 3.0, 2.0 / 3.0, 1.0, 4.0 / 3.0},
            {-1.0 / 3.0, -2.0 / 3.0, 1.0, 5.0 / 3.0},
            {0.0, 0.0, 0.0, 0.0}
        }, new Constraint[0], GoalType.MINIMIZE, false, 1.0e-6);

        isOptimal = SimplexSolver.this.isOptimal(tableau);
        expectedIsOptimal = true;
        assertTrue(isOptimal);
    }

}