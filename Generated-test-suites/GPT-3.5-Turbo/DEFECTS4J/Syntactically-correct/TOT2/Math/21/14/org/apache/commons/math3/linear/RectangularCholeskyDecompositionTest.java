import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test constructor with a positive semidefinite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
        assertNotNull(decomposition);
    }

    @Test
    public void testRectangularCholeskyDecompositionWithNonPositiveDefiniteMatrixException() {
        // Test constructor with a non-positive semidefinite matrix that should throw an exception
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, -4}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0001));
    }

    @Test
    public void testGetRootMatrix() {
        // Test the getRootMatrix method
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
        assertNotNull(decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        // Test the getRank method
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
        assertEquals(2, decomposition.getRank());
    }

}