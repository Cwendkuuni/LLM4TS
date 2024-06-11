import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.getEntry(0, 0));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        assertNotSame(original, copy);
        assertEquals(original, copy);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(original.getEntry(i, j), copy.getEntry(i, j));
            }
        }
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m2.setEntry(1, 1, 3.0);
        RealMatrix sum = m1.add(m2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 5.0);
        assertEquals(expected, sum);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.add(m2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m2.setEntry(0, 0, 1.0);
        m2.setEntry(1, 1, -1.0);
        RealMatrix difference = m1.subtract(m2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 3.0);
        assertEquals(expected, difference);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.subtract(m2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(0, 2, 3.0);
        m2.setEntry(0, 0, 4.0);
        m2.setEntry(1, 1, 5.0);
        RealMatrix product = m1.multiply(m2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 4.0);
        expected.setEntry(0, 1, 15.0);
        expected.setEntry(0, 2, 18.0);
        expected.setEntry(1, 0, 13.0);
        expected.setEntry(1, 1, 25.0);
        expected.setEntry(1, 2, 27.0);
        assertEquals(expected, product);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 1);
        m1.multiply(m2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(0.0, matrix.getEntry(0, 0));
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryNegativeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, -5.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 5.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToEntryNegativeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, -5.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 5.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyEntryNegativeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, -5.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 5.0);
    }

    @Test(expected = org.apache.commons.math.exception.NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Long.MAX_VALUE);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        int key = matrix.computeKey(0, 0);
        assertEquals(0, key % columns);
        assertEquals(0, key / columns);
    }
}