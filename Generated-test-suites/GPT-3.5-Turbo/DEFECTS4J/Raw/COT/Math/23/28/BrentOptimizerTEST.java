import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.*;

public class BrentOptimizerTest {

    // Test case for constructor with convergence checker
    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new DefaultConvergenceChecker<>();
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, checker);
        Assert.assertEquals(0.1, optimizer.getRelativeThreshold(), 0.01);
        Assert.assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.01);
    }

    // Test case for constructor without convergence checker
    @Test
    public void testConstructorWithoutConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        Assert.assertEquals(0.1, optimizer.getRelativeThreshold(), 0.01);
        Assert.assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.01);
    }

    // Test the best() method when a is null and b has a lower value
    @Test
    public void testBestWithNullAAndLowerBValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 0.5);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(b, result);
    }

    // Test the best() method when a has a lower value than b
    @Test
    public void testBestWithLowerAValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.7);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(a, result);
    }

    // Test the best() method when a has a higher value than b
    @Test
    public void testBestWithHigherAValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.7);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(b, result);
    }

    // Test the best() method when a and b have the same value
    @Test
    public void testBestWithSameValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(a, result);
    }

}