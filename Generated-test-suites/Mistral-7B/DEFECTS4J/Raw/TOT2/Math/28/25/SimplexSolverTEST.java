import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for SimplexSolver.
 */
public class SimplexSolverTest {

    /**
     * Test method for {@link SimplexSolver#SimplexSolver()}.
     */
    @Test
    public void testConstruction() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertNotNull(solver);
    }

    /**
     * Test method for {@link SimplexSolver#SimplexSolver(double, int)}.
     */
    @Test
    public void testConstructionWithParameters() {
        SimplexSolver solver = new SimplexSolver(1.0e-8, 5);
        Assert.assertNotNull(solver);
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test
    public void testOptimizeSimpleExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-3.0, -1.0});
        LinearConstraint[] g = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, ConstraintQuality.EQUALITY),
            new LinearConstraint(new double[]{1.0, 1.0}, 0.0, ConstraintQuality.EQUALITY)
        };
        PointValuePair result = new SimplexSolver().doOptimize(new GOptimizationProblem(f, g, SimpleGoalType.MINIMIZE, null));
        Assert.assertEquals(new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, -5.0 / 3.0), result, 0.0001);
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()} with unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{Double.POSITIVE_INFINITY});
        PointValuePair result = new SimplexSolver().doOptimize(new GOptimizationProblem(f, new LinearConstraint[0], SimpleGoalType.MINIMIZE, null));
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()} with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{Double.NEGATIVE_INFINITY});
        LinearConstraint g1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0, ConstraintQuality.EQUALITY);
        PointValuePair result = new SimplexSolver().doOptimize(new GOptimizationProblem(f, new LinearConstraint[]{g1}, SimpleGoalType.MINIMIZE, null));
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()} with maximum iteration count exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{0.0, 0.0});
        LinearConstraint g1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0, ConstraintQuality.EQUALITY);
        SimplexSolver solver = new SimplexSolver(1.0e-8, 1);
        PointValuePair result = solver.doOptimize(new GOptimizationProblem(f, new LinearConstraint[]{g1}, SimpleGoalType.MINIMIZE, null));
    }
}