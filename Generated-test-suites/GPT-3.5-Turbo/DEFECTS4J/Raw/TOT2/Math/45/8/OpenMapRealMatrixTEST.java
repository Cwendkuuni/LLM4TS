import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    // Test data for constructor
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0.0, matrix.getEntry(1, 1), 0.0);
    }

    // Test data for copy method
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 10.0);
        OpenMapRealMatrix copiedMatrix = matrix.copy();
        assertEquals(10.0, copiedMatrix.getEntry(1, 1), 0.0);
    }

    // Test data for add method
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 0.0);
    }

    // Test data for subtract method
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 0.5);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(0.5, result.getEntry(0, 0), 0.0);
    }

    // Test data for multiply method
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(2.0, result.getEntry(0, 0), 0.0);
    }

    // Test data for getEntry method
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    // Test data for setEntry method
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 1, 8.0);
        assertEquals(8.0, matrix.getEntry(0, 1), 0.0);
    }

    // Test data for addToEntry method
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
    }

    // Test data for multiplyEntry method
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 3.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
    }

    // Test data for computeKey method
    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        int key = matrix.computeKey(1, 1);
        assertEquals(3, key);
    }
}