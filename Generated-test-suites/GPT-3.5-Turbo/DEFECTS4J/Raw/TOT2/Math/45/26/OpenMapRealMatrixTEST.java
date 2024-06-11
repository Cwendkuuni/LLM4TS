import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.OpenMapRealMatrix;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        // Test typical case
        matrix1.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 0.0001);
        
        // Test edge case - empty matrices
        OpenMapRealMatrix resultEmpty = matrix1.add(new OpenMapRealMatrix(2, 2));
        assertEquals(1.0, resultEmpty.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        // Test typical case
        matrix1.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        
        // Test edge case - empty matrices
        OpenMapRealMatrix resultEmpty = matrix1.subtract(new OpenMapRealMatrix(2, 2));
        assertEquals(3.0, resultEmpty.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        // Test typical case
        matrix1.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(6.0, result.getEntry(0, 0), 0.0001);
        
        // Test edge case - empty matrices
        OpenMapRealMatrix resultEmpty = matrix1.multiply(new OpenMapRealMatrix(2, 2));
        assertEquals(0.0, resultEmpty.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        // Test typical case
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0001);

        // Test edge case - out of bounds
        // This should throw an exception
        try {
            matrix.getEntry(3, 3);
            fail("Should have thrown an exception");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        // Test typical case
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0001);

        // Test edge case - setting entry to 0.0
        matrix.setEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        // Test typical case
        matrix.setEntry(1, 1, 5.0);
        matrix.addToEntry(1, 1, 3.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0001);

        // Test edge case - increment by 0.0
        matrix.addToEntry(1, 1, 0.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        // Test typical case
        matrix.setEntry(1, 1, 5.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(10.0, matrix.getEntry(1, 1), 0.0001);

        // Test edge case - multiply by 0.0
        matrix.multiplyEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(1, 1), 0.0001);
    }

    // Add more test methods for other public methods as needed

}