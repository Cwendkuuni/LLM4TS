
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        // Create a sample matrix for testing
        double[][] matrixData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Perform Cholesky decomposition
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Failed to perform Cholesky decomposition");
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);

        // Perform assertions on the root matrix
        assertEquals(4, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(2, root.getEntry(0, 0), 0);
        assertEquals(6, root.getEntry(0, 1), 0);
        assertEquals(-8, root.getEntry(0, 2), 0);
        assertEquals(3, root.getEntry(1, 0), 0);
        assertEquals(1, root.getEntry(1, 1), 0);
        assertEquals(-4, root.getEntry(1, 2), 0);
        assertEquals(0, root.getEntry(2, 0), 0);
        assertEquals(5, root.getEntry(2, 1), 0);
        assertEquals(9, root.getEntry(2, 2), 0);
        assertEquals(0, root.getEntry(3, 0), 0);
        assertEquals(0, root.getEntry(3, 1), 0);
        assertEquals(7, root.getEntry(3, 2), 0);
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] invalidData = {
            {1, -2},
            {-2, 4}
        };

        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(invalidData);
        new RectangularCholeskyDecomposition(invalidMatrix);
    }
}
