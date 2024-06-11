package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixUtils;
import org.apache.commons.math.linear.SparseRealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(3, result.size());
        assertEquals(1.0, result.getEntry(0, 0), 0.001);
        assertEquals(5.0, result.getEntry(1, 1), 0.001);
        assertNull(result.getEntry(2, 2));
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(3, result.size());
        assertEquals(-1.0, result.getEntry(0, 0), 0.001);
        assertEquals(-1.0, result.getEntry(1, 1), 0.001);
        assertNull(result.getEntry(2, 2));
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 6.0);

        RealMatrix result = matrix1.multiply(matrix2);

        BlockRealMatrix expectedResult = new BlockRealMatrix(3, 3);
        expectedResult.setEntry(0, 0, 4.0);
        expectedResult.setEntry(0, 1, 10.0);
        expectedResult.setEntry(0, 2, 12.0);
        expectedResult.setEntry(1, 0, 8.0);
        expectedResult.setEntry(1, 1, 11.0);
        expectedResult.setEntry(1, 2, 13.0);
        expectedResult.setEntry(2, 0, 12.0);
        expectedResult.setEntry(2, 1, 13.0);
        expectedResult.setEntry(2, 2, 14.0);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 3.0);
        assertEquals(4.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testGetEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(3, 3));
    }

    @Test
    public void testSetEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(3, 3, 0.0));
    }

    @Test
    public void testAddToEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.addToEntry(3, 3, 0.0));
    }

    @Test
    public void testMultiplyEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.multiplyEntry(3, 3, 0.0));
    }

    @Test
    public void testComputeKey_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.computeKey(3, 3));
    }

    @Test
    public void testSize() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        assertEquals(3, matrix.size());
    }

    @Test
    public void testDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        RealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertThrows(org.apache.commons.math3.linear.DimensionMismatchException.class, () -> matrix1.multiply(matrix2));
    }

    @Test
    public void testNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        try {
            matrix.entries.put(Integer.MAX_VALUE, 1.0);
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }
}