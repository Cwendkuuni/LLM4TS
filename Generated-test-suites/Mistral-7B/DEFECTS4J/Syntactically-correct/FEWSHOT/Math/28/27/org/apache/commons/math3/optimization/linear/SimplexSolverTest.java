package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.apache.commons.math3.optim.PointValuePair.Pair;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver(1e-9, 2);
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1, -3}, new double[]{2.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0.0),
                new LinearConstraint(new double[]{1, 1}, Double.POSITIVE_INFINITY)};
        LinearGoalType goalType = LinearGoalType.MINIMIZE;

        PointValuePair solution = solver.doOptimize();

        assertEquals(Pair.pair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -5.0 / 3.0), solution);
    }

    @Test
    public void testSolvePhase1WithNoArtificialVariables() {
        SimplexSolver solver = new SimplexSolver(1e-9, 2);
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{1, 1}, new double[]{0, 0});
        LinearConstraint[] constraints = new LinearConstraint[]{};
        LinearGoalType goalType = LinearGoalType.MINIMIZE;

        try {
            solver.solvePhase1(new SimplexTableau(function, constraints, goalType, false, 1e-9, 2));
            fail();
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolvePhase1WithUnboundedSolution() {
        SimplexSolver solver = new SimplexSolver(1e-9, 2);
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1, -1}, new double[]{0, 0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0.0),
                new LinearConstraint(new double[]{1, 1}, Double.POSITIVE_INFINITY)};
        LinearGoalType goalType = LinearGoalType.MINIMIZE;

        try {
            solver.solvePhase1(new SimplexTableau(function, constraints, goalType, false, 1e-9, 2));
            fail();
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolvePhase1WithNoFeasibleSolution() {
        SimplexSolver solver = new SimplexSolver(1e-9, 2);
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{1, -1}, new double[]{0, 0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1, 1}, 0.0),
                new LinearConstraint(new double[]{-1, 1}, 0.0)};
        LinearGoalType goalType = LinearGoalType.MINIMIZE;

        try {
            solver.solvePhase1(new SimplexTableau(function, constraints, goalType, false, 1e-9, 2));
            fail();
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, -1}, new double[]{0, 0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{1, 1}, 0.0),
                        new LinearConstraint(new double[]{-1, 1}, 0.0)},
                LinearGoalType.MINIMIZE, false, 1e-9, 2);

        int pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        assertEquals(1, pivotColumn);
    }

    @Test
    public void testGetPivotRowWithMinimumRatio() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, -1}, new double[]{0, 0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{1, 1}, 0.0),
                        new LinearConstraint(new double[]{-1, 1}, 0.0)},
                LinearGoalType.MINIMIZE, false, 1e-9, 2);

        int pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 1);
        assertEquals(2, pivotRow);
    }

    @Test
    public void testGetPivotRowWithMinimumRatioAndDegeneracy() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, -1, -1}, new double[]{0, 0, 0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{1, 1, 1}, 0.0),
                        new LinearConstraint(new double[]{1, 1, -1}, 0.0),
                        new LinearConstraint(new double[]{-1, 1, 1}, 0.0),
                        new LinearConstraint(new double[]{-1, 1, -1}, 0.0)},
                LinearGoalType.MINIMIZE, false, 1e-9, 2);

        int pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 1);
        assertEquals(3, pivotRow);
    }

    @Test
    public void testGetPivotRowWithMinimumRatioAndNoSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, -1}, new double[]{0, 0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{1, 1}, Double.POSITIVE_INFINITY),
                        new LinearConstraint(new double[]{1, -1}, Double.POSITIVE_INFINITY)},
                LinearGoalType.MINIMIZE, false, 1e-9, 2);

        assertNull(SimplexSolver.getInstance().getPivotRow(tableau, 1));
    }
}