package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrixFactory;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.entries.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMatrixNegativeDimension() {
        new OpenMapRealMatrix(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMatrixZeroRows() {
        new OpenMapRealMatrix(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMatrixZeroColumns() {
        new OpenMapRealMatrix(1, 0);
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    @Test
    public void testAddMatrixWithSameSize() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix c = a.add(b);
        assertEquals(a, c);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddMatrixDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        a.add(b);
    }

    @Test
    public void testSubtractMatrixWithSameSize() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix c = a.subtract(b);
        assertEquals(a, c);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractMatrixDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        a.subtract(b);
    }

    @Test
    public void testMultiplyMatrixWithSameSize() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix c = a.multiply(b);
        assertEquals(a, c);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyMatrixDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 2);
        a.multiply(b);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryNegativeRow() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryNegativeColumn() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(0, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(-1, 0, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(-1, 0, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(-1, 0, 2.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 0, 2.0);
        a.setEntry(2, 0, 3.0);

        RealMatrix b = RealMatrixFactory.dense(3, 3).mapMultiply(i -> j -> i * j);

        OpenMapRealMatrix c = a.multiply(b);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(0, 1, 2.0 * 0 + 1.0 * 1 = 1);
        expected.setEntry(0, 2, 2.0 * 0 + 1.0 * 2 = 2);
        expected.setEntry(1, 0, 2.0 * 0 + 1.0 * 0 = 0);
        expected.setEntry(1, 1, 2.0 * 1 + 1.0 * 1 = 5);
        expected.setEntry(1, 2, 2.0 * 1 + 1.0 * 2 = 6);
        expected.setEntry(2, 0, 3.0 * 0 + 1.0 * 0 = 0);
        expected.setEntry(2, 1, 3.0 * 1 + 1.0 * 1 = 7);
        expected.setEntry(2, 2, 3.0 * 1 + 1.0 * 2 = 9);

        assertEquals(expected, c);
    }

    @Test
    public void testMultiplyPost() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        a.setEntry(0, 0, 1.0);
        a.setEntry(0, 1, 2.0);
        a.setEntry(0, 2, 3.0);

        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        b.setEntry(0, 0, 1.0);
        b.setEntry(1, 0, 2.0);
        b.setEntry(2, 0, 3.0);

        OpenMapRealMatrix c = a.multiply(b);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0 * 1.0 + 2.0 * 1.0 + 3.0 * 1.0 = 7);
        expected.setEntry(0, 1, 1.0 * 2.0 + 2.0 * 2.0 + 3.0 * 2.0 = 12);
        expected.setEntry(0, 2, 1.0 * 3.0 + 2.0 * 3.0 + 3.0 * 3.0 = 18);
        expected.setEntry(1, 0, 1.0 * 1.0 + 2.0 * 1.0 + 3.0 * 1.0 = 7);
        expected.setEntry(1, 1, 1.0 * 2.0 + 2.0 * 2.0 + 3.0 * 2.0 = 12);
        expected.setEntry(1, 2, 1.0 * 3.0 + 2.0 * 3.0 + 3.0 * 3.0 = 18);
        expected.setEntry(2, 0, 1.0 * 1.0 + 2.0 * 1.0 + 3.0 * 1.0 = 7);
        expected.setEntry(2, 1, 1.0 * 2.0 + 2.0 * 2.0 + 3.0 * 2.0 = 12);
        expected.setEntry(2, 2, 1.0 * 3.0 + 2.0 * 3.0 + 3.0 * 3.0 = 18);

        OpenMapRealMatrix result = new OpenMapRealMatrix(a.getRowDimension(), b.getColumnDimension());
        a.multiply(b, result);

        assertEquals(expected, result);
    }

    @Test
    public void testAddToEntryWithLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        try {
            matrix.addToEntry(0, 0, Double.MAX_VALUE);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }
}