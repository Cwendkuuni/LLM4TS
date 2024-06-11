import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

public class OpenMapRealMatrixTest {

    @org.junit.Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorZeroDimensions() {
        new OpenMapRealMatrix(0, 3);
    }

    @org.junit.Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertArrayEquals(original.entries.values().stream().mapToDouble(d -> d).toArray(), copy.entries.values().stream().mapToDouble(d -> d).toArray(), 0.001);
    }

    @org.junit.Test
    public void testAddition() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix result = left.add(right);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{6.0, 8.0}, {10.0, 12.0}}));
        assertArrayEquals(expected.entries.values().stream().mapToDouble(d -> d).toArray(), result.entries.values().stream().mapToDouble(d -> d).toArray(), 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        left.add(right);
    }

    @org.junit.Test
    public void testSubtraction() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix result = left.subtract(right);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{-4.0, -4.0}, {-4.0, -4.0}}));
        assertArrayEquals(expected.entries.values().stream().mapToDouble(d -> d).toArray(), result.entries.values().stream().mapToDouble(d -> d).toArray(), 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        left.subtract(right);
    }

    @org.junit.Test
    public void testMultiplication() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        RealMatrix result = left.multiply(right);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{5.0 * 1.0 + 6.0 * 3.0, 5.0 * 2.0 + 6.0 * 4.0},
                {7.0 * 1.0 + 8.0 * 3.0, 7.0 * 2.0 + 8.0 * 4.0}});
        assertArrayEquals(expected.getData(), result.getData(), 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        left.multiply(right);
    }

    @org.junit.Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.001);
        assertNull(matrix.getEntry(2, 2));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        matrix.getEntry(3, 3);
    }

    @org.junit.Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        matrix.setEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 0.001);
        assertNull(matrix.getEntry(1, 1));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        matrix.setEntry(3, 3, 5.0);
    }

    @org.junit.Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
        assertNull(matrix.getEntry(1, 1));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        matrix.addToEntry(3, 3, 2.0);
    }

    @org.junit.Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
        assertNull(matrix.getEntry(1, 1));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testLargeKeys() {
        final int rows = 100_000;
        final int columns = 100_000;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int key = row * columns + col;
                if (key > Integer.MAX_VALUE - 10) {
                    matrix.setEntry(row, col, Double.POSITIVE_INFINITY);
                }
            }
        }
    }

}