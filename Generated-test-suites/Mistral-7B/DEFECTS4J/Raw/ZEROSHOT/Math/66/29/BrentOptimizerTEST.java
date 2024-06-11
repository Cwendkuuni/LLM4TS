import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMin() throws Exception {
        UnivariateRealFunction function = new Function("function", x -> Math.pow(x, 3) - 2 * x * Math.pow(Math.PI, 2));
        double min = 1.0;
        double max = 3.0;
        double startValue = 2.0;
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = new BrentOptimizer().optimize(function, goalType, min, max, startValue);
        double expected = -1.0; // The exact solution is -1.0, but the test should rely on the tolerance of BrentOptimizer
        Assert.assertEquals(expected, solution.getPoint(), 0.001); // Set a small tolerance for the test
    }

    @Test
    public void testBrentOptimizerMax() throws Exception {
        UnivariateRealFunction function = new Function("function", x -> Math.pow(x, 3) + 2 * x * Math.pow(Math.PI, 2));
        double min = 3.0;
        double max = 1.0;
        double startValue = 2.0;
        GoalType goalType = GoalType.MAXIMIZE;

        PointValuePair solution = new BrentOptimizer().optimize(function, goalType, min, max, startValue);
        double expected = 1.0; // The exact solution is 1.0, but the test should rely on the tolerance of BrentOptimizer
        Assert.assertEquals(expected, solution.getPoint(), 0.001); // Set a small tolerance for the test
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new Function("function", x -> Math.sin(x));
        double min = 0.0;
        double max = Math.PI;
        double startValue = Math.PI / 2.0;
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new Function("function", x -> {
            throw new FunctionEvaluationException("Test Function Evaluation Exception");
        });
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        GoalType goalType = GoalType.MINIMIZE;

        new BrentOptimizer().optimize(function, goalType, min, max, startValue);
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;

        public Function(String name, UnivariateRealFunction function) {
            this.name = name;
            this.function = function;
        }

        private UnivariateRealFunction function;

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return function.value(x);
        }

        @Override
        public DerivativeStructure derivative(double x) throws FunctionEvaluationException {
            throw new UnsupportedOperationException("BrentOptimizer does not require derivatives");
        }

        @Override
        public String toString() {
            return name;
        }
    }
}