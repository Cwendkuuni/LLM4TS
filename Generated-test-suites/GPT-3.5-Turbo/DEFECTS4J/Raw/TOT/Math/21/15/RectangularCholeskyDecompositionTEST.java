import org.junit.Test;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 1, 1},
            {1, 2, 3},
            {1, 3, 6}
        });

        double tolerance = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);

        RealMatrix rootMatrix = decomposition.getRootMatrix();

        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0, 0},
            {0.5, 1.5811388300841898, 0},
            {0.5, 2.0864507107267635, 1.224744871391589}
        });

        assertTrue(expectedRootMatrix.equals(rootMatrix, tolerance));
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 1, 1},
            {1, 2, 3},
            {1, 3, 6}
        });

        double tolerance = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);

        int rank = decomposition.getRank();

        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 1, 1},
            {1, -2, 3},
            {1, 3, 6}
        });

        double tolerance = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
    }

    @Test
    public void testSmallDiagonalElementsThreshold() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 1, 1},
            {1, 2, 3},
            {1, 3, 6}
        });

        double tolerance = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);

        RealMatrix rootMatrix = decomposition.getRootMatrix();

        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0, 0},
            {0.5, 1.5811388300841898, 0},
            {0.5, 2.0864507107267635, 1.224744871391589}
        });

        assertTrue(expectedRootMatrix.equals(rootMatrix, tolerance));
    }

}