import static org.junit.Assert.*;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }
    
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix copyMatrix = matrix.copy();
        assertEquals(3, copyMatrix.getRowDimension());
        assertEquals(2, copyMatrix.getColumnDimension());
    }
    
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 3);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(3, newMatrix.getColumnDimension());
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
    
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0001);
    }
    
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0001);
    }
    
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(7.0, matrix.getEntry(1, 1), 0.0001);
    }
    
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(10.0, matrix.getEntry(1, 1), 0.0001);
    }


}