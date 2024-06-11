import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition cholesky;

    @Before
    public void setUp() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{5, 2, 1}, {2, 5, 1}, {1, 1, 5}});
        cholesky = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2.236, 0, 0}, {0.894, 2.358, 0}, {0.447, 0.447, 2.236}});
        double[][] actualData = cholesky.getRootMatrix().getData();
        double[][] expectedData = expected.getData();
        for (int i = 0; i < actualData.length; i++) {
            assertArrayEquals(expectedData[i], actualData[i], 0.001);
        }
    }

    @Test
    public void testGetRank() {
        int expected = 3;
        int actual = cholesky.getRank();
        assertEquals(expected, actual);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorThrowsNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{5, -2, 1}, {-2, 5, 1}, {1, 1, 5}});
        cholesky = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorThrowsNonPositiveDefiniteMatrixException2() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{5, 2, 1}, {2, 0, 1}, {1, 1, 5}});
        cholesky = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorThrowsNonPositiveDefiniteMatrixException3() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{5, 2, 1}, {2, 5, 1}, {1, 1, -5}});
        cholesky = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorThrowsNonPositiveDefiniteMatrixException4() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-5, 2, 1}, {2, 5, 1}, {1, 1, 5}});
        cholesky = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorThrowsNonPositiveDefiniteMatrixException5() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{5, 2, 1}, {2, -5, 1}, {1, 1, -1}});
        cholesky = new RectangularCholeskyDecomposition(matrix);
    }
}