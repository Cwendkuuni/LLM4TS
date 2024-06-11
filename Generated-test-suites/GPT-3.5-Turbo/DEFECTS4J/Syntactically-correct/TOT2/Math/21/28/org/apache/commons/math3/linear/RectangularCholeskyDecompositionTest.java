import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test initialization and basic functionality of RectangularCholeskyDecomposition
        // Create a symmetric positive semidefinite matrix
        double[][] testData = {{4, 1}, {1, 3}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-12);

        // Test the root matrix
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());

        // Test the rank
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmallMatrix() {
        // Test RectangularCholeskyDecomposition with a small matrix
        double[][] testData = {{2}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-12);

        // Test the root matrix
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(1, rootMatrix.getRowDimension());
        assertEquals(1, rootMatrix.getColumnDimension());

        // Test the rank
        assertEquals(1, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        // Test handling of non-positive definite matrix
        double[][] testData = {{-2, 1}, {1, 3}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-12);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionWithZeroThreshold() {
        // Test handling of non-positive definite matrix with zero threshold
        double[][] testData = {{4, 1}, {1, 3}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 0);
    }

}