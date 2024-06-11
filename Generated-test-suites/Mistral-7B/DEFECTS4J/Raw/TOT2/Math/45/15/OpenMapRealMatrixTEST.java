import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        original.setEntry(0, 0, 1.0);
        assertNotSame(original, copy);
        assertEquals(original.getEntry(0, 0), copy.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        assertEquals(3, sum.getRowDimension());
        assertEquals(3, sum.getColumnDimension());
        assertEquals(4.0, sum.getEntry(0, 0), 1e-14);
        assertEquals(5.0, sum.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(3, difference.getRowDimension());
        assertEquals(3, difference.getColumnDimension());
        assertEquals(-2.0, difference.getEntry(0, 0), 1e-14);
        assertEquals(-1.0, difference.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 2.0);
        RealMatrix product = matrix1.multiply(matrix2);
        assertEquals(3, product.getRowDimension());
        assertEquals(3, product.getColumnDimension());
        assertEquals(11.0, product.getEntry(0, 0), 1e-14);
        assertEquals(6.0, product.getEntry(1, 1), 1e-14);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplicationWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCreateMatrixWithNegativeRowsOrColumns() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCreateMatrixWithNegativeColumns() {
        new OpenMapRealMatrix(3, -1);
    }
}