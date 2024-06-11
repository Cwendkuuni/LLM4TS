import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        // Test with a simple linear function
        UnivariateFunction linearFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 3 * x + 5;
            }
        };
        
        // Test lower bound
        UnivariatePointValuePair result = optimizer.optimize(100, 200, linearFunction);
        Assert.assertEquals(100.0, result.getPoint(), 1e-6);
        Assert.assertEquals(305.0, result.getValue(), 1e-6);
        
        // Test upper bound
        result = optimizer.optimize(100, 200, linearFunction);
        Assert.assertEquals(200.0, result.getPoint(), 1e-6);
        Assert.assertEquals(605.0, result.getValue(), 1e-6);
        
        // Test other point
        result = optimizer.optimize(150, 250, linearFunction);
        Assert.assertEquals(150.0, result.getPoint(), 1e-6);
        Assert.assertEquals(455.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testDoOptimizeWithInvalidBoundaries() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        // Test when upper bound is smaller than lower bound
        UnivariateFunction linearFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 3 * x + 5;
            }
        };
        
        UnivariatePointValuePair result = optimizer.optimize(200, 100, linearFunction);
        Assert.assertEquals(100.0, result.getPoint(), 1e-6);
        Assert.assertEquals(305.0, result.getValue(), 1e-6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        UnivariatePointValuePair result = optimizer.optimize(100, 200, null);
    }
    
    // Add more test cases for edge cases and error scenarios.
    
}