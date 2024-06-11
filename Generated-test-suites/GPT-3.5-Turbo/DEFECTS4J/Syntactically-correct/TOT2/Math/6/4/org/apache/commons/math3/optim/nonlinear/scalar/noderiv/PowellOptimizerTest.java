import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        // Test scenario
        // Add test scenarios for the doOptimize method 

    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.5, 0.5, 0.5};
        double optimum = 0.2;
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        assertEquals(1.1, result[0][0], 0.0);
        assertEquals(2.1, result[0][1], 0.0);
        assertEquals(3.1, result[0][2], 0.0);
        assertEquals(0.1, result[1][0], 0.0);
        assertEquals(0.1, result[1][1], 0.0);
        assertEquals(0.1, result[1][2], 0.0);
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.5, 0.5, 0.5};
        PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(1e-6, 1e-6);
        UnivariatePointValuePair result = lineSearch.search(p, d);
        assertTrue(result.getValue() > 0.0);
    }

}