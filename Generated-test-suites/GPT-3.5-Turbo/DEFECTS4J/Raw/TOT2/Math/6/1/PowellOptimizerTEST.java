import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(10, new TestMultivariateFunction(), GoalType.MINIMIZE, new double[] {0, 0});
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-6); // Typical use case
    }

    @Test
    public void testDoOptimizeWithInvalidStartingPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        try {
            optimizer.optimize(10, new TestMultivariateFunction(), GoalType.MINIMIZE, new double[] {}); // Invalid empty starting point
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Other test cases for remaining public methods can be added here

    private static class TestMultivariateFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }
    }
}