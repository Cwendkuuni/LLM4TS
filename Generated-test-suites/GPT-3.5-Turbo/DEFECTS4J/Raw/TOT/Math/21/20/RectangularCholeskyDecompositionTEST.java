import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Typical use case: A is positive semidefinite
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, -2}, {-2, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertNotNull(root);
        Assert.assertEquals(2, root.getRowDimension());
        Assert.assertEquals(2, root.getColumnDimension());
        Assert.assertEquals(2, decomposition.getRank());

        // Edge case: A is a 1x1 matrix
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1}});
        decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        root = decomposition.getRootMatrix();
        Assert.assertNotNull(root);
        Assert.assertEquals(1, root.getRowDimension());
        Assert.assertEquals(1, root.getColumnDimension());
        Assert.assertEquals(1, decomposition.getRank());

        // Error scenario: A is not positive semidefinite
        matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 0}, {0, -1}});
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
            Assert.fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetRank() {
        // Typical use case: A is positive semidefinite
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, -2}, {-2, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        Assert.assertEquals(2, decomposition.getRank());

        // Edge case: A is a 1x1 matrix
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1}});
        decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        Assert.assertEquals(1, decomposition.getRank());

        // Error scenario: A is not positive semidefinite
        matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 0}, {0, -1}});
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
            Assert.fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception expected
        }
    }
}