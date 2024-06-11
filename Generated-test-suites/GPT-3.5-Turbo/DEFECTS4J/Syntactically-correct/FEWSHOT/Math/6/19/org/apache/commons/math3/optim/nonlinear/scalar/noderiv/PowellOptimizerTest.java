import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        // Test case 1
        double[] startPoint1 = {0, 0};
        double result1 = optimizer.doOptimize(startPoint1);
        assertEquals(0, result1, 1.0e-6);

        // Test case 2
        double[] startPoint2 = {-1, 2};
        double result2 = optimizer.doOptimize(startPoint2);
        assertEquals(-5, result2, 1.0e-6);

        // Test case 3
        double[] startPoint3 = {3, 1};
        double result3 = optimizer.doOptimize(startPoint3);
        assertEquals(0.5, result3, 1.0e-6);
    }
}