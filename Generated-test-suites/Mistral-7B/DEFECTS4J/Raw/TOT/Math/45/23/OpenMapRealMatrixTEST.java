import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix result = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{6.0, 8.0}, {10.0, 12.0}});
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 3.0}}));
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{-4.0, -4.0}, {-4.0, -4.0}});
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 3.0}}));
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{11.0, 14.0}, {17.0, 20.0}});
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}}));
        matrix1.multiply(matrix2);
    }

    @Test
    public void testSettingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testGettingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-14);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-14);
        assertEquals(4.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.getEntry(2, 2);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(2, 2, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.multiplyEntry(2, 2, 2.0);
    }

    @Test
    public void testGetSize() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        assertEquals(8, matrix.size());
    }

    @Test
    public void testEquals() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.1}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix4 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.1}}));
        assertTrue(matrix1.equals(matrix1));
        assertTrue(matrix1.equals(matrix2));
        assertFalse(matrix1.equals(matrix3));
        assertFalse(matrix1.equals(matrix4));
        assertFalse(matrix1.equals(null));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix, copy);
    }

    @Test
    public void testNegativeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{-1.0, 2.0}, {3.0, -4.0}});
        assertEquals(-1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-14);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-14);
        assertEquals(-4.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test
    public void testZeroEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{0.0, 2.0}, {3.0, 0.0}});
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-14);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-14);
        assertEquals(0.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test
    public void testSmallEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{0.001, 2.0}, {3.0, 0.001}});
        assertEquals(0.001, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-14);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-14);
        assertEquals(0.001, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testLargeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testLargeNegativeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Double.NEGATIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSize() {
        new OpenMapRealMatrix(-3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeColumnDimension() {
        new OpenMapRealMatrix(3, -3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, null);
    }
}