import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Create a symmetric positive semidefinite matrix for testing
        double[][] data = {{4, 1}, {1, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.0001;

        // Test the RectangularCholeskyDecomposition constructor
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            assertNotNull(decomposition);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown for valid matrix: " + e.getMessage());
        }
    }

    @Test
    public void testGetRootMatrix() {
        // Create a test matrix
        double[][] data = {{4, 1}, {1, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.0001;

        // Get the root matrix and check its dimensions
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(matrix.getRowDimension(), rootMatrix.getRowDimension());
        assertEquals(decomposition.getRank(), rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        // Create a test matrix
        double[][] data = {{4, 1, 0}, {1, 5, 0}, {0, 0, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.0001;

        // Get the rank and check if it matches the expected value
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        // Create a symmetric positive semidefinite matrix with negative diagonal element
        double[][] data = {{4, 1}, {1, -5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.0001;

        // Test if NonPositiveDefiniteMatrixException is thrown
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("NonPositiveDefiniteMatrixException not thrown for negative diagonal element");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }
}