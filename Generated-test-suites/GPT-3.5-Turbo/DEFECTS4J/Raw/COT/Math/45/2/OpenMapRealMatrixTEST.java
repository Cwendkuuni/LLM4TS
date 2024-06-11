import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(1, 0, 2.0);
        OpenMapRealMatrix result = matrix.add(otherMatrix);
        assertEquals(2.0, result.getEntry(1, 0), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(1, 0, 2.0);
        OpenMapRealMatrix result = matrix.subtract(otherMatrix);
        assertEquals(-2.0, result.getEntry(1, 0), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(1, 0, 2.0);
        OpenMapRealMatrix result = matrix.multiply(otherMatrix);
        assertEquals(0.0, result.getEntry(1, 0), 0.0);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertNotSame(matrix, copy);
        assertArrayEquals(matrix.getData(), copy.getData());
    }
}