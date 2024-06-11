import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;
    private double[][] testData;

    @Before
    public void setUp() {
        testData = new double[][]{{9, -1, -4}, {-1, 6, 2}, {-4, 2, 6}};
        decomposition = new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(testData));
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{Math.sqrt(9), 0, 0}, {-1.0 / Math.sqrt(9), Math.sqrt(6 - Math.pow(-1.0 / Math.sqrt(9), 2)), 0}, {-4.0 / Math.sqrt(9), (2 - (-4.0 / Math.sqrt(9)) * -1.0 / Math.sqrt(9)) / Math.sqrt(6 - Math.pow(-1.0 / Math.sqrt(9), 2)), Math.sqrt(6 - Math.pow(-1.0 / Math.sqrt(9), 2) - Math.pow((2 - (-4.0 / Math.sqrt(9)) * -1.0 / Math.sqrt(9)) / Math.sqrt(6 - Math.pow(-1.0 / Math.sqrt(9), 2)), 2))}});
        RealMatrix actual = decomposition.getRootMatrix();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetRank() {
        int expected = 3;
        int actual = decomposition.getRank();
        assertEquals(expected, actual);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException1() {
        double[][] testData = new double[][]{{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(testData));
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException2() {
        double[][] testData = new double[][]{{1, -1, 0}, {-1, 0, 0}, {0, 0, 1}};
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(testData));
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException3() {
        double[][] testData = new double[][]{{1, 0, -1}, {0, -1, 0}, {-1, 0, 1}};
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(testData));
    }

}