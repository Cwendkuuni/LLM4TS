import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        assertTrue(rootMatrix.equals(expectedMatrix));
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }
    
    @Test(expected=NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, -1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
    }
    
    @Test(expected=NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionWithZeroThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0);
    }
}