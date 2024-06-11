import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition_constructor1() {
        // Typical use case: valid input, expect successful initialization

        // Create a valid RealMatrix
        double[][] matrixData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Test constructor
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        // Verify results
        RealMatrix rootMatrix = rcd.getRootMatrix();
        Assert.assertNotNull("Root matrix should not be null", rootMatrix);
        
        int rank = rcd.getRank();
        Assert.assertEquals("Rank should be 3", 3, rank);
    }

    @Test
    public void testRectangularCholeskyDecomposition_constructor2() {
        // Edge case: zero small value, expect successful initialization
        
        // Create a valid RealMatrix
        double[][] matrixData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Test constructor
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 0.0);
        
        // Verify results
        RealMatrix rootMatrix = rcd.getRootMatrix();
        Assert.assertNotNull("Root matrix should not be null", rootMatrix);
        
        int rank = rcd.getRank();
        Assert.assertEquals("Rank should be 3", 3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_constructor3() {
        // Error scenario: non-positive definite matrix, expect NonPositiveDefiniteMatrixException

        // Create a non-positive definite RealMatrix
        double[][] matrixData = {{1, 0, 0}, {0, -1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Test constructor
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_constructor4() {
        // Error scenario: non-positive-definite matrix with small value, expect NonPositiveDefiniteMatrixException
        
        // Create a non-positive definite RealMatrix
        double[][] matrixData = {{1, 0, 0}, {0, -1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        // Test constructor
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 0.1);
    }

    @Test
    public void testGetRootMatrix() {
        // Edge case: root matrix with rank = 0, expect all entries to be zero
        
        // Create a valid RealMatrix with zero rank
        double[][] matrixData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);

        // Test getRootMatrix
        RealMatrix rootMatrix = rcd.getRootMatrix();
        
        // Verify results
        Assert.assertEquals("Row dimension of root matrix should be 3", 3, rootMatrix.getRowDimension());
        Assert.assertEquals("Column dimension of root matrix should be 0", 0, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        // Typical use case: valid input, expect rank to be 3

        // Create a valid RealMatrix with rank = 3
        double[][] matrixData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);

        // Test getRank
        int rank = rcd.getRank();

        // Verify result
        Assert.assertEquals("Rank should be 3", 3, rank);
    }

}