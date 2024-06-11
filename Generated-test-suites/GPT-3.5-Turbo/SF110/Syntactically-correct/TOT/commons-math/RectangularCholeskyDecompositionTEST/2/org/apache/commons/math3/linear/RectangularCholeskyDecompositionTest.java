import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2, 6}, {0, 1}});
        assertEquals(expected, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 0}, {0, 1}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testZeroMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0, 0}, {0, 0}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSmallMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1e-10, 0}, {0, 1}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testLargeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1e10, 0}, {0, 1}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }
}