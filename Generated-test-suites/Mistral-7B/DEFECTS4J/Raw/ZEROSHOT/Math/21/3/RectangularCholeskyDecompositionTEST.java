import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double[][] matrixData = new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 5.0, 6.0, 8.0},
                {3.0, 6.0, 10.0, 12.0},
                {4.0, 8.0, 12.0, 15.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposer.getRootMatrix();
            int rank = decomposer.getRank();

            assertEquals(3, rank);
            SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
            RealMatrix expectedRoot = svd.getU();
            RealMatrix expectedRootTranspose = RealMatrixUtils.transpose(expectedRoot);

            RealMatrix difference = root.subtract(expectedRootTranspose);
            double maxElement = difference.getMaxElement().getValue();
            assertEquals("The root matrix is not equal to the transpose of the U matrix from SVD", 0.0, maxElement, 1e-12);

        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrixData = new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 5.0, 6.0, 8.0},
                {3.0, 6.0, 10.0, 12.0},
                {4.0, 8.0, 12.0, -15.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Exception expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSmallThreshold() {
        double[][] matrixData = new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 5.0, 6.0, 8.0},
                {3.0, 6.0, 10.0, 12.0},
                {4.0, 8.0, 12.0, 15.0},
                {5.0, 8.0, 12.0, 15.0}}; // the last row is different from the previous one
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 1.0);
            RealMatrix root = decomposer.getRootMatrix();
            int rank = decomposer.getRank();

            assertEquals(4, rank);

            SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
            RealMatrix expectedRoot = svd.getU();
            RealMatrix expectedRootTranspose = RealMatrixUtils.transpose(expectedRoot);

            RealMatrix difference = root.subtract(expectedRootTranspose);
            double maxElement = difference.getMaxElement().getValue();
            assertEquals("The root matrix is not equal to the transpose of the U matrix from SVD", 0.0, maxElement, 1e-12);

        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }
}