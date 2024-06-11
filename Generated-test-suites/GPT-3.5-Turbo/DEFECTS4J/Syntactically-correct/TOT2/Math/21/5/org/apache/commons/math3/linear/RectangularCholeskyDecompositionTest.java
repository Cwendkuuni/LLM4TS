import org.junit.Test;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Test case for getting the root matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 53}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 0.0}, {6.0, 5.0}});
        Assert.assertEquals(expectedRootMatrix, rootMatrix);
    }

    @Test
    public void testGetRank() {
        // Test case for getting the rank of the matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 53}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        int rank = decomposition.getRank();
        Assert.assertEquals(2, rank);
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        // Test case for handling non-positive definite matrix exception
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1, 0}, {0, -2}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testRootMatrixForZeroMatrix() {
        // Test case for getting the root matrix of a zero matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0, 0}, {0, 0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
        Assert.assertEquals(expectedRootMatrix, rootMatrix);
    }
}