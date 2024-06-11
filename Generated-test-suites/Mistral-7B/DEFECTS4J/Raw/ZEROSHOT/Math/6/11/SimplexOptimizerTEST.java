import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.Precision.EPSILON;
import static org.apache.commons.math3.optim.Precision.SAFE_MIN;

public class SimplexOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1, null, GoalType.MINIMIZE, new double[0]);
    }

    @Test
    public void testSimpleValueChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(EPSILON, SAFE_MIN));
        MultivariateFunction function = new SineFunction(2);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, new double[0]);
        double expected = Math.PI;
        Assert.assertEquals(expected, result.getValue(), EPSILON);
        Assert.assertArrayEquals(new double[]{Math.sin(Math.PI), Math.sin(Math.PI)}, result.getPoint(), EPSILON);
    }

    @Test
    public void testRelativeAndAbsoluteThresholds() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001d, 0.01d);
        MultivariateFunction function = new SineFunction(2);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, new double[0]);
        double expected = Math.PI;
        Assert.assertEquals(expected, result.getValue(), 0.001d + 0.01d); // Tolerance is the sum of both thresholds
        Assert.assertArrayEquals(new double[]{Math.sin(Math.PI), Math.sin(Math.PI)}, result.getPoint(), EPSILON);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(new double[]{-1.0, -1.0}, 2);
        optimizer.setSimplex(simplex);
        MultivariateFunction function = new SineFunction(2);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, new double[0]);
        double expected = Math.PI;
        Assert.assertEquals(expected, result.getValue(), EPSILON);
        Assert.assertArrayEquals(new double[]{Math.sin(Math.PI), Math.sin(Math.PI)}, result.getPoint(), EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
        MultivariateFunction function = new SineFunction(2);
        optimizer.optimize(10, function, GoalType.MINIMIZE, new double[0]);
    }

    @Test
    public void testNoOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new SineFunction(2);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, new double[0]);
        double expected = Math.PI;
        Assert.assertEquals(expected, result.getValue(), EPSILON);
        Assert.assertArrayEquals(new double[]{Math.sin(Math.PI), Math.sin(Math.PI)}, result.getPoint(), EPSILON);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStartPoint() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new SineFunction(2);
        optimizer.optimize(10, function, GoalType.MINIMIZE, null);
    }

    @Test
    public void testValidInput() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(new double[]{0.0, 0.0}, 2);
        optimizer.setSimplex(simplex);
        MultivariateFunction function = new SineFunction(2);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
        double expected = Math.PI;
        Assert.assertEquals(expected, result.getValue(), EPSILON);
        Assert.assertArrayEquals(new double[]{Math.sin(Math.PI), Math.sin(Math.PI)}, result.getPoint(), EPSILON);
    }

    private static class SineFunction implements MultivariateFunction {

        private final int dimension;

        public SineFunction(int dimension) {
            this.dimension = dimension;
        }

        @Override
        public double value(double[] point) {
            double sum = 0.0;
            for (int i = 0; i < dimension; i++) {
                sum += Math.pow(Math.sin(point[i]), 2);
            }
            return sum;
        }
    }
}