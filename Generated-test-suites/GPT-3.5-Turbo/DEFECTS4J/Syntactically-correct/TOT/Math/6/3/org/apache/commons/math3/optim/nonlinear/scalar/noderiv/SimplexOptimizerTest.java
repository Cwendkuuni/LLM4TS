import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal_Minimize_Convergence() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(1000, new MultivariateFunction() {
            public double value(double[] point) {
                // Dummy objective function
                return point[0] * point[0] + point[1] * point[1];
            }
        }, GoalType.MINIMIZE);

        // Check that the result is close to the origin (0, 0)
        Assert.assertTrue(Math.abs(result.getPoint()[0]) < 0.01);
        Assert.assertTrue(Math.abs(result.getPoint()[1]) < 0.01);
    }

    @Test
    public void testOptimizeInternal_Maximize_Convergence() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(1000, new MultivariateFunction() {
            public double value(double[] point) {
                // Dummy objective function
                return -point[0] * point[0] - point[1] * point[1];
            }
        }, GoalType.MAXIMIZE);

        // Check that the result is close to the origin (0, 0)
        Assert.assertTrue(Math.abs(result.getPoint()[0]) < 0.01);
        Assert.assertTrue(Math.abs(result.getPoint()[1]) < 0.01);
    }

    @Test
    public void testOptimizeInternal_Minimize_MaxEvaluationsExceeded_Convergence() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(1, new MultivariateFunction() {
            public double value(double[] point) {
                // Dummy objective function
                return point[0] * point[0] + point[1] * point[1];
            }
        }, GoalType.MINIMIZE);

        // Check that the result is close to the origin (0, 0)
        Assert.assertTrue(Math.abs(result.getPoint()[0]) < 0.01);
        Assert.assertTrue(Math.abs(result.getPoint()[1]) < 0.01);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeInternal_StartPointIsNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(1000, new MultivariateFunction() {
            public double value(double[] point) {
                // Dummy objective function
                return point[0] * point[0] + point[1] * point[1];
            }
        }, GoalType.MINIMIZE, new OptimizationData[]{ new Simplex(null) });
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex(2);
        simplex.setPoints(new double[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();

        // Check that the result is a valid point in the simplex
        Assert.assertTrue(result.getPoint()[0] == 1 || result.getPoint()[0] == 3 || result.getPoint()[0] == 5);
        Assert.assertTrue(result.getPoint()[1] == 2 || result.getPoint()[1] == 4 || result.getPoint()[1] == 6);
    }

}