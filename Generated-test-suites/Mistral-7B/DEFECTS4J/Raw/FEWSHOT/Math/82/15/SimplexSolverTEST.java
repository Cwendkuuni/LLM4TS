package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

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

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws OptimizationException {
        double[][] f = new double[][]{{-1.0, 2.0}, {-1.0, 3.0}};
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(f);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws OptimizationException {
        double[][] f = new double[][]{{0.0, -1.0}, {1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize(f);
    }

    @Test
    public void testSolveSimpleExample() throws OptimizationException {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize(f);
        assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.001);
        assertEquals(3.0 / 3.0, solution.getValue(), 0.001);
    }

    @Test
    public void testSolveComplexExample() throws OptimizationException {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}, {5.0, -1.0, -3.0, 4.0}, {1.0, 1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize(f);
        assertArrayEquals(new double[]{2.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0}, solution.getPoint(), 0.001);
        assertEquals(6.0 / 5.0, solution.getValue(), 0.001);
    }

    @Test
    public void testIsOptimal() throws OptimizationException {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        assertTrue(solver.isOptimal(tableau));

        double[][] f1 = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}, {5.0, -1.0, -3.0, 4.0}, {1.0, 1.0, 1.0, 1.0}, {1.0, 0.0, 0.0, 1.0}};
        SimplexSolver solver1 = new SimplexSolver();
        SimplexTableau tableau1 = new SimplexTableau(f1, null, null, null, 1.0e-6);
        solver1.solvePhase1(tableau1);
        tableau1.discardArtificialVariables();
        assertFalse(solver1.isOptimal(tableau1));
    }

    @Test
    public void testGetPivotColumn() {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 1.0e-6);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(3, pivotColumn.intValue());

        f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}, {5.0, -1.0, -3.0, 4.0}, {1.0, 1.0, 1.0, 1.0}};
        tableau = new SimplexTableau(f, null, null, null, 1.0e-6);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(5, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 1.0e-6);
        Integer pivotRow = solver.getPivotRow(3, tableau);
        assertEquals(1, pivotRow.intValue());

        f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}, {5.0, -1.0, -3.0, 4.0}, {1.0, 1.0, 1.0, 1.0}};
        tableau = new SimplexTableau(f, null, null, null, 1.0e-6);
        pivotRow = solver.getPivotRow(5, tableau);
        assertEquals(5, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 1.0e-6);

        solver.doIteration(tableau);
        assertEquals(1, tableau.getNumArtificialVariables());

        solver.doIteration(tableau);
        assertFalse(MathUtils.equals(tableau.getEntry(1, 3), 0, 1.0e-6));
        assertEquals(-1.0e-6, tableau.getEntry(1, 2), 0.0);

        solver.doIteration(tableau);
        assertFalse(MathUtils.equals(tableau.getEntry(2, 3), 0, 1.0e-6));
        assertEquals(-1.0e-6, tableau.getEntry(2, 2), 0.0);

        solver.doIteration(tableau);
        assertEquals(0, tableau.getNumArtificialVariables());
    }

    @Test
    public void testIsPhase1Solved() throws OptimizationException {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 1.0e-6);
        assertFalse(solver.isPhase1Solved(tableau));

        solver.solvePhase1(tableau);
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        double[][] f = new double[][]{{-1.0, 2.0, -3.0, 6.0}, {-1.0, 1.0, 2.0, 3.0}, {0.0, 0.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
        assertTrue(Precision.abs(tableau.getEntry(0, 3), 0.0) < 1.0e-6);
    }
}