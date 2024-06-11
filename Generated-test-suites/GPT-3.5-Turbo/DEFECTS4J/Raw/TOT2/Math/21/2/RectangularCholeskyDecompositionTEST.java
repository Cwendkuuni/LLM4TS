import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new Array2DRowRealMatrix(new double[][]{
                {25, 15, -5},
                {15, 18,  0},
                {-5,  0, 11}
        });
    }

    @Test
    public void testRectangularCholeskyDecomposition() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        assertEquals(5, rootMatrix.getEntry(0, 0), 1e-12);
        assertEquals(3, rootMatrix.getEntry(1, 0), 1e-12);
        assertEquals(-1, rootMatrix.getEntry(2, 0), 1e-12);
    }

    @Test
    public void testGetRank() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix invalidMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 0},
                {0, -1}
        });
        new RectangularCholeskyDecomposition(invalidMatrix, 1e-12);
    }

    @Test
    public void testSmallDiagonalElements() {
        RealMatrix matrixWithSmallDiagonal = new Array2DRowRealMatrix(new double[][]{
                {1e-6, 0},
                {0, 1e-12}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrixWithSmallDiagonal, 1e-12);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        assertEquals(1e-3, rootMatrix.getEntry(0, 0), 1e-12);
        assertEquals(0, rootMatrix.getEntry(1, 1), 1e-12);
    }

}