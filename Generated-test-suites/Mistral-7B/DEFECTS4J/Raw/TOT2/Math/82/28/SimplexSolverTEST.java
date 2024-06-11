package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.Precision.equals;

/**
 * Tests for the SimplexSolver class.
 */
public class SimplexSolverTest {

    /** The epsilon value for the solver. */
    private static final double EPSILON = 1.0e-6;

    /**
     * Tests the constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(EPSILON, solver.epsilon, 0);
    }

    /**
     * Tests the constructor with a specified epsilon value.
     */
    @Test
    public void testSpecifiedConstructor() {
        double epsilon = 1.0e-8;
        SimplexSolver solver = new SimplexSolver(epsilon);
        Assert.assertEquals(epsilon, solver.epsilon, 0);
    }

    /**
     * Tests getting the pivot column.
     */
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(1, pivotCol.intValue());
    }

    /**
     * Tests getting the pivot row.
     */
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        Integer pivotRow = new SimplexSolver().getPivotRow(1, tableau);
        Assert.assertEquals(1, pivotRow.intValue());
    }

    /**
     * Tests one iteration of the Simplex method.
     */
    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);

        // set the pivot element to 1
        double pivotVal = tableau.getEntry(1, 1);
        Assert.assertEquals(1.0, pivotVal, EPSILON);

        // set the rest of the pivot column to 0
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != 1) {
                double multiplier = tableau.getEntry(i, 1);
                double expectedResult = pivotVal * multiplier;
                Assert.assertEquals(expectedResult, tableau.getEntry(i, 1), EPSILON);
            }
        }
    }

    /**
     * Tests Phase 1 is solved.
     */
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, -1.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        boolean result = new SimplexSolver().isPhase1Solved(tableau);
        Assert.assertFalse(result);

        tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 0.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, 0.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        result = new SimplexSolver().isPhase1Solved(tableau);
        Assert.assertTrue(result);
    }

    /**
     * Tests whether the problem is at an optimal state.
     */
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, 0.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        boolean result = new SimplexSolver().isOptimal(tableau);
        Assert.assertFalse(result);

        tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 0.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, 0.0, 1.0, 0.0, 0.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        result = new SimplexSolver().isOptimal(tableau);
        Assert.assertTrue(result);
    }

    /**
     * Tests the solve phase 1 method.
     */
    @Test
    public void testSolvePhase1() throws OptimizationException, NumberIsTooSmallException {
        SimplexTableau tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, 0.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        new SimplexSolver().solvePhase1(tableau);
        Assert.assertEquals(2, tableau.getNumArtificialVariables());

        tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 0.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, 0.0, 1.0, 0.0, 0.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        new SimplexSolver().solvePhase1(tableau);
        Assert.assertEquals(0, tableau.getNumArtificialVariables());
    }

    /**
     * Tests the optimization method.
     */
    @Test
    public void testOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, 0.0, -1.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        RealPointValuePair result = new SimplexSolver().doOptimize();
        Assert.assertEquals(-3.0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(-3.0, result.getValue(), EPSILON);

        tableau = new SimplexTableau(
            new double[][] { { -3.0, 2.0, 1.0, 0.0, 1.0 }, { 1.0, 2.0, 1.0, 1.0, 1.0 }, { 1.0, 0.0, 1.0, 0.0, 0.0 } },
            new LinearConstraint[] { new LinearConstraint(2.0), new LinearConstraint(-3.0) },
            GoalType.MINIMIZE, true, EPSILON);
        result = new SimplexSolver().doOptimize();
        Assert.assertEquals(0.0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0.0, result.getValue(), EPSILON);
    }

}