package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 1.0, 1.0}, {-1.0, 0.0, 1.0}, {-1.0, -1.0, 2.0}};
        SimplexConstraint[] constraints = {new SimplexConstraint(0, 0, 1.0, false),
                                           new SimplexConstraint(1, 1, 1.0, false),
                                           new SimplexConstraint(2, 2, 1.0, false)};
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        RealPointValuePair result = solver.doOptimize();

        PointValuePair expected = new PointValuePair(new double[]{-0.5, 1.0, 0.5}, -1.5);
        assertTrue(MathUtils.isClose(result.getValue(), expected.getValue(), 1e-6));
        assertArrayEquals(result.getPoint(), expected.getPoint(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        SimplexSolver solver = new SimplexSolver(1e-12);
        double[][] f = {{-1.0, 1.0, 1.0}, {-1.0, 0.0, 1.0}, {-1.0, -1.0, 2.0}};
        SimplexConstraint[] constraints = {new SimplexConstraint(0, 0, 1.0, false),
                                           new SimplexConstraint(1, 1, 1.0, false),
                                           new SimplexConstraint(2, 2, 1.0, false)};
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        solver.setMaxIterations(1);
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 1.0, 1.0}, {-1.0, 0.0, 1.0}, {-1.0, -1.0, 0.0}};
        SimplexConstraint[] constraints = {new SimplexConstraint(0, 0, 1.0, false),
                                           new SimplexConstraint(1, 1, 1.0, false),
                                           new SimplexConstraint(2, 2, -1.0, false)};
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 1.0, 1.0}, {-1.0, 0.0, 1.0}, {-1.0, 0.0, 1.0}};
        SimplexConstraint[] constraints = {};
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        solver.doOptimize();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{-1.0, -1.0, 1.0, 1.0, 1.0},
                                                                     {-1.0, 0.0, 1.0, 2.0, 2.0},
                                                                     {0.0, 1.0, 0.0, 1.0, 0.0}},
                                                           1, 4, SimplexSolver.GoalType.MINIMIZE,
                                                           false, 1e-6);

        Integer result = SimplexSolver.this.getPivotColumn(tableau);
        Integer expected = 2;
        assertEquals(expected, result);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{-1.0, -1.0, 1.0, 1.0, 1.0},
                                                                     {-1.0, 0.0, 1.0, 2.0, 2.0},
                                                                     {0.0, 1.0, 0.0, 1.0, 0.0}},
                                                           1, 4, SimplexSolver.GoalType.MINIMIZE,
                                                           false, 1e-6);

        Integer result = SimplexSolver.this.getPivotRow(2, tableau);
        Integer expected = 1;
        assertEquals(expected, result);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{-1.0, -1.0, 1.0, 1.0, 1.0},
                                                                     {-1.0, 0.0, 1.0, 2.0, 2.0},
                                                                     {0.0, 1.0, 0.0, 1.0, 0.0}},
                                                           1, 4, SimplexSolver.GoalType.MINIMIZE,
                                                           false, 1e-6);

        boolean result = SimplexSolver.this.isPhase1Solved(tableau);
        boolean expected = false;
        assertFalse(expected, result);

        tableau = new SimplexTableau(new double[][]{{-1.0, -1.0, 1.0, 1.0, 1.0},
                                                     {-1.0, 0.0, 1.0, 0.0, 0.0},
                                                     {0.0, 1.0, 0.0, 1.0, 0.0}},
                                             1, 4, SimplexSolver.GoalType.MINIMIZE,
                                             false, 1e-6);

        result = SimplexSolver.this.isPhase1Solved(tableau);
        expected = true;
        assertTrue(expected, result);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{-1.0, -1.0, 1.0, 1.0, 1.0},
                                                                     {-1.0, 0.0, 1.0, 2.0, 2.0},
                                                                     {0.0, 1.0, 0.0, 1.0, 0.0}},
                                                           1, 4, SimplexSolver.GoalType.MINIMIZE,
                                                           false, 1e-6);

        boolean result = SimplexSolver.this.isOptimal(tableau);
        boolean expected = false;
        assertFalse(expected, result);

        tableau = new SimplexTableau(new double[][]{{-1.0, -1.0, 1.0, 1.0, 1.0},
                                                     {-1.0, 0.0, 1.0, 0.0, 0.0},
                                                     {0.0, 1.0, 0.0, 1.0, 0.0}},
                                             1, 4, SimplexSolver.GoalType.MINIMIZE,
                                             false, 1e-6);

        result = SimplexSolver.this.isOptimal(tableau);
        expected = true;
        assertTrue(expected, result);
    }
}