import org.junit.Test;

import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 0.1);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 0.1);
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.1, 0.2, 0.3};
        double optimum = 0.5;
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        assertNotNull(result);
        assertEquals(result.length, 2);
        assertArrayEquals(result[0], new double[]{1.05, 2.1, 3.15}, 1e-15);
        assertArrayEquals(result[1], new double[]{0.05, 0.1, 0.15}, 1e-15);
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer.LineSearch lineSearch = new PowellOptimizer.LineSearch(1.0, 0.1);
        double[] p = {1.0};
        double[] d = {0.1};
        UnivariatePointValuePair result = lineSearch.search(p, d);
        assertNotNull(result);
    }

}