import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1: Minimum value
        BrentOptimizer optimizer1 = new BrentOptimizer(0.0001, 0.0001);
        optimizer1.setMin(-10);
        optimizer1.setStartValue(0);
        optimizer1.setMax(10);
        UnivariatePointValuePair result1 = optimizer1.doOptimize();
        Assert.assertEquals(result1.getPoint(), 0.0, 0.0001);
        Assert.assertEquals(result1.getValue(), -2.0, 0.0001);

        // Test case 2: Maximum value
        BrentOptimizer optimizer2 = new BrentOptimizer(0.0001, 0.0001);
        optimizer2.setMin(-10);
        optimizer2.setStartValue(0);
        optimizer2.setMax(10);
        optimizer2.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        Assert.assertEquals(result2.getPoint(), 0.0, 0.0001);
        Assert.assertEquals(result2.getValue(), 2.0, 0.0001);
    }

    @Test
    public void testBest() {
        // Test case 1: a is null, b is not null, minimization
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 2.0);
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(result, b);

        // Test case 2: a is not null, b is null, minimization
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = null;
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(result, a);

        // Test case 3: a and b are null, minimization
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertNull(result);

        // Test case 4: a and b are not null, minimization
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(result, b);

        // Test case 5: a is null, b is not null, maximization
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 2.0);
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        Assert.assertEquals(result, b);

        // Test case 6: a is not null, b is null, maximization
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = null;
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        Assert.assertEquals(result, a);

        // Test case 7: a and b are null, maximization
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        Assert.assertNull(result);

        // Test case 8: a and b are not null, maximization
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        Assert.assertEquals(result, a);
    }
}