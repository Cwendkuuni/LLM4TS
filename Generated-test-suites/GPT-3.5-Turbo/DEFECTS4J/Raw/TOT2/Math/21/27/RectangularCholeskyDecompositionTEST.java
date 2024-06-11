import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition_RootMatrix() {
        // Test the calculation of the root matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        // Verify the dimensions of the root matrix
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());

        // Verify the values of the root matrix elements
        assertEquals(2.0, rootMatrix.getEntry(0, 0), 1e-10);
        assertEquals(6.0, rootMatrix.getEntry(0, 1), 1e-10);
        assertEquals(0.0, rootMatrix.getEntry(1, 0), 1e-10);
        assertEquals(5.0, rootMatrix.getEntry(1, 1), 1e-10);
    }

    @Test
    public void testRectangularCholeskyDecomposition_Rank() {
        // Test the calculation of the rank
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        int rank = decomposition.getRank();

        // Verify the rank of the matrix
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_NonPositiveDefiniteMatrixException() {
        // Test for NonPositiveDefiniteMatrixException when the matrix is not positive semidefinite
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, -2}, {-2, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}