import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create a BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        // Test doOptimize() method
        // #1: Test with a simple linear function f(x) = 2x + 1
        optimizer.setObjectiveFunction(x -> 2 * x + 1);
        UnivariatePointValuePair result1 = optimizer.doOptimize();
        Assert.assertEquals(-0.5, result1.getPoint(), 1e-6);
        Assert.assertEquals(0, result1.getValue(), 1e-6);

        // #2: Test with a quadratic function f(x) = x^2 - 4x + 4
        optimizer.setObjectiveFunction(x -> x * x - 4 * x + 4);
        UnivariatePointValuePair result2 = optimizer.doOptimize();
        Assert.assertEquals(2, result2.getPoint(), 1e-6);
        Assert.assertEquals(0, result2.getValue(), 1e-6);

        // #3: Test with a cubic function f(x) = x^3 - 27
        optimizer.setObjectiveFunction(x -> x * x * x - 27);
        UnivariatePointValuePair result3 = optimizer.doOptimize();
        Assert.assertEquals(3, result3.getPoint(), 1e-6);
        Assert.assertEquals(0, result3.getValue(), 1e-6);
        }

    @Test
    public void testBest() {
        // Create BrentOptimizerTest instance
        BrentOptimizerTest test = new BrentOptimizerTest();

        // Test best() method
        UnivariatePointValuePair point1 = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair point2 = new UnivariatePointValuePair(2, 5);
        UnivariatePointValuePair result1 = test.best(point1, point2, true);
        UnivariatePointValuePair result2 = test.best(point1, point2, false);
        Assert.assertEquals(2, result1.getPoint(), 1e-6);
        Assert.assertEquals(2, result2.getPoint(), 1e-6);
        Assert.assertEquals(5, result1.getValue(), 1e-6);
        Assert.assertEquals(10, result2.getValue(), 1e-6);
    }
}