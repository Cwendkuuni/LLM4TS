import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    // Test constructor with valid input matrix
    @Test
    public void testValidInputMatrix() {
        double[][] matrixData = {{4, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    // Test constructor with null input matrix should throw IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testNullInputMatrix() {
        new RectangularCholeskyDecomposition(null, 1e-10);
    }

    // Test constructor with nonpositive semidefinite matrix should throw NonPositiveDefiniteMatrixException
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[][] matrixData = {{1, -1}, {-1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    // Test getRootMatrix() method
    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRootMatrix().getColumnDimension());
    }

    // Test getRank() method
    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertEquals(2, decomposition.getRank());
    }

}