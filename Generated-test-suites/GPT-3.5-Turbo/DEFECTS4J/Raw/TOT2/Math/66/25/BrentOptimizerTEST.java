import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> x*x, GoalType.MINIMIZE, 0, 10, 2);
        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> x*x, GoalType.MINIMIZE, 0, 10);
        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testLocalMinEdgeCases() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, x -> Math.sin(x), GoalType.MINIMIZE, 0, 1, 2, 1e-10, 1e-12);
        assertEquals(0.0, result, 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> x*x, GoalType.MINIMIZE, 0, 1, 2, -1, 1e-12);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> x*x, GoalType.MINIMIZE, 0, 1, 2, 1e-10, -1);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> Math.sin(x), GoalType.MINIMIZE, 0, 1, 2, 1e-10, 1e-12);
    }
}