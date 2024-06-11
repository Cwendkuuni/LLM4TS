import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer;

public class AbstractLeastSquaresOptimizerTest {

    private static final double[] TARGET = {1, 2, 3};
    private static final double[] WEIGHTS = {1, 1, 1};
    private static final double[] START_POINT = {0, 0, 0};

    @Test
    public void testOptimize_withValidInput_returnOptimalPointAndValue() {
        DummyLeastSquaresOptimizer optimizer = new DummyLeastSquaresOptimizer();
        VectorialPointValuePair result = optimizer.optimize(new DummyFunction(), TARGET, WEIGHTS, START_POINT);

        Assert.assertArrayEquals(new double[]{1, 2, 3}, result.getPoint(), 0);
        Assert.assertEquals(0, result.getValue(), 0);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_withInvalidTargetAndWeights_throwOptimizationException() {
        DummyLeastSquaresOptimizer optimizer = new DummyLeastSquaresOptimizer();
        optimizer.optimize(new DummyFunction(), new double[]{1, 2}, new double[]{1, 1, 1}, START_POINT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_withInvalidStartPoint_throwIllegalArgumentException() {
        DummyLeastSquaresOptimizer optimizer = new DummyLeastSquaresOptimizer();
        optimizer.optimize(new DummyFunction(), TARGET, WEIGHTS, new double[]{0, 0});
    }

    private static class DummyLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
        @Override
        protected VectorialPointValuePair doOptimize() {
            return new VectorialPointValuePair(new double[]{1, 2, 3}, 0);
        }
    }

    private static class DummyFunction implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] point) {
            return new double[]{point[0], point[1], point[2]};
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return null;
        }
    }
}