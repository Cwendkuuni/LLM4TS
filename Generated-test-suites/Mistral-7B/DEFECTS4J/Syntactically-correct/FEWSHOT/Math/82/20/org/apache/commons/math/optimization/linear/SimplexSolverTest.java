package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.commons.math.optimization.linear.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(1.0e-5);
        assertEquals(1.0e-5, solver.epsilon, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() {
        new SimplexSolver(-1.0e-6);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, 2, "MINIMIZE", true, 1.0e-6);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 1.0}, {2.0, -1.0, 3.0}}, 2, "MINIMIZE", true, 1.0e-6);
        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = OptimizationException.class)
    public void testDoIteration_UnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 1.0}, {2.0, 0.0, 3.0}}, 2, "MINIMIZE", true, 1.0e-6);
        new SimplexSolver().doIteration(tableau);
        throw new UnboundedSolutionException();
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1_MaxIterationsExceeded() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 1.0}, {2.0, 0.0, 3.0}, {3.0, -1.0, 1.0}}, 3, "MINIMIZE", true, 1.0e-6);
        new SimplexSolver().solvePhase1(tableau);
        throw new MaxIterationsExceededException();
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 1.0}, {2.0, 0.0, 3.0}, {3.0, 1.0, 1.0}}, 3, "MINIMIZE", true, 1.0e-6);
        new SimplexSolver().solvePhase1(tableau);
        throw new NoFeasibleSolutionException();
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, 2, "MINIMIZE", true, 1.0e-6);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(0, 1, 10.0);
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, 2, "MINIMIZE", true, 1.0e-6);
        assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(0, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolve() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, 2, "MINIMIZE", true, 1.0e-6);
        RealPointValuePair solution = new SimplexSolver().doOptimize();
        PointValuePair expectedSolution = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -5.0 / 3.0);
        assertEquals(expectedSolution, solution);
    }

    @Test
    public void testSolve_MultipleObjectives() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 4.0, 3.0}}, 2, "MINIMIZE", true, 1.0e-6);
        RealPointValuePair solution = new SimplexSolver().doOptimize();
        PointValuePair[] expectedSolutions = new PointValuePair[]{new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -5.0 / 3.0),
                                                                   new PointValuePair(new double[]{2.0 / 3.0, 1.0 / 3.0}, -5.0 / 3.0)};
        assertEquals(expectedSolutions, solution.getPoints());
        assertEquals(-5.0 / 3.0, solution.getValue(), Precision.SAFE_EQ);
    }

    @Test
    public void testSolve_Maximize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, 2, "MAXIMIZE", true, 1.0e-6);
        RealPointValuePair solution = new SimplexSolver().doOptimize();
        PointValuePair expectedSolution = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 5.0 / 3.0);
        assertEquals(expectedSolution, solution);
    }

}