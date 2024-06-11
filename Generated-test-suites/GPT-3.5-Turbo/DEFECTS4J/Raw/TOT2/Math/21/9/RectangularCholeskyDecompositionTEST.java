import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case for RectangularCholeskyDecomposition constructor
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        double small = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        assertNotNull(decomposition);
    }

    @Test
    public void testGetRootMatrix() {
        // Test case for getRootMatrix method
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        double small = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        assertNotNull(rootMatrix);
    }

    @Test
    public void testGetRank() {
        // Test case for getRank method
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        double small = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        // Test case for NonPositiveDefiniteMatrixException when the matrix is not positive semidefinite
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, 1}});
        double small = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

}