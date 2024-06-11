import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMatrixNegativeRows() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMatrixNegativeColumns() {
        new OpenMapRealMatrix(2, -1);
    }

    @Test
    public void testCreateMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertNotSame(original, copy);
        for (int i = 0; i < original.getRowDimension(); i++) {
            for (int j = 0; j < original.getColumnDimension(); j++) {
                assertEquals(original.getEntry(i, j), copy.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(matrix2));
        for (int i = 0; i < sum.getRowDimension(); i++) {
            for (int j = 0; j < sum.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), sum.getEntry(i, j), 0.0);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).subtract(matrix2));
        for (int i = 0; i < difference.getRowDimension(); i++) {
            for (int j = 0; j < difference.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), difference.getEntry(i, j), 0.0);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).add(new OpenMapRealMatrix(3, 3))).subtract(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new BlockRealMatrix(2, 3).multiply(matrix2));
        for (int i = 0; i < product.getRowDimension(); i++) {
            for (int j = 0; j < product.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), product.getEntry(i, j), 0.0);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        try {
            matrix.getEntry(2, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        matrix.setEntry(2, 2, 3.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        matrix.setEntry(2, 2, 3.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.addToEntry(2, 2, 3.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        matrix.multiplyEntry(1, 1, 4.0);
        assertEquals(3.0 * 1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(4.0 * 2.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3))).subtract(new OpenMapRealMatrix(2, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.multiplyEntry(2, 2, 3.0);
    }

}