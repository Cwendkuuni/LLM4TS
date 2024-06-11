package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class SimplexSolverTest {
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setColumn(0, new double[]{1, 2, 3, 4});
        tableau.setColumn(1, new double[]{5, 6, 7, 8});
        tableau.setColumn(2, new double[]{9, 10, 11, 12});

        SimplexSolver solver = new SimplexSolver();
        Integer pivotCol = solver.getPivotColumn(tableau);

        assertEquals(new Integer(0), pivotCol);
    }

    @Test
    public void testGetPivotColumnAllPositiveEntries() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setColumn(0, new double[]{1, 2, 3, 4});
        tableau.setColumn(1, new double[]{5, 6, 7, 8});
        tableau.setColumn(2, new double[]{9, 10, 11, 12});

        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(1, 0, 5);
        tableau.setEntry(1, 1, 6);
        tableau.setEntry(1, 2, 7);
        tableau.setEntry(2, 0, 9);
        tableau.setEntry(2, 1, 10);
        tableau.setEntry(2, 2, 11);
        tableau.setEntry(3, 0, 4);
        tableau.setEntry(3, 1, 8);
        tableau.setEntry(3, 2, 12);

        SimplexSolver solver = new SimplexSolver();
        Integer pivotCol = solver.getPivotColumn(tableau);

        assertNull(pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setRow(0, new double[]{1, 2, 3, 4});
        tableau.setRow(1, new double[]{5, 6, 7, 8});
        tableau.setRow(2, new double[]{9, 10, 11, 12});

        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(tableau, 1);

        assertEquals(new Integer(2), pivotRow);
    }

    @Test
    public void testGetPivotRowNoPositiveEntries() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setRow(0, new double[]{1, 2, 3, 4});
        tableau.setRow(1, new double[]{5, 6, 7, 8});
        tableau.setRow(2, new double[]{9, 10, 11, 12});

        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        tableau.setEntry(1, 0, -5);
        tableau.setEntry(1, 1, -6);
        tableau.setEntry(1, 2, -7);
        tableau.setEntry(2, 0, -9);
        tableau.setEntry(2, 1, -10);
        tableau.setEntry(2, 2, -11);

        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(tableau, 1);

        assertNull(pivotRow);
    }
    
    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(3, 3);
        tableau.setObjectiveFunction(new double[]{-1, -2, -3});
        tableau.setCostOfPivotVariable(-4);
        tableau.setMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        });
        tableau.setRightHandSide(new double[]{10, 11, 12});

        tableau.dropPhase1Objective();

        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);

        PointValuePair solution = tableau.getSolution();

        assertTrue(Precision.equals(solution.getValue(), -20d, epsilon));
        assertEquals(solution.getPoint(), new double[]{0, 0, 30});
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(3, 3);
        tableau.setObjectiveFunction(new double[]{-1, -2, -3});
        tableau.setCostOfPivotVariable(-4);
        tableau.setMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        });
        tableau.setRightHandSide(new double[]{10, 11, 12});

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        assertTrue(tableau.isOptimal());
    }
    
    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(3, 3);
        tableau.setObjectiveFunction(new double[]{-1, -2, -3});
        tableau.setCostOfPivotVariable(-4);
        tableau.setMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        });
        tableau.setRightHandSide(new double[]{10, 11, 12});

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize(tableau);

        assertTrue(Precision.equals(solution.getValue(), -20d, epsilon));
        assertEquals(solution.getPoint(), new double[]{0, 0, 30});
    }
}