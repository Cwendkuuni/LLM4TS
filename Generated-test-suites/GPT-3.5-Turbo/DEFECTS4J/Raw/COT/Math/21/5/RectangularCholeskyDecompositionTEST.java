import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Create a test matrix
        double[][] testData = {{4, 12}, {12, 37}};

        // Create a RealMatrix from the test data
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        // Perform the rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(testMatrix, 1e-10);

        // Get the root matrix from the decomposition
        RealMatrix rootMatrix = decomp.getRootMatrix();

        // Verify the root matrix is correct
        double[][] expectedData = {{2, 6}, {0, 1}};

        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);

        assertArrayEquals("Root matrix is incorrect", expectedMatrix.getData(), rootMatrix.getData());
    }

    @Test
    public void testGetRank() {
        // Create a test matrix
        double[][] testData = {{4, 12}, {12, 37}};

        // Create a RealMatrix from the test data
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        // Perform the rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(testMatrix, 1e-10);

        // Get the rank from the decomposition
        int rank = decomp.getRank();

        // Verify the rank is correct
        assertEquals("Rank is incorrect", 2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        // Create a test matrix with a negative diagonal element
        double[][] testData = {{-4, 12}, {12, 37}};

        // Create a RealMatrix from the test data
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        // Perform the rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(testMatrix, 1e-10);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException2() {
        // Create a test matrix with a sufficiently negative diagonal element
        double[][] testData = {{4, 12}, {12, -37}};

        // Create a RealMatrix from the test data
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        // Perform the rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(testMatrix, 1e-10);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException3() {
        // Create a test matrix with all diagonal elements close to zero
        double[][] testData = {{1e-12, 1e-12}, {1e-12, 1e-12}};

        // Create a RealMatrix from the test data
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        // Perform the rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(testMatrix, 1e-10);
    }

}