package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtils;
import org.junit.Test;

import org.apache.commons.math.linear.OpenMapRealMatrix;

/**
 * Unit tests for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test construction with correct dimensions.
     */
    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(4, matrix.getColumnDimension());
    }

    /**
     * Test construction with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithInvalidDimensions() {
        new OpenMapRealMatrix(-1, 4);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    /**
     * Test addition.
     */
    @Test
    public void testAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sum = a.add(b);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                expected.setEntry(i, j, a.getEntry(i, j) + b.getEntry(i, j));
            }
        }
        assertEquals(expected, sum);
    }

    /**
     * Test addition with incorrect dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAdditionWithInvalidDimensions() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        a.add(b);
    }

    /**
     * Test subtraction.
     */
    @Test
    public void testSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix difference = a.subtract(b);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                expected.setEntry(i, j, a.getEntry(i, j) - b.getEntry(i, j));
            }
        }
        assertEquals(expected, difference);
    }

    /**
     * Test subtraction with incorrect dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractionWithInvalidDimensions() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        a.subtract(b);
    }

    /**
     * Test multiplication with a vector.
     */
    @Test
    public void testMultiplicationWithVector() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        RealVector vector = VectorUtils.createVector(new double[] {1.0, 2.0});
        RealVector result = matrix.multiply(vector);
        RealVector expected = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}})
                .operate(vector);
        assertEquals(expected, result);
    }

    /**
     * Test multiplication with a matrix.
     */
    @Test
    public void testMultiplicationWithMatrix() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix product = a.multiply(b);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 2; ++k) {
                    expected.setEntry(i, j, expected.getEntry(i, j) + a.getEntry(i, k) * b.getEntry(k, j));
                }
            }
        }
        assertEquals(expected, product);
    }

    /**
     * Test multiplication with an incorrect matrix size.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplicationWithIncorrectMatrixSize() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 4);
        a.multiply(b);
    }

    /**
     * Test setting an entry.
     */
    @Test
    public void testSettingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-15);
    }

    /**
     * Test setting an entry out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    /**
     * Test setting a large entry.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingLargeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test getting an entry.
     */
    @Test
    public void testGettingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-15);
    }

    /**
     * Test getting an entry out of bounds.
     */
    @Test
    public void testGettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertNull(matrix.getEntry(3, 3));
    }

    /**
     * Test getting an entry that has not been set.
     */
    @Test
    public void testGettingUnsetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test getting an entry that has been set to null.
     */
    @Test
    public void testGettingNullEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, null);
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test adding an entry.
     */
    @Test
    public void testAddingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-15);
    }

    /**
     * Test adding an entry out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    /**
     * Test multiplying an entry.
     */
    @Test
    public void testMultiplyingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0 * matrix.getEntry(0, 0), matrix.getEntry(0, 0), 1e-15);
    }

    /**
     * Test multiplying an entry out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }
}