import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(2, 2, 6.0);
        
        OpenMapRealMatrix result = matrix1.add(matrix2);
        
        assertEquals(5.0, result.getEntry(0, 0), 0.0001);
        assertEquals(7.0, result.getEntry(1, 1), 0.0001);
        assertEquals(9.0, result.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(2, 2, 6.0);
        
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        
        assertEquals(-3.0, result.getEntry(0, 0), 0.0001);
        assertEquals(-3.0, result.getEntry(1, 1), 0.0001);
        assertEquals(-3.0, result.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 2, 2.0);
        matrix1.setEntry(1, 1, 3.0);
        
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(2, 2, 6.0);
        
        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        assertEquals(4.0, result.getEntry(0, 0), 0.0001);
        assertEquals(10.0, result.getEntry(0, 1), 0.0001);
        assertEquals(15.0, result.getEntry(1, 2), 0.0001);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0001);
        
        matrix.setEntry(1, 1, 4.0);
        
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0001);
        
        matrix.addToEntry(1, 1, 1.0);
        
        assertEquals(3.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0001);
        
        matrix.multiplyEntry(1, 1, 2.0);
        
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

}