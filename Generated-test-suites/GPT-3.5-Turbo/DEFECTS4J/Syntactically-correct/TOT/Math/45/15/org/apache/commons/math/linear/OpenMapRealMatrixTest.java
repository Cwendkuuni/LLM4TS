import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;
    
    @Before
    public void setup() {
        matrix = new OpenMapRealMatrix(3, 3);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        
        assertEquals(0.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(2, 2), 0.001);
    }
    
    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertNotSame(matrix, copy);
        assertEquals(3, copy.getRowDimension());
        assertEquals(3, copy.getColumnDimension());
        
        assertEquals(0.0, copy.getEntry(0, 0), 0.001);
        assertEquals(0.0, copy.getEntry(2, 2), 0.001);
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrixToAdd = new OpenMapRealMatrix(3, 3);
        matrixToAdd.setEntry(0, 0, 1.0);
        matrixToAdd.setEntry(1, 1, 2.0);
        
        OpenMapRealMatrix result = matrix.add(matrixToAdd);
        
        assertEquals(1.0, result.getEntry(0, 0), 0.001);
        assertEquals(2.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(2, 2), 0.001);
    }
    
    @Test
    public void testAdd_dimensionMismatchException() {
        OpenMapRealMatrix matrixToAdd = new OpenMapRealMatrix(2, 3);
        
        try {
            matrix.add(matrixToAdd);
            fail("Should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // pass
        }
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrixToSubtract = new OpenMapRealMatrix(3, 3);
        matrixToSubtract.setEntry(0, 0, 1.0);
        matrixToSubtract.setEntry(1, 1, 2.0);
        
        OpenMapRealMatrix result = matrix.subtract(matrixToSubtract);
        
        assertEquals(-1.0, result.getEntry(0, 0), 0.001);
        assertEquals(-2.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(2, 2), 0.001);
    }
    
    @Test
    public void testSubtract_dimensionMismatchException() {
        OpenMapRealMatrix matrixToSubtract = new OpenMapRealMatrix(2, 3);
        
        try {
            matrix.subtract(matrixToSubtract);
            fail("Should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // pass
        }
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrixToMultiply = new OpenMapRealMatrix(3, 2);
        matrixToMultiply.setEntry(0, 0, 1.0);
        matrixToMultiply.setEntry(1, 1, 2.0);
        matrixToMultiply.setEntry(2, 0, 3.0);
        
        OpenMapRealMatrix result = matrix.multiply(matrixToMultiply);
        
        assertEquals(0.0, result.getEntry(0, 0), 0.001);
        assertEquals(0.0, result.getEntry(2, 1), 0.001);
        assertEquals(0.0, result.getEntry(0, 1), 0.001);
        assertEquals(0.0, result.getEntry(2, 0), 0.001);
    }
    
    @Test
    public void testMultiply_dimensionMismatchException() {
        OpenMapRealMatrix matrixToMultiply = new OpenMapRealMatrix(2, 3);
        
        try {
            matrix.multiply(matrixToMultiply);
            fail("Should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // pass
        }
    }
    
    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 1, 2.0);
        
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }
    
    @Test
    public void testGetEntry_outOfBoundsException() {
        try {
            matrix.getEntry(3, 3);
            fail("Should throw OutOfBoundsException");
        } catch (OutOfBoundsException e) {
            // pass
        }
    }
    
    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }
    
    @Test
    public void testSetEntry_outOfBoundsException() {
        try {
            matrix.setEntry(3, 3, 1.0);
            fail("Should throw OutOfBoundsException");
        } catch (OutOfBoundsException e) {
            // pass
        }
    }
    
    @Test
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }
    
    @Test
    public void testAddToEntry_outOfBoundsException() {
        try {
            matrix.addToEntry(3, 3, 1.0);
            fail("Should throw OutOfBoundsException");
        } catch (OutOfBoundsException e) {
            // pass
        }
    }
    
    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        
        assertEquals(6.0, matrix.getEntry(0, 0), 0.001);
    }
    
    @Test
    public void testMultiplyEntry_outOfBoundsException() {
        try {
            matrix.multiplyEntry(3, 3, 1.0);
            fail("Should throw OutOfBoundsException");
        } catch (OutOfBoundsException e) {
            // pass
        }
    }
    
}