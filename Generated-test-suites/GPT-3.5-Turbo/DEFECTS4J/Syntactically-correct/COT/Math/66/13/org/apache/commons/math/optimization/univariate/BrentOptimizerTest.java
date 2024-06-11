import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0, 1);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0, 1, 0.5);
        assertEquals(0.0, result, 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(10);
        optimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new ExceptionFunction(), GoalType.MINIMIZE, 0, 1);
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidEpsilon() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0, 1, 0.5, 0, -0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0, 1, 0.5, -0.1, 0);
    }

    @Test
    public void testOptimizeWithMaxValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0, Double.MAX_VALUE);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testOptimizeWithMinValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, Double.MIN_VALUE, 1);
        assertEquals(0.0, result, 0.001);
    }
}