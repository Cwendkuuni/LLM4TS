import org.junit.Test;
import org.apache.commons.math3.linear.*;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    // Test case for the constructor with a small diagonal element
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithSmallDiagonalElement() {
        double[][] matrixData = {{1, 2}, {2, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.00001);
    }

    // Test case for the constructor with a non-positive semidefinite matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveSemidefiniteMatrix() {
        double[][] matrixData = {{-1, 0}, {0, -2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.00001);
    }

    // Test case for the getRootMatrix method
    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.00001);

        double[][] expectedData = {{2, 0}, {6, 1}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);

        RealMatrix rootMatrix = decomposition.getRootMatrix();

        assertEquals(expectedMatrix, rootMatrix);
    }

    // Test case for the getRank method
    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.00001);

        int expectedRank = 2;
        int rank = decomposition.getRank();

        assertEquals(expectedRank, rank);
    }

}