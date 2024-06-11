import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompose_ValidMatrix_ReturnsRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();

        // Verify that root * root.transpose() is almost equal to the original matrix
        RealMatrix product = root.multiply(root.transpose());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getRowDimension(); j++) {
                Assert.assertEquals(matrix.getEntry(i, j), product.getEntry(i, j), 1e-6);
            }
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompose_InvalidMatrix_ThrowsNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, -98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRank_ValidMatrix_ReturnsRank() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        // Verify that rank is equal to the number of rows in the root matrix
        Assert.assertEquals(matrix.getRowDimension(), rank);
    }

    @Test
    public void testGetRootMatrix_ValidMatrix_ReturnsRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();

        // Verify that the dimensions of the root matrix are correct
        Assert.assertEquals(matrix.getRowDimension(), root.getRowDimension());
        Assert.assertEquals(matrix.getRowDimension(), root.getColumnDimension());
    }

    @Test
    public void testDecompose_SmallDiagonalElements_ReturnsRootMatrixWithReducedRank() {
        double[][] matrixData = {{1e-10, 0, 0}, {0, 1e-10, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        // Verify that rank is less than the number of rows in the root matrix
        Assert.assertTrue(rank < matrix.getRowDimension());
    }

    @Test
    public void testDecompose_LargeDiagonalElements_ReturnsRootMatrixWithFullRank() {
        double[][] matrixData = {{1e10, 0, 0}, {0, 1e10, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        // Verify that rank is equal to the number of rows in the root matrix
        Assert.assertEquals(matrix.getRowDimension(), rank);
    }

    @Test
    public void testDecompose_ZeroMatrix_ReturnsRootMatrixWithZeroRank() {
        double[][] matrixData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        // Verify that rank is zero
        Assert.assertEquals(0, rank);
    }

}