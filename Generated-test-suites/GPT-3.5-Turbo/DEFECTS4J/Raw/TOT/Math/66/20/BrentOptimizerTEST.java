import org.apache.commons.math.analysis.*;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-6;

    private BrentOptimizer optimizer = new BrentOptimizer();

    // Test cases for optimize(UnivariateRealFunction, GoalType, double, double, double)

    @Test
    public void testOptimize_withStartValue() throws Exception {
        // Create a simple linear function f(x) = 2x + 3
        UnivariateRealFunction function = new LinearFunction(2, 3);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;

        double result = optimizer.optimize(function, goalType, min, max, startValue);

        Assert.assertEquals(-3, result, EPSILON);
    }

    @Test
    public void testOptimize_withoutStartValue() throws Exception {
        // Create a simple quadratic function f(x) = x^2 - 3x + 2
        UnivariateRealFunction function = new QuadraticFunction(1, -3, 2);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 4;

        double result = optimizer.optimize(function, goalType, min, max);

        Assert.assertEquals(1.5, result, EPSILON);
    }

    // Test cases for localMin(boolean, UnivariateRealFunction, GoalType, double, double, double, double, double)

    @Test
    public void testLocalMin_minimize() throws Exception {
        // Create a simple exponential function f(x) = e^x
        UnivariateRealFunction function = new ExponentialFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double lo = -10;
        double mid = -8;
        double hi = -6;
        double eps = 1e-6;
        double t = 1e-12;

        double result = optimizer.localMin(false, function, goalType, lo, mid, hi, eps, t);

        Assert.assertEquals(-7.616495, result, EPSILON);
    }

    @Test
    public void testLocalMin_maximize() throws Exception {
        // Create a simple logarithmic function f(x) = log(x)
        UnivariateRealFunction function = new LogarithmicFunction();
        GoalType goalType = GoalType.MAXIMIZE;
        double lo = 0.1;
        double mid = 1;
        double hi = 10;
        double eps = 1e-4;
        double t = 1e-6;

        double result = optimizer.localMin(true, function, goalType, lo, mid, hi, eps, t);

        Assert.assertEquals(1, result, EPSILON);
    }

    // Test case for the empty doOptimize() method

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws Exception {
        optimizer.doOptimize();
    }

    private class LinearFunction implements UnivariateRealFunction {

        private double slope;
        private double intercept;

        public LinearFunction(double slope, double intercept) {
            this.slope = slope;
            this.intercept = intercept;
        }

        @Override
        public double value(double x) {
            return slope * x + intercept;
        }
    }

    private class QuadraticFunction implements UnivariateRealFunction {

        private double a;
        private double b;
        private double c;

        public QuadraticFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }
    }

    private class ExponentialFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.exp(x);
        }
    }

    private class LogarithmicFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.log(x);
        }
    }
}