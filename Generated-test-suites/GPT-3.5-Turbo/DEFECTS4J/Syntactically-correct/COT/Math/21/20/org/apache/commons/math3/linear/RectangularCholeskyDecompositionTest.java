
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;
    private double[][] matrixData;
    private RealMatrix matrix;

    @Before
    public void setUp() {
        matrixData = new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{
                {2, 0, 0},
                {6, 1, 0},
                {-8, -5, 3}
        });
        Assert.assertTrue(expectedRoot.subtract(decomposition.getRootMatrix()).getNorm() < 1e-10);
    }

    @Test
    public void testGetRank() {
        Assert.assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] invalidMatrixData = new double[][]{{4, 12, -16}, {12, -37, -43}, {-16, -43, 98}};
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(invalidMatrixData);
        RectangularCholeskyDecomposition invalidDecomposition = new RectangularCholeskyDecomposition(invalidMatrix, 1e-10);
    }

}
