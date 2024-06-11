import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] startPoint1 = {1, 2, 3};
        PowellOptimizer optimizer1 = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result1 = optimizer1.doOptimize();
        assertEquals(3, result1.getPoint().length);
        assertEquals(3.0, result1.getPoint()[0], 1e-6);
        assertEquals(3.0, result1.getPoint()[1], 1e-6);
        assertEquals(3.0, result1.getPoint()[2], 1e-6);
        assertEquals(0.0, result1.getValue(), 1e-6);

        // Test case 2
        double[] startPoint2 = {0, 0};
        PowellOptimizer optimizer2 = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result2 = optimizer2.doOptimize();
        assertEquals(2, result2.getPoint().length);
        assertEquals(0.0, result2.getPoint()[0], 1e-6);
        assertEquals(0.0, result2.getPoint()[1], 1e-6);
        assertEquals(0.0, result2.getValue(), 1e-6);
    }
}