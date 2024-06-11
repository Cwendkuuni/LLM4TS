import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition choleskyDecomposition;

    @Before
    public void setup() {
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(new double[][]{
                {4.0, 12.0},
                {12.0, 37.0}
        });
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 1.0}
        });
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 4.0},
                {3.0, 4.0, 1.0}
        });
        choleskyDecomposition = new RectangularCholeskyDecomposition(matrix1);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
                {2.0, 0.0},
                {6.0, 1.0}
        });
        RealMatrix actual = choleskyDecomposition.getRootMatrix();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetRank() {
        int expected = 2;
        int actual = choleskyDecomposition.getRank();
        assertEquals(expected, actual);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException1() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, -1.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException2() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 4.0},
                {3.0, 4.0, -1.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    // Add more test cases for edge cases and exception handling scenarios

}