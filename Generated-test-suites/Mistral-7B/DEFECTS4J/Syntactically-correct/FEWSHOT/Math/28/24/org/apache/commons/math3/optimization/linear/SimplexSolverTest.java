package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleExample() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-3.0, -2.0, 4.0, 5.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, 2.0),
                new LinearConstraint(new double[]{1.0, 1.0, 1.0, -1.0}, 2.0),
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexTableau tableau = new SimplexTableau(func, constraints, goalType, true, 1e-6, 10);

        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();

        while (!tableau.isOptimal()) {
            solver.doIteration(tableau);
        }

        PointValuePair solution = tableau.getSolution();
        assertEquals(2.0, solution.getPoint()[0], 1e-6);
        assertEquals(2.0, solution.getPoint()[1], 1e-6);
        assertEquals(-11.0, solution.getValue(), 1e-6);

        // Check that the tableau is optimal
        for (int i = 0; i < tableau.getHeight(); i++) {
            for (int j = tableau.getNumObjectiveFunctions(); j < tableau.getWidth(); j++) {
                final double entry = tableau.getEntry(i, j);
                // Check if the entry is zero or smaller than the tolerance
                assertTrue(Precision.isZero(entry, 10) || Precision.abs(entry) < 1e-6);
            }
        }
    }

    @Test
    public void testUnboundedSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{0.0}, new double[]{1.0});
        SimplexTableau tableau = new SimplexTableau(func, new LinearConstraint[0], GoalType.MINIMIZE, true, 1e-6, 10);
        assertThrows(UnboundedSolutionException.class, () -> solver.solvePhase1(tableau));
    }

    @Test
    public void testMaxCountExceededException() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-3.0, -2.0, 4.0, 5.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, Double.POSITIVE_INFINITY),
                new LinearConstraint(new double[]{1.0, 1.0, 1.0, -1.0}, Double.POSITIVE_INFINITY),
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexTableau tableau = new SimplexTableau(func, constraints, goalType, true, 1e-6, 1);
        assertThrows(MaxCountExceededException.class, () -> solver.solvePhase1(tableau));
    }

    @Test
    public void testNoFeasibleSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-3.0, -2.0, 4.0, 5.0}, new double[]{1.0, 1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0, 1.0, 1.0}, 1.0),
                new LinearConstraint(new double[]{1.0, 1.0, 1.0, -1.0}, 1.0),
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexTableau tableau = new SimplexTableau(func, constraints, goalType, true, 1e-6, 10);
        assertThrows(NoFeasibleSolutionException.class, () -> solver.solvePhase1(tableau));
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, -1.0, 3.0}, new double[]{1.0, 1.0, 1.0, 1.0}), new LinearConstraint[0], GoalType.MINIMIZE, true, 1e-6, 10);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(2, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, -1.0, 3.0}, new double[]{1.0, 1.0, 1.0, 1.0}), new LinearConstraint[0], GoalType.MINIMIZE, true, 1e-6, 10);
        Integer pivotRow = solver.getPivotRow(tableau, 2);
        assertEquals(2, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, -1.0, 3.0}, new double[]{1.0, 1.0, 1.0, 1.0}), new LinearConstraint[0], GoalType.MINIMIZE, true, 1e-6, 10);
        solver.doIteration(tableau);

        // Check that the pivot element is 1
        assertEquals(1.0, tableau.getEntry(2, 2), 1e-6);

        // Check that the rest of the pivot column is 0
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != 2) {
                assertEquals(0.0, tableau.getEntry(i, 2), 1e-6);
            }
        }
    }
}