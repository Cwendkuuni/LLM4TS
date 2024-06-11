import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test function f(x) = x^2
        UnivariateRealFunction functionSquared = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        PointValuePair solution = optimizer.optimize(functionSquared, GoalType.MINIMIZE, new SimpleBounds(-10.0, 10.0), null);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-6);
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);

        // Test function f(x) = |x|
        UnivariateRealFunction functionAbs = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x);
            }
        };

        solution = optimizer.optimize(functionAbs, GoalType.MINIMIZE, new SimpleBounds(-10.0, 10.0), null);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-6);
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);

        // Test function f(x) = sin(x)
        UnivariateRealFunction functionSin = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        solution = optimizer.optimize(functionSin, GoalType.MINIMIZE, new SimpleBounds(-10.0, 10.0), null);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-3);
        Assert.assertEquals(-1.273212455926177, solution.getValue(), 1e-3);

        // Test function f(x) = x^3
        UnivariateRealFunction functionCubed = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x;
            }
        };

        solution = optimizer.optimize(functionCubed, GoalType.MINIMIZE, new SimpleBounds(-10.0, 10.0), null);
        Assert.assertEquals(-1.0, solution.getPoint(), 1e-6);
        Assert.assertEquals(-1.0, solution.getValue(), 1e-6);
    }
}