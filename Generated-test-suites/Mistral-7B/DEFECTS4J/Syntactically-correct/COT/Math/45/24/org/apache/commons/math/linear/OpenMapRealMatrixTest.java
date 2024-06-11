import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertNotSame(original.entries, copy.entries);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 0.5);
        matrix2.setEntry(0, 1, -1.5);
        matrix2.setEntry(1, 0, -2.5);
        matrix2.setEntry(1, 1, 1.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(5.5, result.getEntry(0, 0), 0.001);
        assertEquals(0.5, result.getEntry(0, 1), 0.001);
        assertEquals(-1.5, result.getEntry(0, 1), 0.001);
        assertEquals(-1.5, result.getEntry(1, 0), 0.001);
        assertEquals(5.0, result.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAddDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 0.5);
        matrix2.setEntry(0, 1, -1.5);
        matrix2.setEntry(1, 0, -2.5);
        matrix2.setEntry(1, 1, 1.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(2.5, result.getEntry(0, 0), 0.001);
        assertEquals(-3.5, result.getEntry(0, 1), 0.001);
        assertEquals(-5.5, result.getEntry(1, 0), 0.001);
        assertEquals(3.0, result.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 0.5);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(0, 2, -1.0);
        matrix2.setEntry(1, 0, -2.0);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 2, 2.0);

        RealMatrix result = matrix1.multiply(matrix2);

        assertEquals(2, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(1.0, result.getEntry(0, 0), 0.001);
        assertEquals(1.5, result.getEntry(0, 1), 0.001);
        assertEquals(-3.0, result.getEntry(0, 2), 0.001);
        assertEquals(3.0, result.getEntry(1, 0), 0.001);
        assertEquals(5.0, result.getEntry(1, 1), 0.001);
        assertEquals(6.0, result.getEntry(1, 2), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplyDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryIllegalArgumentException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToEntryIllegalArgumentException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyEntryNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, Integer.MAX_VALUE);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 2);
    }
}