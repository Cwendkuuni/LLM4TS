import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizationFactory;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testCustomEpsilonConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001);
        Assert.assertEquals(0.001, solver.epsilon, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPivotColumnNullTableau() {
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.getPivotColumn(null);
        } catch (NullPointerException e) {
            throw new AssertionError("Expected UnsupportedOperationException");
        }
    }

    @Test
    public void testGetPivotColumnTableauNoObjectiveFunctions() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{}, new double[]{1.0});
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{});
        tableau.setObjectiveFunction(objFunc);
        Integer expected = 0;
        Integer actual = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPivotColumnTableau() {
        LinearObjectiveFunction objFunc1 = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearObjectiveFunction objFunc2 = new LinearObjectiveFunction(new double[]{1.0, -1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc1, LinearOptimizationFactory.GOAL_MINIMIZE);
        Integer expected = 0;
        Integer actual = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(expected, actual);

        expected = 1;
        tableau.setObjectiveFunction(objFunc2);
        actual = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPivotRowNullTableau() {
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.getPivotRow(-1, null);
        } catch (NullPointerException e) {
            throw new AssertionError("Expected IllegalArgumentException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPivotRowInvalidColumn() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{});
        tableau.setObjectiveFunction(objFunc);
        try {
            new SimplexSolver().getPivotRow(-1, tableau);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Expected IllegalArgumentException");
        }
    }

    @Test
    public void testGetPivotRowTableau() {
        LinearObjectiveFunction objFunc1 = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearObjectiveFunction objFunc2 = new LinearObjectiveFunction(new double[]{1.0, -1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc1, LinearOptimizationFactory.GOAL_MINIMIZE);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Integer expectedRow = 1;
        Integer actualRow = new SimplexSolver().getPivotRow(pivotColumn, tableau);
        Assert.assertEquals(expectedRow, actualRow);

        tableau.setObjectiveFunction(objFunc2);
        pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        expectedRow = 0;
        actualRow = new SimplexSolver().getPivotRow(pivotColumn, tableau);
        Assert.assertEquals(expectedRow, actualRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{});
        tableau.setObjectiveFunction(objFunc);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1SolvedTableauEmpty() {
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{});
        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsPhase1SolvedTableauSolved() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE);
        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, 0);
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 2);
        tableau.setEntry(2, 2, -1);
        Assert.assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsPhase1SolvedTableauNotSolved() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE);
        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, 0);
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 2);
        tableau.setEntry(2, 2, 0);
        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1FeasibleSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE);
        RealPointValuePair expected = new PointValuePair(new double[]{0.0, 1.0}, 1.0);
        RealPointValuePair actual = new SimplexSolver().doOptimize();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDoOptimizeWithCustomEpsilon() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE);
        PointValuePair expected = new PointValuePair(new double[]{0.0, 1.0}, 1.0);
        SimplexSolver solver = new SimplexSolver(1.0e-9);
        RealPointValuePair actual = solver.doOptimize();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDoOptimizeWithConstraints() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, 2, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1, constraint2}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE);
        RealPointValuePair expected = new PointValuePair(new double[]{0.0, 1.0}, 1.0);
        RealPointValuePair actual = new SimplexSolver().doOptimize();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDoOptimizeWithRestrictToNonNegative() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, -1.0}, new double[]{2.0, 1.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0, Double.POSITIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearConstraint[]{constraint1}, objFunc, LinearOptimizationFactory.GOAL_MINIMIZE, true);
        RealPointValuePair expected = new PointValuePair(new double[]{1.0, 0.0}, 2.0);
        RealPointValuePair actual = new SimplexSolver().doOptimize();
        Assert.assertEquals(expected, actual);
    }
}