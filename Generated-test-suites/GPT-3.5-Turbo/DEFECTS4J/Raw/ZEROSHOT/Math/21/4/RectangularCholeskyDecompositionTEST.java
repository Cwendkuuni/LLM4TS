import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor() {
        // Create a test matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        // Create a RectangularCholeskyDecomposition object
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        // Test the getRootMatrix method
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{2,  0, 0},
                                                                              {6,  1, 0},
                                                                              {-8, 5, 3}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertTrue(expectedRoot.equals(actualRoot, 1e-10));
        
        // Test the getRank method
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        Assert.assertEquals(expectedRank, actualRank);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        // Create a non-positive definite matrix
        double[][] matrixData = {{-2, -1}, {-1, -2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        // Try to create a RectangularCholeskyDecomposition object
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
}