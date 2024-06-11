import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws MaxIterationsExceededException {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test optimize(UnivariateRealFunction, GoalType, double, double, double)
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(2.0, result.getPoint(), 0.001);
        Assert.assertEquals(-8.0, result.getValue(), 0.01);

        // Test optimize(UnivariateRealFunction, GoalType, double, double)
        result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(2.0, result.getPoint(), 0.001);
        Assert.assertEquals(-8.0, result.getValue(), 0.01);

        // Test exception handling: optimize(UnivariateRealFunction, GoalType, double, double, double) with invalid arguments
        try {
            optimizer.optimize(null, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, -10.0, 0.0);
            Assert.fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 10.0, 10.0, 0.0);
            Assert.fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, Double.NaN);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, Double.NEGATIVE_INFINITY);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, Double.POSITIVE_INFINITY);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        // Test optimize(UnivariateRealFunction, GoalType, double, double) with invalid arguments
        try {
            optimizer.optimize(null, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, null, -10.0, 10.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, -10.0);
            Assert.fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 10.0, 10.0);
            Assert.fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, Double.NaN);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, Double.NEGATIVE_INFINITY);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, Double.POSITIVE_INFINITY);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        // Test edge cases
        optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);

        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x) + 1);
            }
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -1.0, -0.5);
            Assert.fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // expected
        }

        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1 / (1 + Math.exp(-x));
            }
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // expected
        }

        optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1000);

        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) * Math.sin(Math.PI * x);
            }
        };

        result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertEquals(1.2027556513881308d, result.getPoint(), 0.0001);
        Assert.assertEquals(-3.141592653589793d, result.getValue(), 0.0001);

        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(Math.PI * x / 2.0)) - 1;
            }
        };

        result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(0.0, result.getPoint(), 0.0001);
        Assert.assertEquals(-1.1102230246251565E-16d, result.getValue(), 0.0);

        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(Math.PI * x / 2.0 + 0.25)) - 1;
            }
        };

        result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(1.4463349357128481d, result.getPoint(), 0.0001);
        Assert.assertEquals(-0.7357592366865941d, result.getValue(), 0.0001);
    }
}