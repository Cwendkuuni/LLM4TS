import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testValidDecomposition() {
        double small = 1e-6;
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.42261883, 0.29552843},
                    {0.81649658, 2.0, 1.5},
                    {0.0, 0.6, 3.0}}, false);
            Assert.assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-6);
            Assert.assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularMatrix() {
        double small = 1e-6;
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 0.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix root = svd.getV();
        Assert.assertEquals(3, root.getRowDimension());
    }

    @Test
    public void testIdentityMatrix() {
        double small = 1e-12;
        double[][] matrixData = {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            Assert.assertEquals(3, rank);
            Assert.assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.70710678, 0.70710678, 0.0},
                    {0.0, 0.70710678, 1.0}}, root.getData(), 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMatrix() {
        new RectangularCholeskyDecomposition(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSmallParameter() {
        new RectangularCholeskyDecomposition(RealMatrixUtils.createIdentityMatrix(3), -1);
    }

}