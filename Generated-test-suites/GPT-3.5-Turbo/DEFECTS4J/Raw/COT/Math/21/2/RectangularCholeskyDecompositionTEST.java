import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test with positive semidefinite matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        // Assert statements
        assertEquals(3, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertEquals(2, rank);

        // Test with non-positive semidefinite matrix
        double[][] matrixData2 = {{1, -1}, {-1, 1}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(matrixData2);
        try {
            RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix2, 0.0001);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception expected
        }
    }
}