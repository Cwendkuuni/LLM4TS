package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public class SimplexSolverTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testDefaultConstructorThrowsUOE() {
        new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}), new LinearConstraint[0], LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        Integer pivotCol = new SimplexSolver(1e-6, 10).getPivotColumn(tableau);
        assertEquals(0, pivotCol.intValue());
    }

    @Test
    public void testGetPivotColumnWithTie() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2,-1}), new LinearConstraint[0], LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        List<Integer> pivotCols = new ArrayList<>();
        pivotCols.add(0);
        pivotCols.add(2);
        List<Integer> expectedRows = new ArrayList<>();
        expectedRows.add(1);
        expectedRows.add(2);

        for (int i = 0; i < tableau.getHeight(); i++) {
            Integer pivotRow = new SimplexSolver(1e-6, 10).getPivotRow(tableau, 0);
            Integer pivotCol = new SimplexSolver(1e-6, 10).getPivotColumn(tableau);
            assertEquals(expectedRows.remove(0), pivotRow);
            assertEquals(pivotCols.remove(0), pivotCol);
        }
        assertArrayEquals(new int[0], tableau.getBasicRow(0));
    }

    @Test
    public void testGetPivotRowWithNoBasisChange() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}), new LinearConstraint[0], LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        Integer pivotRow = new SimplexSolver(1e-6, 10).getPivotRow(tableau, 0);
        assertNull(pivotRow);
    }

    @Test
    public void testGetPivotRowWithBasicChange() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2, 1}), new LinearConstraint[0], LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        Integer pivotRow = new SimplexSolver(1e-6, 10).getPivotRow(tableau, 0);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearConstraint constraint = new LinearConstraint(new double[]{-1, 1}, 0);
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1}), new LinearConstraint[]{constraint}, LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        new SimplexSolver(1e-6, 10).solvePhase1(tableau);
        assertArrayEquals(new int[]{1, 0}, tableau.getBasicRow(0));
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionInPhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 0}), new LinearConstraint[0], LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        new SimplexSolver(1e-6, 10).solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionInPhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -1, 1}), new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0)}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6, 10);
        new SimplexSolver(1e-6, 10).solvePhase1(tableau);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededInPhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -1, 1}), new LinearConstraint[0], LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        new SimplexSolver(1e-6, 1).solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1, 1}, 0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, -1}, 0);
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -1, -1}), new LinearConstraint[]{constraint1, constraint2}, LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        PointValuePair solution = new SimplexSolver(1e-6, 10).doOptimize();
        assertEquals(-3.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.0, 1.0, 1.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionInDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, -1}, 0);
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -1}), new LinearConstraint[]{constraint1}, LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 10);
        new SimplexSolver(1e-6, 10).doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionInDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, -1}, 0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1, 1}, 0);
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -1, -1}), new LinearConstraint[]{constraint1, constraint2}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6, 10);
        new SimplexSolver(1e-6, 10).doOptimize();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededInDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearConstraint constraint = new LinearConstraint(new double[]{-1, 1}, 0);
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -1, -1}), new LinearConstraint[]{constraint}, LinearObjectiveFunction.Type.MINIMIZE, true, 1e-6, 1);
        new SimplexSolver(1e-6, 1).doOptimize();
    }
}