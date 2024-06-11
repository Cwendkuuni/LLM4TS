import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimize() throws Exception {
        UnivariateRealFunction function = new Function("x^3 - 4x^2 + 3x - 2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = 2.0;
        double startValue = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);

        double expectedResult = -1.0;
        assertEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(-3.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMaximize() throws Exception {
        UnivariateRealFunction function = new Function("x^3 - 4x^2 + 3x - 2");
        GoalType goalType = GoalType.MAXIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = 2.0;
        double startValue = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);

        double expectedResult = 1.0;
        assertEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(3.0, result.getValue(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizerMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizerFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new Function("x / 0") {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0) {
                    throw new FunctionEvaluationException("Cannot evaluate function at x = 0");
                }
                return x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        BrentOptimizer optimizer = new BrentOptimizer();

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    private static class Function implements UnivariateRealFunction {
        private final String equation;

        public Function(String equation) {
            this.equation = equation;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            String[] parts = equation.split(" ");
            double a = Double.parseDouble(parts[1]);
            double b = Double.parseDouble(parts[2]);
            double c = Double.parseDouble(parts[3]);
            double d = 0;

            if (parts.length > 4) {
                d = Double.parseDouble(parts[4]);
            }

            switch (parts[0]) {
                case "x":
                    return a * Math.pow(x, 3) + b * Math.pow(x, 2) + c * x + d;
                default:
                    throw new FunctionEvaluationException("Invalid function definition: " + equation);
            }
        }
    }
}