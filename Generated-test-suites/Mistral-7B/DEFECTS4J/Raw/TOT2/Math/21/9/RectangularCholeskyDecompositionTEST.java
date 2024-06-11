import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-12;
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.41421356, 0.70710678, 0.70710678}, {0.70710678, 1.41421356, 0.70710678}, {0.70710678, 0.70710678, 1.41421356}});
            assertArrayEquals(expectedRoot.getData(), decomposition.getRootMatrix().getData(), 1e-12);
            assertArrayEquals(new int[]{2, 2}, decomposition.getRootMatrix().getRowDimension(), decomposition.getRootMatrix().getColumnDimension());
            assertArrayEquals(new int[]{3, 2}, matrix.getRowDimension(), matrix.getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // This exception should not be thrown, as the matrix is positive semidefinite
            System.out.println("Unexpected exception thrown: " + e.getMessage());
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
}