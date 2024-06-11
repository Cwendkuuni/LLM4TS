import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] data = {{4, 1, 2}, {1, 5, 3}, {2, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] data = {{-1, 1, 2}, {1, 5, 3}, {2, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    @Test
    public void testSmallDiagonalElementsThreshold() {
        double[][] data = {{4, 1, 2}, {1, 5, 3}, {2, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 10);
        assertEquals(2, decomposition.getRank());
    }

    // Add more test cases as needed for edge cases

}