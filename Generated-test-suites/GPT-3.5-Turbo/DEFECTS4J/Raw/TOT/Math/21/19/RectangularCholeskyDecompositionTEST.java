import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, -2, 2}, {-2, 2, -4}, {2, -4, 11}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);

        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][]{{2, 0}, {-1, 1}, {1, -2}});
        Assert.assertEquals(expectedRootMatrix, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, -2, 2}, {-2, 2, -4}, {2, -4, 11}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);

        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-4, -2, 2}, {-2, 2, -4}, {2, -4, 11}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testZeroDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, -2, 2}, {-2, 0, -4}, {2, -4, 11}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
    }

    @Test
    public void testSmallDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, -2, 2}, {-2, 0.0001, -4}, {2, -4, 11}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);

        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][]{{2, 0}, {-1, 1}, {1, -2}});
        Assert.assertEquals(expectedRootMatrix, decomposition.getRootMatrix());
    }
}