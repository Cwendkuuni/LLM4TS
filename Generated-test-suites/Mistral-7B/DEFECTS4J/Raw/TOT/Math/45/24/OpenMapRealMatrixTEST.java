package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

import org.junit.Test;

/**
 * Tests for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test construction with correct dimensions.
     */
    @Test
    public void testConstruction() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        assertEquals(rows, matrix.getRowDimension());
        assertEquals(columns, matrix.getColumnDimension());
    }

    /**
     * Test creation by copying another matrix.
     */
    @Test
    public void testCopyConstructor() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix original = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    /**
     * Test adding matrices with the same dimensions.
     */
    @Test
    public void testAddition() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix result = left.add(right);
        assertEquals(rows, result.getRowDimension());
        assertEquals(columns, result.getColumnDimension());

        // Set some values
        left.setEntry(0, 0, 1.0);
        left.setEntry(0, 1, 2.0);
        left.setEntry(0, 2, 3.0);
        right.setEntry(0, 0, 4.0);
        right.setEntry(0, 1, 5.0);
        right.setEntry(0, 2, 6.0);
        right.setEntry(1, 0, 7.0);
        right.setEntry(1, 1, 8.0);
        right.setEntry(1, 2, 9.0);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                double expected = left.getEntry(i, j) + right.getEntry(i, j);
                assertEquals(expected, result.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    /**
     * Test adding matrices with different dimensions.
     */
    @Test(expected = AssertionError.class)
    public void testAdditionWithDifferentDimensions() {
        final int rows1 = 2;
        final int columns1 = 3;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows1, columns1);
        final int rows2 = 3;
        final int columns2 = 2;
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows2, columns2);
        OpenMapRealMatrix result = left.add(right);
    }

    /**
     * Test subtracting matrices with the same dimensions.
     */
    @Test
    public void testSubtraction() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix result = left.subtract(right);
        assertEquals(rows, result.getRowDimension());
        assertEquals(columns, result.getColumnDimension());

        // Set some values
        left.setEntry(0, 0, 1.0);
        left.setEntry(0, 1, 2.0);
        left.setEntry(0, 2, 3.0);
        left.setEntry(1, 0, 4.0);
        left.setEntry(1, 1, 5.0);
        left.setEntry(1, 2, 6.0);

        right.setEntry(0, 0, 1.0);
        right.setEntry(0, 1, 2.0);
        right.setEntry(0, 2, 3.0);
        right.setEntry(1, 0, 4.0);
        right.setEntry(1, 1, 5.0);
        right.setEntry(1, 2, 6.0);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                double expected = left.getEntry(i, j) - right.getEntry(i, j);
                assertEquals(expected, result.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    /**
     * Test subtracting matrices with different dimensions.
     */
    @Test(expected = AssertionError.class)
    public void testSubtractionWithDifferentDimensions() {
        final int rows1 = 2;
        final int columns1 = 3;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows1, columns1);
        final int rows2 = 3;
        final int columns2 = 2;
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows2, columns2);
        OpenMapRealMatrix result = left.subtract(right);
    }

    /**
     * Test multiplying matrices.
     */
    @Test
    public void testMultiplication() {
        final int rows = 2;
        final int columns = 3;
        final int outColumns = 2;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix right = new OpenMapRealMatrix(columns, outColumns);
        OpenMapRealMatrix result = left.multiply(right);

        assertEquals(rows, result.getRowDimension());
        assertEquals(outColumns, result.getColumnDimension());

        // Set some values
        left.setEntry(0, 0, 1.0);
        left.setEntry(0, 1, 2.0);
        left.setEntry(0, 2, 3.0);
        left.setEntry(1, 0, 4.0);
        left.setEntry(1, 1, 5.0);
        left.setEntry(1, 2, 6.0);

        right.setEntry(0, 0, 7.0);
        right.setEntry(0, 1, 8.0);
        right.setEntry(1, 0, 9.0);
        right.setEntry(1, 1, 10.0);

        double[][] expected = new double[][]{
            {15.0, 18.0},
            {32.0, 39.0},
        };

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < outColumns; ++j) {
                double expectedValue = 0.0;
                for (int k = 0; k < columns; ++k) {
                    expectedValue += left.getEntry(i, k) * right.getEntry(k, j);
                }
                assertEquals(expectedValue, result.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    /**
     * Test multiplying matrices with different dimensions.
     */
    @Test(expected = AssertionError.class)
    public void testMultiplicationWithDifferentDimensions() {
        final int rows = 2;
        final int columns = 3;
        final int outColumns = 2;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix right = new OpenMapRealMatrix(outColumns, columns);
        OpenMapRealMatrix result = left.multiply(right);
    }

    /**
     * Test getting entries.
     */
    @Test
    public void testGettingEntries() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);

        // Test valid index
        int row = 0;
        int column = 1;
        double expected = 0.0;
        matrix.setEntry(row, column, expected);
        double actual = matrix.getEntry(row, column);
        assertEquals(expected, actual, Precision.SAFE_EPSILON);

        // Test invalid row index
        row = rows + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(row, column));

        // Test invalid column index
        column = columns + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(row, column));
    }

    /**
     * Test setting entries.
     */
    @Test
    public void testSettingEntries() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);

        // Test valid index
        int row = 0;
        int column = 1;
        double value = 42.0;
        matrix.setEntry(row, column, value);
        double actual = matrix.getEntry(row, column);
        assertEquals(value, actual, Precision.SAFE_EPSILON);

        // Test invalid row index
        row = rows + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(row, column, value));

        // Test invalid column index
        column = columns + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(row, column, value));
    }

    /**
     * Test adding to entries.
     */
    @Test
    public void testAddingToEntries() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);

        // Test valid index
        int row = 0;
        int column = 1;
        double value = 42.0;
        matrix.addToEntry(row, column, value);
        double actual = matrix.getEntry(row, column);
        assertEquals(value, actual, Precision.SAFE_EPSILON);

        // Test invalid row index
        row = rows + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.addToEntry(row, column, value));

        // Test invalid column index
        column = columns + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.addToEntry(row, column, value));
    }

    /**
     * Test multiplying entries.
     */
    @Test
    public void testMultiplyingEntries() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);

        // Test valid index
        int row = 0;
        int column = 1;
        double value = 42.0;
        matrix.multiplyEntry(row, column, value);
        double actual = matrix.getEntry(row, column);
        assertEquals(value * PrecisionUtils.SAFE_EPSILON, actual, Precision.SAFE_EPSILON);

        // Test invalid row index
        row = rows + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.multiplyEntry(row, column, value));

        // Test invalid column index
        column = columns + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.multiplyEntry(row, column, value));
    }

    /**
     * Test exception when creating a matrix with too large dimensions.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testExceptionForLargeMatrix() {
        final int rows = 2;
        final int columns = Integer.MAX_VALUE;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
    }
}