import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 2, 1}, {2, 6, 3}, {1, 3, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix root = decomposition.getRootMatrix();

        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {1, 2, 0}, {0.5, 1, 2}});
        Assert.assertEquals(expectedRoot, root);

        int rank = decomposition.getRank();
        Assert.assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 2, 1}, {2, -6, 3}, {1, 3, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    @Test
    public void testRectangularCholeskyDecompositionZeroThreshold() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 2, 1}, {2, 6, 3}, {1, 3, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0);
        RealMatrix root = decomposition.getRootMatrix();

        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {1, 2, 0}, {0.5, 1, 2}});
        Assert.assertEquals(expectedRoot, root);

        int rank = decomposition.getRank();
        Assert.assertEquals(3, rank);
    }

}