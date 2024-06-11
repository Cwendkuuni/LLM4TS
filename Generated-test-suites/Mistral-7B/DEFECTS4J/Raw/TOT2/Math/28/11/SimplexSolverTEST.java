package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalTypeConstraint;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test solver with a simple optimization problem.
     * @throws MaxCountExceededException if the solver exceeds the maximum number of iterations
     */
    @Test
    public void testSimpleProblem() throws MaxCountExceededException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[] {-5.0, -2.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[] {1.0, 1.0}, 2.0),
            new LinearConstraint(new double[] {2.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {1.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {2.0, 1.0}, -1.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair result = solver.optimize(objective, constraints, goalType);
        Assert.assertEquals(-5.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(-7.0, result.getValue(), 1e-6);
    }

    /**
     * Test solver with a problem with no feasible solution.
     * @throws MaxCountExceededException if the solver exceeds the maximum number of iterations
     * @throws UnboundedSolutionException if the problem has no bounded solution
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testProblemWithNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[] {-5.0, -2.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[] {1.0, 1.0}, 3.0),
            new LinearConstraint(new double[] {2.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {1.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {2.0, 1.0}, -1.0)
        };
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(objective, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test solver with a problem having no optimal solution.
     * @throws MaxCountExceededException if the solver exceeds the maximum number of iterations
     */
    @Test(expected = MaxCountExceededException.class)
    public void testProblemWithNoOptimalSolution() throws MaxCountExceededException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}}, new double[] {-5.0, -2.0, -1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[] {1.0, 1.0, 0.0}, 2.0),
            new LinearConstraint(new double[] {1.0, 2.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {0.0, 1.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {2.0, 1.0, 1.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(objective, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test solver with a degenerate problem.
     * @throws MaxCountExceededException if the solver exceeds the maximum number of iterations
     */
    @Test
    public void testDegenerateProblem() throws MaxCountExceededException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[] {-5.0, -2.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[] {1.0, 1.0}, 2.0),
            new LinearConstraint(new double[] {2.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {1.0, 1.0}, 1.0),
            new LinearConstraint(new double[] {2.0, 1.0}, -1.0)
        };
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair result = solver.optimize(objective, constraints, GoalType.MINIMIZE);
        Assert.assertEquals(-5.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(-7.0, result.getValue(), 1e-6);
    }
}