import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {

    private static final double EPSILON = 1e-15;

    private PowellOptimizer optimizer;
  
    @Before
    public void setup() {
        optimizer = new PowellOptimizer(1e-10, 1e-10);
    }
  
    @Test
    public void testDoOptimize() {
        double[] startPoint = {1, 2, 3, 4};
        double[] expected = {4.712389, -7.068583, 4.712389, 4.712389};
        PointValuePair result = optimizer.optimize(startPoint);
        double[] actual = result.getPoint();
        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i], EPSILON);
        }
    }

    @Test
    public void testDoOptimizeEmptyStartPoint() {
        double[] startPoint = {};
        PointValuePair result = optimizer.optimize(startPoint);
        assertEquals(null, result);
    }

    @Test
    public void testDoOptimizeNullStartPoint() {
        double[] startPoint = null;
        PointValuePair result = optimizer.optimize(startPoint);
        assertEquals(null, result);
    }

    // Edge Cases
    // ... 

    // Exception Handling
    // ...
  
}