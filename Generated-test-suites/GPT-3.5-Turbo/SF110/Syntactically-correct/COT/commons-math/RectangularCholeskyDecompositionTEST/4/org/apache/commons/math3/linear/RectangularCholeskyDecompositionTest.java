import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor(){
        // Test case 1: valid matrix input
        double[][] data = {{2, -1, 0}, {-1, 2, -1}, {0, -1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        // Assertion: The rank and root matrix should be calculated correctly
        assert rcd.getRank() == 3;
        assert rcd.getRootMatrix().getRowDimension() == 3;
        assert rcd.getRootMatrix().getColumnDimension() == 3;

        // Test case 2: invalid matrix input
        double[][] invalidData = {{1, -1, 0}, {-1, 1, -1}, {0, -1, 1}};
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(invalidData);
        try {
            RectangularCholeskyDecomposition invalidRcd = new RectangularCholeskyDecomposition(invalidMatrix);
            // Assertion: Exception should be thrown when the matrix is not positive definite
            assert false;
        } catch (NonPositiveDefiniteMatrixException e) {
            // Assertion: Exception should be caught
            assert true;
        }
    }
}