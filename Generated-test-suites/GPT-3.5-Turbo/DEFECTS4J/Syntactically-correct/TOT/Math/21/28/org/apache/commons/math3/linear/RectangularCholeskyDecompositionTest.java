import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompose() {
        // Typical Use Case
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        double small = 0.001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        // Verify results
        double[][] expectedRootData = {{2.0, 0.0}, {6.0, 1.0}, {-8.0, -7.0}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
        Assert.assertTrue(expectedRoot.subtract(root).isZero(small));
        int expectedRank = 2;
        Assert.assertEquals(expectedRank, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecomposeNonPositiveDefinite() {
        // Error Scenario: Non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 1}, {1, -1}});
        double small = 0.001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testDecomposeSmallDiagonalElements() {
        // Edge Case: Small diagonal elements causing columns to be discarded
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 0.00001}});
        double small = 0.001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        // Verify results
        double[][] expectedRootData = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
        Assert.assertTrue(expectedRoot.subtract(root).isZero(small));
        int expectedRank = 2;
        Assert.assertEquals(expectedRank, rank);
    }

}