import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.0001, 0.0001);
        PointValuePair result = powellOptimizer.doOptimize();

        assertNotNull(result);
        assertEquals(2, result.getPoint().length);
        assertEquals(2.0, result.getPoint()[0], 1e-6);
        assertEquals(2.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.0001, 0.0001);
        double[] p = new double[]{1.0, 2.0};
        double[] d = new double[]{3.0, 4.0};
        double optimum = 2.0;
        
        double[][] result = powellOptimizer.newPointAndDirection(p, d, optimum);
        
        assertNotNull(result);
        assertEquals(2, result.length);
        assertNotNull(result[0]);
        assertEquals(2, result[0].length);
        assertEquals(7.0, result[0][0], 1e-6);
        assertEquals(10.0, result[0][1], 1e-6);
        
        assertNotNull(result[1]);
        assertEquals(2, result[1].length);
        assertEquals(6.0, result[1][0], 1e-6);
        assertEquals(8.0, result[1][1], 1e-6);
    }

    @Test
    public void testSearch() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.0001, 0.0001);
        LineSearch lineSearch = powellOptimizer.new LineSearch(powellOptimizer.relativeThreshold, powellOptimizer.absoluteThreshold);
        double[] p = new double[]{1.0, 2.0};
        double[] d = new double[]{3.0, 4.0};
        
        UnivariatePointValuePair result = lineSearch.search(p, d);

        assertNotNull(result);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}