
import static org.junit.Assert.*;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        // Initialize the matrices
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(1, 0, 3.0);
        m1.setEntry(1, 1, 4.0);
        m2.setEntry(0, 0, 2.0);
        m2.setEntry(0, 1, 4.0);
        m2.setEntry(1, 0, 6.0);
        m2.setEntry(1, 1, 8.0);

        // Perform the addition
        OpenMapRealMatrix result = m1.add(m2);

        // Check the result
        assertEquals(3.0, result.getEntry(0, 0), 0.0001);
        assertEquals(6.0, result.getEntry(0, 1), 0.0001);
        assertEquals(9.0, result.getEntry(1, 0), 0.0001);
        assertEquals(12.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSubtract() {
        // Initialize the matrices
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(1, 0, 3.0);
        m1.setEntry(1, 1, 4.0);
        m2.setEntry(0, 0, 2.0);
        m2.setEntry(0, 1, 4.0);
        m2.setEntry(1, 0, 6.0);
        m2.setEntry(1, 1, 8.0);

        // Perform the subtraction
        OpenMapRealMatrix result = m1.subtract(m2);

        // Check the result
        assertEquals(-1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(-2.0, result.getEntry(0, 1), 0.0001);
        assertEquals(-3.0, result.getEntry(1, 0), 0.0001);
        assertEquals(-4.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiply() {
        // Initialize the matrices
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(1, 0, 3.0);
        m1.setEntry(1, 1, 4.0);
        m2.setEntry(0, 0, 2.0);
        m2.setEntry(0, 1, 4.0);
        m2.setEntry(1, 0, 6.0);
        m2.setEntry(1, 1, 8.0);

        // Perform the multiplication
        OpenMapRealMatrix result = m1.multiply(m2);

        // Check the result
        assertEquals(14.0, result.getEntry(0, 0), 0.0001);
        assertEquals(20.0, result.getEntry(0, 1), 0.0001);
        assertEquals(30.0, result.getEntry(1, 0), 0.0001);
        assertEquals(44.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetEntry() {
        // Initialize the matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        // Check the entries
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSetEntry() {
        // Initialize the matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        // Set the entries
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        // Check the entries
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testAddToEntry() {
        // Initialize the matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        // Add to the entries
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(0, 1, 1.0);
        matrix.addToEntry(1, 0, 1.0);
        matrix.addToEntry(1, 1, 1.0);

        // Check the entries
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(3.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        // Initialize the matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        // Multiply the entries
        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 1, 2.0);
        matrix.multiplyEntry(1, 0, 2.0);
        matrix.multiplyEntry(1, 1, 2.0);

        // Check the entries
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(6.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0001);
    }

}
