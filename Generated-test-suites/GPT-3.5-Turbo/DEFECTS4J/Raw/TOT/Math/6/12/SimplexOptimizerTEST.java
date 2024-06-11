import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;
    private double relThreshold;
    private double absThreshold;

    @Before
    public void setUp() {
        relThreshold = 1e-10;
        absThreshold = 1e-12;
        optimizer = new SimplexOptimizer(relThreshold, absThreshold);
    }

    @Test
    public void testOptimizeInternal() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];  // f(x, y) = x^2 + y^2
            }
        };

        PointValuePair expected = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        PointValuePair result = optimizer.optimizeInternal(1000, function, GoalType.MINIMIZE);
        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), absThreshold);
        Assert.assertEquals(expected.getValue(), result.getValue(), absThreshold);
    }

    @Test
    public void testDoOptimize() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];  // f(x, y) = x^2 + y^2
            }
        };

        PointValuePair expected = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        PointValuePair result = optimizer.doOptimize(1000, function, GoalType.MINIMIZE);
        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), absThreshold);
        Assert.assertEquals(expected.getValue(), result.getValue(), absThreshold);
    }

    @Test
    public void testSimplexOptimizer() {
        Assert.assertNotNull(optimizer);
        Assert.assertTrue(optimizer.getConvergenceChecker() instanceof SimpleValueChecker);
        Assert.assertEquals(relThreshold, ((SimpleValueChecker) optimizer.getConvergenceChecker()).getRelativeThreshold(), absThreshold);
        Assert.assertEquals(absThreshold, ((SimpleValueChecker) optimizer.getConvergenceChecker()).getAbsoluteThreshold(), absThreshold);
    }

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new Simplex(new double[]{1, 2}, new double[]{2, 3}, new double[]{3, 4});
        optimizer.setSimplex(simplex);
        Assert.assertEquals(simplex, optimizer.getSimplex());
    }

}