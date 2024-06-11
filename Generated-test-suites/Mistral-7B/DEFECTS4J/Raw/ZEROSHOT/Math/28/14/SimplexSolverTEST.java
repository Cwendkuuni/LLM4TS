import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunctionSet;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0}, new double[]{0.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 0.0);
        Assert.assertNull(result.getPoint());
    }

    @Test
    public void testZeroObjective() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{0.0}, new double[]{0.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.0}, result.getPoint(), 0.0);
    }

    @Test
    public void testSimpleConstraint() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, 2.0});
        LinearConstraint c = new LinearConstraintSet(new double[]{-1.0, 1.0}, 0.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f, c);
        Assert.assertEquals(1.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0}, result.getPoint(), 0.0);
    }

    @Test
    public void testNegativeConstraint() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, 2.0});
        LinearConstraint c = new LinearConstraintSet(new double[]{-1.0, 1.0}, 1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f, c);
        Assert.assertEquals(-1.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{-1.0}, result.getPoint(), 0.0);
    }

    @Test
    public void testMultipleConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, 2.0, 3.0});
        LinearConstraintSet cs = new LinearConstraintSet(new LinearConstraint[]{
                new LinearConstraintSet(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraintSet(new double[]{1.0, -1.0}, 1.0),
                new LinearConstraintSet(new double[]{1.0, 1.0}, 2.0)
        });
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f, cs);
        Assert.assertEquals(1.0 / 3.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, 2.0});
        SimplexSolver solver = new SimplexSolver(0.0, 0);
        solver.optimize(f);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, Double.POSITIVE_INFINITY});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, -1.0});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f);
    }

    @Test
    public void testGoalType() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, 2.0});
        SimplexSolver solver = new SimplexSolver(new SimpleGoalType(GoalType.MINIMIZE));
        PointValuePair result = solver.optimize(f);
        Assert.assertEquals(-1.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{-1.0}, result.getPoint(), 0.0);

        solver = new SimplexSolver(new SimpleGoalType(GoalType.MAXIMIZE));
        result = solver.optimize(f);
        Assert.assertEquals(1.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0}, result.getPoint(), 0.0);
    }

    @Test
    public void testRestrictToNonNegative() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-1.0, 1.0}, new double[]{0.0, 2.0});
        SimplexSolver solver = new SimplexSolver(new SimpleGoalType(GoalType.MINIMIZE), true);
        PointValuePair result = solver.optimize(f);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.0}, result.getPoint(), 0.0);
    }

}