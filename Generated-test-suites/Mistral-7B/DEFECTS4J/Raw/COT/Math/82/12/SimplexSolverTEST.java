import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraints;
import org.apache.commons.math3.optim.linear.LinearOptimizationFactory;
import org.apache.commons.math3.optim.linear.LinearSolver;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.special.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001);
        Assert.assertEquals(0.001, solver.epsilon, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() {
        new SimplexSolver(-1.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimpleTableau tableau = new SimpleTableau(2, 3, 3);
        for (int i = 0; i < 3; i++) {
            tableau.setEntry(0, i, -1.0);
        }
        Assert.assertEquals(0, solver.getPivotColumn(tableau).intValue());

        tableau = new SimpleTableau(2, 3, 3);
        for (int i = 0; i < 3; i++) {
            tableau.setEntry(0, i, -i);
        }
        Assert.assertEquals(2, solver.getPivotColumn(tableau).intValue());

        tableau = new SimpleTableau(2, 3, 3);
        for (int i = 0; i < 3; i++) {
            tableau.setEntry(0, i, i);
        }
        Assert.assertEquals(1, solver.getPivotColumn(tableau).intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimpleTableau tableau = new SimpleTableau(3, 3, 3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tableau.setEntry(i, j, -1.0);
            }
        }
        tableau.setEntry(1, 2, 1.0);

        Assert.assertEquals(1, solver.getPivotRow(2, tableau).intValue());
        Assert.assertEquals(2, solver.getPivotRow(1, tableau).intValue());
        Assert.assertEquals(-1, solver.getPivotRow(0, tableau).intValue());

        tableau = new SimpleTableau(3, 3, 3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tableau.setEntry(i, j, -i);
            }
        }
        tableau.setEntry(2, 2, 1.0);

        Assert.assertEquals(2, solver.getPivotRow(2, tableau).intValue());
        Assert.assertEquals(-1, solver.getPivotRow(0, tableau).intValue());
        Assert.assertEquals(-1, solver.getPivotRow(1, tableau).intValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoIteration_NoTableau() {
        try {
            solver.doIteration(null);
        } catch (OptimizationException e) {
            throw new AssertionError(e);
        }
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() {
        SimpleTableau tableau = new SimpleTableau(3, 3, 3);
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, Double.POSITIVE_INFINITY);

        try {
            solver.doIteration(tableau);
        } catch (OptimizationException e) {
            Assert.assertEquals("unbounded solution", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testDoIteration() {
        SimpleTableau tableau = new SimpleTableau(3, 3, 3);
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, 0.0);
        tableau.setEntry(1, 0, -2.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(2, 0, 1.0);
        tableau.setEntry(2, 1, 1.0);
        tableau.setEntry(2, 2, 1.0);

        try {
            solver.doIteration(tableau);
            Assert.assertEquals(1.0 / 3.0, tableau.getEntry(1, 1), 0.0);
            Assert.assertEquals(2.0 / 3.0, tableau.getEntry(1, 2), 0.0);
            Assert.assertEquals(-1.0 / 3.0, tableau.getEntry(2, 1), 0.0);
            Assert.assertEquals(0.0, tableau.getEntry(2, 2), 0.0);
        } catch (OptimizationException e) {
            throw new AssertionError(e);
        }
    }

    @Test(expected = OptimizationException.class)
    public void testIsPhase1Solved_Unsolved() {
        SimpleTableau tableau = new SimpleTableau(2, 3, 3);
        for (int i = 0; i < 3; i++) {
            tableau.setEntry(0, i, -1.0);
        }

        try {
            solver.isPhase1Solved(tableau);
            Assert.fail();
        } catch (OptimizationException e) {
            // expected
        }
    }

    @Test
    public void testIsPhase1Solved_Solved() {
        SimpleTableau tableau = new SimpleTableau(2, 3, 3);
        for (int i = 0; i < 3; i++) {
            tableau.setEntry(0, i, -i);
        }

        try {
            Assert.assertTrue(solver.isPhase1Solved(tableau));
        } catch (OptimizationException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testIsOptimal_Unsolved() {
        SimpleTableau tableau = new SimpleTableau(2, 3, 3);
        for (int i = 0; i < 3; i++) {
            tableau.setEntry(0, i, -1.0);
        }

        try {
            Assert.assertFalse(solver.isOptimal(tableau));
        } catch (OptimizationException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testIsOptimal_Optimal() {
        SimpleTableau tableau = new SimpleTableau(2, 3, 3);
        for (int i = 0; i < 3; i++) {
            tableau.setEntry(0, i, -i);
        }

        try {
            Assert.assertTrue(solver.isOptimal(tableau));
        } catch (OptimizationException e) {
            throw new AssertionError(e);
        }
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1_NoTableau() {
        try {
            solver.solvePhase1(null);
        } catch (OptimizationException e) {
            Assert.assertEquals("tableau is null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1_UnboundedSolution() {
        SimpleTableau tableau = new SimpleTableau(3, 3, 3);
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, Double.POSITIVE_INFINITY);

        try {
            solver.solvePhase1(tableau);
            Assert.fail();
        } catch (OptimizationException e) {
            Assert.assertEquals("unbounded solution", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() {
        SimpleTableau tableau = new SimpleTableau(3, 3, 3);
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, Double.NEGATIVE_INFINITY);

        try {
            solver.solvePhase1(tableau);
            Assert.fail();
        } catch (OptimizationException e) {
            Assert.assertEquals("no feasible solution", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSolvePhase1() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{0.5, 1.5});
        LinearConstraints constraints = LinearOptimizationFactory.parseConstraints("x1 >= 0, x2 >= 0");

        SimpleTableau tableau = new SimpleTableau(f, constraints, SimpleGoalType.MINIMIZE, null, 1.0e-6);

        try {
            solver.solvePhase1(tableau);
            Assert.assertEquals(1.0 / 3.0, tableau.getEntry(1, 1), 0.0);
            Assert.assertEquals(2.0 / 3.0, tableau.getEntry(1, 2), 0.0);
            Assert.assertEquals(-1.0 / 3.0, tableau.getEntry(2, 1), 0.0);
            Assert.assertEquals(0.0, tableau.getEntry(2, 2), 0.0);
            Assert.assertEquals(0.0, tableau.getEntry(0, 0), 0.0);
            Assert.assertEquals(0.0, tableau.getEntry(0, 1), 0.0);
            Assert.assertEquals(0.0, tableau.getEntry(0, 2), 0.0);
            Assert.assertEquals(1.0 / 3.0, tableau.getEntry(1, 0), 0.0);
            Assert.assertEquals(2.0 / 3.0, tableau.getEntry(1, 2), 0.0);
            Assert.assertEquals(-1.0 / 3.0, tableau.getEntry(2, 0), 0.0);
            Assert.assertEquals(0.0, tableau.getEntry(2, 2), 0.0);
        } catch (OptimizationException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testOptimize() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{0.5, 1.5});
        LinearConstraints constraints = LinearOptimizationFactory.parseConstraints("x1 >= 0, x2 >= 0");

        SimpleTableau tableau = null;
        try {
            tableau = (SimpleTableau) solver.doOptimize();
        } catch (Exception e) {
            Assert.fail("Exception while optimizing: " + e.getMessage());
            return;
        }

        PointValuePair solution = new PointValuePair(new double[]{tableau.getEntry(1, 0), tableau.getEntry(1, 1)}, tableau.getEntry(0, 0));
        List<Double> expectedSolution = Arrays.asList(1.0 / 3.0, 2.0 / 3.0);
        List<Double> expectedObjectiveValue = Arrays.asList(0.5);

        Assert.assertEquals(solution.getPoint(), expectedSolution.toArray(), 0.0);
        Assert.assertEquals(solution.getValue(), expectedObjectiveValue.get(0), 0.0);
    }
}