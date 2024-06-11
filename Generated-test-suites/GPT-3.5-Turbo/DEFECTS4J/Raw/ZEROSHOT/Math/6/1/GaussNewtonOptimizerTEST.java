import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set parameters
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);

        // Call the method to be tested
        PointVectorValuePair result = optimizer.doOptimize();

        // Check the result
        double[] expectedPoint = {1.0, 2.0, 3.0};
        double[] expectedValues = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-9);
        assertArrayEquals(expectedValues, result.getValue(), 1e-9);
        assertEquals(0.0, optimizer.getCost(), 1e-9);
    }
}