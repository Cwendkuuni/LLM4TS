import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.junit.Test;

public class PowellOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] startPoint1 = {1.0, 2.0};
        double[] expectedResult1 = {0.0, 0.0};
        PowellOptimizer optimizer1 = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result1 = optimizer1.optimize(1000, (p) -> {
            double x = p[0];
            double y = p[1];
            return x * x + y * y;
        }, GoalType.MINIMIZE, startPoint1);
        for (int i = 0; i < expectedResult1.length; i++) {
            assertEquals(expectedResult1[i], result1.getPoint()[i], 1e-6);
        }

        // Test case 2
        double[] startPoint2 = {-1.0, 2.0};
        double[] expectedResult2 = {-2.0, -1.0};
        PowellOptimizer optimizer2 = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result2 = optimizer2.optimize(1000, (p) -> {
            double x = p[0];
            double y = p[1];
            return x * x + Math.cos(x) - y;
        }, GoalType.MINIMIZE, startPoint2);
        for (int i = 0; i < expectedResult2.length; i++) {
            assertEquals(expectedResult2[i], result2.getPoint()[i], 1e-6);
        }
    }
}