import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithSimpleFunction() throws MaxIterationsExceededException {
        UnivariateRealFunction function = f -> Math.pow(f, 2);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 0.01);
        Assert.assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizerWithMaxIterationsExceededException() throws MaxIterationsExceededException {
        UnivariateRealFunction function = f -> Math.abs(Math.sin(f)) + 1;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    @Test
    public void testBrentOptimizerWithFunctionEvaluationException() {
        UnivariateRealFunction function = f -> {
            throw new FunctionEvaluationException("Function evaluation error");
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizerWithNotStrictlyPositiveException() {
        UnivariateRealFunction function = f -> Math.log(f);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0, 0.0);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizerWithDifferentBounds() throws MaxIterationsExceededException {
        UnivariateRealFunction function = f -> Math.pow(f, 2);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 1.0, 5.0, 0.0);
        Assert.assertEquals(1.0, result.getPoint(), 0.01);
        Assert.assertEquals(1.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerWithMultipleCalls() throws MaxIterationsExceededException {
        List<Double> results = new ArrayList<>();
        UnivariateRealFunction function = f -> Math.pow(f, 2);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        for (int i = 0; i < 10; i++) {
            PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
            results.add(result.getPoint());
        }
        double average = results.stream().mapToDouble(d -> d).average().getAsDouble();
        Assert.assertEquals(2.5, average, 0.5);
    }
}