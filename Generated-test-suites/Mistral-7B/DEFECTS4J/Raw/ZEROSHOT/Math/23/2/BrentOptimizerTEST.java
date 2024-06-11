import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.RidgePointException;
import org.apache.commons.math3.optim.NonConvergenceException;
import org.apache.commons.math3.optim.PointValuePairFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test with default constructor
        BrentOptimizer optimizer = new BrentOptimizer(null);

        // Test with custom constructor
        BrentOptimizer optimizerCustom = new BrentOptimizer(0.001, 0.01, null);

        // Test optimization for simple functions
        SimpleFunction function1 = new SimpleFunction("function1");
        UnivariateOptimizer result1 = optimizer.optimize(function1, new SimpleBounds(0.0, 10.0), new InitialGuess(2.5));
        PointValuePair expected1 = new PointValuePair(2.44948974278317, 0.0);
        Assert.assertEquals(expected1.getValue(), result1.getValue(), 0.001);
        Assert.assertEquals(expected1, result1.getPoint());

        SimpleFunction function2 = new SimpleFunction("function2");
        UnivariateOptimizer result2 = optimizerCustom.optimize(function2, new SimpleBounds(0.0, 10.0), new InitialGuess(5.0));
        PointValuePair expected2 = new PointValuePair(3.0, 2.25);
        Assert.assertEquals(expected2.getValue(), result2.getValue(), 0.01);
        Assert.assertEquals(expected2, result2.getPoint());

        // Test optimization for custom function with multiple solutions
        SimpleFunction function3 = new SimpleFunction("function3");
        UnivariateOptimizer result3 = optimizerCustom.optimize(function3, new SimpleBounds(-10.0, 10.0), new InitialGuess(0.0));
        List<PointValuePair> expected3 = new ArrayList<>();
        expected3.add(new PointValuePair(-3.16227766025349, -27.88627110457273));
        expected3.add(new PointValuePair(3.16227766025349, 27.88627110457273));

        for (PointValuePair expected : expected3) {
            PointValuePair actual = result3.getPoints().get(result3.getPoints().size() - 1);
            Assert.assertEquals(expected.getValue(), actual.getValue(), 0.01);
            Assert.assertEquals(expected, actual);
        }

        // Test optimization for function with a ridge point
        SimpleFunction function4 = new SimpleFunction("function4");
        try {
            optimizerCustom.optimize(function4, new SimpleBounds(0.0, 2.0), new InitialGuess(1.0));
            Assert.fail();
        } catch (RidgePointException e) {
            // Expected
        }

        // Test optimization for non-convergent function
        SimpleFunction function5 = new SimpleFunction("function5");
        try {
            optimizerCustom.optimize(function5, new SimpleBounds(0.0, 10.0), new InitialGuess(5.0));
            Assert.fail();
        } catch (NonConvergenceException e) {
            // Expected
        }
    }

    private static class SimpleFunction implements UnivariateFunction {

        private final String name;

        public SimpleFunction(String name) {
            this.name = name;
        }

        @Override
        public double value(double x) {
            switch (name) {
                case "function1":
                    return Math.pow(x, 3) - 5 * x * Math.sin(Math.PI * x) + 3;
                case "function2":
                    return Math.pow(Math.E, x) * Math.sin(x) - 2;
                case "function3":
                    return Math.pow(x - 3, 2) * (x - 2) * (x - 1) * (x + 1) * (x + 2);
                case "function4":
                    if (x > 0 && x < 1) {
                        return Math.log(Math.abs(Math.sin(Math.PI * x / 2) / (2 * Math.PI * x)));
                    } else {
                        throw new IllegalArgumentException("x must be between 0 and 1");
                    }
                case "function5":
                    throw new UnsupportedOperationException();
                default:
                    throw new IllegalStateException("Invalid function name: " + name);
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

class SimpleBounds implements Bounds {

    private final double lowerBound;
    private final double upperBound;

    public SimpleBounds(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }
}

class SimplePointValuePairFactory implements PointValuePairFactory {

    @Override
    public PointValuePair create(double value, double point) {
        return new PointValuePair(point, value);
    }
}