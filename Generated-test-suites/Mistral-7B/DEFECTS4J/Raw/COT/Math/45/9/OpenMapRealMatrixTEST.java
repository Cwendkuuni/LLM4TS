package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for OpenMapRealMatrix.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor OpenMapRealMatrix().
     */
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
    }

    /**
     * Test method getColumnDimension().
     */
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test method add().
     */
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);;
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);;
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);;
        expected.setEntry(0, 0, 6.0);
        expected.setEntry(0, 1, 8.0);
        expected.setEntry(1, 0, 10.0);
        expected.setEntry(1, 1, 12.0);

        assertArrayEquals(expected.getEntryValuesArray(), result.getEntryValuesArray(), 0.001);
    }

    /**
     * Test method subtract().
     */
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);;
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);;
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);;
        expected.setEntry(0, 0, -4.0);
        expected.setEntry(0, 1, -4.0);
        expected.setEntry(1, 0, -4.0);
        expected.setEntry(1, 1, -4.0);

        assertArrayEquals(expected.getEntryValuesArray(), result.getEntryValuesArray(), 0.001);
    }

    /**
     * Test method multiply().
     */
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);;
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);;
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 8.0);
        matrix2.setEntry(1, 1, 9.0);
        matrix2.setEntry(1, 2, 10.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 3);;
        expected.setEntry(0, 0, 15.0);
        expected.setEntry(0, 1, 18.0);
        expected.setEntry(0, 2, 21.0);
        expected.setEntry(1, 0, 24.0);
        expected.setEntry(1, 1, 27.0);
        expected.setEntry(1, 2, 30.0);

        assertArrayEquals(expected.getEntryValuesArray(), result.getEntryValuesArray(), 0.001);
    }

    /**
     * Test method multiplyEntry().
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);;
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);

        double[] expected = new double[] {2.0, 2.0, 6.0, 8.0};
        assertArrayEquals(expected, matrix.getEntryValuesArray(), 0.001);
    }

    /**
     * Test method addToEntry().
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);;
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 2.0);

        double[] expected = new double[] {3.0, 2.0, 5.0, 6.0};
        assertArrayEquals(expected, matrix.getEntryValuesArray(), 0.001);
    }

    /**
     * Test method setEntry().
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);;
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.setEntry(0, 0, 5.0);

        double[] expected = new double[] {5.0, 2.0, 3.0, 4.0};
        assertArrayEquals(expected, matrix.getEntryValuesArray(), 0.001);
    }

    /**
     * Test method getEntry().
     */
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);;
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(1.0, value, 0.001);
    }

    /**
     * Test method createMatrix().
     */
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);

        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    /**
     * Test method copy().
     */
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);;
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        OpenMapRealMatrix copy = matrix.copy();

        assertArrayEquals(matrix.getEntryValuesArray(), copy.getEntryValuesArray(), 0.001);
    }

    /**
     * Test method getRowDimension().
     */
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        assertEquals(2, matrix.getRowDimension());
    }

    /**
     * Test method computeKey().
     */
    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);;
        assertEquals(0, matrix.computeKey(0, 0));
        assertEquals(1, matrix.computeKey(0, 1));
        assertEquals(2, matrix.computeKey(1, 0));
        assertEquals(3, matrix.computeKey(1, 1));
    }

    /**
     * Test edge cases and exception handling.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        final int dimension = Integer.MAX_VALUE + 1;
        new OpenMapRealMatrix(dimension, dimension);
    }

    /**
     * Test method multiply(RealMatrix).
     */
    @Test
    public void testMultiplyRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        RealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 8.0);
        matrix2.setEntry(1, 1, 9.0);
        matrix2.setEntry(1, 2, 10.0);

        OpenMapRealMatrix result = (OpenMapRealMatrix) matrix1.multiply(matrix2);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 3);
        expected.setEntry(0, 0, 15.0);
        expected.setEntry(0, 1, 18.0);
        expected.setEntry(0, 2, 21.0);
        expected.setEntry(1, 0, 24.0);
        expected.setEntry(1, 1, 27.0);
        expected.setEntry(1, 2, 30.0);

        assertArrayEquals(expected.getEntryValuesArray(), result.getEntryValuesArray(), 0.001);
    }
}