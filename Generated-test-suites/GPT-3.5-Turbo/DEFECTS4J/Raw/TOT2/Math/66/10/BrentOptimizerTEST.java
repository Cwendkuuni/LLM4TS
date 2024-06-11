import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction function = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = 0;
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 5);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
        assertEquals(0, result, 0.0001);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction function = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = 0;
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
        assertEquals(0, result, 0.0001);
    }

    @Test
    public void testLocalMinEdgeCases() {
        UnivariateRealFunction function = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.localMin(true, function, GoalType.MINIMIZE, 0, 1, 2, 1E-10, 1E-14);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLocalMinException() {
        UnivariateRealFunction function = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.localMin(true, function, GoalType.MINIMIZE, 1, 2, 0, 0, 1E-14);
            fail("Exception not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Success
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Wrong exception thrown");
        }
    }
}