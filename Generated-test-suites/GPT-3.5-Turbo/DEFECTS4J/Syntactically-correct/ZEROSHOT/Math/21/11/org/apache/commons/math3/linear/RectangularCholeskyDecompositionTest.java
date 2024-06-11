import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] testData = {{4, 1, 1},
                               {1, 4, 1},
                               {1, 1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix expected = MatrixUtils.createRealMatrix(testData);

        assertTrue(decomposition.getRootMatrix().equals(expected));
    }

    @Test
    public void testGetRank() {
        double[][] testData = {{4, 1, 1},
                               {1, 4, 1},
                               {1, 1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        int expected = 3;

        assertEquals(expected, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] testData = {{4, -1, 1},
                               {-1, 4, 1},
                               {1, 1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
}