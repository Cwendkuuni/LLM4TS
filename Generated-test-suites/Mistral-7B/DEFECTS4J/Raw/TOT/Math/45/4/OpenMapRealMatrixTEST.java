package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    /**
     * Test the construction of a sparse matrix with the correct dimensions.
     */
    @Test
    public void testConstruction() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(4, matrix.getColumnDimension());
    }

    /**
     * Test the creation of a matrix by copying another one.
     */
    @Test
    public void testCopy() {
        final OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        original.setEntry(1, 1, 1.0);
        original.setEntry(2, 2, 2.0);
        final OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
        assertFalse(original == copy);
        assertNull(copy.getEntry(0, 0));
        copy.setEntry(0, 0, 3.0);
        assertNotEquals(original, copy);
    }

    /**
     * Test the creation of a matrix by adding another matrix.
     */
    @Test
    public void testAddition() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(3, 3);
        left.setEntry(1, 1, 1.0);
        left.setEntry(2, 2, 2.0);
        right.setEntry(1, 1, 1.0);
        right.setEntry(2, 2, 2.0);
        final OpenMapRealMatrix sum = left.add(right);
        assertEquals(left, sum);
        sum.setEntry(0, 0, 2.0);
        assertNotEquals(left, sum);
    }

    /**
     * Test the creation of a matrix by subtracting another matrix.
     */
    @Test
    public void testSubtraction() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(3, 3);
        left.setEntry(1, 1, 1.0);
        left.setEntry(2, 2, 2.0);
        right.setEntry(1, 1, 1.0);
        right.setEntry(2, 2, 2.0);
        final OpenMapRealMatrix difference = left.subtract(right);
        assertEquals(left, difference);
        difference.setEntry(0, 0, -1.0);
        assertNotEquals(left, difference);
    }

    /**
     * Test the creation of a matrix by multiplying another matrix.
     */
    @Test
    public void testMultiplication() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(3, 3);
        left.setEntry(1, 1, 1.0);
        left.setEntry(1, 2, 2.0);
        left.setEntry(2, 1, 3.0);
        left.setEntry(2, 2, 4.0);
        right.setEntry(1, 1, 1.0);
        right.setEntry(1, 2, 2.0);
        right.setEntry(2, 1, 3.0);
        right.setEntry(2, 2, 4.0);
        final OpenMapRealMatrix product = left.multiply(right);
        assertEquals(3, product.getRowDimension());
        assertEquals(3, product.getColumnDimension());
        assertEquals(16.0, product.getEntry(0, 0), 0.0);
        assertEquals(12.0, product.getEntry(0, 1), 0.0);
        assertEquals(13.0, product.getEntry(0, 2), 0.0);
        assertEquals(9.0, product.getEntry(1, 0), 0.0);
        assertEquals(11.0, product.getEntry(1, 1), 0.0);
        assertEquals(15.0, product.getEntry(1, 2), 0.0);
        assertEquals(12.0, product.getEntry(2, 0), 0.0);
        assertEquals(14.0, product.getEntry(2, 1), 0.0);
        assertEquals(16.0, product.getEntry(2, 2), 0.0);
    }

    /**
     * Test the setting of a value in a matrix.
     */
    @Test
    public void testSetEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 0.0);
    }

    /**
     * Test the setting of a null value in a matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryNull() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, null);
    }

    /**
     * Test the setting of a value larger than the maximum allowed in a matrix.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryTooLarge() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, Double.POSITIVE_INFINITY);
    }

    /**
     * Test the addition of a null matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullMatrix() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        assertThrows(IllegalArgumentException.class, () -> left.add(null));
    }

    /**
     * Test the addition of a matrix with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddDifferentDimensions() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(2, 3);
        left.setEntry(1, 1, 1.0);
        left.setEntry(2, 2, 2.0);
        assertThrows(DimensionMismatchException.class, () -> left.add(right));
    }

    /**
     * Test the subtraction of a null matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractNullMatrix() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        assertThrows(IllegalArgumentException.class, () -> left.subtract(null));
    }

    /**
     * Test the subtraction of a matrix with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDifferentDimensions() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(2, 3);
        left.setEntry(1, 1, 1.0);
        left.setEntry(2, 2, 2.0);
        assertThrows(DimensionMismatchException.class, () -> left.subtract(right));
    }

    /**
     * Test the multiplication of a matrix with a null matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyNullMatrix() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        assertThrows(IllegalArgumentException.class, () -> left.multiply(null));
    }

    /**
     * Test the multiplication of a matrix with a matrix of different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyDifferentDimensions() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        left.setEntry(1, 1, 1.0);
        left.setEntry(1, 2, 2.0);
        left.setEntry(2, 1, 3.0);
        left.setEntry(2, 2, 4.0);
        assertThrows(DimensionMismatchException.class, () -> left.multiply(right));
    }

    /**
     * Test the multiplication of a matrix with a matrix with fewer columns.
     */
    @Test
    public void testMultiplyFewerColumns() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        left.setEntry(1, 1, 1.0);
        left.setEntry(1, 2, 2.0);
        left.setEntry(2, 1, 3.0);
        left.setEntry(2, 2, 4.0);
        final OpenMapRealMatrix product = left.multiply(right);
        assertEquals(3, product.getRowDimension());
        assertEquals(2, product.getColumnDimension());
        assertEquals(7.0, product.getEntry(0, 0), 0.0);
        assertEquals(6.0, product.getEntry(0, 1), 0.0);
        assertEquals(10.0, product.getEntry(1, 0), 0.0);
        assertEquals(8.0, product.getEntry(1, 1), 0.0);
        assertEquals(12.0, product.getEntry(2, 0), 0.0);
        assertEquals(10.0, product.getEntry(2, 1), 0.0);
    }

    /**
     * Test the multiplication of a matrix with a square matrix.
     */
    @Test
    public void testMultiplySquareMatrix() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(3, 3);
        left.setEntry(1, 1, 1.0);
        left.setEntry(1, 2, 2.0);
        left.setEntry(2, 1, 3.0);
        left.setEntry(2, 2, 4.0);
        right.setEntry(1, 1, 1.0);
        right.setEntry(1, 2, 2.0);
        right.setEntry(2, 1, 3.0);
        right.setEntry(2, 2, 4.0);
        final OpenMapRealMatrix product = left.multiply(right);
        assertEquals(3, product.getRowDimension());
        assertEquals(3, product.getColumnDimension());
        assertEquals(31.0, product.getEntry(0, 0), 0.0);
        assertEquals(32.0, product.getEntry(0, 1), 0.0);
        assertEquals(33.0, product.getEntry(0, 2), 0.0);
        assertEquals(23.0, product.getEntry(1, 0), 0.0);
        assertEquals(26.0, product.getEntry(1, 1), 0.0);
        assertEquals(29.0, product.getEntry(1, 2), 0.0);
        assertEquals(23.0, product.getEntry(2, 0), 0.0);
        assertEquals(26.0, product.getEntry(2, 1), 0.0);
        assertEquals(29.0, product.getEntry(2, 2), 0.0);
    }

    /**
     * Test the creation of a dense matrix by multiplying a sparse matrix with a dense matrix.
     */
    @Test
    public void testMultiplyWithDenseMatrix() {
        final OpenMapRealMatrix left = new OpenMapRealMatrix(3, 3);
        final OpenMapRealMatrix right = new OpenMapRealMatrix(3, 3);
        left.setEntry(1, 1, 1.0);
        left.setEntry(1, 2, 2.0);
        left.setEntry(2, 1, 3.0);
        left.setEntry(2, 2, 4.0);
        final Array2DRowRealMatrix denseRight = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });
        final RealMatrix product = left.multiply(denseRight);
        assertEquals(3, product.getRowDimension());
        assertEquals(3, product.getColumnDimension());
        assertEquals(31.0, product.getEntry(0, 0), 0.0);
        assertEquals(37.0, product.getEntry(0, 1), 0.0);
        assertEquals(43.0, product.getEntry(0, 2), 0.0);
        assertEquals(23.0, product.getEntry(1, 0), 0.0);
        assertEquals(30.0, product.getEntry(1, 1), 0.0);
        assertEquals(37.0, product.getEntry(1, 2), 0.0);
        assertEquals(23.0, product.getEntry(2, 0), 0.0);
        assertEquals(30.0, product.getEntry(2, 1), 0.0);
        assertEquals(37.0, product.getEntry(2, 2), 0.0);
    }

}