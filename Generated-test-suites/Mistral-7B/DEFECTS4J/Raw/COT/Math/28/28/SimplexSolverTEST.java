import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optimization.SimpleLinearConstraint;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SimpleCase() throws Exception {
        LinearObjectiveFunction f = new SimpleLinearObjectiveFunction(new double[]{-1.0, -3.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new SimpleLinearConstraint(new double[]{1.0, 2.0}, 3.0),
            new SimpleLinearConstraint(new double[]{1.0, 2.0}, 5.0)
        };
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{3.0 / 2.0, 1.5}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimize_FeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new SimpleLinearObjectiveFunction(new double[]{2.0, -1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new SimpleLinearConstraint(new double[]{1.0, 1.0}, 2.0),
            new SimpleLinearConstraint(new double[]{1.0, -1.0}, 3.0)
        };
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new SimpleLinearObjectiveFunction(new double[]{0.0, -1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new SimpleLinearConstraint(new double[]{1.0, 1.0}, 0.0)
        };
        SimplexSolver solver = new SimplexSolver();

        solver.doOptimize();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxIterationsExceeded() throws Exception {
        LinearObjectiveFunction f = new SimpleLinearObjectiveFunction(new double[]{0.0, -1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new SimpleLinearConstraint(new double[]{1.0, 1.0}, 0.0)
        };
        SimplexSolver solver = new SimplexSolver(1, 0);

        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new SimpleLinearObjectiveFunction(new double[]{0.0, -1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new SimpleLinearConstraint(new double[]{1.0, 1.0}, 2.0),
            new SimpleLinearConstraint(new double[]{0.0, 1.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();

        solver.doOptimize();
    }

}