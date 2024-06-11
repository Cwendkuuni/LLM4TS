import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition cholesky;

    @Before
    public void setUp() {
        double[][] data = {
            { 4, -2, 4 },
            { -2, 8, -2 },
            { 4, -2, 4 }
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        cholesky = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] expectedData = {
            { 2, 0, 2 },
            { -1, 3, -1 }
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix rootMatrix = cholesky.getRootMatrix();
        assertArrayEquals(expectedMatrix.getData(), rootMatrix.getData());
    }

    @Test
    public void testGetRank() {
        int expectedRank = 2;
        int rank = cholesky.getRank();
        assertEquals(expectedRank, rank);
    }
}