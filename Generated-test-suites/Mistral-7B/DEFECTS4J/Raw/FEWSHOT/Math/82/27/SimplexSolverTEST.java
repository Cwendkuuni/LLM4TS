package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

import org.apache.commons.cli.SimplexSolver;
import org.apache.commons.cli.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        double[] f = new double[]{1.0, 2.0};
        double[][] constraints = new double[][]{{-1.0, -1.0, 1.0}, {-1.0, 1.0, 1.0}, {-1.0, 1.0, -1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.doOptimize();
        PointValuePair solution = new PointValuePair(new double[]{3.0 / 2.0, 3.0 / 2.0}, 1.5);
        assertEquals(solution.getPoint(), result.getPoint(), Precision.SAFE_EPSILON);
        assertEquals(solution.getValue(), result.getValue(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testDoOptimizeWithBounds() {
        double[] f = new double[]{1.0, 2.0};
        double[][] constraints = new double[][]{{-1.0, -1.0, 1.0}, {-1.0, 1.0, 1.0}, {-1.0, 1.0, -1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}};
        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, 1.0}, new double[]{0.0, 2.0});
        SimplexSolver solver = new SimplexSolver(bounds);
        RealPointValuePair result = solver.doOptimize();
        PointValuePair solution = new PointValuePair(new double[]{3.0 / 2.0, 3.0 / 2.0}, 1.5);
        assertEquals(solution.getPoint(), result.getPoint(), Precision.SAFE_EPSILON);
        assertEquals(solution.getValue(), result.getValue(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testIsOptimal() {
        double[][] tableau = new double[][]{{0.0, 1.0, 1.0, 0.0, -1.0}, {1.0, 1.0, 0.0, 1.0, 1.0}, {1.0, 0.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 1.0, 1.0, -1.0}, {0.0, 0.0, 0.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(new SimplexTableau(tableau));
        assertTrue(solver.isOptimal(new SimplexTableau(tableau)));
    }

    @Test
    public void testIsNotOptimal() {
        double[][] tableau = new double[][]{{0.0, 1.0, 1.0, 0.0, -1.0}, {1.0, 1.0, 0.0, 1.0, 1.0}, {1.0, 0.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 1.0, 1.0, -1.0}, {0.0, 0.0, 0.0, 1.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(new SimplexTableau(tableau));
        assertFalse(solver.isOptimal(new SimplexTableau(tableau)));
    }

    @Test
    public void testGetPivotColumn() {
        double[][] tableau = new double[][]{{0.0, -1.0, 1.0, 0.0}, {-1.0, 0.0, 1.0, -1.0}, {-1.0, 1.0, 0.0, 1.0},
                {1.0, -1.0, 0.0, 0.0}, {1.0, 1.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau st = new SimplexTableau(tableau);
        assertEquals(3, solver.getPivotColumn(st).intValue());
    }

    @Test
    public void testGetPivotRow() {
        double[][] tableau = new double[][]{{0.0, -1.0, 1.0, 0.0}, {-1.0, 0.0, 1.0, -1.0}, {-1.0, 1.0, 0.0, 1.0},
                {1.0, -1.0, 0.0, 0.0}, {1.0, 1.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau st = new SimplexTableau(tableau);
        assertEquals(1, solver.getPivotRow(3, st).intValue());
    }

    @Test
    public void testDoIteration() {
        double[][] tableau = new double[][]{{1.0, -1.0, 1.0, 0.0}, {-1.0, 0.0, 1.0, -1.0}, {-1.0, 1.0, 0.0, 1.0},
                {0.0, -1.0, 0.0, 1.0}, {0.0, 1.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau st = new SimplexTableau(tableau);
        Integer pivotCol = solver.getPivotColumn(st);
        Integer pivotRow = solver.getPivotRow(pivotCol.intValue(), st);
        solver.doIteration(st);
        assertArrayEquals(new double[]{-1.0 / 3.0, 1.0 / 3.0, 0.0, 0.0, 2.0 / 3.0}, st.getRow(pivotRow), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0, -1.0 / 3.0, 1.0 / 3.0}, st.getRow(0), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{0.0, 1.0, 0.0, 0.0, 0.0}, st.getRow(1), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{1.0, 0.0, 0.0, 0.0, 0.0}, st.getRow(2), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 1.0, -1.0 / 3.0}, st.getRow(3), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 1.0 / 3.0}, st.getRow(4), PrecisionUtils.DEFAULT_EPSILON);
    }

    @Test
    public void testSolvePhase1() {
        double[][] tableau = new double[][]{{1.0, -1.0, 1.0, 0.0}, {-1.0, 0.0, 1.0, -1.0}, {-1.0, 1.0, 0.0, 1.0},
                {0.0, -1.0, 0.0, 1.0}, {0.0, 1.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau st = new SimplexTableau(tableau);
        solver.solvePhase1(st);
        assertArrayEquals(new double[]{-1.0 / 3.0, 1.0 / 3.0, 0.0, 0.0, 2.0 / 3.0}, st.getRow(1), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0, -1.0 / 3.0, 1.0 / 3.0}, st.getRow(0), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{0.0, 1.0, 0.0, 0.0, 0.0}, st.getRow(2), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{1.0, 0.0, 0.0, 0.0, 0.0}, st.getRow(3), PrecisionUtils.DEFAULT_EPSILON);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 1.0, -1.0 / 3.0}, st.getRow(4), PrecisionUtils.DEFAULT_EPSILON);
    }

    @Test
    public void testUnboundedSolution() {
        double[][] tableau = new double[][]{{0.0, 1.0, 1.0, 0.0}, {-1.0, 0.0, 1.0, -1.0}, {-1.0, 1.0, 0.0, 1.0},
                {1.0, -1.0, 0.0, 0.0}, {1.0, 1.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.solvePhase1(new SimplexTableau(tableau)));
    }

    @Test
    public void testNoFeasibleSolution() {
        double[][] tableau = new double[][]{{0.0, 1.0, 1.0, 0.0}, {-1.0, 0.0, 1.0, -1.0}, {-1.0, 1.0, 0.0, 1.0},
                {1.0, -1.0, 0.0, 1.0}, {1.0, 1.0, 0.0, 0.0}, {1.0, 1.0, 0.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.solvePhase1(new SimplexTableau(tableau)));
    }

    @Test
    public void testMaxIterationsExceeded() {
        double[][] tableau = new double[][]{{0.0, 1.0, 1.0, 0.0}, {-1.0, 0.0, 1.0, -1.0}, {-1.0, 1.0, 0.0, 1.0},
                {1.0, -1.0, 0.0, 1.0}, {1.0, 1.0, 0.0, 0.0}, {1.0, 1.0, 0.0, 1.0}, {1.0, 1.0, 0.0, 1.0}};
        SimplexSolver solver = new SimplexSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solvePhase1(new SimplexTableau(tableau)));
    }
}