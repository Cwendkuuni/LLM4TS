package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

import org.apache.commons.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.commons.math.optimization.linear.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testSolveOptimizationProblem() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, -3.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();

        assertArrayEquals(new double[]{-3.0 / 2.0, -3.0 / 2.0}, solution.getPoint(), 0.001);
        assertEquals(-6.0, solution.getValue(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, -3.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver(1);
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, -3.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, 0.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, 0.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);

        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, 0.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver();

        assertFalse(solver.isPhase1Solved(tableau));
        solver.solvePhase1(tableau);

        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, -3.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver();

        assertEquals(1, solver.getPivotColumn(tableau).intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.0, 1.0, -1.0}, {-1.0, 0.0, 2.0}, {1.0, 2.0, -3.0}},
                new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, new int[]{0, 1}, PointValuePair.MINIMIZE, true, 1e-6);

        SimplexSolver solver = new SimplexSolver();

        assertEquals(2, solver.getPivotRow(0, tableau).intValue());
    }
}