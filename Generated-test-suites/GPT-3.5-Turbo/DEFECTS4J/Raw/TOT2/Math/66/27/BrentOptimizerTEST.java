import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = x -> x * x - 4;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 3, 1);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = x -> x * x - 4;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 3);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            double result = optimizer.localMin(true, x -> x * x, GoalType.MINIMIZE, 0, 0, 1, 1e-6, 1e-6);
            assertEquals(0.0, result, 1e-6);
        } catch (Exception e) {
            assertEquals("MaxIterationsExceededException", e.getClass().getSimpleName());
        }
    }

}