import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstruct() {
        BrentOptimizer optimizer = new BrentOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeMinFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -2, 2, 1.0);
        Assert.assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizeMaxFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.cos(Math.PI * x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -2, 2, 1.0);
        Assert.assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizeBoundedFunctionMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2) - 4;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 1.0);
        Assert.assertEquals(-2.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizeBoundedFunctionMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2) + 4;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, 1.0);
        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeException1() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Divide by zero");
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, -2, 2, 1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeException2() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, -2, 2, 1.0);
    }
}