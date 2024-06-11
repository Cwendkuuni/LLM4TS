import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryInvalidRow() {
        matrix.getEntry(3, 0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryInvalidColumn() {
        matrix.getEntry(0, 3);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryInvalidRow() {
        matrix.setEntry(3, 0, 1.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryInvalidColumn() {
        matrix.setEntry(0, 3, 1.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.add(m);

        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIncompatibleDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(4, 4);

        matrix.add(m);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.subtract(m);

        assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        assertEquals(-2.0, result.getEntry(1, 1), 0.0);
        assertEquals(-3.0, result.getEntry(2, 2), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractIncompatibleDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(4, 4);

        matrix.subtract(m);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.multiply(m);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyIncompatibleDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(4, 4);

        matrix.multiply(m);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, 2.0);
        matrix.addToEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntryInvalidRow() {
        matrix.addToEntry(3, 0, 1.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntryInvalidColumn() {
        matrix.addToEntry(0, 3, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(1, 1, 3.0);
        matrix.multiplyEntry(2, 2, 4.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(6.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(12.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidRow() {
        matrix.multiplyEntry(3, 0, 1.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidColumn() {
        matrix.multiplyEntry(0, 3, 1.0);
    }
}