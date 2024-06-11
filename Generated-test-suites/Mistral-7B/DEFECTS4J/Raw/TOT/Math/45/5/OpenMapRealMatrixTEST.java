package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

import org.apache.commons.math.linear.OpenMapRealMatrix;

/**
 * Tests for the OpenMapRealMatrix class.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test construction of a new OpenMapRealMatrix.
     */
    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
        assertTrue(matrix.entries.isEmpty());
    }

    /**
     * Test addition of two OpenMapRealMatrix.
     */
    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(1, 0, 5.0);
        matrix2.setEntry(1, 1, 6.0);
        matrix2.setEntry(1, 2, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(2, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(10.0, result.getEntry(0, 0), 1e-14);
        assertEquals(6.0, result.getEntry(0, 1), 1e-14);
        assertEquals(11.0, result.getEntry(0, 2), 1e-14);
        assertEquals(5.0, result.getEntry(1, 0), 1e-14);
        assertEquals(12.0, result.getEntry(1, 1), 1e-14);
        assertEquals(12.0, result.getEntry(1, 2), 1e-14);
    }

    /**
     * Test subtraction of two OpenMapRealMatrix.
     */
    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(1, 0, 5.0);
        matrix2.setEntry(1, 1, 6.0);
        matrix2.setEntry(1, 2, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(2, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(-4.0, result.getEntry(0, 0), 1e-14);
        assertEquals(-2.0, result.getEntry(0, 1), 1e-14);
        assertEquals(-4.0, result.getEntry(0, 2), 1e-14);
        assertEquals(0.0, result.getEntry(1, 0), 1e-14);
        assertEquals(4.0, result.getEntry(1, 1), 1e-14);
        assertEquals(1.0, result.getEntry(1, 2), 1e-14);
    }

    /**
     * Test multiplication of an OpenMapRealMatrix with a RealMatrix.
     */
    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{5.0, 6.0}, {1.0, 2.0}, {3.0, 4.0}});

        OpenMapRealMatrix result = (OpenMapRealMatrix) matrix1.multiply(matrix2);

        assertEquals(2, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(15.0, result.getEntry(0, 0), 1e-14);
        assertEquals(16.0, result.getEntry(0, 1), 1e-14);
        assertEquals(18.0, result.getEntry(0, 2), 1e-14);
        assertEquals(3.0, result.getEntry(1, 0), 1e-14);
        assertEquals(11.0, result.getEntry(1, 1), 1e-14);
        assertEquals(13.0, result.getEntry(1, 2), 1e-14);
    }

    /**
     * Test setting and getting entries of an OpenMapRealMatrix.
     */
    @Test
    public void testSettingAndGettingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);

        matrix.setEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);

        matrix.setEntry(2, 2, 3.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 1e-14);

        matrix.addToEntry(0, 0, 0.5);
        assertEquals(1.5, matrix.getEntry(0, 0), 1e-14);

        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test setting an entry that would exceed the maximum possible value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingEntryTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        long maxValue = Long.MAX_VALUE;
        matrix.put(1, 1, maxValue + 1);
    }

    /**
     * Test that the OpenMapRealMatrix implements the SparseRealMatrix interface.
     */
    @Test
    public void testSparseRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertTrue(matrix instanceof SparseRealMatrix);
    }

    /**
     * Test clone method of OpenMapRealMatrix.
     */
    @Test
    public void testClone() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 1, 4.0);

        OpenMapRealMatrix clone = matrix.copy();

        assertEquals(2, clone.getRowDimension());
        assertEquals(3, clone.getColumnDimension());
        assertEquals(1.0, clone.getEntry(0, 0), 1e-14);
        assertEquals(2.0, clone.getEntry(0, 1), 1e-14);
        assertEquals(4.0, clone.getEntry(1, 1), 1e-14);
    }
}