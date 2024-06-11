import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testDefaultConstructor() {
        solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.getEpsilon(), 0);
        Assert.assertEquals(10, solver.getMaxUlps());
    }

    @Test
    public void testCustomConstructor() {
        double epsilon = 1.0e-8;
        int maxUlps = 5;
        solver = new SimplexSolver(epsilon, maxUlps);
        Assert.assertEquals(epsilon, solver.getEpsilon(), 0);
        Assert.assertEquals(maxUlps, solver.getMaxUlps());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPivotColumnThrowsOnEmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0}), new LinearConstraintSet());
        solver.getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotColumnReturnsCorrectValue() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2.0, -1.0, -3.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 3.0));
        constraints.addConstraint(new LinearConstraint(new double[]{2.0, -1.0}, Relation.GREATER_EQUAL, 0.0));
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        Assert.assertEquals(2, solver.getPivotColumn(tableau).intValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPivotRowThrowsOnInvalidColumn() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0, 4.0, 3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, -1.0, 2.0}, Relation.LEQUAL, 3.0));
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 2.0, 3.0}, Relation.LEQUAL, 1.0);
        constraints.addConstraint(constraint);
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        solver.getPivotRow(tableau, 4);
    }

    @Test
    public void testGetPivotRowReturnsCorrectValue() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2.0, -1.0, -3.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 3.0));
        LinearConstraint constraint = new LinearConstraint(new double[]{2.0, -1.0}, Relation.GREATER_EQUAL, 0.0);
        constraints.addConstraint(constraint);
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        Assert.assertEquals(1, solver.getPivotRow(tableau, 2).intValue());
    }

    @Test
    public void testDoIterationThrowsOnUnboundedSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2.0, -1.0, -3.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 3.0));
        LinearConstraint constraint = new LinearConstraint(new double[]{2.0, -1.0}, Relation.GREATER_EQUAL, 0.0);
        constraints.addConstraint(constraint);
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        try {
            solver.doIteration(tableau);
            Assert.fail();
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testDoIterationMakesCorrectChanges() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{2.0, 4.0, 4.0, 0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 3.0));
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, -1.0}, Relation.GREATER_EQUAL, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0}, Relation.GREATER_EQUAL, 1.0);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        solver.doIteration(tableau);
        Assert.assertEquals(4.0 / 5.0, tableau.getEntry(0, 2), solver.maxUlps);
        Assert.assertEquals(0.0, tableau.getEntry(1, 1), solver.maxUlps);
        Assert.assertEquals(2.0 / 5.0, tableau.getEntry(1, 2), solver.maxUlps);
    }

    @Test
    public void testSolvePhase1ThrowsOnMaxIterations() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, -1.0, -1.0}, Relation.LEQUAL, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0, -1.0}, Relation.LEQUAL, 0.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{1.0, 1.0, 1.0, -1.0}, Relation.LEQUAL, 0.0);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        constraints.addConstraint(constraint3);
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        try {
            solver.solvePhase1(tableau);
            Assert.fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testSolvePhase1ReturnsCorrectResult() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, -2.0, 3.0, 4.0, -2.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, Relation.GREATER_EQUAL, 0.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{1.0, 1.0, 1.0, -1.0}, Relation.LEQUAL, 0.0);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        constraints.addConstraint(constraint3);
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        solver.solvePhase1(tableau);
        Assert.assertEquals(2.0, tableau.getEntry(0, tableau.getWidth() - 1), solver.maxUlps);
    }

    @Test
    public void testDoOptimizeReturnsCorrectResult() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, -2.0, 3.0, 4.0, -2.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, Relation.GREATER_EQUAL, 0.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{1.0, 1.0, 1.0, -1.0}, Relation.LEQUAL, 0.0);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        constraints.addConstraint(constraint3);
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        PointValuePair result = solver.doOptimize();
        Assert.assertEquals(-2.0, result.getPoint()[0], solver.maxUlps);
        Assert.assertEquals(-5.0, result.getValue(), solver.maxUlps);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeThrowsOnEmptyFunction() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{});
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexTableau tableau = new SimplexTableau(obj, constraints.getConstraints(), null);
        solver.doOptimize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeThrowsOnEmptyConstraints() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0, 3.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexTableau tableau = new SimplexTableau(obj, new LinearConstraint[0], null);
        solver.doOptimize();
    }
}