package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.util.MathUtils;

import org.apache.commons.cli.SimplexSolver;

/**
 * Tests for the SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Tests the getPivotColumn method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetPivotColumn() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        Integer pivotCol = SimplexSolver.instance().getPivotColumn(tableau);
        assertTrue(pivotCol.intValue() == 2);
    }

    /**
     * Tests the getPivotRow method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetPivotRow() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        Integer pivotRow = SimplexSolver.instance().getPivotRow(2, tableau);
        assertTrue(pivotRow.intValue() == 0);
    }

    /**
     * Tests the doIteration method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDoIteration() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver(1.0e-9);
        solver.doIteration(tableau);
        double[][] expectedTableau = new double[][]{{1.0 / 3.0, -1.0 / 3.0, 1.0}, {0.0, 1.0 / 3.0, 2.0 / 3.0}, {0.0, 0.0, 1.0}};
        assertArrayEquals(expectedTableau, tableau.getData(), 0.0);
    }

    /**
     * Tests the isPhase1Solved method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsPhase1Solved() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        assertTrue(SimplexSolver.instance().isPhase1Solved(tableau) == false);

        tableau.setEntry(0, 2, -1.0e6);
        assertTrue(SimplexSolver.instance().isPhase1Solved(tableau) == true);
    }

    /**
     * Tests the isOptimal method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIsOptimal() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        assertTrue(SimplexSolver.instance().isOptimal(tableau) == false);

        tableau.setEntry(0, 2, 0.0);
        tableau.setEntry(1, 2, 0.0);
        tableau.setEntry(2, 2, 0.0);
        assertTrue(SimplexSolver.instance().isOptimal(tableau) == true);
    }

    /**
     * Tests the solvePhase1 method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        solver.solvePhase1(tableau);
        double[][] expectedTableau = new double[][]{{1.0 / 3.0, -1.0 / 3.0, 1.0}, {0.0, 1.0 / 3.0, 2.0 / 3.0}, {0.0, 0.0, 1.0}};
        assertArrayEquals(expectedTableau, tableau.getData(), 0.0);
    }

    /**
     * Tests the doOptimize method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDoOptimize() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        RealPointValuePair solution = solver.doOptimize();
        double[] expectedX = new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0};
        double[] expectedF = new double[]{0.0};
        PointValuePair expectedPointValuePair = new PointValuePair(expectedX, expectedF);
        assertArrayEquals(expectedX, solution.getPoint(), 0.0);
        assertEquals(expectedF[0], solution.getValue(), 0.0);
    }

    /**
     * Tests the doOptimize method with unbounded solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnbounded() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        tableau.setEntry(2, 2, Double.POSITIVE_INFINITY);
        solver.doOptimize();
    }

    /**
     * Tests the doOptimize method with no feasible solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasible() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        tableau.setEntry(0, tableau.getWidth() - 1, Double.NEGATIVE_INFINITY);
        solver.doOptimize();
    }

    /**
     * Tests the doOptimize method with maximum iteration count exceeded.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testDoOptimizeMaxIterations() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, 2.0}, {-2.0, 3.0, 1.0}}, 3, 3, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        tableau.setMaxIterations(0);
        solver.doOptimize();
    }
}