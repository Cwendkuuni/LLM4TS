import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] data = {
                {2, 1, 1},
                {1, 2, 1},
                {1, 1, 2}
        };
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        double[][] expectedData = {
                {1.414, 0, 0},
                {0.707, 1.225, 0},
                {0.707, 0.612, 1.155}
        };
        assertArrayEquals(expectedData, root.getData());
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeEigenvalue() {
        double[][] data = {
                {1, 2, 3},
                {2, 5, 7},
                {3, 7, 10}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testZeroEigenvalue() {
        double[][] data = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix);
    }
}