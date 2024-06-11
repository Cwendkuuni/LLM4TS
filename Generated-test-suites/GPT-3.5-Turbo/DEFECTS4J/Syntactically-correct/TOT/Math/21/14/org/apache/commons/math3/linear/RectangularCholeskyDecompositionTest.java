import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-10;
        
        // Test 1: Typical Use Case
        double[][] data = {{4, -2, 2}, {-2, 6, -3}, {2, -3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcholDecomp = new RectangularCholeskyDecomposition(matrix, small);
        
        // Verify the root matrix
        double[][] rootMatrixData = {{2, 0, 0}, {-1, 1, 0}, {1, -2, 1}};
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(rootMatrixData);
        Assert.assertEquals(expectedRootMatrix, rcholDecomp.getRootMatrix());
        
        // Test 2: Edge Case - Matrix with all diagonal elements being close to 0
        double[][] data2 = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);
        RectangularCholeskyDecomposition rcholDecomp2 = new RectangularCholeskyDecomposition(matrix2, small);
        
        // Verify the root matrix
        double[][] rootMatrixData2 = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix expectedRootMatrix2 = MatrixUtils.createRealMatrix(rootMatrixData2);
        Assert.assertEquals(expectedRootMatrix2, rcholDecomp2.getRootMatrix());
        
        // Test 3: Error Scenario - Non-Positive Definite Matrix
        double[][] data3 = {{1, -2, 2}, {-2, 6, -3}, {2, -3, 5}};
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(data3);
        try {
            RectangularCholeskyDecomposition rcholDecomp3 = new RectangularCholeskyDecomposition(matrix3, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException was not thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception was thrown as expected
        }
    }
}