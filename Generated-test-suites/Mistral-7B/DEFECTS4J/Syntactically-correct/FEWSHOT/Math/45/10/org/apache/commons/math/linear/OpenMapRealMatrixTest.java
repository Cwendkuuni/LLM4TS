package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertTrue(matrix instanceof OpenMapRealMatrix);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixZeroDimensions() {
        new OpenMapRealMatrix(0, 3);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(matrix1);
        assertEquals(matrix1, matrix2);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(1, 1), 0.001);
        assertNull(result.getEntry(1, 0));
        assertNull(result.getEntry(1, 2));
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(-1.0, result.getEntry(1, 1), 0.001);
        assertNull(result.getEntry(1, 0));
        assertNull(result.getEntry(1, 2));
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 1.0);
        matrix1.setEntry(1, 2, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(2, 1, 4.0);
        RealMatrix result = matrix1.multiply(matrix2);
        assertArrayEquals(new double[]{
                {3.0, 12.0},
                {6.0, 16.0},
                {9.0, 20.0}
        }, result.getData(), 0.001);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
        assertNull(matrix.getEntry(1, 0));
        assertNull(matrix.getEntry(1, 2));
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.001);
        assertNull(matrix.getEntry(1, 0));
        assertNull(matrix.getEntry(1, 2));
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.001);
        assertNull(matrix.getEntry(1, 0));
        assertNull(matrix.getEntry(1, 2));
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(3.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixGetEntryNull() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertNull(matrix.getEntry(1, 1));
    }

    @Test
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertNull(matrix.getEntry(10, 1));
        assertNull(matrix.getEntry(1, 10));
    }

    @Test
    public void testOpenMapRealMatrixWidthAndHeight() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testOpenMapRealMatrixGetEntryValueZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(0.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixGetEntryValueNull() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrixGetEntryValueNotNull() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        OpenIntToDoubleHashMap entries = matrix.entries;
        assertTrue(entries.containsKey(computeKey(1, 1)));
        assertEquals(1.0, entries.get(computeKey(1, 1)), 0.001);
        assertFalse(entries.containsKey(computeKey(1, 2)));
    }

    @Test
    public void testOpenMapRealMatrixComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(0, matrix.computeKey(0, 0));
        assertEquals(1, matrix.computeKey(1, 0));
        assertEquals(2, matrix.computeKey(1, 1));
        assertEquals(3, matrix.computeKey(1, 2));
        assertEquals(4, matrix.computeKey(2, 0));
        assertEquals(5, matrix.computeKey(2, 1));
        assertEquals(6, matrix.computeKey(2, 2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixKeyOutOfBounds() {
        new OpenMapRealMatrix(3, 3).computeKey(10, 10);
    }

    private int computeKey(int row, int column) {
        return row * 3 + column;
    }

    @Test
    public void testOpenMapRealMatrixEquals() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        assertFalse(matrix1.equals(matrix2));

        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 1, 1.0);
        assertTrue(matrix1.equals(matrix2));

        matrix1.setEntry(1, 1, 2.0);
        assertFalse(matrix1.equals(matrix2));
    }

    @Test
    public void testOpenMapRealMatrixToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertEquals("openmaprealmatrix[rows=3,columns=3]\n" +
                "  [, ]\n" +
                "  [1,] \n" +
                "  [, ]\n", matrix.toString());
    }

    @Test
    public void testOpenMapRealMatrixBlockMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(1, 1, 1.0);
        matrix1.setEntry(1, 2, 2.0);
        matrix1.setEntry(2, 1, 3.0);
        matrix1.setEntry(2, 2, 4.0);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);
        RealMatrix result = matrix1.multiply(matrix2);
        BlockRealMatrix expected = new BlockRealMatrix(3, 2)
                .setEntry(1, 1, 15.0)
                .setEntry(1, 2, 18.0)
                .setEntry(2, 1, 21.0)
                .setEntry(2, 2, 24.0);
        assertArrayEquals(expected.getData(), result.getData(), 0.001);
    }
}