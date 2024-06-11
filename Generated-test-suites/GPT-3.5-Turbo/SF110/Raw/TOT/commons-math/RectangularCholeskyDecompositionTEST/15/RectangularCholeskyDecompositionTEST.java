import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, -5, 3}});
        assertEquals(expected, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        int expected = 3;
        assertEquals(expected, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, -98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testSmallValue() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.1;
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixWithSmallValue() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, -98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.1;
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

}