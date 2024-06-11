import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test the constructor with a valid symmetric positive semidefinite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());

        // Test the constructor with an invalid symmetric positive semidefinite matrix
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}});
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetRootMatrix() {
        // Test getting the root matrix of the decomposition
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRootMatrix().getRowDimension());
        assertEquals(2, decomposition.getRootMatrix().getColumnDimension());
    }

    @Test
    public void testGetRank() {
        // Test getting the rank of the symmetric positive semidefinite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertEquals(2, decomposition.getRank());
    }

}