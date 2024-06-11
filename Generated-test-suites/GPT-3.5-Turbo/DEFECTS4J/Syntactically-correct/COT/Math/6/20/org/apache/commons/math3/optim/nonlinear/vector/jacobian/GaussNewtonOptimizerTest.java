
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Create some sample data and set it in the optimizer
        double[] targetValues = {1, 2, 3};
        optimizer.setTarget(targetValues);

        // Set the start point in the optimizer
        double[] startPoint = {0, 0, 0};
        optimizer.setStartPoint(startPoint);

        // Call the doOptimize method
        PointVectorValuePair result = optimizer.doOptimize();

        // Check that the result is not null
        assertNotNull(result);

        // Check that the result has the correct point and value
        double[] expectedPoint = {1, 2, 3};
        assertArrayEquals(expectedPoint, result.getPoint());
        double[] expectedValue = {1, 4, 9};
        assertArrayEquals(expectedValue, result.getValue());
    }

    @Test
    public void testDoOptimizeWithDefaultSettingsAndStartPoint() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Call the doOptimize method without setting target values
        try {
            PointVectorValuePair result = optimizer.doOptimize();
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        }

        // Set the target values in the optimizer
        double[] targetValues = {1, 2, 3};
        optimizer.setTarget(targetValues);

        // Set an invalid start point in the optimizer
        double[] startPoint = {};
        optimizer.setStartPoint(startPoint);

        // Call the doOptimize method
        PointVectorValuePair result = optimizer.doOptimize();

        // Check that the result is not null
        assertNotNull(result);

        // Check that the result has the correct point and value
        double[] expectedPoint = {0, 0, 0};
        assertArrayEquals(expectedPoint, result.getPoint());
        double[] expectedValue = {0, 0, 0};
        assertArrayEquals(expectedValue, result.getValue());
    }

    @Test
    public void testDoOptimizeWithLU() {
        // Create an instance of the GaussNewtonOptimizer with LU decomposition
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);

        // Set the target values in the optimizer
        double[] targetValues = {1, 2, 3};
        optimizer.setTarget(targetValues);

        // Set the start point in the optimizer
        double[] startPoint = {0, 0, 0};
        optimizer.setStartPoint(startPoint);

        // Call the doOptimize method
        PointVectorValuePair result = optimizer.doOptimize();

        // Check that the result is not null
        assertNotNull(result);

        // Check that the result has the correct point and value
        double[] expectedPoint = {1, 2, 3};
        assertArrayEquals(expectedPoint, result.getPoint());
        double[] expectedValue = {1, 4, 9};
        assertArrayEquals(expectedValue, result.getValue());
    }

    @Test
    public void testDoOptimizeWithQR() {
        // Create an instance of the GaussNewtonOptimizer with QR decomposition
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);

        // Set the target values in the optimizer
        double[] targetValues = {1, 2, 3};
        optimizer.setTarget(targetValues);

        // Set the start point in the optimizer
        double[] startPoint = {0, 0, 0};
        optimizer.setStartPoint(startPoint);

        // Call the doOptimize method
        PointVectorValuePair result = optimizer.doOptimize();

        // Check that the result is not null
        assertNotNull(result);

        // Check that the result has the correct point and value
        double[] expectedPoint = {1, 2, 3};
        assertArrayEquals(expectedPoint, result.getPoint());
        double[] expectedValue = {1, 4, 9};
        assertArrayEquals(expectedValue, result.getValue());
    }

    @Test
    public void testDoOptimizeWithSingularMatrix() {
        // Create an instance of the GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Set the target values in the optimizer
        double[] targetValues = {1, 2, 3};
        optimizer.setTarget(targetValues);

        // Set the start point in the optimizer
        double[] startPoint = {0, 0, 0};
        optimizer.setStartPoint(startPoint);

        // Set a weight matrix with zeros on the diagonal
        double[][] weightMatrixData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        optimizer.setWeight(weightMatrix);

        // Call the doOptimize method
        try {
            PointVectorValuePair result = optimizer.doOptimize();
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }
}

