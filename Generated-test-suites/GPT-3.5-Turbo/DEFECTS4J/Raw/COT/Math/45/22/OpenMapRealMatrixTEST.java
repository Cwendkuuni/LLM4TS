import static org.junit.Assert.*;

import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;
    private double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(data);
    }

    @Test
    public void testGetEntry() {
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(9.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 10.0);
        assertEquals(10.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(data);
        OpenMapRealMatrix result = matrix.add(other);
        assertEquals(2.0, result.getEntry(0, 0), 0.0);
        assertEquals(10.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(data);
        OpenMapRealMatrix result = matrix.subtract(other);
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(data);
        OpenMapRealMatrix result = matrix.multiply(other);
        assertEquals(30.0, result.getEntry(0, 0), 0.0);
        assertEquals(84.0, result.getEntry(2, 2), 0.0);
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
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertArrayEquals(matrix.getData(), copy.getData());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(5, 5);
        assertEquals(5, newMatrix.getRowDimension());
        assertEquals(5, newMatrix.getColumnDimension());
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 10.0);
        assertEquals(11.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyWithMismatchSizeMatrix() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(5, 5);
        matrix.multiply(other);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testAddWithMismatchSizeMatrix() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(5, 5);
        matrix.add(other);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testSubtractWithMismatchSizeMatrix() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(5, 5);
        matrix.subtract(other);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidRowIndex() {
        matrix.setEntry(5, 0, 10.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidColumnIndex() {
        matrix.setEntry(0, 5, 10.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidRowIndex() {
        matrix.getEntry(5, 0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidColumnIndex() {
        matrix.getEntry(0, 5);
    }

}