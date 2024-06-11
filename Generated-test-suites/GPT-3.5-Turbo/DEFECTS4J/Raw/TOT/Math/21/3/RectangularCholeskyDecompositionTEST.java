import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        double[][] expectedData = {{2, 6, -8}, {6, 1, 5}, {-8, 5, 4}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

        assertEquals(expected, rootMatrix);
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        int rank = decomposition.getRank();

        int expectedRank = 3;

        assertEquals(expectedRank, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[][] data = {{4, 12, -16}, {12, -1, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
    }

    @Test
    public void testSmallDiagonalElementsThreshold() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1000);
        int rank = decomposition.getRank();

        int expectedRank = 1;

        assertEquals(expectedRank, rank);
    }

}