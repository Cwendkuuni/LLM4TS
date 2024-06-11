package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealPointValuePair;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

import org.apache.commons.cli.SimplexSolver;
import org.apache.commons.cli.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testSolver() {
        double[][] f = {{1.0, 1.0, 1.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 0.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize(new LinearObjectiveFunction(new Array2DRowRealMatrix(f), null),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
        RealVector expectedSolution = new ArrayRealVector(new double[]{3.0, 1.0, 1.0});
        assertArrayEquals(expectedSolution.toArray(), solution.getPoint().toArray(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        double[][] f = {{1.0, 1.0, -1.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 0.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(new LinearObjectiveFunction(new Array2DRowRealMatrix(f), null),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        double[][] f = {{1.0, 1.0, 1.0}, {2.0, 0.0, 4.0}, {3.0, 1.0, 1.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(new LinearObjectiveFunction(new Array2DRowRealMatrix(f), null),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
    }

    @Test
    public void testIsOptimal() {
        double[][] f = {{1.0, 1.0, 1.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 0.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(f),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
        assertFalse(solver.isOptimal(tableau));
        solver.solvePhase1(tableau);
        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testIsPhase1Solved() {
        double[][] f = {{1.0, 1.0, 1.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 0.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(f),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
        assertFalse(solver.isPhase1Solved(tableau));
        solver.solvePhase1(tableau);
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testDoIteration() {
        double[][] f = {{1.0, 1.0, 1.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 0.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(f),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
        solver.solvePhase1(tableau);
        int pivotCol = 1;
        int pivotRow = 1;
        double pivotVal = 1.0;
        solver.doIteration(tableau);
        assertArrayEquals(new double[]{-1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0}, tableau.getColumnVector(pivotCol).toArray(), 1e-6);
        assertTrue(MathUtils.compareTo(tableau.getEntry(pivotRow, pivotCol), 1.0, 1e-6) > 0);
        assertTrue(MathUtils.compareTo(tableau.getEntry(pivotRow, 2), 0.0, 1e-6) < 0);
        assertArrayEquals(new double[]{1.0, -1.0 / 3.0, 1.0 / 3.0}, tableau.getRowVector(pivotRow).toArray(), 1e-6);
    }

    @Test
    public void testGetPivotColumn() {
        double[][] f = {{1.0, 1.0, 1.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 0.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(f),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
        solver.solvePhase1(tableau);
        int pivotCol = 1;
        Integer result = solver.getPivotColumn(tableau);
        assertEquals(pivotCol, result.intValue());
        assertNull(solver.getPivotColumn(null));
    }

    @Test
    public void testGetPivotRow() {
        double[][] f = {{1.0, 1.0, 1.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 0.0}};
        double[][] constraints = {{0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(f),
                new LinearConstraintSet(new Array2DRowRealMatrix(constraints)), GoalType.MINIMIZE, false, 1e-6);
        solver.solvePhase1(tableau);
        int pivotCol = 1;
        int pivotRow = 1;
        Integer result = solver.getPivotRow(pivotCol, tableau);
        assertEquals(pivotRow, result.intValue());
        assertNull(solver.getPivotRow(pivotCol, null));
    }
}