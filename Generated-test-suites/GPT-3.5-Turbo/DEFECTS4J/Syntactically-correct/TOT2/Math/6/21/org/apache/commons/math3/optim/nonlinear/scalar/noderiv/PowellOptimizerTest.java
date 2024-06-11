import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testConstructor() {
        // Test Constructor with valid input
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 1e-7);
        assertNotNull(powellOptimizer);
    }

    @Test
    public void testNewPointAndDirection() {
        // Test newPointAndDirection method
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 1e-7);
        double[] p = {1, 2, 3};
        double[] d = {0.5, 0.2, 0.1};
        double optimum = 0.3;
        double[][] result = powellOptimizer.newPointAndDirection(p, d, optimum);

        assertArrayEquals(new double[]{1.15, 2.06, 3.03}, result[0], 0.01);
        assertArrayEquals(new double[]{0.15, 0.06, 0.03}, result[1], 0.01);
    }

    @Test
    public void testSearch() {
        // Test LineSearch search method
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 1e-7);
        double[] p = {1, 2};
        double[] d = {0.5, 0.2};
        PowellOptimizer.LineSearch lineSearch = powellOptimizer.new LineSearch(1e-5, 1e-7);
        PowellOptimizer.UnivariatePointValuePair result = lineSearch.search(p, d);

        assertNotNull(result);
    }
}