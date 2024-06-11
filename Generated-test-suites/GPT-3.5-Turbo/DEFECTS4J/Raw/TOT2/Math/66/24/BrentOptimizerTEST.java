import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 2.0;
        double max = 3.0;
        double startValue = 2.5;
        double result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 2.0;
        double max = 3.0;
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(2.0, result, 1e-10);
    }

    // Add more test cases for edge cases and error scenarios here

}