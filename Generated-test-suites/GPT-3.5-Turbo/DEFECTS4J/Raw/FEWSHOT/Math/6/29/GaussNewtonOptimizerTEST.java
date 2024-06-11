import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] target1 = {1, 2, 3};
        double[] startPoint1 = {0, 0, 0};
        RealMatrix weight1 = new BlockRealMatrix(new double[][] {{1, 0, 0},
                                                                 {0, 1, 0},
                                                                 {0, 0, 1}});
        optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(target1);
        optimizer.setStartPoint(startPoint1);
        optimizer.setWeight(weight1);
        
        PointVectorValuePair result1 = optimizer.doOptimize();
        double[] expectedPoint1 = {1, 2, 3};
        double[] expectedValue1 = {0, 0, 0};
        assertArrayEquals(expectedPoint1, result1.getPoint(), 0);
        assertArrayEquals(expectedValue1, result1.getValue(), 0);

        // Test case 2
        double[] target2 = {-1, -2, -3};
        double[] startPoint2 = {1, 1, 1};
        RealMatrix weight2 = new BlockRealMatrix(new double[][] {{1, 0, 0},
                                                                 {0, 1, 0},
                                                                 {0, 0, 1}});
        optimizer = new GaussNewtonOptimizer(false);
        optimizer.setTarget(target2);
        optimizer.setStartPoint(startPoint2);
        optimizer.setWeight(weight2);
        
        PointVectorValuePair result2 = optimizer.doOptimize();
        double[] expectedPoint2 = {0, 0, 0};
        double[] expectedValue2 = {0, 0, 0};
        assertArrayEquals(expectedPoint2, result2.getPoint(), 0);
        assertArrayEquals(expectedValue2, result2.getValue(), 0);

        // Test case 3
        double[] target3 = {1, 2, 3};
        double[] startPoint3 = {1, 1, 1};
        RealMatrix weight3 = new BlockRealMatrix(new double[][] {{1, 0, 0},
                                                                 {0, 1, 0},
                                                                 {0, 0, 1}});
        optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(target3);
        optimizer.setStartPoint(startPoint3);
        optimizer.setWeight(weight3);
        
        PointVectorValuePair result3 = optimizer.doOptimize();
        double[] expectedPoint3 = {1, 2, 3};
        double[] expectedValue3 = {0, 0, 0};
        assertArrayEquals(expectedPoint3, result3.getPoint(), 0);
        assertArrayEquals(expectedValue3, result3.getValue(), 0);
    }
}