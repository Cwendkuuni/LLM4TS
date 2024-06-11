package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(1, 1));
        assertEquals(0.0, matrix.getEntry(0, 1), 1e-14);
        assertEquals(0.0, matrix.getEntry(1, 0), 1e-14);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);

        // test modification of copy does not affect original
        copy.setEntry(0, 0, 5.0);
        assertNotEquals(original, copy);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{2.0, -1.0}, {-1.0, 2.0}});
        OpenMapRealMatrix result = a.add(b);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{3.0, 1.0}, {4.0, 6.0}});
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 5.0}});
        a.add(b);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{2.0, -1.0}, {-1.0, 2.0}});
        OpenMapRealMatrix result = a.subtract(b);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0}, {2.0, 2.0}});
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 5.0}});
        a.subtract(b);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{5.0}, {6.0}});
        OpenMapRealMatrix result = a.multiply(b);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {15.0, 18.0},
            {39.0, 48.0}
        });
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithIncompatibleSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealVector b = new ArrayRealVector(new double[]{5.0, 6.0});
        a.multiply(b);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 1), 1e-14);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-14);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-14);
        assertEquals(4.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.getEntry(3, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.setEntry(3, 1, 5.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(0, 0, 5.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testAddToEntryZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(0, 0, 0.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(3, 1, 5.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.multiplyEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testMultiplyEntryZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.multiplyEntry(0, 0, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testHighPrecisionMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 3.0}});
        OpenMapRealMatrix result = a.multiply(b);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {2.0, 0.0},
            {0.0, 3.0}
        });
        assertEquals(expected, result, 1e-14);
    }

    @Test
    public void testMultiplicationWithLargeMatrix() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[10000][10000]);
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{
            {1.0},
            {0.0}
        });
        OpenMapRealMatrix result = a.multiply(b);
        RealMatrix expected = new Array2DRowRealMatrix(new double[]{
            {0.0},
            {0.0}
        });
        assertEquals(expected, result);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplicationWithTooLargeMatrix() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[10000 * 10000][10000 * 10000]);
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{
            {1.0},
            {0.0}
        });
        a.multiply(b);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        int key1 = matrix.computeKey(0, 0);
        int key2 = matrix.computeKey(1, 1);
        int key3 = matrix.computeKey(1, 2);
        int key4 = matrix.computeKey(2, 1);
        int key5 = matrix.computeKey(2, 2);

        int[] expected = new int[]{0, 3, 3, 1, 4};
        assertArrayEquals(expected, new int[]{key1, key2, key3, key4, key5});
    }
}