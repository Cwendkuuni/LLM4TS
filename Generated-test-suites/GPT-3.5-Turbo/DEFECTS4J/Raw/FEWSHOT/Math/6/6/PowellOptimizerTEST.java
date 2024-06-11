import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        double[] startPoint = {1.0, 2.0, 3.0};

        optimizer.setStartPoint(startPoint);
        optimizer.setObjectiveFunction((double[] point) -> {
            // Define the objective function here
            return point[0] * point[1] + point[2];
        });

        PointValuePair result = optimizer.doOptimize();

        // Assertions for the result
        assertEquals(2.0, result.getPoint()[0], 0.0001);
        assertEquals(1.0, result.getPoint()[1], 0.0001);
        assertEquals(-1.0, result.getPoint()[2], 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }
}