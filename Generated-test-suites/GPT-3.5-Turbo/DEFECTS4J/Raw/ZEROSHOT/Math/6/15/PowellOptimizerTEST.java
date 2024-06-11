import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PowellOptimizerTest {
    private PowellOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-8, 1e-8);
    }

    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] startPoint1 = {0, 0};
        double[] expectedPoint1 = {0, 0};
        double expectedValue1 = 0;

        optimizer.setStartPoint(startPoint1);
        PointValuePair result1 = optimizer.doOptimize();

        assertArrayEquals(expectedPoint1, result1.getPoint(), 1e-8);
        assertEquals(expectedValue1, result1.getValue(), 1e-8);

        // Test case 2
        double[] startPoint2 = {-1, -1};
        double[] expectedPoint2 = {0, 0};
        double expectedValue2 = 0;

        optimizer.setStartPoint(startPoint2);
        PointValuePair result2 = optimizer.doOptimize();

        assertArrayEquals(expectedPoint2, result2.getPoint(), 1e-8);
        assertEquals(expectedValue2, result2.getValue(), 1e-8);
    }
}