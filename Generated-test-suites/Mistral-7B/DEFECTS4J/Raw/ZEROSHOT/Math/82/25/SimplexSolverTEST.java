package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import org.apache.commons.math.optimization.linear.GoalType;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.MaxEval;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;

/**
 * Tests the {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /**
     * Test with a simple linear optimization problem.
     */
    @Test
    public void testSimpleLinearOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 4}, new double[]{2, 3});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1, 2}, 5, Relationship.LEQ),
            new LinearConstraint(new double[]{3, 1}, 4, Relationship.LEQ),
            new LinearConstraint(new double[]{-2, 1}, 3, Relationship.LEQ),
            new LinearConstraint(new double[]{1, -3}, 2, Relationship.LEQ)
        };
        GoalType goalType = GoalType.MINIMIZE;

        SimplexSolver solver = new SimplexSolver();
        try {
            RealPointValuePair solution = solver.optimize(new MaxEval(f, constraints), goalType);
            assertEquals(0, solution.getPoint()[0], 0.001);
            assertEquals(5, solution.getValue(), 0.001);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test with an unbounded linear optimization problem.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedLinearOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0, 1}, new double[]{1, 1});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(new MaxEval(f, constraints), goalType);
    }

    /**
     * Test with a linear optimization problem with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleLinearOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 1}, new double[]{0, 1});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1, 2}, 5, Relationship.LEQ),
            new LinearConstraint(new double[]{3, 1}, 4, Relationship.LEQ),
            new LinearConstraint(new double[]{-2, 1}, 3, Relationship.LEQ),
            new LinearConstraint(new double[]{1, -3}, 2, Relationship.LEQ)
        };
        GoalType goalType = GoalType.MINIMIZE;

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(new MaxEval(f, constraints), goalType);
    }

    /**
     * Test with a linear optimization problem with a maximal number of iterations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsLinearOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0, 0}, new double[]{0, 0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;

        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(new MaxEval(f, constraints), goalType);
    }

}