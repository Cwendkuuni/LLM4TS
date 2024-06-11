import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
        Assert.assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.001);
        Assert.assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.001);
    }

    @Test
    public void testDoOptimizeThrowsUnsupportedOperationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAccuracies() {
        new BrentOptimizer(-0.01, 0.01);
    }

    @Test
    public void testMinimization() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = 0.5;
        double max = 1.5;
        double startValue = 1.0;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x + 1;
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
        Assert.assertEquals(min, result, 0.001);
        Assert.assertEquals(-3.0, function.value(result), 0.001);
    }

    @Test
    public void testMinimizationWithStartValueCloseToMinimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = 0.5;
        double max = 1.5;
        double startValue = min - 0.1;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x + 1;
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
        Assert.assertEquals(min, result, 0.001);
        Assert.assertEquals(-3.0, function.value(result), 0.001);
    }

    @Test
    public void testMaximization() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = 0.5;
        double max = 1.5;
        double startValue = 1.0;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 3) + 2 * x - 1;
            }
        };
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);
        Assert.assertEquals(max, result, 0.001);
        Assert.assertEquals(3.0, function.value(result), 0.001);
    }

    @Test
    public void testMaximizationWithStartValueCloseToMaximum() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = 0.5;
        double max = 1.5;
        double startValue = max - 0.1;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 3) + 2 * x - 1;
            }
        };
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);
        Assert.assertEquals(max, result, 0.001);
        Assert.assertEquals(3.0, function.value(result), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = 0.5;
        double max = 1.5;
        double startValue = 1.0;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        optimizer.setMaximalIterationCount(0);
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double min = 0.5;
        double max = 1.5;
        double startValue = 1.0;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Function evaluation error.");
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }
}