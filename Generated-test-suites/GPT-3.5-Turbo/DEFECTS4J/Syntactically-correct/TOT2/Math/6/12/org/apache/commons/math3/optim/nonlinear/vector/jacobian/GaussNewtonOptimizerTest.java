import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);

        // Set up test data
        double[] startPoint = {1.0, 2.0};
        double[][] jacobianData = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix jacobianMatrix = new Array2DRowRealMatrix(jacobianData);
        double[] targetValues = {3.0, 4.0};
        RealVector target = new ArrayRealVector(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setJacobian(jacobianMatrix);
        optimizer.setTarget(target);

        // Perform optimization
        PointVectorValuePair result = optimizer.doOptimize();

        // Check if result is not null
        assertNotNull(result);

        // Check if the result point matches the expected result
        double[] expectedPoint = {1.0, 1.0}; // Expected result after optimization
        double[] actualPoint = result.getPoint();
        assertArrayEquals(expectedPoint, actualPoint, 0.0001);
    }

    @Test
    public void testDoOptimizeWithSingularMatrix() {
        // Create an instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);

        // Set up test data with a singular matrix
        double[] startPoint = {1.0};
        double[][] jacobianData = {{0.0}};
        RealMatrix jacobianMatrix = new Array2DRowRealMatrix(jacobianData);
        double[] targetValues = {3.0};
        RealVector target = new ArrayRealVector(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setJacobian(jacobianMatrix);
        optimizer.setTarget(target);

        // Perform optimization
        try {
            PointVectorValuePair result = optimizer.doOptimize();
            fail("Expected SingularMatrixException was not thrown");
        } catch (ConvergenceException e) {
            // Exception caught as expected
        }
    }

    // Add more test cases for other methods as needed

}