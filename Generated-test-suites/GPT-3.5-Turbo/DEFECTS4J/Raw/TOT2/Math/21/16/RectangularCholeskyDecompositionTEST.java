import org.junit.Test;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test the RectangularCholeskyDecomposition constructor
        // Create a symmetric positive semidefinite matrix
        double[][] data = {{4, 2}, {2, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.0;
        
        // Test the constructor
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Validate the rank and root matrix
        Assert.assertEquals(2, decomposition.getRank());
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{2.0, 0.0}, {1.0, 1.0}});
        Assert.assertEquals(expectedRoot, decomposition.getRootMatrix());
    }

    @Test
    public void testRectangularCholeskyDecompositionException() {
        // Test the RectangularCholeskyDecomposition constructor with a non-positive semidefinite matrix
        // Create a non-positive semidefinite matrix
        double[][] data = {{-1, 2}, {2, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.0;

        // Test for NonPositiveDefiniteMatrixException
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception, test passed
        }
    }

    // Add more test cases for other methods here

}