import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new TestFunction();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 5);
        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new TestFunction();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
        assertEquals(0.0, result, 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new TestFunction();
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, -1, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNegativeT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new TestFunction();
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 0.1, -1);
    }

    private static class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }
}