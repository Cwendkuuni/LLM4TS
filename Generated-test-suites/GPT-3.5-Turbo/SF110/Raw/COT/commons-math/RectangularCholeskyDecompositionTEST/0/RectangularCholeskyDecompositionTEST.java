import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;
    private double[][] data = {
            {1, 2, 3},
            {2, 5, 6},
            {3, 6, 9}
    };

    @Before
    public void setup() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
                {1, 0},
                {2, 1},
                {3, 0}
        });
        RealMatrix actual = decomposition.getRootMatrix();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRank() {
        int expected = 2;
        int actual = decomposition.getRank();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructorWithSmall() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposition);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] data = {
                {1, 2, 3},
                {2, 5, 6},
                {9, 6, 9}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
}