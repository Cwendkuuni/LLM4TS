import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        // Test optimize method with start value provided
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -1, 1, 0);
        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        // Test optimize method without start value provided
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -1, 1);
        assertEquals(0.0, result, 1e-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        // Test for MaxIterationsExceededException
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -1, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        // Test for FunctionEvaluationException
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(x -> {
            throw new RuntimeException();
        }, GoalType.MINIMIZE, -1, 1);
    }

}