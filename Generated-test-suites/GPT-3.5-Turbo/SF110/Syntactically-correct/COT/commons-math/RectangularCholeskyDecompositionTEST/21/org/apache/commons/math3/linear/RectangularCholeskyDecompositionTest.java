
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case for RectangularCholeskyDecomposition(RealMatrix)
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.assertNotNull(decomposition.getRootMatrix());
            Assert.assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Expected a positive definite matrix.");
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmall() {
        // Test case for RectangularCholeskyDecomposition(RealMatrix, double)
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
            Assert.assertNotNull(decomposition.getRootMatrix());
            Assert.assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Expected a positive definite matrix.");
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        // Test case for RectangularCholeskyDecomposition(RealMatrix)
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, -1, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithSmallNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        // Test case for RectangularCholeskyDecomposition(RealMatrix, double)
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, -1, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
            Assert.assertArrayEquals(expectedData, rootMatrix.getData());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Expected a positive definite matrix.");
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Expected a positive definite matrix.");
        }
    }
}
