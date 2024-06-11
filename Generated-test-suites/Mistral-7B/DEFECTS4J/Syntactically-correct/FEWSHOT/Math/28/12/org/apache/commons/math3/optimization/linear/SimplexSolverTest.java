package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.nonlinear.SimpleXSolver;
import org.apache.commons.math3.optimization.linear.LinearConstraints;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

import java.util.Arrays;

public class SimplexSolverTest {

    /**
     * Test Solves a simple linear optimization problem using SimplexSolver.
     */
    @Test
    public void testSimpleLinearOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 1.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{-5.0 <= new LinearExpression(new double[]{1.0}, 1.0) <= 5.0
                , -3.0 <= new LinearExpression(new double[]{0.0, 1.0}, new double[]{2.0, 1.0}) <= 3.0});
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();
        double[] x = solution.getPoint();
        double objectiveValue = solution.getValue();

        assertTrue(Precision.isClose(x[0], 1.5, 1));
        assertTrue(Precision.isClose(x[1], 1.5, 1));
        assertTrue(Precision.isClose(objectiveValue, -6.0, 1));
    }

    /**
     * Test Solves a simple linear optimization problem using SimplexSolver with custom epsilon and maxUlps.
     */
    @Test
    public void testCustomLinearOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 1.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{-5.0 <= new LinearExpression(new double[]{1.0}, 1.0) <= 5.0
                , -3.0 <= new LinearExpression(new double[]{0.0, 1.0}, new double[]{2.0, 1.0}) <= 3.0});
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1.0e-8, 15);

        PointValuePair solution = solver.doOptimize();
        double[] x = solution.getPoint();
        double objectiveValue = solution.getValue();

        assertTrue(Precision.isClose(x[0], 1.5, 0.00001));
        assertTrue(Precision.isClose(x[1], 1.5, 0.00001));
        assertTrue(Precision.isClose(objectiveValue, -6.0, 0.00001));
    }

    /**
     * Test Solves a linear optimization problem with an unbounded solution using SimplexSolver.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{-5.0 <= new LinearExpression(new double[]{1.0}, 1.0) <= 5.0});
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        solver.doOptimize();
    }

    /**
     * Test Solves a linear optimization problem with a solution that is not feasible using SimplexSolver.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, new double[]{1.0, 1.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{-5.0 <= new LinearExpression(new double[]{1.0}, 1.0) <= 5.0
                , new LinearConstraint[]{Integer.MIN_VALUE, Integer.MAX_VALUE} <= new LinearExpression(new double[]{0.0, 1.0}, new double[]{2.0, 1.0})});
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        solver.doOptimize();
    }

    /**
     * Test Solves a linear optimization problem with a maximum number of iterations using SimplexSolver.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{-5.0 <= new LinearExpression(new double[]{1.0}, 1.0) <= 5.0
                , -3.0 <= new LinearExpression(new double[]{0.0, 1.0}, new double[]{2.0, 1.0}) <= 3.0});
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1.0e-6, 1);

        PointValuePair solution = null;
        try {
            solution = solver.doOptimize();
        } catch (Exception e) {
        }

        assertNull(solution);
    }

    /**
     * Test getting the pivot column.
     */
    @Test
    public void testGetPivotColumn() {
        double[][] tableau = new double[][]{{-2.0, 1.0, 1.0, 1.0, 1.0}, {0.0, 1.0, 0.0, 2.0, 1.0}, {0.0, 0.0, 0.0, -1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}};
        SimplexTableau simplexTableau = new SimplexTableau(tableau);
        Integer pivotColumn = SimplexSolver.this.getPivotColumn(simplexTableau);
        assertTrue(pivotColumn.intValue() == 4);
    }

    /**
     * Test getting the pivot row.
     */
    @Test
    public void testGetPivotRow() {
        double[][] tableau = new double[][]{{-2.0, 1.0, 1.0, 1.0, 1.0}, {0.0, 1.0, 0.0, 2.0, 1.0}, {0.0, 0.0, 0.0, -1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}, {0.0, 0.0, 0.0, 0.0, 0.0}};
        SimplexTableau simplexTableau = new SimplexTableau(tableau);
        Integer pivotRow = SimplexSolver.this.getPivotRow(simplexTableau, 4);
        assertTrue(pivotRow.intValue() == 3);
    }

    /**
     * Test if the solver can handle a problem with only objective functions.
     */
    @Test
    public void testSolverWithOnlyObjectiveFunctions() {
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[1];
        functions[0] = new LinearObjectiveFunction(new double[]{-2.0}, new double[]{1.0});
        LinearConstraints constraints = new LinearConstraints();
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize(functions, constraints, goalType);
        double[] x = solution.getPoint();
        double objectiveValue = solution.getValue();

        assertTrue(Precision.isClose(x[0], 0.0, 1));
        assertTrue(Precision.isClose(objectiveValue, -2.0, 1));
    }

    /**
     * Test if the solver can handle a problem with only linear constraints.
     */
    @Test
    public void testSolverWithOnlyConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[]{-5.0 <= new LinearExpression(new double[]{1.0}, 1.0) <= 5.0
                , -3.0 <= new LinearExpression(new double[]{0.0, 1.0}, new double[]{2.0, 1.0}) <= 3.0});
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize(new LinearObjectiveFunction[0], constraints, goalType);
        double[] x = solution.getPoint();
        double objectiveValue = solution.getValue();

        assertTrue(Arrays.equals(new double[0], x));
        assertTrue(Precision.isClose(objectiveValue, 0.0, 1));
    }
}