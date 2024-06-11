package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.apache.commons.math3.util.Precision.equals;
import static org.apache.commons.math3.util.Precision.compareTo;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.GoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearPoint;
import org.apache.commons.math3.optimization.linear.LinearTableau;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalTypeConstraint;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0, true),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 1, true)},
                GoalType.MINIMIZE, false, 1.0e-6, 10);

        int initialHeight = tableau.getHeight();
        int initialWidth = tableau.getWidth();

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doIteration(tableau);
        } catch (Exception e) {
            // should not reach here
        }

        assertEquals(initialHeight + 1, tableau.getHeight());
        assertEquals(initialWidth, tableau.getWidth());

        int pivotCol = tableau.getWidth() - 1;
        int pivotRow = tableau.getNumObjectiveFunctions();

        assertEquals(1.0, tableau.getEntry(pivotRow, pivotCol), 1.0e-6);
        assertEquals(0.0, tableau.getEntry(i, pivotCol), 1.0e-6);
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow) {
                assertEquals(-tableau.getEntry(pivotRow, pivotCol), tableau.getEntry(i, pivotCol), 1.0e-6);
            }
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0, true),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 1, true)},
                GoalType.MINIMIZE, false, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
        } catch (Exception e) {
            // should not reach here
        }

        // check if the tableau is optimal
        assertEquals(0.0, tableau.getEntry(0, tableau.getRhsOffset()), 1.0e-6);
        for (int i = 0; i < tableau.getWidth() - 1; i++) {
            assertEquals(0.0, tableau.getEntry(0, i), 1.0e-6);
        }
        assertEquals(Double.POSITIVE_INFINITY, tableau.getEntry(1, 0), 1.0e-6);
        assertEquals(Double.POSITIVE_INFINITY, tableau.getEntry(1, 1), 1.0e-6);
    }

    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0, true),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 1, true)},
                GoalType.MINIMIZE, false, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();

        LinearPoint optimalPoint = new LinearPoint(new double[]{0.0, 0.0});
        assertEquals(optimalPoint, solution.getPoint(), 1.0e-6);
        assertEquals(-1.0, solution.getValue(), 1.0e-6);
    }

    @Test
    public void testUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 2.0}, 1),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0, true),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 1, true)},
                GoalType.MINIMIZE, false, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();

        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testNoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0, false),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 1, true)},
                GoalType.MINIMIZE, false, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();

        assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testMaxCountExceededException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0, true),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 1, true)},
                GoalType.MINIMIZE, false, 1.0e-6, 0);

        SimplexSolver solver = new SimplexSolver();

        assertThrows(MaxCountExceededException.class, () -> solver.doOptimize());
    }

    @Test
    public void testGoalTypeConstraint() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{2.0, -1.0, 1.0}, 0);
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, -1.0}, 0, new GoalTypeConstraint(GoalType.MAXIMIZE));

        SimplexTableau tableau = new SimplexTableau(objective, new LinearConstraint[]{constraint}, GoalType.MINIMIZE, false, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();

        LinearPoint optimalPoint = new LinearPoint(new double[]{1.0, 0.0});
        assertEquals(optimalPoint, solution.getPoint(), 1.0e-6);
        assertEquals(2.0, solution.getValue(), 1.0e-6);
    }
}