package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

public class SimplexSolverTest {

    @Test
    public void testSolve() throws MaxCountExceededException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1d, 2d}, {3d, 4d}}, new double[]{-5d, -2d});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[][]{{1d, 2d}, {1d, 1d}}, 5d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, 1d}}, 3d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, -1d}}, 3d)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertTrue(solution != null);
        assertArrayEquals(new double[]{1.5d, 2.5d}, solution.getPoint(), 0.001d);
        assertArrayEquals(new double[]{-1.5d, -0.5d}, solution.getValue(), 0.001d);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws MaxCountExceededException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1d, 2d}, {3d, 4d}}, new double[]{-5d, -2d});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[][]{{1d, 2d}, {1d, 1d}}, 5d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, 1d}}, 3d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, -1d}}, 3d)
        };
        SimplexSolver solver = new SimplexSolver(1, 1);
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1d, 2d}, {3d, 4d}}, new double[]{-5d, -2d});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[][]{{1d, 2d}, {1d, 1d}}, 5d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, 1d}}, 3d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, -1d}}, 2d)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws MaxCountExceededException, NoFeasibleSolutionException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1d, 2d}, {3d, 4d}}, new double[]{-5d, -2d});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[][]{{1d, 2d}, {1d, 1d}}, 5d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, 1d}}, 3d),
            new LinearConstraint(new double[][]{{1d, 2d}, {-1d, -1d}}, 2d),
            new LinearConstraint(new double[][]{{1d, 2d}, {0d, 1d}}, 1d)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1d, 2d, 3d, 4d, 5d},
            {2d, 3d, 1d, 4d, 5d},
            {3d, 2d, 1d, 1d, 6d},
            {4d, 2d, 2d, 1d, 7d}
        }, new double[]{5d, 3d, 4d, 2d, 1d}, "MAX");
        Integer pivotCol = SimplexSolver.getInstance().getPivotColumn(tableau);
        assertEquals(3, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1d, 2d, 3d, 4d, 5d},
            {2d, 3d, 1d, 4d, 5d},
            {3d, 2d, 1d, 1d, 6d},
            {4d, 2d, 2d, 1d, 7d}
        }, new double[]{5d, 3d, 4d, 2d, 1d}, "MAX");
        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 3);
        assertEquals(0, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1d, 2d, 3d, 4d, 5d},
            {2d, 3d, 1d, 4d, 5d},
            {3d, 2d, 1d, 1d, 6d},
            {4d, 2d, 2d, 1d, 7d}
        }, new double[]{5d, 3d, 4d, 2d, 1d}, "MAX");
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        assertEquals(1, tableau.getNumBasicVariables());
        assertEquals(3, tableau.getBasicVariable(0));
        assertEquals(1, tableau.getNumNonBasicVariables());
        assertEquals(2, tableau.getNonBasicVariable(0));
        assertEquals(0.0, tableau.getEntry(1, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(1, 3) + tableau.getEntry(1, 4), 0.001);
        assertEquals(-1.0 / 6.0, tableau.getEntry(1, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(2, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(2, 4), 0.001);
        assertEquals(1.0 / 6.0, tableau.getEntry(2, 5), 0.001);
        assertEquals(1.0, tableau.getEntry(3, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(3, 4), 0.001);
        assertEquals(0.0, tableau.getEntry(3, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(4, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(4, 4), 0.001);
        assertEquals(0.0, tableau.getEntry(4, 5), 0.001);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1d, 2d, 3d, 4d, 5d},
            {2d, 3d, 1d, 4d, 5d},
            {3d, 2d, 1d, 1d, 6d},
            {4d, 2d, 2d, 1d, 7d},
            {0d, 0d, 1d, 1d, 0d}
        }, new double[]{5d, 3d, 4d, 2d, 1d}, "MAX");
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        assertEquals(1, tableau.getNumBasicVariables());
        assertEquals(3, tableau.getBasicVariable(0));
        assertEquals(1, tableau.getNumNonBasicVariables());
        assertEquals(2, tableau.getNonBasicVariable(0));
        assertEquals(0.0, tableau.getEntry(1, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(1, 3) + tableau.getEntry(1, 4), 0.001);
        assertEquals(-1.0 / 6.0, tableau.getEntry(1, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(2, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(2, 4), 0.001);
        assertEquals(1.0 / 6.0, tableau.getEntry(2, 5), 0.001);
        assertEquals(1.0, tableau.getEntry(3, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(3, 4), 0.001);
        assertEquals(0.0, tableau.getEntry(3, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(4, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(4, 4), 0.001);
        assertEquals(0.0, tableau.getEntry(4, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(5, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(5, 4), 0.001);
        assertEquals(1.0, tableau.getEntry(5, 5), 0.001);
        assertEquals(1.0, tableau.getEntry(5, 5), 0.001);
    }

    @Test
    public void testDropPhase1Objective() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {1d, 2d, 3d, 4d, 5d},
            {2d, 3d, 1d, 4d, 5d},
            {3d, 2d, 1d, 1d, 6d},
            {4d, 2d, 2d, 1d, 7d},
            {0d, 0d, 1d, 1d, 0d}
        }, new double[]{5d, 3d, 4d, 2d, 1d}, "MAX");
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        solver.dropPhase1Objective(tableau);
        assertEquals(1, tableau.getNumBasicVariables());
        assertEquals(3, tableau.getBasicVariable(0));
        assertEquals(4, tableau.getNumNonBasicVariables());
        assertEquals(2, tableau.getNonBasicVariable(0));
        assertEquals(4, tableau.getNonBasicVariable(1));
        assertEquals(5, tableau.getNonBasicVariable(2));
        assertEquals(0.0, tableau.getEntry(1, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(1, 3) + tableau.getEntry(1, 4), 0.001);
        assertEquals(1.0 / 6.0, tableau.getEntry(1, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(2, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(2, 4), 0.001);
        assertEquals(1.0 / 6.0, tableau.getEntry(2, 5), 0.001);
        assertEquals(1.0, tableau.getEntry(3, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(3, 4), 0.001);
        assertEquals(0.0, tableau.getEntry(3, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(4, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(4, 4), 0.001);
        assertEquals(0.0, tableau.getEntry(4, 5), 0.001);
        assertEquals(0.0, tableau.getEntry(5, 3), 0.001);
        assertEquals(1.0, tableau.getEntry(5, 4), 0.001);
        assertEquals(0.0, tableau.getEntry(5, 5), 0.001);
    }

}