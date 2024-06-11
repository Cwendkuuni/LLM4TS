import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(2, 3);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
        assertEquals(0.0, matrix.getEntry(0, 2), 0.0001);
        assertEquals(0.0, matrix.getEntry(1, 2), 0.0001);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryInvalidIndex() {
        matrix.getEntry(2, 0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryInvalidIndex() {
        matrix.setEntry(2, 0, 1.0);
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);
        
        OpenMapRealMatrix sum = matrix.add(m);
        assertEquals(1.0, sum.getEntry(0, 0), 0.0001);
        assertEquals(2.0, sum.getEntry(0, 1), 0.0001);
        assertEquals(3.0, sum.getEntry(1, 0), 0.0001);
        assertEquals(4.0, sum.getEntry(1, 1), 0.0001);
    }
    
    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testAddMismatchedDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 2);
        matrix.add(m);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);
        
        OpenMapRealMatrix diff = matrix.subtract(m);
        assertEquals(-1.0, diff.getEntry(0, 0), 0.0001);
        assertEquals(-2.0, diff.getEntry(0, 1), 0.0001);
        assertEquals(-3.0, diff.getEntry(1, 0), 0.0001);
        assertEquals(-4.0, diff.getEntry(1, 1), 0.0001);
    }
    
    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testSubtractMismatchedDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 2);
        matrix.subtract(m);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 2);
        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);
        m.setEntry(2, 0, 5.0);
        m.setEntry(2, 1, 6.0);
        
        OpenMapRealMatrix product = matrix.multiply(m);
        assertEquals(0.0, product.getEntry(0, 0), 0.0001);
        assertEquals(0.0, product.getEntry(0, 1), 0.0001);
        assertEquals(0.0, product.getEntry(1, 0), 0.0001);
        assertEquals(0.0, product.getEntry(1, 1), 0.0001);
    }
    
    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testMultiplyMismatchedDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 3);
        matrix.multiply(m);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertTrue(matrix != copy);
        assertTrue(matrix.equals(copy));
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(2, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 3);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(3, newMatrix.getColumnDimension());
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntryInvalidIndex() {
        matrix.addToEntry(2, 0, 1.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(4.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidIndex() {
        matrix.multiplyEntry(2, 0, 1.0);
    }

    @Test
    public void testComputeKey() {
        assertEquals(0, matrix.computeKey(0, 0));
        assertEquals(1, matrix.computeKey(0, 1));
        assertEquals(3, matrix.computeKey(1, 0));
        assertEquals(4, matrix.computeKey(1, 1));
    }

}