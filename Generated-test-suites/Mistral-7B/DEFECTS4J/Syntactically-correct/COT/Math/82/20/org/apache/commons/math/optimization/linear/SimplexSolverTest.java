package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.GoalType;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;

/**
 * Test class for SimplexSolver
 */
public class SimplexSolverTest {

    /**
     * Test constructor with default settings
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0);
    }

    /**
     * Test constructor with specified settings
     */
    @Test
    public void testCustomConstructor() {
        double customEpsilon = 1.0e-4;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        assertEquals(customEpsilon, solver.epsilon, 0);
    }

    /**
     * Test getPivotColumn method
     */
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    /**
     * Test getPivotRow method
     */
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);
        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    /**
     * Test doIteration method
     * @throws OptimizationException
     */
    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().doIteration(tableau);
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 0), 1, 1.0e-6));
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 1), -1, 1.0e-6));
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 2), -3, 1.0e-6));
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 3), 0, 1.0e-6));
    }

    /**
     * Test isPhase1Solved method
     */
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(0, 3, 0);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -3);
        tableau.setEntry(1, 3, 0);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    /**
     * Test isOptimal method
     */
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);
        assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(0, 3, 0);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -3);
        tableau.setEntry(1, 3, 0);
        tableau.setEntry(1, 4, 0);

        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(2, 3, 0);
        tableau.setEntry(2, 4, -1);

        tableau.setEntry(3, 0, 0);
        tableau.setEntry(3, 1, 0);
        tableau.setEntry(3, 2, 3);
        tableau.setEntry(3, 3, 0);
        tableau.setEntry(3, 4, 0);

        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    /**
     * Test solvePhase1 method
     * @throws OptimizationException
     */
    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 0), 1, 1.0e-6));
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 1), -1, 1.0e-6));
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 2), -3, 1.0e-6));
        assertEquals(0, MathUtils.compareTo(tableau.getEntry(1, 3), 0, 1.0e-6));
    }

    /**
     * Test doOptimize method
     * @throws OptimizationException
     */
    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        tableau.discardArtificialVariables();

        RealPointValuePair solution = new SimplexSolver().doOptimize();
        assertEquals(-3, solution.getPoint()[0], 1.0e-6);
        assertEquals(-5, solution.getValue(), 1.0e-6);
    }

    /**
     * Test UnboundedSolutionException
     */
    @Test
    public void testUnboundedSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        tableau.discardArtificialVariables();

        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(1, 3, 0);

        assertThrows(UnboundedSolutionException.class, () -> new SimplexSolver().doOptimize());
    }

    /**
     * Test NoFeasibleSolutionException
     */
    @Test
    public void testNoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        tableau.discardArtificialVariables();

        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, 0);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -3);
        tableau.setEntry(1, 3, 0);
        tableau.setEntry(1, 4, 0);
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(2, 3, 0);
        tableau.setEntry(2, 4, -1);

        assertThrows(NoFeasibleSolutionException.class, () -> new SimplexSolver().doOptimize());
    }

    /**
     * Test MaxIterationsExceededException
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{3, 4}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relation.LEQ, 0),
                        new LinearConstraint(new double[]{2, 1}, Relation.LEQ, 0)
                }, GoalType.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().setMaxIterations(1);
        new SimplexSolver().doOptimize();
    }
}