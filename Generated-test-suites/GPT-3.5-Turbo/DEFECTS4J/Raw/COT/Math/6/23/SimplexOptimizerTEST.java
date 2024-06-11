import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] startPoint = {0.0, 0.0};
        double[] expected = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow((point[0] - 1), 2) + Math.pow((point[1] - 1), 2);
            }
        }, GoalType.MINIMIZE, startPoint);
        assertArrayEquals(expected, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithConstrainedBounds() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        double[] lowerBounds = {-10.0, -10.0};
        double[] upperBounds = {10.0, 10.0};
        double[] startPoint = {0.0, 0.0};
        double[] expected = {10.0, 10.0};
        PointValuePair result = optimizer.optimize(100, new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow((point[0] - 1), 2) + Math.pow((point[1] - 1), 2);
            }
        }, GoalType.MINIMIZE, new OptimizationData[] {
            new InitialGuess(startPoint),
            new SimpleBounds(lowerBounds, upperBounds)
        });
        assertArrayEquals(expected, result.getPoint(), 1e-6);
    }
}