package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;

public class SimplexSolverTest {

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 1.0}, {-1.0, -1.0, 1.0},
            {-1.0, 1.0, -1.0}}, new int[][]{new int[]{0, 2}, new int[]{1, 2}, new int[]{1, 1}},
            SimplexTableau.GoalType.MINIMIZE, true, 1.0e-6);

        solver.solvePhase1(tableau);
        solver.doIteration(tableau); // pivotRow should be null
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws Exception {
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 1.0}, {-1.0, -1.0, 1.0},
            {-1.0, 1.0, -1.0}}, new int[][]{new int[]{0, 2}, new int[]{1, 2}, new int[]{1, 1}},
            SimplexTableau.GoalType.MINIMIZE, true, 1.0e-6);

        solver.solvePhase1(tableau);
        solver.solvePhase1(tableau); // W should be zero after Phase 1
        solver.doIteration(tableau); // pivotRow should be null
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 1.0}, {-1.0, -1.0, 1.0},
            {-1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}}, new int[][]{new int[]{0, 2}, new int[]{1, 2},
            new int[]{1, 1}, new int[]{2, 2}}, SimplexTableau.GoalType.MINIMIZE, true, 1.0e-6);

        solver.solvePhase1(tableau);
        solver.solvePhase1(tableau);
        solver.solvePhase1(tableau);
        solver.solvePhase1(tableau);
        solver.solvePhase1(tableau); // maximal iteration count exceeded
    }

    @Test
    public void testSolve() throws Exception {
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 2.0, 1.0}, {-1.0, -1.0, 1.0},
            {-1.0, 1.0, -1.0}}, new int[][]{new int[]{0, 2}, new int[]{1, 2}, new int[]{1, 1}},
            SimplexTableau.GoalType.MINIMIZE, true, 1.0e-6);

        solver.solvePhase1(tableau);
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);

        PointValuePair solution = solver.doOptimize();
        RealPointValuePair expectedSolution = new RealPointValuePair(new double[]{3.0 / 2.0, 3.0 / 2.0},
            5.0 / 2.0);
        assertEquals("Invalid solution", expectedSolution, solution);
    }

    @Test
    public void testSolveWithNoObjectiveFunctions() throws Exception {
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0}, {1.0, 1.0}},
            new int[0][], SimplexTableau.GoalType.MINIMIZE, true, 1.0e-6);

        try {
            solver.doOptimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithNoConstraints() throws Exception {
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0}}, new int[0][],
            SimplexTableau.GoalType.MINIMIZE, true, 1.0e-6);

        try {
            solver.doOptimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }
    }
}