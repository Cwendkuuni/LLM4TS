package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

import org.junit.Test;

public class OpenMapRealMatrixTest {

    /**
     * Test the construction of a new OpenMapRealMatrix.
     */
    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertNotNull(matrix);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertFalse(matrix.isFullMatrix());
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test setting and getting entries in the OpenMapRealMatrix.
     */
    @Test
    public void testSettingAndGettingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
        assertEquals(3.0, matrix.getEntry(2, 2), 1e-14);
    }

    /**
     * Test adding matrices.
     */
    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(2.0, result.getEntry(0, 0), 1e-14);
        assertEquals(4.0, result.getEntry(1, 1), 1e-14);
        assertEquals(6.0, result.getEntry(2, 2), 1e-14);
    }

    /**
     * Test subtracting matrices.
     */
    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result;

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        result = matrix1.subtract(matrix2);

        assertEquals(0.0, result.getEntry(0, 0), 1e-14);
        assertEquals(0.0, result.getEntry(1, 1), 1e-14);
        assertEquals(0.0, result.getEntry(2, 2), 1e-14);
    }

    /**
     * Test multiplying a matrix by a scalar.
     */
    @Test
    public void testMultiplyByScalar() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix = matrix.multiply(2.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(4.0, matrix.getEntry(1, 1), 1e-14);
        assertEquals(6.0, matrix.getEntry(2, 2), 1e-14);
    }

    /**
     * Test multiplying matrices.
     */
    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result;

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 0, 2.0);
        matrix1.setEntry(2, 0, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 2, 3.0);

        result = matrix1.multiply(matrix2);

        assertEquals(1.0, result.getEntry(0, 0), 1e-14);
        assertEquals(3.0, result.getEntry(0, 1), 1e-14);
        assertEquals(6.0, result.getEntry(0, 2), 1e-14);
        assertEquals(2.0, result.getEntry(1, 0), 1e-14);
        assertEquals(6.0, result.getEntry(1, 1), 1e-14);
        assertEquals(11.0, result.getEntry(1, 2), 1e-14);
        assertEquals(3.0, result.getEntry(2, 0), 1e-14);
        assertEquals(9.0, result.getEntry(2, 1), 1e-14);
        assertEquals(15.0, result.getEntry(2, 2), 1e-14);
    }

    /**
     * Test creating a dense matrix from a sparse one.
     */
    @Test
    public void testDenseMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        RealMatrix denseMatrix = new Array2DRowRealMatrix(matrix.getData(), false);

        assertEquals(1.0, denseMatrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, denseMatrix.getEntry(1, 1), 1e-14);
        assertEquals(3.0, denseMatrix.getEntry(2, 2), 1e-14);
    }

    /**
     * Test setting an entry that is too large.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingEntryTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
    }

    /**
     * Test setting an entry that is not in the map.
     */
    @Test
    public void testSettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(4, 4, 1.0);
        assertEquals(0.0, matrix.getEntry(4, 4), 1e-14);
    }

    /**
     * Test getting an entry that is not in the map.
     */
    @Test
    public void testGettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(0.0, matrix.getEntry(4, 4), 1e-14);
    }
}