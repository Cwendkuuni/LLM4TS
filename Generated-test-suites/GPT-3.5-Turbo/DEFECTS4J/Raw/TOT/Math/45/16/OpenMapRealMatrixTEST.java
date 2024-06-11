import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NumberIsTooLargeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructor() {
        // Check rows and columns
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());

        // Check all entries are initialized to zero
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(0.0, matrix.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testCopy() {
        // Create a new matrix with same dimensions
        OpenMapRealMatrix copy = matrix.copy();

        // Check dimensions match
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());

        // Check all entries are equal
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), copy.getEntry(i, j), 0.0);
            }
        }

        // Modify the original matrix
        matrix.setEntry(0, 0, 1.0);

        // Check that the copy remains unaffected
        assertEquals(0.0, copy.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAdd() {
        // Create another matrix with same dimensions
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);

        // Add the other matrix to the original matrix
        OpenMapRealMatrix result = matrix.add(other);

        // Check that the result matrix has the same dimensions
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(matrix.getColumnDimension(), result.getColumnDimension());

        // Check that all entries in the result matrix are equal to the original matrix
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }

        // Modify the other matrix
        other.setEntry(0, 0, 1.0);

        // Add the modified other matrix to the original matrix
        result = matrix.add(other);

        // Check that the result matrix is updated correctly
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(2, 2);
        matrix.add(other);
    }

    @Test
    public void testSubtract() {
        // Create another matrix with same dimensions
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);

        // Subtract the other matrix from the original matrix
        OpenMapRealMatrix result = matrix.subtract(other);

        // Check that the result matrix has the same dimensions
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(matrix.getColumnDimension(), result.getColumnDimension());

        // Check that all entries in the result matrix are equal to the original matrix
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }

        // Modify the other matrix
        other.setEntry(0, 0, 1.0);

        // Subtract the modified other matrix from the original matrix
        result = matrix.subtract(other);

        // Check that the result matrix is updated correctly
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(2, 2);
        matrix.subtract(other);
    }

    @Test
    public void testMultiplyOpenMapRealMatrix() {
        // Create another matrix with compatible dimensions
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);

        // Multiply the original matrix with the other matrix
        OpenMapRealMatrix result = matrix.multiply(other);

        // Check that the result matrix has the correct dimensions
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(other.getColumnDimension(), result.getColumnDimension());

        // Check that all entries in the result matrix are equal to zero
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < other.getColumnDimension(); j++) {
                assertEquals(0.0, result.getEntry(i, j), 0.0);
            }
        }

        // Modify the other matrix
        other.setEntry(0, 0, 1.0);

        // Multiply the original matrix with the modified other matrix
        result = matrix.multiply(other);

        // Check that the result matrix is updated correctly
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyOpenMapRealMatrixDimensionMismatch() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(2, 2);
        matrix.multiply(other);
    }

    @Test
    public void testGetEntry() {
        // Set an entry in the matrix
        matrix.setEntry(0, 0, 10.0);

        // Check that the entry can be retrieved correctly
        assertEquals(10.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSetEntry() {
        // Set an entry in the matrix
        matrix.setEntry(0, 0, 10.0);

        // Check that the entry was set correctly
        assertEquals(10.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAddToEntry() {
        // Set an entry in the matrix
        matrix.setEntry(0, 0, 10.0);

        // Add a value to the entry
        matrix.addToEntry(0, 0, 5.0);

        // Check that the entry was updated correctly
        assertEquals(15.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        // Set an entry in the matrix
        matrix.setEntry(0, 0, 10.0);

        // Multiply the entry by a factor
        matrix.multiplyEntry(0, 0, 2.0);

        // Check that the entry was updated correctly
        assertEquals(20.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testComputeKeyNumberIsTooLargeException() {
        matrix.computeKey(1000, 1000);
    }

}