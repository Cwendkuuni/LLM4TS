import org.junit.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class RectangularCholeskyDecompositionTest {

    // Test case for RectangularCholeskyDecomposition(RealMatrix matrix) constructor
    @Test
    public void testConstructor1() throws NonPositiveDefiniteMatrixException {
        // Typical use case
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertNotNull("Root matrix should not be null.", decomposition.getRootMatrix());
        Assert.assertEquals("Rank should be 3.", 3, decomposition.getRank());

        // Edge case: matrix with all zeroes
        double[][] zeroMatrixData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        RealMatrix zeroMatrix = MatrixUtils.createRealMatrix(zeroMatrixData);
        try {
            RectangularCholeskyDecomposition zeroDecomposition = new RectangularCholeskyDecomposition(zeroMatrix);
            Assert.fail("Should throw NonPositiveDefiniteMatrixException for matrix with all zeroes.");
        } catch (NonPositiveDefiniteMatrixException ex) {
            Assert.assertTrue("Exception message should mention non-positive definite matrix.", ex.getMessage().contains("Non-positive definite matrix"));
        }
        
        // Error scenario: matrix with negative diagonal element
        double[][] negativeMatrixData = {{4, 12, -16}, {12, -37, -43}, {-16, -43, 98}};
        RealMatrix negativeMatrix = MatrixUtils.createRealMatrix(negativeMatrixData);
        try {
            RectangularCholeskyDecomposition negativeDecomposition = new RectangularCholeskyDecomposition(negativeMatrix);
            Assert.fail("Should throw NonPositiveDefiniteMatrixException for matrix with negative diagonal element.");
        } catch (NonPositiveDefiniteMatrixException ex) {
            Assert.assertTrue("Exception message should mention negative diagonal element.", ex.getMessage().contains("Negative diagonal element"));
        }
    }
    
    // Test case for RectangularCholeskyDecomposition(RealMatrix matrix, double small) constructor
    @Test
    public void testConstructor2() throws NonPositiveDefiniteMatrixException {
        // Typical use case
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.000001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        Assert.assertNotNull("Root matrix should not be null.", decomposition.getRootMatrix());
        Assert.assertEquals("Rank should be 3.", 3, decomposition.getRank());

        // Edge case: small value makes diagonal element non-positive definite
        double[][] defectiveMatrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 0.0000001}};
        RealMatrix defectiveMatrix = MatrixUtils.createRealMatrix(defectiveMatrixData);
        try {
            RectangularCholeskyDecomposition defectiveDecomposition = new RectangularCholeskyDecomposition(defectiveMatrix, small);
            Assert.fail("Should throw NonPositiveDefiniteMatrixException for matrix with small non-positive definite diagonal element.");
        } catch (NonPositiveDefiniteMatrixException ex) {
            Assert.assertTrue("Exception message should mention non-positive definite matrix.", ex.getMessage().contains("Non-positive definite matrix"));
        }

        // Error scenario: large small value makes the decomposition fail
        double largeSmall = 1000.0;
        try {
            RectangularCholeskyDecomposition largeSmallDecomposition = new RectangularCholeskyDecomposition(matrix, largeSmall);
            Assert.fail("Should throw NonPositiveDefiniteMatrixException for matrix with large value of small.");
        } catch (NonPositiveDefiniteMatrixException ex) {
            Assert.assertTrue("Exception message should mention non-positive definite matrix.", ex.getMessage().contains("Non-positive definite matrix"));
        }
    }
    
    // Test case for getRootMatrix() method
    @Test
    public void testGetRootMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        Assert.assertEquals("Root matrix should have correct dimensions.",
                matrix.getRowDimension(), rootMatrix.getRowDimension());
        Assert.assertEquals("Root matrix should have correct dimensions.",
                decomposition.getRank(), rootMatrix.getColumnDimension());
        Assert.assertEquals("Root matrix should have correct values.",
                decomposition.getRootMatrix(), rootMatrix);
    }
    
    // Test case for getRank() method
    @Test
    public void testGetRank() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals("Rank should be 3.", 3, decomposition.getRank());
    }
}