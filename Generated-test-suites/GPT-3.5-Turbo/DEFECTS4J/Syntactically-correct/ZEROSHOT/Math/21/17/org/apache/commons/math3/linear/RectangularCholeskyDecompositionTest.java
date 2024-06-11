import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.001;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        double[][] expectedRootMatrixData = {{2, 6, -8}, {0, 1, -5}, {0, 0, 3}};
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(expectedRootMatrixData);

        Assert.assertEquals(expectedRootMatrix, rootMatrix);
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.001;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        int expectedRank = 3;

        Assert.assertEquals(expectedRank, rank);
    }

}