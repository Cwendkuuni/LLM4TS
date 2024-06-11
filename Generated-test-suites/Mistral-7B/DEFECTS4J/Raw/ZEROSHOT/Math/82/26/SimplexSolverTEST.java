import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimizeWithPositiveObjectiveFunction() throws OptimizationException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver simplexSolver = new SimplexSolver();
        RealPointValuePair result = simplexSolver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{1.0 / 3.0, 1.0}, -1.0 / 3.0);
        Assert.assertEquals(expected.getValue(), result.getValue(), 0.001);
        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeWithNegativeObjectiveFunction() throws OptimizationException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.0, 3.0}, new double[]{-1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver simplexSolver = new SimplexSolver();
        RealPointValuePair result = simplexSolver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{1.0 / 3.0, 1.0}, 1.0 / 3.0);
        Assert.assertEquals(expected.getValue(), result.getValue(), 0.001);
        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), 0.001);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeWithNoFeasibleSolution() throws OptimizationException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.0, 3.0}, new double[]{-1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{-1.0, 1.0}, 0.0, "<=", Double.POSITIVE_INFINITY)};
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeWithUnboundedSolution() throws OptimizationException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{0.0, 0.0}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0}, 0.0, "=", 0.0)};
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.doOptimize();
    }
}