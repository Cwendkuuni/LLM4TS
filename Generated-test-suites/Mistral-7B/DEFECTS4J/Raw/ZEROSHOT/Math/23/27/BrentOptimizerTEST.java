import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.simple.SimpleValueFunction;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimum() {
        UnivariateFunction function = new SimpleValueFunction(Math::pow, 2.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, 1.5);
        Assert.assertEquals(0.5, result.getPoint(), 1e-6);
        Assert.assertEquals(-6.25, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximum() {
        UnivariateFunction function = new SimpleValueFunction(Math::pow, -2.5);
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, 1.5);
        Assert.assertEquals(1.5, result.getPoint(), 1e-6);
        Assert.assertEquals(15.625, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        double relThreshold = 1e-5;
        double absoluteThreshold = -1e-5;
        new BrentOptimizer(relThreshold, absoluteThreshold);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        double relThreshold = 1e-10;
        double absoluteThreshold = 1e-5;
        new BrentOptimizer(relThreshold, absoluteThreshold);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullFunction() {
        UnivariateFunction function = null;
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.optimize(function, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidInterval() {
        UnivariateFunction function = x -> Math.pow(x, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.optimize(function, 1, 0);
    }

    @Test
    public void testBrentOptimizerUserDefinedConvergenceChecker() {
        UnivariateFunction function = Math::sin;
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = (iter, last, current) -> (current.getValue() - last.getValue()) < 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, convergenceChecker);
        UnivariatePointValuePair result = optimizer.optimize(function, 0, Math.PI);
        Assert.assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
        Assert.assertEquals(1, result.getValue(), 1e-6);
    }
}