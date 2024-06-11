import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.CalculusValueInitializer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test with a simple quadratic function f(x) = x^2
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateOptimizer result = optimizer.optimize(function, new CalculusValueInitializer(2.0));

        PointValuePair expected = new PointValuePair(1.0, 0.0);
        Assert.assertEquals(expected.getPoint(), result.getPoint(), 1e-5);
        Assert.assertEquals(expected.getValue(), result.getValue(), 1e-5);

        // Test with a function that has no derivative
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(Math.sin(x)));
            }
        };

        optimizer = new BrentOptimizer(1e-5, 1e-5);
        result = optimizer.optimize(function, new CalculusValueInitializer(0.0));

        Assert.assertEquals(Math.PI / 2, result.getPoint(), 1e-5);
        Assert.assertEquals(1.0, result.getValue(), 1e-5);
    }
}