import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001);

        // Test the doOptimize method with typical input
        PointValuePair result = optimizer.doOptimize();
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test
    public void testNewPointAndDirection() {
        // Create an instance of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001);

        // Test the newPointAndDirection method with edge case input
        double[][] result = optimizer.newPointAndDirection(new double[]{1.0, 2.0}, new double[]{0.5, 0.5}, 0.5);
        assertEquals(expectedX, result[0], 0.001);
        assertEquals(expectedD, result[1], 0.001);
    }

    @Test
    public void testLineSearch() {
        // Create an instance of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001);

        // Test the LineSearch method with error scenario
        LineSearch lineSearch = optimizer.new LineSearch(0.001, 0.001);
        UnivariatePointValuePair result = lineSearch.search(new double[]{1.0}, new double[]{0.5});
        assertEquals(expectedValue, result.getValue(), 0.001);
    }
}