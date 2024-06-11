import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testGetEntry() {
        // Typical use case
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);

        // Edge case: Minimum possible row and column indices
        matrix.setEntry(0, 0, 0.0);
        Assert.assertEquals(0.0, matrix.getEntry(0, 0), 0.0);

        // Edge case: Maximum possible row and column indices
        matrix.setEntry(2, 2, 10.0);
        Assert.assertEquals(10.0, matrix.getEntry(2, 2), 0.0);

        // Error scenario: Row index out of bounds
        try {
            matrix.getEntry(3, 1);
            Assert.fail("Expected org.apache.commons.math.exception.MatrixIndexException");
        } catch (org.apache.commons.math.exception.MatrixIndexException e){
            Assert.assertTrue(e instanceof org.apache.commons.math.exception.MatrixIndexException);
        }

        // Error scenario: Column index out of bounds
        try {
            matrix.getEntry(1, 3);
            Assert.fail("Expected org.apache.commons.math.exception.MatrixIndexException");
        } catch (org.apache.commons.math.exception.MatrixIndexException e){
            Assert.assertTrue(e instanceof org.apache.commons.math.exception.MatrixIndexException);
        }
    }

    @Test
    public void testSetEntry() {
        // Typical use case
        matrix.setEntry(0, 0, 1.5);
        Assert.assertEquals(1.5, matrix.getEntry(0, 0), 0.0);

        // Edge case: Minimum possible row and column indices
        matrix.setEntry(0, 0, 0.0);
        Assert.assertEquals(0.0, matrix.getEntry(0, 0), 0.0);

        // Edge case: Maximum possible row and column indices
        matrix.setEntry(2, 2, 10.0);
        Assert.assertEquals(10.0, matrix.getEntry(2, 2), 0.0);

        // Error scenario: Row index out of bounds
        try {
            matrix.setEntry(3, 1, 5.0);
            Assert.fail("Expected org.apache.commons.math.exception.MatrixIndexException");
        } catch (org.apache.commons.math.exception.MatrixIndexException e){
            Assert.assertTrue(e instanceof org.apache.commons.math.exception.MatrixIndexException);
        }

        // Error scenario: Column index out of bounds
        try {
            matrix.setEntry(1, 3, 5.0);
            Assert.fail("Expected org.apache.commons.math.exception.MatrixIndexException");
        } catch (org.apache.commons.math.exception.MatrixIndexException e){
            Assert.assertTrue(e instanceof org.apache.commons.math.exception.MatrixIndexException);
        }
    }

    @Test
    public void testAdd() {
        // Typical use case
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix.add(m);
        Assert.assertEquals(1.0, result.getEntry(0, 0), 0.0);

        // Edge case: Adding identity matrix
        OpenMapRealMatrix identity = new OpenMapRealMatrix(3, 3);
        identity.setEntry(0, 0, 1.0);
        identity.setEntry(1, 1, 1.0);
        identity.setEntry(2, 2, 1.0);
        result = matrix.add(identity);
        Assert.assertEquals(1.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(1.0, result.getEntry(1, 1), 0.0);
        Assert.assertEquals(1.0, result.getEntry(2, 2), 0.0);

        // Error scenario: Matrices with different dimensions
        OpenMapRealMatrix differentDimensions = new OpenMapRealMatrix(2, 3);
        try {
            matrix.add(differentDimensions);
            Assert.fail("Expected org.apache.commons.math.exception.DimensionMismatchException");
        } catch (org.apache.commons.math.exception.DimensionMismatchException e) {
            Assert.assertTrue(e instanceof org.apache.commons.math.exception.DimensionMismatchException);
        }
    }

    @Test
    public void testSubtract() {
        // Typical use case
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix.subtract(m);
        Assert.assertEquals(-1.0, result.getEntry(0, 0), 0.0);

        // Edge case: Subtracting identity matrix
        OpenMapRealMatrix identity = new OpenMapRealMatrix(3, 3);
        identity.setEntry(0, 0, 1.0);
        identity.setEntry(1, 1, 1.0);
        identity.setEntry(2, 2, 1.0);
        result = matrix.subtract(identity);
        Assert.assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(-1.0, result.getEntry(1, 1), 0.0);
        Assert.assertEquals(-1.0, result.getEntry(2, 2), 0.0);

        // Error scenario: Matrices with different dimensions
        OpenMapRealMatrix differentDimensions = new OpenMapRealMatrix(2, 3);
        try {
            matrix.subtract(differentDimensions);
            Assert.fail("Expected org.apache.commons.math.exception.DimensionMismatchException");
        } catch (org.apache.commons.math.exception.DimensionMismatchException e) {
            Assert.assertTrue(e instanceof org.apache.commons.math.exception.DimensionMismatchException);
        }
    }

    @Test
    public void testMultiply() {
        // Typical use case
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix.multiply(m);
        Assert.assertEquals(0.0, result.getEntry(0, 0), 0.0);

        // Edge case: Multiplying by identity matrix
        OpenMapRealMatrix identity = new OpenMapRealMatrix(3, 3);
        identity.setEntry(0, 0, 1.0);
        identity.setEntry(1, 1, 1.0);
        identity.setEntry(2, 2, 1.0);
        result = matrix.multiply(identity);
        Assert.assertEquals(0.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, result.getEntry(1, 1), 0.0);
        Assert.assertEquals(0.0, result.getEntry(2, 2), 0.0);

        // Error scenario: Matrices with incompatible dimensions
        OpenMapRealMatrix incompatibleDimensions = new OpenMapRealMatrix(2, 3);
        try {
            matrix.multiply(incompatibleDimensions);
            Assert.fail("Expected org.apache.commons.math.exception.DimensionMismatchException");
        } catch (org.apache.commons.math.exception.DimensionMismatchException e) {
            Assert.assertTrue(e instanceof org.apache.commons.math.exception.DimensionMismatchException);
        }
    }

}