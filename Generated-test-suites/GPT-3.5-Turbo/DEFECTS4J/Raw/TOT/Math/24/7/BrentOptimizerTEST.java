import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithValidArgs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelArg() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsArg() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, -1.0);
    }

    @Test
    public void testDoOptimizeWithMinimizeGoal() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertEquals(0.0, result.getPoint(), 0.0001);
        Assert.assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeWithMaximizeGoal() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertEquals(10.0, result.getPoint(), 0.0001);
        Assert.assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeWithInvalidRange() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        optimizer.setMin(10.0);
        optimizer.setMax(5.0);
        optimizer.setStartValue(8.0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        Assert.assertEquals(8.0, result.getPoint(), 0.0001);
        Assert.assertEquals(Double.NaN, result.getValue(), 0.0001);
    }

    // Additional test cases for other methods can be added as needed
    
}