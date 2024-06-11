import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private SimplexOptimizer simplexOptimizer;

    @Before
    public void setup() {
        simplexOptimizer = new SimplexOptimizer();
    }

    @Test
    public void testOptimizeInternal_WithValidParameters_OptimizeObjectiveFunction() {
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                double result = 0;
                for (int i = 0; i < point.length; i++) {
                    result += point[i] * point[i];
                }
                return result;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = simplexOptimizer.optimizeInternal(maxEval, f, goalType);

        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_WithoutSimplex_SetSimplexToNull() {
        simplexOptimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_WithSimplex_DoOptimization() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        simplex.add(new double[]{1.0, 2.0}, 10.0);
        simplex.add(new double[]{3.0, 4.0}, 20.0);
        simplex.add(new double[]{5.0, 6.0}, 30.0);
        simplexOptimizer.setSimplex(simplex);

        PointValuePair result = simplexOptimizer.doOptimize();

        double[] expectedPoint = {1.0, 2.0};
        double expectedValue = 10.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
    }

}