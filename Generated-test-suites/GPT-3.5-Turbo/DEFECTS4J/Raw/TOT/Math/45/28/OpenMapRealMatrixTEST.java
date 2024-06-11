import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    // Test case for add() method
    @Test
    public void testAdd() {
        // Create two OpenMapRealMatrix objects with different dimensions
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        // Set values in the matrices
        matrix1.setEntry(1, 1, 1.0);
        matrix1.setEntry(2, 2, 2.0);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(2, 2, 2.0);

        // Perform addition
        OpenMapRealMatrix result = matrix1.add(matrix2);

        // Verify result
        assertEquals(2.0, result.getEntry(1, 1), 0.0001);
        assertEquals(4.0, result.getEntry(2, 2), 0.0001);
    }

    // Test case for subtract() method
    @Test
    public void testSubtract() {
        // Create two OpenMapRealMatrix objects with different dimensions
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        // Set values in the matrices
        matrix1.setEntry(1, 1, 3.0);
        matrix1.setEntry(2, 2, 2.0);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(2, 2, 1.0);

        // Perform subtraction
        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        // Verify result
        assertEquals(2.0, result.getEntry(1, 1), 0.0001);
        assertEquals(1.0, result.getEntry(2, 2), 0.0001);
    }

    // Test case for multiply() method
    @Test
    public void testMultiply() {
        // Create two OpenMapRealMatrix objects with different dimensions
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);

        // Set values in the matrices
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(2, 0, 5.0);
        matrix2.setEntry(2, 1, 6.0);

        // Perform multiplication
        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        // Verify result
        assertEquals(19.0, result.getEntry(0, 0), 0.0001);
        assertEquals(26.0, result.getEntry(0, 1), 0.0001);
        assertEquals(38.0, result.getEntry(1, 0), 0.0001);
        assertEquals(52.0, result.getEntry(1, 1), 0.0001);
        assertEquals(57.0, result.getEntry(2, 0), 0.0001);
        assertEquals(78.0, result.getEntry(2, 1), 0.0001);
    }

    // Test case for getEntry() and setEntry() methods
    @Test
    public void testGetSetEntry() {
        // Create an OpenMapRealMatrix object
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        // Set a value
        matrix.setEntry(1, 1, 2.5);

        // Verify the set value
        assertEquals(2.5, matrix.getEntry(1, 1), 0.0001);
    }

    // Test case for addToEntry() method
    @Test
    public void testAddToEntry() {
        // Create an OpenMapRealMatrix object
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        // Add a value
        matrix.setEntry(1, 1, 3.0);
        matrix.addToEntry(1, 1, 2.5);

        // Verify the updated value
        assertEquals(5.5, matrix.getEntry(1, 1), 0.0001);
    }

    // Test case for multiplyEntry() method
    @Test
    public void testMultiplyEntry() {
        // Create an OpenMapRealMatrix object
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        // Multiply a value
        matrix.setEntry(1, 1, 3.0);
        matrix.multiplyEntry(1, 1, 2.5);

        // Verify the updated value
        assertEquals(7.5, matrix.getEntry(1, 1), 0.0001);
    }
}