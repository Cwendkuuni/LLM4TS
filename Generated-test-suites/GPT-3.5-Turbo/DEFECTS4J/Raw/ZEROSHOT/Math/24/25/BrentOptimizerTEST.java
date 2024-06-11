import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        double min = 0;
        double max = 10;
        double startValue = 5;

        UnivariatePointValuePair result = optimizer.optimize(100,
                new UnivariateObjectiveFunction(x -> (x - 5) * (x - 5)),
                GoalType.MINIMIZE,
                min,
                max,
                startValue);

        assertEquals(5, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test case 2
        optimizer = new BrentOptimizer(1e-6, 1e-6);

        min = -10;
        max = 10;
        startValue = 0;

        result = optimizer.optimize(100,
                new UnivariateObjectiveFunction(x -> (x - 5) * (x - 5)),
                GoalType.MINIMIZE,
                min,
                max,
                startValue);

        assertEquals(5, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test case 3 - Unbounded optimization
        optimizer = new BrentOptimizer(1e-6, 1e-6);

        startValue = 5;

        result = optimizer.optimize(100,
                new UnivariateObjectiveFunction(x -> (x - 5) * (x - 5)),
                GoalType.MINIMIZE,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                startValue);

        assertEquals(5, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }
}