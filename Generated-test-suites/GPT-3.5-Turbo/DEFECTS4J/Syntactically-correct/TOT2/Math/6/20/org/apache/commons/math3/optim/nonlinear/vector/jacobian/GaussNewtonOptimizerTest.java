import static org.junit.Assert.*;

import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create GaussNewtonOptimizer instance
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Test typical case - check if optimization converges
        PointVectorValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(0, optimizer.getCost(), 1e-6);

        // Test edge case - start point as null
        optimizer.setStartPoint(null);
        assertThrows(NullArgumentException.class, () -> optimizer.doOptimize());

        // Test exception handling - singular matrix problem
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        double[][] weightMatrixData = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        optimizer.setWeight(new BlockRealMatrix(weightMatrixData));
        assertThrows(ConvergenceException.class, () -> optimizer.doOptimize());
    }

    @Test
    public void testConstructor() {
        // Test default constructor - LU decomposition used
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertTrue(optimizer.isUseLU());

        // Test constructor with QR decomposition
        GaussNewtonOptimizer optimizerQR = new GaussNewtonOptimizer(false);
        assertFalse(optimizerQR.isUseLU());
    }

    @Test
    public void testGettersAndSetters() {
        // Test setter and getter methods
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);
        assertArrayEquals(startPoint, optimizer.getStartPoint(), 1e-6);
    }

    // Add more test cases for other methods in GaussNewtonOptimizer class

}