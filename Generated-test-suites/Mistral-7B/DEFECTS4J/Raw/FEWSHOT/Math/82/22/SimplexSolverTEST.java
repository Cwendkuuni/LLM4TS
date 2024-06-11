package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSolve() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 0.0));
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(obj, constraints, restrictToNonNegative, 0.001);
        PointValuePair expected = new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, -1.0 / 3.0);
        assertTrue(MathUtils.equals(solution.getValue(), expected.getValue(), 1e-6));
        assertArrayEquals(solution.getPoint(), new double[]{1.0 / 3.0, 1.0 / 3.0}, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 0.0));
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver(0.0);
        solver.optimize(obj, constraints, restrictToNonNegative, 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolveNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(obj, constraints, restrictToNonNegative, 0.001);
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, 1.0);
        constraints.addConstraint(constraint);
        solver.optimize(obj, constraints, restrictToNonNegative, 0.001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolveUnboundedSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{-1.0, -1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(obj, constraints, restrictToNonNegative, 0.001);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, restrictToNonNegative, 0.001);
        solver.solvePhase1(tableau);
        assertTrue(tableau.isPhase1Solved());
    }

    @Test
    public void testSolvePhase1Unbounded() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, restrictToNonNegative, 0.001);
        tableau.setEntry(0, tableau.getWidth() - 1, Double.NEGATIVE_INFINITY);
        assertFalse(solver.solvePhase1(tableau));
    }

    @Test
    public void testIsOptimal() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, restrictToNonNegative, 0.001);
        tableau.solvePhase1();
        tableau.discardArtificialVariables();
        assertFalse(solver.isOptimal(tableau));
        solver.doIteration(tableau);
        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testGetPivotColumn() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, restrictToNonNegative, 0.001);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertEquals(pivotCol.intValue(), 1);
    }

    @Test
    public void testGetPivotRow() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet restrictToNonNegative = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, restrictToNonNegative, 0.001);
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertEquals(pivotRow.intValue(), 1);
    }

}