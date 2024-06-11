import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    
    OpenMapRealMatrix matrix;
    
    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }
    
    // Test case for OpenMapRealMatrix(int rowDimension, int columnDimension)
    
    @Test
    public void testConstructor() {
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }
    
    // Test case for copy() method
    
    @Test
    public void testCopy() {
        OpenMapRealMatrix copiedMatrix = matrix.copy();
        assertNotSame(matrix, copiedMatrix);
        assertEquals(matrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copiedMatrix.getColumnDimension());
    }
    
    // Test case for createMatrix(int rowDimension, int columnDimension) method
    
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertNotNull(newMatrix);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }
    
    // Test case for getColumnDimension() method
    
    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }
    
    // Test case for add(OpenMapRealMatrix m) method
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m1.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m2.setEntry(0, 0, 1.0);
        m2.setEntry(1, 1, 2.0);
        m2.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix result = matrix.add(m1);
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        
        result = result.add(m2);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
    }
    
    // Test case for subtract(OpenMapRealMatrix m) method
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 3.0);
        m1.setEntry(1, 1, 2.0);
        m1.setEntry(2, 2, 1.0);
        
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m2.setEntry(0, 0, 1.0);
        m2.setEntry(1, 1, 1.0);
        m2.setEntry(2, 2, 1.0);
        
        OpenMapRealMatrix result = matrix.subtract(m1);
        assertEquals(-3.0, result.getEntry(0, 0), 0.0);
        
        result = result.subtract(m2);
        assertEquals(-4.0, result.getEntry(1, 1), 0.0);
    }
    
    // Test case for multiply(OpenMapRealMatrix m) method
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m1.setEntry(2, 0, 3.0);
        
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);
        m2.setEntry(0, 0, 1.0);
        m2.setEntry(1, 1, 2.0);
        m2.setEntry(0, 2, 3.0);
        
        OpenMapRealMatrix result = matrix.multiply(m1);
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        
        result = result.multiply(m2);
        assertEquals(6.0, result.getEntry(0, 0), 0.0);
    }
    
    // Test case for getEntry(int row, int column) method
    
    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }
    
    // Test case for getRowDimension() method
    
    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }
    
    // Test case for setEntry(int row, int column, double value) method
    
    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }
    
    // Test case for addToEntry(int row, int column, double increment) method
    
    @Test
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
    }
    
    // Test case for multiplyEntry(int row, int column, double factor) method
    
    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
    }
    
    // Test cases for error scenarios
    
    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testAddInvalidDimensions() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        matrix.add(m1);
        matrix.add(m2);
    }
    
    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testSubtractInvalidDimensions() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        matrix.subtract(m1);
        matrix.subtract(m2);
    }
    
    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testMultiplyInvalidDimensions() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        matrix.multiply(m1);
        matrix.multiply(m2);
    }

}