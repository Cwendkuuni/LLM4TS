package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    /**
     * Test that an empty problem is solved correctly.
     */
    @Test
    public void testEmptyProblem() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{}, 0);
        PointValuePair solution = solver.optimize(objective);
        Assert.assertEquals(new SimplePointValuePair(new double[0], 0), solution);
    }

    /**
     * Test that the solver finds the optimal solution for a simple linear program.
     */
    @Test
    public void testSimpleLinearProgram() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        for (int i = 1; i < coefficients.length; i++) {
            LinearConstraint constraint = new LinearConstraint(coefficients[i], constants[i], 0.0);
            solver.addConstraint(constraint);
        }

        PointValuePair solution = solver.optimize();
        double[] expectedSolution = new double[]{1.0 / 4.0, 1.0 / 2.0, 1.0};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        Assert.assertEquals(2.5, solution.getValue(), 1e-6);
    }

    /**
     * Test that an unbounded solution is correctly detected and an exception is thrown.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        for (int i = 1; i < coefficients.length; i++) {
            LinearConstraint constraint = new LinearConstraint(coefficients[i], constants[i], 0.0);
            solver.addConstraint(constraint);
        }

        solver.setMaxIterations(1);
        solver.optimize();
    }

    /**
     * Test that a no feasible solution is correctly detected and an exception is thrown.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        for (int i = 1; i < coefficients.length; i++) {
            LinearConstraint constraint = new LinearConstraint(coefficients[i], constants[i], 1.0);
            solver.addConstraint(constraint);
        }

        PointValuePair solution = solver.optimize();
    }

    /**
     * Test that the solver correctly handles constraints with non-negative variables.
     */
    @Test
    public void testConstraintsWithNonNegativeVariables() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        for (int i = 1; i < coefficients.length; i++) {
            LinearConstraint constraint = new LinearConstraint(coefficients[i], constants[i], 0.0);
            solver.addConstraint(constraint);
            solver.setRestrictToNonNegative(true);
        }

        PointValuePair solution = solver.optimize();
        double[] expectedSolution = new double[]{1.0 / 4.0, 0.0, 1.0 / 4.0};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        Assert.assertEquals(2.25, solution.getValue(), 1e-6);
    }

    /**
     * Test that the solver correctly handles constraints with non-zero right hand sides.
     */
    @Test
    public void testConstraintsWithNonZeroRightHandSides() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}, {3.0, 0.0, 0.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0, 1.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        for (int i = 1; i < coefficients.length; i++) {
            LinearConstraint constraint = new LinearConstraint(coefficients[i], constants[i], 0.0);
            solver.addConstraint(constraint);
        }

        PointValuePair solution = solver.optimize();
        double[] expectedSolution = new double[]{1.0 / 4.0, 1.0 / 2.0, 1.0 / 4.0};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        Assert.assertEquals(2.5, solution.getValue(), 1e-6);
    }

    /**
     * Test that the solver correctly handles constraints with equalities.
     */
    @Test
    public void testConstraintsWithEqualities() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}, {3.0, -2.0, 0.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0, 1.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        for (int i = 1; i < coefficients.length; i++) {
            LinearConstraint constraint;
            if (i == 2) {
                constraint = new LinearConstraint(coefficients[i], constants[i], 0.0);
                constraint.setType(LinearConstraint.Type.EQUAL);
            } else {
                constraint = new LinearConstraint(coefficients[i], constants[i], 0.0);
            }
            solver.addConstraint(constraint);
        }

        PointValuePair solution = solver.optimize();
        double[] expectedSolution = new double[]{1.0 / 4.0, 1.0 / 2.0, 0.5};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        Assert.assertEquals(2.25, solution.getValue(), 1e-6);
    }

    /**
     * Test that the solver correctly handles constraints with inequalities.
     */
    @Test
    public void testConstraintsWithInequalities() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}, {3.0, -2.0, 1.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0, 2.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        for (int i = 1; i < coefficients.length; i++) {
            LinearConstraint constraint;
            if (i == 2) {
                constraint = new LinearConstraint(coefficients[i], constants[i], Double.POSITIVE_INFINITY);
            } else {
                constraint = new LinearConstraint(coefficients[i], constants[i], 0.0);
            }
            solver.addConstraint(constraint);
        }

        PointValuePair solution = solver.optimize();
        double[] expectedSolution = new double[]{1.0 / 4.0, 1.0 / 2.0, 1.0 / 4.0};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        Assert.assertEquals(2.5, solution.getValue(), 1e-6);
    }

    /**
     * Test that the solver correctly handles problems with multiple objectives.
     */
    @Test
    public void testMultipleObjectives() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}, {3.0, -2.0, 0.0}, {-1.0, 1.0, 0.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0, 1.0, 2.0};
        LinearObjectiveFunction objective1 = new LinearObjectiveFunction(coefficients[0], constants[0]);
        LinearObjectiveFunction objective2 = new LinearObjectiveFunction(coefficients[4], constants[4]);
        solver.setGoalType(GoalType.MINIMIZE_SUM);
        PointValuePair solution = solver.optimize(objective1, objective2);
        double[] expectedSolution1 = new double[]{1.0 / 4.0, 1.0 / 2.0, 0.5};
        double[] expectedSolution2 = new double[]{1.0 / 4.0, 1.0, 0.5};
        Assert.assertArrayEquals(expectedSolution1, solution.getPoint1(), 1e-6);
        Assert.assertEquals(1.75, solution.getValue1(), 1e-6);
        Assert.assertArrayEquals(expectedSolution2, solution.getPoint2(), 1e-6);
        Assert.assertEquals(3.75, solution.getValue2(), 1e-6);
    }

    /**
     * Test that the solver correctly handles problems with an unbounded objective.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testUnboundedObjective() {
        double[][] coefficients = new double[][]{{2.0, 1.0, -1.0}, {-3.0, 2.0, 1.0},
            {-1.0, -1.0, 1.0}, {3.0, -2.0, 0.0}};
        double[] constants = new double[]{3.0, -5.0, 1.0, 1.0};
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients[0], constants[0]);
        solver.setMaxIterations(1);
        solver.optimize(objective);
    }
}