import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] matrixData = {
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1e-5);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {
                {2, 6, -8},
                {0, 1, -5},
                {0, 0, 3}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(rootMatrix.equals(expectedMatrix));
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 99}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1e-5);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionWithZeroThreshold() {
        double[][] matrixData = {
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, 0);
    }

    @Test
    public void testSmallThreshold() {
        double[][] matrixData = {
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {
                {2, 6, -8},
                {0, 1, -5},
                {0, 0, 3}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(rootMatrix.equals(expectedMatrix));
    }

    @Test
    public void testMaximalThreshold() {
        double[][] matrixData = {
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, Double.MAX_VALUE);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {
                {2, 6, -8},
                {0, 1, -5},
                {0, 0, 3}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(rootMatrix.equals(expectedMatrix));
    }

    @Test
    public void testSubMatrix() {
        double[][] matrixData = {
                {4, 12},
                {12, 37},
                {-16, -43},
                {1, 0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1e-5);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {
                {2, 6},
                {0, 1},
                {0, 0},
                {0, 0}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(rootMatrix.equals(expectedMatrix));
    }

}