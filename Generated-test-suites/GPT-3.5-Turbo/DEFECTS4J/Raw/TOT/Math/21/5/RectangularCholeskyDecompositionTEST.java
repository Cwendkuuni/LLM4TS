import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRootMatrix() {
        // Create a test matrix
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(data);

        // Perform rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-9);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        // Perform assertions
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][] {{2, 6, -8}, {0, 1, 4}});
        Assert.assertEquals(expectedRootMatrix, rootMatrix);
    }

    @Test
    public void testRank() {
        // Create a test matrix
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(data);

        // Perform rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-9);
        int rank = decomposition.getRank();

        // Perform assertions
        int expectedRank = 2;
        Assert.assertEquals(expectedRank, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        // Create a test matrix
        double[][] data = {{1, 2, 3}, {2, 4, 6}, {3, 6, 10}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(data);

        // Perform rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-9);
    }

}