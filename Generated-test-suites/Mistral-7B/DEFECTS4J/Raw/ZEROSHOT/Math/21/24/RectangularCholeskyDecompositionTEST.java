import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        // Test with a positive definite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.5811388350531623, 1.161573783976435}, {1.0, 1.161573783976435, 2.161951322858851}});
        RealMatrix result = decomposition.getRootMatrix();
        assertEquals(expected.getEntry(0, 0), result.getEntry(0, 0), 1e-6);
        assertEquals(expected.getEntry(0, 1), result.getEntry(0, 1), 1e-6);
        assertEquals(expected.getEntry(0, 2), result.getEntry(0, 2), 1e-6);
        assertEquals(expected.getEntry(1, 0), result.getEntry(1, 0), 1e-6);
        assertEquals(expected.getEntry(1, 1), result.getEntry(1, 1), 1e-6);
        assertEquals(expected.getEntry(1, 2), result.getEntry(1, 2), 1e-6);
        assertEquals(expected.getEntry(2, 0), result.getEntry(2, 0), 1e-6);
        assertEquals(expected.getEntry(2, 1), result.getEntry(2, 1), 1e-6);
        assertEquals(expected.getEntry(2, 2), result.getEntry(2, 2), 1e-6);
        assertEquals(decomposition.getRank(), expected.getRowDimension());

        // Test with a singular matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition1 = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        // Test with a positive semi-definite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 1.0}, {2.0, 1.0, 1.0}});
        RectangularCholeskyDecomposition decomposition2 = null;
        try {
            decomposition2 = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Ignore
        }

        assertTrue(decomposition2 != null);
        RealMatrix expected2 = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 0.0}, {1.0, 0.0, 0.0}});
        RealMatrix result2 = decomposition2.getRootMatrix();
        assertEquals(expected2.getEntry(0, 0), result2.getEntry(0, 0), 1e-6);
        assertEquals(expected2.getEntry(0, 1), result2.getEntry(0, 1), 1e-6);
        assertEquals(expected2.getEntry(0, 2), result2.getEntry(0, 2), 1e-6);
        assertEquals(expected2.getEntry(1, 0), result2.getEntry(1, 0), 1e-6);
        assertEquals(expected2.getEntry(1, 1), result2.getEntry(1, 1), 1e-6);
        assertEquals(expected2.getEntry(1, 2), result2.getEntry(1, 2), 1e-6);
        assertEquals(expected2.getEntry(2, 0), result2.getEntry(2, 0), 1e-6);
        assertEquals(expected2.getEntry(2, 1), result2.getEntry(2, 1), 1e-6);
        assertEquals(expected2.getEntry(2, 2), result2.getEntry(2, 2), 1e-6);
        assertEquals(decomposition2.getRank(), expected2.getRowDimension());
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, 1.0}, {2.0, 1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 1.0}, {2.0, 1.0, 1.0}});
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix result = svd.getU();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.6427746006805182, 0.7343864161233862, -0.4324423351918732}, {-0.7343864161233862, 0.6427746006805182, 0.4324423351918732}, {-0.4324423351918732, -0.4324423351918732, 0.8164965809255203}});
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), result.getEntry(i, j), 1e-6);
            }
        }
    }
}