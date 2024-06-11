import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = x -> x * x - 4;
        GoalType goalType = GoalType.MINIMIZE;
        double result = optimizer.optimize(f, goalType, 0, 4, 1);
        assertEquals(2.0, result, 1e-5);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = x -> x * x - 4;
        GoalType goalType = GoalType.MINIMIZE;
        double result = optimizer.optimize(f, goalType, 0, 4);
        assertEquals(2.0, result, 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = x -> x;
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.localMin(false, f, goalType, 0, 1, 2, -1, 0.1);
    }

    @Test
    public void testLocalMinWithMaximalIterations() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = x -> x;
        GoalType goalType = GoalType.MINIMIZE;
        try {
            optimizer.localMin(false, f, goalType, 0, 1, 2, 0.1, 0.01);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        } catch (Exception e) {
            fail("Expected MaxIterationsExceededException");
        }
    }
}