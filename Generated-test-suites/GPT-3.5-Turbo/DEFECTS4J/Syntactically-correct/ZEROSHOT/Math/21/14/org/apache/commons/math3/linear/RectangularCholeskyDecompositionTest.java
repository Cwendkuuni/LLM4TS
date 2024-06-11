import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix createMatrix(double[][] data) {
        return MatrixUtils.createRealMatrix(data);
    }

    @Test
    public void testConstructor() {
        double[][] data = {{4, 2, 1}, {2, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = createMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);

        assertNotNull(decomposition);
        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrixException() {
        double[][] data = {{4, 2, 1}, {2, 5, 3}, {1, 3, -6}};
        RealMatrix matrix = createMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 2, 1}, {2, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = createMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);

        RealMatrix expected = createMatrix(data);
        assertEquals(expected, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, 2, 1}, {2, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = createMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);

        assertEquals(3, decomposition.getRank());
    }
}