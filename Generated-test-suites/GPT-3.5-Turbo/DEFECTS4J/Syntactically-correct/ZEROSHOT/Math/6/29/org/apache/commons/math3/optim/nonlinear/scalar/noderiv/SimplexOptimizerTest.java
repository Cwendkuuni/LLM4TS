import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {
    
    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        
        // Set up the optimization problem
        double[] startPoint = {0.0, 0.0};
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        
        // Run the optimization
        PointValuePair result = optimizer.optimize(100, objectiveFunction, goalType, startPoint);
        
        // Check the result
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        assertEquals(expectedValue, result.getValue(), 0.0001);
    }
}