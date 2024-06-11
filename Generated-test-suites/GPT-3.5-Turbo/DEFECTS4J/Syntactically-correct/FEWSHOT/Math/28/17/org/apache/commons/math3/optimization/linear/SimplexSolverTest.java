package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {
    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setData(new double[][]{{1, -2, 3, -4}, {5, -6, 7, -8}});
        Integer column = solver.getPivotColumn(tableau);
        assertEquals(new Integer(3), column);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setData(new double[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}});
        Integer row = solver.getPivotRow(tableau, 2);
        assertEquals(new Integer(1), row);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setData(new double[][]{{1, 2, 3, 4}, {5, 6, 7, 8}});
        solver.doIteration(tableau);
        double[][] expectedData = new double[][]{{1, 0, -1, -2}, {0, 1, 2, 4}};
        assertTrue(compareTableauData(expectedData, tableau.getData()));
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setData(new double[][]{{-1, 2, -3, 4, -5, 6}});
        solver.solvePhase1(tableau);
        double[][] expectedData = new double[][]{{0, -2, 3, -4, 5, -6}};
        assertTrue(compareTableauData(expectedData, tableau.getData()));
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setData(new double[][]{{1, 2, 3, 4, 5, 6}, {7, 8, 9, 10, 11, 12}});
        tableau.setNumArtificialVariables(2);
        tableau.setRhsOffset(2);
        tableau.setNumObjectiveFunctions(1);
        tableau.setBasicRow(3, 0);
        tableau.setBasicRow(4, 1);
        tableau.setBasicRow(5, 0);
        PointValuePair solution = solver.doOptimize();
        assertEquals(new PointValuePair(new double[]{0, 0, 0, 0, 0, 0}, 0), solution);
    }

    private boolean compareTableauData(double[][] expectedData, double[][] actualData) {
        if (expectedData.length != actualData.length) {
            return false;
        }
        for (int i = 0; i < expectedData.length; i++) {
            if (expectedData[i].length != actualData[i].length) {
                return false;
            }
            for (int j = 0; j < expectedData[i].length; j++) {
                if (Math.abs(expectedData[i][j] - actualData[i][j]) > 1e-6) {
                    return false;
                }
            }
        }
        return true;
    }
}