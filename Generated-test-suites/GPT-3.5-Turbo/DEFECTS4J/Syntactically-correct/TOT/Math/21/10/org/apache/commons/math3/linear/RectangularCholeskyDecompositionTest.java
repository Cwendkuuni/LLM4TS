import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    // Test 1: Test the creation of a RectangularCholeskyDecomposition object

    @Test
    public void testCreateRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }

        Assert.assertNotNull(decomposition);

        // Check that the root matrix has the same dimensions as the input matrix
        int matrixRows = matrix.getRowDimension();
        int matrixCols = matrix.getColumnDimension();
        int rootRows = decomposition.getRootMatrix().getRowDimension();
        int rootCols = decomposition.getRootMatrix().getColumnDimension();

        Assert.assertEquals(matrixRows, rootRows);
        Assert.assertEquals(matrixCols, rootCols);

        // Check that the rank is correct
        int expectedRank = Math.min(matrixRows, matrixCols);
        int actualRank = decomposition.getRank();

        Assert.assertEquals(expectedRank, actualRank);
    }

    // Test 2: Test the creation of a RectangularCholeskyDecomposition object with a non-positive semidefinite matrix

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testCreateRectangularCholeskyDecompositionWithNonPositiveSemidefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 2, 3},
                {2, 5, 4},
                {3, 4, 6}
        });
        double small = 1e-6;

        new RectangularCholeskyDecomposition(matrix, small);
    }

    // Test 3: Test the creation of a RectangularCholeskyDecomposition object with a small diagonal element

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testCreateRectangularCholeskyDecompositionWithSmallDiagonalElement() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {4, 2, 3},
                {2, 1e-8, 4},
                {3, 4, 6}
        });
        double small = 1e-6;

        new RectangularCholeskyDecomposition(matrix, small);
    }

    // Test 4: Test the getRootMatrix method

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        });
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }

        // Check the dimensions of the root matrix
        int matrixRows = matrix.getRowDimension();
        int matrixCols = matrix.getColumnDimension();
        int rootRows = decomposition.getRootMatrix().getRowDimension();
        int rootCols = decomposition.getRootMatrix().getColumnDimension();

        Assert.assertEquals(matrixRows, rootRows);
        Assert.assertEquals(matrixCols, rootCols);

        // Check the correctness of the root matrix by multiplying it with its transpose
        RealMatrix root = decomposition.getRootMatrix();
        RealMatrix rootTranspose = root.transpose();
        RealMatrix rootTimesRootTranspose = root.multiply(rootTranspose);

        int rows = rootTimesRootTranspose.getRowDimension();
        int cols = rootTimesRootTranspose.getColumnDimension();

        // Check that the result matrix has the correct dimensions
        Assert.assertEquals(matrixRows, rows);
        Assert.assertEquals(matrixCols, cols);

        // Check that each element in the result matrix is almost equal to the corresponding element in the input matrix
        double[][] resultData = rootTimesRootTranspose.getData();
        double[][] inputData = matrix.getData();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double result = resultData[i][j];
                double input = inputData[i][j];
                Assert.assertEquals(input, result, small);
            }
        }
    }

    // Test 5: Test the getRank method

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        });
        double small = 1e-6;

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }

        int expectedRank = 3;
        int actualRank = decomposition.getRank();

        Assert.assertEquals(expectedRank, actualRank);
    }

}