import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test with a positive definite matrix
        RealMatrix matrixPD = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decompositionPD = new RectangularCholeskyDecomposition(matrixPD, 0.0);
            RealMatrix expectedPD = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.5, 0.5}, {0.0, 0.5, 1.0}});
            Assert.assertEquals(expectedPD, decompositionPD.getRootMatrix());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a positive semidefinite matrix
        RealMatrix matrixPSD = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 0.5, 0.5}, {1.0, 0.5, 0.5}});
        try {
            RectangularCholeskyDecomposition decompositionPSD = new RectangularCholeskyDecomposition(matrixPSD, 0.0);
            RealMatrix expectedPSD = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 0.70710678118654755, 0.40824829046386304}, {0.0, 0.40824829046386304, 0.5}});
            Assert.assertEquals(expectedPSD, decompositionPSD.getRootMatrix(), 0.001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a non-positive definite matrix
        RealMatrix matrixNP = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrixNP, 0.0);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
            Assert.assertEquals(-1.0, e.getDiagonalElement(), 0.0);
            Assert.assertEquals(0, e.getRowIndex());
            Assert.assertEquals(0.0, e.getSmall());
        }

        // Test rank
        RealMatrix matrixRankTest = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decompositionRankTest = new RectangularCholeskyDecomposition(matrixRankTest, 0.0);
            Assert.assertEquals(3, decompositionRankTest.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix matrixRank1Test = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decompositionRank1Test = new RectangularCholeskyDecomposition(matrixRank1Test, 0.0);
            Assert.assertEquals(2, decompositionRank1Test.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrixNPD = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrixNPD, 0.0);
    }

    @Test
    public void testGetRankAndRootMatrix() {
        RealMatrix matrixTest = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decompositionTest = new RectangularCholeskyDecomposition(matrixTest, 0.0);
            Assert.assertEquals(3, decompositionTest.getRank());
            Assert.assertEquals(3, decompositionTest.getRootMatrix().getRowDimension());
            Assert.assertEquals(3, decompositionTest.getRootMatrix().getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetRankAndRootMatrixWithSmall() {
        RealMatrix matrixTest = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 1.5, 0.5}, {1.0, 0.5, 0.5}});
        try {
            RectangularCholeskyDecomposition decompositionTest = new RectangularCholeskyDecomposition(matrixTest, 0.5);
            Assert.assertEquals(3, decompositionTest.getRank());
            Assert.assertEquals(3, decompositionTest.getRootMatrix().getRowDimension());
            Assert.assertEquals(3, decompositionTest.getRootMatrix().getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix expectedSmall = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 0.70710678118654755, 0.40824829046386304}, {0.0, 0.40824829046386304, 0.5}});
        Assert.assertEquals(expectedSmall, decompositionTest.getRootMatrix(), 0.001);
    }
}