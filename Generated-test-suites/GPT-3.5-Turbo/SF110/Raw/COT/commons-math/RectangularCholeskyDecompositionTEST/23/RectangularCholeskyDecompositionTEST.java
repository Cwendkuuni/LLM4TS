import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Basic test case
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // Test the root matrix
        double[][] expectedRootData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(expectedRootData);
        Assert.assertTrue(expectedRootMatrix.equals(decomposition.getRootMatrix()));

        // Test the rank
        Assert.assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithSmallParam() {
        // Test case with small parameter
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNonPositiveDefiniteMatrix() {
        // Test case with non-positive definite matrix
        double[][] matrixData = {{1, -1}, {-1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    // Add more test cases for other public methods...

}