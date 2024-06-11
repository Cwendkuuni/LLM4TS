import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    private RectangularCholeskyDecomposition rcd;

    @Before
    public void setUp() {
        double[][] matrixData = {{4, 1, 1}, {1, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        rcd = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] expectedRootData = {{2, 0, 0}, {0.5, 2.9154759474226504, 0}, {0.5, 1.7452854618718645, 1.121620027816168}};
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(expectedRootData);

        RealMatrix actualRootMatrix = rcd.getRootMatrix();

        assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData());
    }

    @Test
    public void testGetRank() {
        int expectedRank = 3;

        int actualRank = rcd.getRank();

        assertEquals(expectedRank, actualRank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{4, 1, 1}, {1, 5, 3}, {1, 3, -6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        rcd = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testZeroMatrix() {
        double[][] matrixData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        rcd = new RectangularCholeskyDecomposition(matrix);

        double[][] expectedRootData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(expectedRootData);

        RealMatrix actualRootMatrix = rcd.getRootMatrix();

        assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData());
    }
}