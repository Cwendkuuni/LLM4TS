import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.univariate.BrentOptimizer.GOLDEN_SECTION;
import static org.apache.commons.math3.optim.univariate.BrentOptimizer.MIN_RELATIVE_TOLERANCE;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);

        UnivariatePointValuePair result = optimizer.optimize(function, 2.0, 4.0);

        double expected = 3.0;
        Assert.assertEquals(expected, result.getPoint(), 0.001);
        Assert.assertEquals(expected * Math.exp(-1), result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, new MyConvergenceChecker());

        UnivariatePointValuePair result = optimizer.optimize(function, 2.0, 4.0);

        double expected = 3.0;
        Assert.assertEquals(expected, result.getPoint(), 0.001);
        Assert.assertEquals(expected * Math.exp(-1), result.getValue(), 0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.001, -0.01);
    }

    @Test(expected = IllegalStateException.class)
    public void testBrentOptimizerWithoutFunctionOrInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
    }

    @Test(expected = IllegalStateException.class)
    public void testBrentOptimizerWithInvalidInterval() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, function);
        optimizer.setMin(4.0);
        optimizer.setMax(2.0);
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.exp(-1) * Math.pow(x - 3.0, 2);
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return iteration > 100 || FastMath.abs(current.getValue()) <= MIN_RELATIVE_TOLERANCE * Math.exp(-1);
        }
    }
}