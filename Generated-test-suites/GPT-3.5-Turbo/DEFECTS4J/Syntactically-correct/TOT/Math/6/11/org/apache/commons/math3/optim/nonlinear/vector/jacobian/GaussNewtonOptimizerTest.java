import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test
    public void testDoOptimize_WithValidData_ShouldReturnOptimizedPoint() {
        // Arrange
        double[] startPoint = {1, 1};
        double[] target = {2, 4};
        double[][] weights = {{1, 0}, {0, 1}};
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setWeight(new BlockRealMatrix(weights));
        
        // Act
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Assert
        double[] expectedValue = {1, 2};
        assertArrayEquals(expectedValue, result.getPoint(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_WithNullConvergenceChecker_ShouldThrowNullArgumentException() {
        // Arrange
        double[] startPoint = {1, 1};
        double[] target = {2, 4};
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setConvergenceChecker(null);
        
        // Act
        optimizer.doOptimize();

        // Assert (Exception expected)
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_WithSingularMatrix_ShouldThrowConvergenceException() {
        // Arrange
        double[] startPoint = {1, 1};
        double[] target = {1, 0};
        double[][] weights = {{1, 0}, {0, 1}};
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setWeight(new BlockRealMatrix(weights));

        // Act
        optimizer.doOptimize();

        // Assert (Exception expected)
    }

    // Add more test cases for each method in the GaussNewtonOptimizer class

}