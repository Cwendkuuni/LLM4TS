package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.nonlinear.convergence.StopCondition;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private double[][] f;
    private int[][] constraints;
    private SimplexTableau tableau;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        f = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        constraints = new int[][]{{0, 1, 1}, {0, 2, 1}, {1, 1, -1}};
        tableau = new SimplexTableau(f, constraints, null, null, 1.0e-6);
    }

    @Test
    public void testDoOptimize() {
        try {
            RealPointValuePair solution = solver.doOptimize();
            PointValuePair pvp = new PointValuePair(new double[]{3.0 / 5.0, 2.0 / 5.0, -1.0 / 5.0}, 1.0 / 5.0 * (2.0 * 3.0 - 1.0 * 5.0));
            assertArrayEquals(new double[]{3.0 / 5.0, 2.0 / 5.0, -1.0 / 5.0}, solution.getPoint(), 1e-6);
            assertEquals(1.0 / 5.0 * (2.0 * 3.0 - 1.0 * 5.0), solution.getValue(), 1e-6);
        } catch (OptimizationException e) {
            e.printStackTrace();
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() {
        int[][] constraints2 = new int[][]{{0, 1, 1}, {0, 2, 1}, {1, 1, 0}};
        SimplexTableau tableau2 = new SimplexTableau(f, constraints2, null, null, 1.0e-6);
        solver.solvePhase1(tableau2);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() {
        int[][] constraints3 = new int[][]{{0, 1, -1}, {0, 2, 1}, {1, 1, 1}};
        SimplexTableau tableau3 = new SimplexTableau(f, constraints3, null, null, 1.0e-6);
        solver.solvePhase1(tableau3);
        solver.doIteration(tableau3);
    }

    @Test
    public void testIsOptimal() {
        tableau.setEntry(1, 2, 0);
        assertTrue(solver.isOptimal(tableau));
        tableau.setEntry(1, 2, -1);
        assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testIsPhase1Solved() {
        tableau.setEntry(1, 2, 1);
        assertFalse(solver.isPhase1Solved(tableau));
        tableau.setEntry(1, 2, 0);
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testGetPivotColumn() {
        tableau.setEntry(1, 1, -1);
        tableau.setEntry(2, 1, -3);
        Integer pivotColumn1 = solver.getPivotColumn(tableau);
        assertEquals(1, pivotColumn1.intValue());
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -1);
        Integer pivotColumn2 = solver.getPivotColumn(tableau);
        assertEquals(2, pivotColumn2.intValue());
    }

    @Test
    public void testGetPivotRow() {
        tableau.setEntry(1, 1, -2);
        tableau.setEntry(2, 1, -1);
        tableau.setEntry(2, 2, 2);
        Integer pivotRow1 = solver.getPivotRow(0, tableau);
        assertEquals(1, pivotRow1.intValue());
        tableau.setEntry(1, 1, 2);
        Integer pivotRow2 = solver.getPivotRow(0, tableau);
        assertEquals(2, pivotRow2.intValue());
    }

    @Test
    public void testDoIteration() {
        tableau.setEntry(1, 1, -1);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, 1);
        Integer pivotCol = new Integer(0);
        Integer pivotRow = new Integer(1);
        solver.doIteration(tableau);
        assertEquals(1.0 / 2.0, tableau.getEntry(1, 1), 1e-6);
        assertEquals(0.0, tableau.getEntry(1, 2), 1e-6);
        assertEquals(0.0, tableau.getEntry(2, 1), 1e-6);
        assertEquals(1.0 / 2.0, tableau.getEntry(2, 2), 1e-6);
        assertEquals(0.0, tableau.getEntry(1, 0), 1e-6);
        assertEquals(0.5, tableau.getEntry(2, 0), 1e-6);
        assertEquals(1.0, tableau.getEntry(2, tableau.getWidth() - 1), 1e-6);

        pivotCol = new Integer(1);
        pivotRow = new Integer(2);
        solver.doIteration(tableau);
        assertEquals(-1.0 / 2.0, tableau.getEntry(1, 1), 1e-6);
        assertEquals(0.0, tableau.getEntry(1, 2), 1e-6);
        assertEquals(0.0, tableau.getEntry(2, 1), 1e-6);
        assertEquals(-1.0 / 2.0, tableau.getEntry(2, 2), 1e-6);
        assertEquals(0.0, tableau.getEntry(1, 0), 1e-6);
        assertEquals(1.0, tableau.getEntry(2, 0), 1e-6);
        assertEquals(1.0, tableau.getEntry(2, tableau.getWidth() - 1), 1e-6);
    }

    @Test
    public void testSolvePhase1() {
        tableau.setEntry(1, 1, -1);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, 1);
        tableau.setEntry(1, tableau.getWidth() - 1, 1);
        tableau.setEntry(2, tableau.getWidth() - 1, 2);

        solver.solvePhase1(tableau);
        assertEquals(1.0, tableau.getEntry(1, 1), 1e-6);
        assertEquals(0.0, tableau.getEntry(1, 2), 1e-6);
        assertEquals(0.0, tableau.getEntry(2, 1), 1e-6);
        assertEquals(-1.0, tableau.getEntry(2, 2), 1e-6);
        assertEquals(0.0, tableau.getEntry(1, tableau.getWidth() - 1), 1e-6);
        assertEquals(1.0, tableau.getEntry(2, tableau.getWidth() - 1), 1e-6);
    }

    @Test
    public void testDiscardArtificialVariables() {
        tableau.setEntry(1, 1, -1);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, 1);
        tableau.setEntry(1, tableau.getWidth() - 1, 1);
        tableau.setEntry(2, tableau.getWidth() - 1, 2);

        solver.solvePhase1(tableau);
        solver.discardArtificialVariables(tableau);

        assertEquals(1.0 / 2.0, tableau.getEntry(1, 1), 1e-6);
        assertEquals(0.0, tableau.getEntry(1, 2), 1e-6);
        assertEquals(0.0, tableau.getEntry(2, 1), 1e-6);
        assertEquals(-1.0 / 2.0, tableau.getEntry(2, 2), 1e-6);
        assertNull(tableau.getEntry(1, tableau.getWidth() - 1));
        assertNull(tableau.getEntry(2, tableau.getWidth() - 1));
    }
}