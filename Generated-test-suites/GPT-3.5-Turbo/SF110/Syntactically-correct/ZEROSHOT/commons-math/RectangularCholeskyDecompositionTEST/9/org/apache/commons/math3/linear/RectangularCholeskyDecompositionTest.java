import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        double[][] data = {{4, 1, 1},
                           {1, 7, -2},
                           {1, -2, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        Assert.assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] data = {{1, 2},
                           {2, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 1, 1},
                           {1, 7, -2},
                           {1, -2, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        RealMatrix rootMatrix = decomposition.getRootMatrix();

        double[][] expectedData = {{2, 0, 0},
                                   {0.5, 2.5, 0},
                                   {0.5, -0.8, 2.8}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

        Assert.assertEquals(expected, rootMatrix);
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, 1, 1},
                           {1, 7, -2},
                           {1, -2, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        Assert.assertEquals(3, decomposition.getRank());
    }
}