import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        // Test with default coefficients
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{10.0, 10.0});
        double[] result = optimizer.optimize(new TestFunction(), x, bounds);
        Assert.assertEquals(new double[]{0.0, 0.0}, result, 0.001);

        // Test with custom coefficients
        MultiDirectional optimizerCustom = new MultiDirectional(3.0, 0.6);
        double[] xCustom = {1.5, 2.5};
        double[] resultCustom = optimizerCustom.optimize(new TestFunction(), xCustom, bounds);
        Assert.assertEquals(new double[]{0.0, 0.0}, resultCustom, 0.001);
    }

    // Test helper class for the optimize method
    private static class TestFunction implements org.apache.commons.math3.optim.UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }
}