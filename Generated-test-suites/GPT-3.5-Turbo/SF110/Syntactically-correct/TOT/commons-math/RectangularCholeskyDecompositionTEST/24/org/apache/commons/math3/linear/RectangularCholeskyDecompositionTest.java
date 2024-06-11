import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        // Initialize test data
        matrix = new RealMatrixImpl(new double[][]{
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        });
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructor_WithPositiveDefiniteMatrix_ShouldInitializeRootMatrixAndRank() {
        // Verify root matrix dimensions
        assertEquals(matrix.getRowDimension(), decomposition.getRootMatrix().getRowDimension());
        assertEquals(decomposition.getRank(), decomposition.getRootMatrix().getColumnDimension());

        // Verify that all elements in root matrix are positive
        for (int i = 0; i < decomposition.getRootMatrix().getRowDimension(); i++) {
            for (int j = 0; j < decomposition.getRootMatrix().getColumnDimension(); j++) {
                assertTrue(decomposition.getRootMatrix().getEntry(i, j) >= 0);
            }
        }

        // Verify rank is within expected range
        assertTrue(decomposition.getRank() >= 0 && decomposition.getRank() <= matrix.getRowDimension());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_WithNonPositiveDefiniteMatrix_ShouldThrowException() {
        // Use a matrix with non-positive diagonal element
        matrix = new RealMatrixImpl(new double[][]{
                {-1, 0, 0},
                {0, 2, 0},
                {0, 0, 3}
        });
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_WithMatrixContainingNegativeDiagonalElement_ShouldThrowException() {
        // Use a matrix with negative diagonal element
        matrix = new RealMatrixImpl(new double[][]{
                {1, 0, 0},
                {0, -2, 0},
                {0, 0, 3}
        });
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_WithMatrixContainingZeroDiagonalElement_ShouldThrowException() {
        // Use a matrix with zero diagonal element
        matrix = new RealMatrixImpl(new double[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 3}
        });
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }
}