import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double[][] data = {{4, -1, 1}, {-1, 4, -2}, {1, -2, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(matrix, root.multiply(root.transpose()));
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, -1, 1}, {-1, 4, -2}, {1, -2, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] data = {{-1, 0, 0}, {0, -1, 0}, {0, 0, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    @Test
    public void testSmallDiagonalElementThreshold() {
        double[][] data = {{4, -1, 1}, {-1, 4, -2}, {1, -2, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(matrix, root.multiply(root.transpose()));
    }
}