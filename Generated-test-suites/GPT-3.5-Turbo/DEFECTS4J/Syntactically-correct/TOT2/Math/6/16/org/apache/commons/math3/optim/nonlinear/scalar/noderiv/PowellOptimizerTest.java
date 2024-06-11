import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test optimal case
        // Create an instance of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        // Define a dummy objective function
        MultivariateFunction dummyFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        // Set the optimizer's objective function
        optimizer.setObjectiveFunction(dummyFunction);
        // Set the start point
        optimizer.setStartPoint(new double[]{1, 1});
        // Optimize
        PointValuePair result = optimizer.optimize();
        // Check the result
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testNewPointAndDirection() {
        // Test newPointAndDirection method
        double[] p = {1, 1};
        double[] d = {1, 1};
        double alpha = 2;
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[][] result = optimizer.newPointAndDirection(p, d, alpha);
        assertArrayEquals(new double[]{3, 3}, result[0], 1e-6);
        assertArrayEquals(new double[]{2, 2}, result[1], 1e-6);
    }

    @Test
    public void testLineSearch() {
        // Test LineSearch method
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(1e-6, 1e-6);
        double[] p = {1, 1};
        double[] d = {1, 1};
        UnivariatePointValuePair result = lineSearch.search(p, d);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

}