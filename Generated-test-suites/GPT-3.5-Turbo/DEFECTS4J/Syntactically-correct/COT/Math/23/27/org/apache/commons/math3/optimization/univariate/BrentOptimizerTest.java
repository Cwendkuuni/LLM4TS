import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);

        // Initialize necessary variables for testing
        double min = 0;
        double max = 10;

        // Create a custom objective function
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow((x - 5), 2);
            }
        };

        // Set the objective function
        optimizer.setObjective(objective);

        // Set the search interval
        optimizer.setMin(min);
        optimizer.setMax(max);

        // Test the optimize() method
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(5.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBest() {
        // Create some test points
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 20.0);

        // Test best() method for minimizing
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair resultMin = optimizer.best(pointA, pointB, true);
        assertEquals(1.0, resultMin.getPoint(), 0.001);
        assertEquals(10.0, resultMin.getValue(), 0.001);

        // Test best() method for maximizing
        UnivariatePointValuePair resultMax = optimizer.best(pointA, pointB, false);
        assertEquals(2.0, resultMax.getPoint(), 0.001);
        assertEquals(20.0, resultMax.getValue(), 0.001);
    }
}