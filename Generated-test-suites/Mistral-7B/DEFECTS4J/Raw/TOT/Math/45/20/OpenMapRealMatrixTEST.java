package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.junit.Test;

/**
 * Test cases for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor with correct dimensions.
     */
    @Test
    public void testOpenMapRealMatrixConstructor() {
        final int rows = 3;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        assertEquals(rows, matrix.getRowDimension());
        assertEquals(columns, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test constructor with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixConstructorInvalidDimensions() {
        new OpenMapRealMatrix(-1, 2);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testOpenMapRealMatrixCopyConstructor() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix original = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(original.getEntry(i, j), copy.getEntry(i, j));
            }
        }
    }

    /**
     * Test copy method.
     */
    @Test
    public void testOpenMapRealMatrixCopy() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix original = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(original.getEntry(i, j), copy.getEntry(i, j));
            }
        }
    }

    /**
     * Test addition with correct dimensions.
     */
    @Test
    public void testOpenMapRealMatrixAdditionCorrectDimensions() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix result = left.add(right);
        assertEquals(left.getRowDimension(), result.getRowDimension());
        assertEquals(left.getColumnDimension(), result.getColumnDimension());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double expected = left.getEntry(i, j) + right.getEntry(i, j);
                assertEquals(expected, result.getEntry(i, j), 0.001);
            }
        }
    }

    /**
     * Test addition with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixAdditionIncorrectDimensions() {
        final int rows1 = 2;
        final int columns1 = 2;
        final int rows2 = 3;
        final int columns2 = 3;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows1, columns1);
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows2, columns2);
        left.add(right);
    }

    /**
     * Test subtraction with correct dimensions.
     */
    @Test
    public void testOpenMapRealMatrixSubtractionCorrectDimensions() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix result = left.subtract(right);
        assertEquals(left.getRowDimension(), result.getRowDimension());
        assertEquals(left.getColumnDimension(), result.getColumnDimension());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double expected = left.getEntry(i, j) - right.getEntry(i, j);
                assertEquals(expected, result.getEntry(i, j), 0.001);
            }
        }
    }

    /**
     * Test subtraction with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixSubtractionIncorrectDimensions() {
        final int rows1 = 2;
        final int columns1 = 2;
        final int rows2 = 3;
        final int columns2 = 3;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows1, columns1);
        OpenMapRealMatrix right = new OpenMapRealMatrix(rows2, columns2);
        left.subtract(right);
    }

    /**
     * Test multiplication with correct dimensions.
     */
    @Test
    public void testOpenMapRealMatrixMultiplicationCorrectDimensions() {
        final int rows = 2;
        final int columns1 = 3;
        final int columns2 = 2;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows, columns1);
        OpenMapRealMatrix right = new OpenMapRealMatrix(columns1, columns2);
        RealMatrix result = left.multiply(right);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(rows, columns2);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns2; j++) {
                double sum = 0.0;
                for (int k = 0; k < columns1; k++) {
                    sum += left.getEntry(i, k) * right.getEntry(k, j);
                }
                assertEquals(sum, expected.getEntry(i, j), 0.001);
            }
        }
    }

    /**
     * Test multiplication with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplicationIncorrectDimensions() {
        final int rows1 = 2;
        final int columns1 = 3;
        final int columns2 = 2;
        OpenMapRealMatrix left = new OpenMapRealMatrix(rows1, columns1);
        OpenMapRealMatrix right = new OpenMapRealMatrix(columns2, columns1);
        left.multiply(right);
    }

    /**
     * Test setting an entry.
     */
    @Test
    public void testOpenMapRealMatrixSetEntry() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test setting an entry with index out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.setEntry(rows, columns, 1.0);
    }

    /**
     * Test getting an entry with index out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.getEntry(rows, columns);
    }

    /**
     * Test adding an entry with index out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryOutOfBounds() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.addToEntry(rows, columns, 1.0);
    }

    /**
     * Test adding an entry with too large value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixAddToEntryTooLarge() {
        final int rows = 2;
        final int columns = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.addToEntry(0, 0, Double.MAX_VALUE + 1.0);
    }
}