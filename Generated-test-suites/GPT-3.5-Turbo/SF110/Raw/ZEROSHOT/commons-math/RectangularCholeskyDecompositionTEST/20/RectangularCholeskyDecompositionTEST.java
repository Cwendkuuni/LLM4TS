import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test case 1: Non positive definite matrix exception
        try {
            RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
            matrix.setEntry(0, 0, -1.0);
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getMatrixEntry(), 0.0);
            assertEquals(0, e.getRow(), 0);
        }

        // Test case 2: Full matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());

        assertEquals(1.0, rootMatrix.getEntry(0, 0), 0.0);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.0);
        assertEquals(0.0, rootMatrix.getEntry(0, 2), 0.0);

        assertEquals(0.0, rootMatrix.getEntry(1, 0), 0.0);
        assertEquals(1.4142, rootMatrix.getEntry(1, 1), 0.0001);
        assertEquals(0.0, rootMatrix.getEntry(1, 2), 0.0);

        assertEquals(0.0, rootMatrix.getEntry(2, 0), 0.0);
        assertEquals(0.0, rootMatrix.getEntry(2, 1), 0.0);
        assertEquals(1.7320, rootMatrix.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(3, decomposition.getRank());
    }

}