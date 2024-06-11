import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    // Test case for OpenMapRealMatrix(int rowDimension, int columnDimension) method
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    // Test case for OpenMapRealMatrix(OpenMapRealMatrix matrix) method
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(matrix1);
        assertEquals(1.0, matrix2.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix2.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix2.getEntry(2, 2), 0.0);
    }

    // Test case for copy() method
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(1.0, matrix2.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix2.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix2.getEntry(2, 2), 0.0);
    }

    // Test case for createMatrix(int rowDimension, int columnDimension) method
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = OpenMapRealMatrix.createMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    // Test case for getColumnDimension() method
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(4, matrix.getColumnDimension());
    }

    // Test case for add(OpenMapRealMatrix m) method
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 1.0);
        
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(4.0, result.getEntry(0, 0), 0.0);
        assertEquals(4.0, result.getEntry(1, 1), 0.0);
        assertEquals(4.0, result.getEntry(2, 2), 0.0);
    }

    // Test case for subtract(OpenMapRealMatrix m) method
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 1.0);
        
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(-2.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(2.0, result.getEntry(2, 2), 0.0);
    }

    // Test case for multiply(RealMatrix m) method
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(0, 1), 0.0);
        assertEquals(0.0, result.getEntry(1, 0), 0.0);
        assertEquals(8.0, result.getEntry(1, 1), 0.0);
    }

    // Test case for getEntry(int row, int column) method
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 2, 4.0);
        assertEquals(4.0, matrix.getEntry(1, 2), 0.0);
    }

    // Test case for getRowDimension() method
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
    }

    // Test case for setEntry(int row, int column, double value) method
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 2, 4.0);
        assertEquals(4.0, matrix.getEntry(1, 2), 0.0);
    }

    // Test case for addToEntry(int row, int column, double increment) method
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 2, 4.0);
        matrix.addToEntry(1, 2, 2.0);
        assertEquals(6.0, matrix.getEntry(1, 2), 0.0);
        matrix.addToEntry(1, 2, -3.0);
        assertEquals(3.0, matrix.getEntry(1, 2), 0.0);
    }

    // Test case for multiplyEntry(int row, int column, double factor) method
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 2, 4.0);
        matrix.multiplyEntry(1, 2, 2.0);
        assertEquals(8.0, matrix.getEntry(1, 2), 0.0);
        matrix.multiplyEntry(1, 2, 0.5);
        assertEquals(4.0, matrix.getEntry(1, 2), 0.0);
    }

    // Test case for computeKey(int row, int column) method
    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        int key = matrix.computeKey(1, 2);
        assertEquals(5, key);
    }
}