import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Test case 1
        GaussNewtonOptimizer optimizer1 = new GaussNewtonOptimizer(true);
        double[] target1 = {1.0, 2.0, 3.0};
        optimizer1.setTarget(target1);
        double[] startPoint1 = {0.0, 0.0, 0.0};
        optimizer1.setStartPoint(startPoint1);
        double[][] weight1 = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        optimizer1.setWeight(weight1);
        double[] expectedPoint1 = {1.0, 2.0, 3.0};
        PointVectorValuePair result1 = optimizer1.doOptimize();
        assertArrayEquals(expectedPoint1, result1.getPoint(), 0.0001);

        // Test case 2
        GaussNewtonOptimizer optimizer2 = new GaussNewtonOptimizer(false);
        double[] target2 = {1.0, 2.0, 3.0};
        optimizer2.setTarget(target2);
        double[] startPoint2 = {0.0, 0.0, 0.0};
        optimizer2.setStartPoint(startPoint2);
        double[][] weight2 = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        optimizer2.setWeight(weight2);
        double[] expectedPoint2 = {1.0, 2.0, 3.0};
        PointVectorValuePair result2 = optimizer2.doOptimize();
        assertArrayEquals(expectedPoint2, result2.getPoint(), 0.0001);
    }
}