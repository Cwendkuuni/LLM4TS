import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        // Test with a positive definite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertEquals(2, decompositon.getRank());
            RealMatrix root = decompositon.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0 / Math.sqrt(2.0)}, {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
            assertEquals(expected, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a positive semidefinite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertEquals(2, decompositon.getRank());
            RealMatrix root = decompositon.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0 / Math.sqrt(2.0)}, {1.0, 0.0}});
            assertEquals(expected, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a non positive definite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, -1.0}});
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Exception expected: NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        // Test with a non positive semidefinite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Exception expected: NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        // Test with a small tolerance
        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0e-10}});
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 1.0e-10);
            assertEquals(2, decompositon.getRank());
            RealMatrix root = decompositon.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0 / Math.sqrt(2.0 + 1.0e-10 * 1.0e-10)}, {1.0 / Math.sqrt(2.0 + 1.0e-10 * 1.0e-10), 1.0 / Math.sqrt(2.0)}});
            assertTrue(Precision.equals(expected.getData(), root.getData(), 1e-13));
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}