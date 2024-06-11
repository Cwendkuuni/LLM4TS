import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test valid input
        double[][] inputMatrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputMatrixData);
        double small = FastMath.pow(10, -6);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(inputMatrix, small);

        RealMatrix rootMatrix = decomposition.getRootMatrix();
        Assert.assertNotNull(rootMatrix);
        Assert.assertEquals(3, rootMatrix.getRowDimension());
        Assert.assertEquals(2, rootMatrix.getColumnDimension());

        int rank = decomposition.getRank();
        Assert.assertEquals(2, rank);

        // Test non-positive definite matrix
        double[][] invalidMatrixData = {{1, 2}, {2, 4}};
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(invalidMatrixData);

        try {
            RectangularCholeskyDecomposition invalidDecomposition = new RectangularCholeskyDecomposition(invalidMatrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Test passed
        }
    }

    @Test
    public void testGetRootMatrix() {
        double[][] inputMatrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputMatrixData);
        double small = FastMath.pow(10, -6);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(inputMatrix, small);

        RealMatrix rootMatrix = decomposition.getRootMatrix();
        Assert.assertNotNull(rootMatrix);
        Assert.assertEquals(3, rootMatrix.getRowDimension());
        Assert.assertEquals(2, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        double[][] inputMatrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputMatrixData);
        double small = FastMath.pow(10, -6);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(inputMatrix, small);

        int rank = decomposition.getRank();
        Assert.assertEquals(2, rank);
    }
}