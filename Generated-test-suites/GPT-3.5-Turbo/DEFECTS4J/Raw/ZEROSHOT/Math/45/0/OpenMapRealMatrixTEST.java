import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);
        
        OpenMapRealMatrix result = matrix1.add(matrix2);
        
        assertEquals(6.0, result.getEntry(0, 0), 0.0);
        assertEquals(8.0, result.getEntry(0, 1), 0.0);
        assertEquals(10.0, result.getEntry(1, 0), 0.0);
        assertEquals(12.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);
        
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        
        assertEquals(-4.0, result.getEntry(0, 0), 0.0);
        assertEquals(-4.0, result.getEntry(0, 1), 0.0);
        assertEquals(-4.0, result.getEntry(1, 0), 0.0);
        assertEquals(-4.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);
        
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        
        assertEquals(19.0, result.getEntry(0, 0), 0.0);
        assertEquals(22.0, result.getEntry(0, 1), 0.0);
        assertEquals(43.0, result.getEntry(1, 0), 0.0);
        assertEquals(50.0, result.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
        
        matrix.setEntry(0, 0, 5.0);
        matrix.setEntry(0, 1, 6.0);
        matrix.setEntry(1, 0, 7.0);
        matrix.setEntry(1, 1, 8.0);
        
        assertEquals(5.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(6.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(7.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(0, 1, 2.0);
        matrix.addToEntry(1, 0, 3.0);
        matrix.addToEntry(1, 1, 4.0);
        
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(6.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 1, 2.0);
        matrix.multiplyEntry(1, 0, 2.0);
        matrix.multiplyEntry(1, 1, 2.0);
        
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(6.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
    }
    
    @Test
    public void testColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

}