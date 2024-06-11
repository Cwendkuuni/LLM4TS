import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.Points.uniformlySpaced;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws Exception {
        UnivariateRealFunction function = new Function("f(x) = x^3 - 2*x^2 + x - 1");

        BrentOptimizer optimizer = new BrentOptimizer();

        double min = -1.5;
        double max = 1.5;
        double initialGuess = 0.0;

        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, initialGuess);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(min, result.getX(), 1e-6);
        Assert.assertEquals(-2.220446049250313E-16, result.getValue(), 1e-14);

        // Test with initial guess closer to the minimum
        initialGuess = -1.2;
        result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, initialGuess);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(min, result.getX(), 1e-6);
        Assert.assertEquals(-2.220446049250313E-16, result.getValue(), 1e-14);

        // Test with initial guess outside the interval
        initialGuess = 2.5;
        result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, initialGuess);

        // The optimizer should not find the minimum in this case
        Assert.assertEquals(Double.NaN, result.getX(), 0.0);
        Assert.assertEquals(Double.NaN, result.getValue(), 0.0);

        // Test the maximization goal type
        result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, initialGuess);

        Assert.assertEquals(GoalType.MAXIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(min, result.getX(), 1e-6);
        Assert.assertEquals(2.220446049250313E-16, result.getValue(), 1e-14);

        // Test with a function that has multiple local minima
        UnivariateRealFunction multipleMinimaFunction = new Function("g(x) = x^4 - 4*x^3 + 6*x^2 - 4*x + 1");

        result = optimizer.optimize(multipleMinimaFunction, GoalType.MINIMIZE, -3.0, 3.0, 0.0);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertFalse(result.getX() == -1.222456139795752, 0.0);
        Assert.assertFalse(result.getX() == -0.731643252825155, 0.0);
        Assert.assertFalse(result.getX() == -0.319854245692338, 0.0);
        Assert.assertEquals(min, result.getX(), 1e-6);
        Assert.assertEquals(-4.0, result.getValue(), 1e-14);

        // Test the convergence rate
        UnivariateRealFunction convergenceRateFunction = new Function("h(x) = x^2");
        double tolerance = 1e-12;
        double maxIterations = 1000;

        result = optimizer.optimize(convergenceRateFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.0);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(0.0, result.getX(), tolerance);
        Assert.assertEquals(0.0, result.getValue(), tolerance);

        int iterations = 0;
        double error = tolerance;
        double x = 1.0;

        while (error > tolerance && iterations < maxIterations) {
            x = optimizer.optimize(convergenceRateFunction, GoalType.MINIMIZE, 0.0, 1.0, x).getX();
            error = Math.abs(x - 0.0);
            iterations++;
        }

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(0.0, result.getX(), tolerance);
        Assert.assertEquals(0.0, result.getValue(), tolerance);
        Assert.assertTrue(iterations <= maxIterations);
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;
        private final String expression;

        public Function(String name, String expression) {
            this.name = name;
            this.expression = expression;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            String[] parts = expression.split("\\s+");
            double a = Double.parseDouble(parts[1]);
            double b = 0.0;
            double c = 0.0;
            double d = 0.0;

            if (parts.length > 3) {
                b = Double.parseDouble(parts[2]);
                c = Double.parseDouble(parts[3]);
            }

            switch (parts.length) {
                case 1:
                    return a;
                case 2:
                    return a * x * x + b;
                case 3:
                    return a * x * x * x + b * x * x + c * x + d;
                default:
                    throw new RuntimeException("Invalid function definition");
            }
        }

        @Override
        public String toString() {
            return name + "(" + expression + ")";
        }
    }
}