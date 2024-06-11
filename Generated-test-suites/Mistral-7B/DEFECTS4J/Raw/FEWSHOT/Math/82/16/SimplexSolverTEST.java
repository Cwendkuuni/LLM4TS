package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;

import org.apache.commons.cli.SimplexSolver;
import org.apache.commons.cli.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleExample() {
        SimplexSolver solver = new SimplexSolver(1e-6);

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {-1.0, 1.0, 3.0},
                {-1.0, 2.0, 1.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        RealVector b = new Array2DRowRealVector(new double[][] {{3.0}, {2.0}, {0.0}, {0.0}});
        SimplexTableau tableau = new SimplexTableau(matrix, b, SimplexTableau.GOAL_MINIMIZE);

        try {
            SimplexTableau solution = solver.doOptimize();
            assertEquals(3, solution.getNumRows());
            assertEquals(3, solution.getNumColumns());
            assertTrue(MathUtils.areEqual(solution.getEntry(2, 2), 1, 1e-6));
            assertTrue(MathUtils.areEqual(solution.getEntry(3, 2), 0, 1e-6));
            assertTrue(MathUtils.areEqual(solution.getEntry(1, 2), -1 / 3, 1e-6));
            assertTrue(MathUtils.areEqual(solution.getEntry(0, 2), -2 / 3, 1e-6));
        } catch (OptimizationException e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testUnboundedSolution() {
        SimplexSolver solver = new SimplexSolver(1e-6);

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {-1.0, 1.0, 3.0},
                {-1.0, 2.0, 1.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, Double.POSITIVE_INFINITY}
        });

        RealVector b = new Array2DRowRealVector(new double[][] {{3.0}, {2.0}, {0.0}, {0.0}});
        SimplexTableau tableau = new SimplexTableau(matrix, b, SimplexTableau.GOAL_MINIMIZE);

        try {
            solver.doOptimize();
            fail("An UnboundedSolutionException should have been thrown.");
        } catch (UnboundedSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        SimplexSolver solver = new SimplexSolver(1e-6);

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {-1.0, 1.0, 3.0},
                {-1.0, 2.0, 1.0},
                {0.0, 0.0, 2.0},
                {0.0, 0.0, 0.0}
        });

        RealVector b = new Array2DRowRealVector(new double[][] {{3.0}, {2.0}, {0.0}, {0.0}});
        SimplexTableau tableau = new SimplexTableau(matrix, b, SimplexTableau.GOAL_MINIMIZE);

        try {
            solver.doOptimize();
            fail("A NoFeasibleSolutionException should have been thrown.");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver(1e-6);

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {-1.0, 1.0, 3.0, 1.0, 1.0},
                {-1.0, 2.0, 1.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0, -1.0, -1.0}
        });

        RealVector b = new Array2DRowRealVector(new double[][] {{3.0}, {2.0}, {0.0}, {1.0}, {1.0}});
        SimplexTableau tableau = new SimplexTableau(matrix, b, SimplexTableau.GOAL_MINIMIZE);

        try {
            solver.solvePhase1(tableau);
            assertTrue(MathUtils.areEqual(tableau.getEntry(4, 4), 1, 1e-6));
            assertTrue(MathUtils.areEqual(tableau.getEntry(3, 4), 0, 1e-6));
            assertTrue(MathUtils.areEqual(tableau.getEntry(1, 4), -1 / 2, 1e-6));
            assertTrue(MathUtils.areEqual(tableau.getEntry(0, 4), -1 / 2, 1e-6));
            assertTrue(MathUtils.areEqual(tableau.getEntry(2, 4), 0, 1e-6));
            assertTrue(MathUtils.areEqual(tableau.getEntry(5, 4), 0, 1e-6));
        } catch (OptimizationException e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver(1e-6);

        SimplexTableau tableau = new SimplexTableau(
                new double[][] {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}},
                new double[][] {{0.0}, {0.0}, {0.0}},
                SimplexTableau.GOAL_MINIMIZE);

        assertTrue(solver.isPhase1Solved(tableau));

        tableau.setEntry(0, 2, 1);
        tableau.setEntry(1, 2, -1);
        assertFalse(solver.isPhase1Solved(tableau));
    }
}