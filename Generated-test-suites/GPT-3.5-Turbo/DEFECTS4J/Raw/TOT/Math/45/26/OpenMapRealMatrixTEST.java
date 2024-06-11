import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    // Creating an instance of OpenMapRealMatrix
    OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

    @Test
    public void testGetEntry() {
        // Setting an entry in the matrix
        matrix.setEntry(1, 1, 5.0);

        // Testing the getEntry method with a valid entry
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);

        // Testing the getEntry method with an invalid entry
        assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSetEntry() {
        // Setting an entry in the matrix
        matrix.setEntry(1, 1, 5.0);

        // Testing the setEntry method with a valid entry
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);

        // Testing the setEntry method with an invalid entry
        matrix.setEntry(-1, -1, 10.0);  // Should throw an exception
    }

    @Test
    public void testAdd() {
        // Creating a new matrix for addition
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 3.0);

        // Adding the new matrix to the original matrix
        OpenMapRealMatrix result = matrix.add(matrix2);

        // Testing the addition result
        assertEquals(3.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        // Creating a new matrix for subtraction
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 3.0);

        // Subtracting the new matrix from the original matrix
        OpenMapRealMatrix result = matrix.subtract(matrix2);

        // Testing the subtraction result
        assertEquals(-3.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        // Creating a new matrix for multiplication
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 3.0);

        // Multiplying the original matrix with the new matrix
        OpenMapRealMatrix result = matrix.multiply(matrix2);

        // Testing the multiplication result
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAdd_invalidDimensions() {
        // Creating a new matrix with different dimensions for addition
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        // Testing if the addition throws an exception
        try {
            OpenMapRealMatrix result = matrix.add(matrix2);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (org.apache.commons.math.exception.DimensionMismatchException e) {
            assertEquals("Invalid exception message", "Matrix dimensions must match", e.getMessage());
        }
    }

    @Test
    public void testSubtract_invalidDimensions() {
        // Creating a new matrix with different dimensions for subtraction
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        // Testing if the subtraction throws an exception
        try {
            OpenMapRealMatrix result = matrix.subtract(matrix2);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (org.apache.commons.math.exception.DimensionMismatchException e) {
            assertEquals("Invalid exception message", "Matrix dimensions must match", e.getMessage());
        }
    }

    @Test
    public void testMultiply_invalidDimensions() {
        // Creating a new matrix with different dimensions for multiplication
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        // Testing if the multiplication throws an exception
        try {
            OpenMapRealMatrix result = matrix.multiply(matrix2);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (org.apache.commons.math.exception.DimensionMismatchException e) {
            assertEquals("Invalid exception message", "Matrix dimensions must match", e.getMessage());
        }
    }

    @Test
    public void testAdd_zeroValue() {
        // Creating a new matrix for addition with zero value
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 0.0);

        // Adding the new matrix to the original matrix
        OpenMapRealMatrix result = matrix.add(matrix2);

        // Testing the addition result
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract_zeroValue() {
        // Creating a new matrix for subtraction with zero value
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 0.0);

        // Subtracting the new matrix from the original matrix
        OpenMapRealMatrix result = matrix.subtract(matrix2);

        // Testing the subtraction result
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply_zeroValue() {
        // Creating a new matrix for multiplication with zero value
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 0.0);

        // Multiplying the original matrix with the new matrix
        OpenMapRealMatrix result = matrix.multiply(matrix2);

        // Testing the multiplication result
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply_invalidDimensions_zeroValue() {
        // Creating a new matrix with different dimensions for multiplication with zero value
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(1, 1, 0.0);

        // Testing if the multiplication throws an exception
        try {
            OpenMapRealMatrix result = matrix.multiply(matrix2);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (org.apache.commons.math.exception.DimensionMismatchException e) {
            assertEquals("Invalid exception message", "Matrix dimensions must match", e.getMessage());
        }
    }
}