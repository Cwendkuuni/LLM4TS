import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NumberIsTooLargeException;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    private OpenMapRealMatrix matrix;

    @Before
    public void setup() {
        matrix = new OpenMapRealMatrix(3, 4);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertNotSame(matrix, copy);
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        int rowDimension = 5;
        int columnDimension = 6;
        OpenMapRealMatrix createdMatrix = matrix.createMatrix(rowDimension, columnDimension);
        assertNotSame(matrix, createdMatrix);
        assertEquals(rowDimension, createdMatrix.getRowDimension());
        assertEquals(columnDimension, createdMatrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix result = matrix.add(m);
        assertNotSame(matrix, result);
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(matrix.getColumnDimension(), result.getColumnDimension());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddWithMismatchedDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 5);
        matrix.add(m);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix result = matrix.subtract(m);
        assertNotSame(matrix, result);
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(matrix.getColumnDimension(), result.getColumnDimension());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractWithMismatchedDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 5);
        matrix.subtract(m);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(4, 5);
        RealMatrix result = matrix.multiply(m);
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(m.getColumnDimension(), result.getColumnDimension());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithMismatchedDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 5);
        matrix.multiply(m);
    }

    @Test
    public void testGetEntry() {
        double value = 1.0;
        int row = 2;
        int column = 3;
        matrix.setEntry(row, column, value);
        assertEquals(value, matrix.getEntry(row, column), 0.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidRow() {
        matrix.getEntry(-1, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidColumn() {
        matrix.getEntry(0, -1);
    }

    @Test
    public void testSetEntry() {
        double value = 1.0;
        int row = 2;
        int column = 3;
        matrix.setEntry(row, column, value);
        assertEquals(value, matrix.getEntry(row, column), 0.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidRow() {
        matrix.setEntry(-1, 0, 1.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidColumn() {
        matrix.setEntry(0, -1, 1.0);
    }

    @Test
    public void testAddToEntry() {
        double value = 1.0;
        int row = 2;
        int column = 3;
        matrix.addToEntry(row, column, value);
        assertEquals(value, matrix.getEntry(row, column), 0.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidRow() {
        matrix.addToEntry(-1, 0, 1.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidColumn() {
        matrix.addToEntry(0, -1, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        double value = 2.0;
        int row = 2;
        int column = 3;
        matrix.setEntry(row, column, 1.0);
        matrix.multiplyEntry(row, column, value);
        assertEquals(value, matrix.getEntry(row, column), 0.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidRow() {
        matrix.multiplyEntry(-1, 0, 2.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidColumn() {
        matrix.multiplyEntry(0, -1, 2.0);
    }
}