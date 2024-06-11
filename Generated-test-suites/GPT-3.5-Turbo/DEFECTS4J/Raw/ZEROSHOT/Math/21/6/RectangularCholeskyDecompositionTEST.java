import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, Math.pow(10, -10));

        double[][] expectedData = {{2, 6}, {0, 1}, {-8, 5}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);

        Assert.assertArrayEquals(expectedMatrix.getData(), decomposition.getRootMatrix().getData());
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, Math.pow(10, -10));

        Assert.assertEquals(2, decomposition.getRank());
    }
}