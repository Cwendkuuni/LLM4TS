import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);

        // Set up test data
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};

        // Set the test data
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        // Perform optimization
        PointVectorValuePair result = optimizer.doOptimize();

        // Check the result
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getPoint()[1], 0.0001);
        assertEquals(0.0, result.getPoint()[2], 0.0001);
    }
}