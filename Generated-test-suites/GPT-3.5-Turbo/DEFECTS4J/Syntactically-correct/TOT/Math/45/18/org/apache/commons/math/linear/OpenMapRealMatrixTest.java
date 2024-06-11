import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.OpenMapRealMatrix;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor1() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }
    
    @Test
    public void testConstructor2() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(0, 0);
        assertEquals(0, matrix.getRowDimension());
        assertEquals(0, matrix.getColumnDimension());
    }
    
    @Test
    public void testConstructor3() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(matrix1.getRowDimension(), matrix2.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), matrix2.getColumnDimension());
    }
    
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix result = matrix.createMatrix(3, 3);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(1, 1, 1.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(1.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(1, 1, 1.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(-1.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(1, 1, 1.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(0.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 1.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
    }
    
}