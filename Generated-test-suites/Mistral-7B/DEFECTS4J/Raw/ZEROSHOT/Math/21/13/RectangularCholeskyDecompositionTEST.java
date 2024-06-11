import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-5;

        // Test with a positive definite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.71542857, 1.0, 0.0}, {0.42261833, 1.22474487, 1.0}});
            assertEquals(expected, decomp.getRootMatrix());
            assertEquals(3, decomp.getRank());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Test with a positive semidefinite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 2.5, 2.0}, {1.0, 2.0, 2.5}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.71542857, 1.0, 0.0}, {0.42261833, 1.0, 1.0}});
            assertEquals(expected, decomp.getRootMatrix());
            assertEquals(3, decomp.getRank());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Test with a non-positive definite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, -5.0, 2.0}, {1.0, 2.0, 4.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            fail("NonPositiveDefiniteMatrixException should be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }

        // Test with a singular matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 2.0, 2.0}, {1.0, 2.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            fail("NonPositiveDefiniteMatrixException should be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }

        // Test with a diagonal matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 0.0, 0.0}, {0.0, 5.0, 0.0}, {0.0, 0.0, 4.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
            assertEquals(expected, decomp.getRootMatrix());
            assertEquals(3, decomp.getRank());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Test with a small tolerance
        small = 1e-9;
        matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.71542857, 1.0, 0.0}, {0.42261833, 1.22474487, 1.0}});
            assertEquals(expected, decomp.getRootMatrix(), 1e-8);
            assertEquals(3, decomp.getRank());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Test with a large tolerance
        small = 1e-1;
        matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.71542857, 1.0, 0.0}, {0.42261833, 1.22474487, 1.0}});
            assertEquals(expected, decomp.getRootMatrix(), 1);
            assertEquals(3, decomp.getRank());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Test with a diagonal matrix with small elements
        matrix = new Array2DRowRealMatrix(new double[][]{{3.0e-5, 0.0, 0.0}, {0.0, 5.0e-5, 0.0}, {0.0, 0.0, 4.0e-5}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
            assertEquals(expected, decomp.getRootMatrix(), 1e-6);
            assertEquals(3, decomp.getRank());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

}