package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.linear.AbstractRealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.SparseRealMatrix;
import org.apache.commons.math.util.MatrixUtils;

import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor_rowDimension_columnDimension() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(1, 1));
    }

    @Test
    public void testConstructor_copy() {
        final OpenMapRealMatrix original = new OpenMapRealMatrix(2, 2);
        original.setEntry(0, 0, 1.0);
        original.setEntry(1, 1, 2.0);

        final OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertEquals(original.getEntry(0, 0), copy.getEntry(0, 0));
        assertEquals(original.getEntry(1, 1), copy.getEntry(1, 1));
    }

    @Test
    public void testCopy() {
        final OpenMapRealMatrix original = new OpenMapRealMatrix(2, 2);
        original.setEntry(0, 0, 1.0);
        original.setEntry(1, 1, 2.0);

        final OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertEquals(original.getEntry(0, 0), copy.getEntry(0, 0));
        assertEquals(original.getEntry(1, 1), copy.getEntry(1, 1));
    }

    @Test
    public void testCreateMatrix() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(1, 1));
    }

    @Test
    public void testGetColumnDimension() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_null() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.add(null);
    }

    @Test
    public void testAdd() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        final OpenMapRealMatrix sum = matrix1.add(matrix2);
        assertEquals(matrix1.getRowDimension(), sum.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), sum.getColumnDimension());
        assertEquals(1.0, sum.getEntry(0, 0), 0.0);
        assertEquals(3.0, sum.getEntry(0, 1), 0.0);
        assertEquals(3.0, sum.getEntry(1, 1), 0.0);
        assertNull(sum.getEntry(1, 0));
    }

    @Test
    public void testSubtract_RealMatrix() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(1, 1, 1.0);

        final OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(matrix1.getRowDimension(), difference.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), difference.getColumnDimension());
        assertEquals(0.0, difference.getEntry(0, 0), 0.0);
        assertEquals(-1.0, difference.getEntry(0, 1), 0.0);
        assertEquals(1.0, difference.getEntry(1, 1), 0.0);
        assertNull(difference.getEntry(1, 0));
    }

    @Test
    public void testSubtract_OpenMapRealMatrix() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(1, 1, 1.0);

        final OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(matrix1.getRowDimension(), difference.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), difference.getColumnDimension());
        assertEquals(0.0, difference.getEntry(0, 0), 0.0);
        assertEquals(-1.0, difference.getEntry(0, 1), 0.0);
        assertEquals(1.0, difference.getEntry(1, 1), 0.0);
        assertNull(difference.getEntry(1, 0));
    }

    @Test
    public void testMultiply_RealMatrix() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 8.0);
        matrix2.setEntry(1, 1, 9.0);
        matrix2.setEntry(1, 2, 10.0);

        final RealMatrix product = matrix1.multiply(matrix2);
        assertEquals(matrix1.getRowDimension(), product.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), product.getColumnDimension());
        assertArrayEquals(
            new double[][]{
                {15.0, 18.0, 21.0},
                {21.0, 24.0, 27.0}
            }, product.getData());
    }

    @Test
    public void testMultiply_OpenMapRealMatrix() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 8.0);
        matrix2.setEntry(1, 1, 9.0);
        matrix2.setEntry(1, 2, 10.0);

        final OpenMapRealMatrix product = matrix1.multiply(matrix2);
        assertEquals(matrix1.getRowDimension(), product.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), product.getColumnDimension());
        assertArrayEquals(
            new double[][]{
                {15.0, 18.0, 21.0},
                {21.0, 24.0, 27.0}
            }, product.getData());
    }

    @Test
    public void testGetEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);

        assertNull(matrix.getEntry(0, 1));
        assertNull(matrix.getEntry(1, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntry_outOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 2);
    }

    @Test
    public void testSetEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);

        matrix.setEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);

        assertNull(matrix.getEntry(0, 1));
        assertNull(matrix.getEntry(1, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntry_outOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 1.0);
    }

    @Test
    public void testAddToEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);

        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0);

        matrix.addToEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);

        matrix.addToEntry(1, 1, 3.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);

        assertNull(matrix.getEntry(0, 1));
        assertNull(matrix.getEntry(1, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntry_outOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0 * 1.0, matrix.getEntry(0, 0), 0.0);

        matrix.multiplyEntry(1, 1, 3.0);
        assertEquals(3.0 * 2.0, matrix.getEntry(1, 1), 0.0);

        assertNull(matrix.getEntry(0, 1));
        assertNull(matrix.getEntry(1, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntry_outOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(2, 2, 1.0);
    }

    @Test
    public void testComputeKey() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        assertEquals(0, matrix.computeKey(0, 0));
        assertEquals(1, matrix.computeKey(0, 1));
        assertEquals(2, matrix.computeKey(1, 0));
        assertEquals(3, matrix.computeKey(1, 1));
    }

    @Test
    public void testEntriesIterator() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        for (OpenMapRealMatrix.Entry entry : matrix.entries()) {
            assertEquals(entry.getRow(), entry.getKey() / matrix.getColumnDimension());
            assertEquals(entry.getColumn(), entry.getKey() % matrix.getColumnDimension());
            assertEquals(entry.getValue(), matrix.getEntry(entry.getRow(), entry.getColumn()));
        }
    }

    @Test
    public void testEntriesIterator_empty() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        for (OpenMapRealMatrix.Entry entry : matrix.entries()) {
            assertNull(entry);
        }
    }

    @Test
    public void testMultiply_multiplyByZero() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertThrows(NumberIsTooLargeException.class, () -> {
            final OpenMapRealMatrix zero = new OpenMapRealMatrix(2, 2);
            zero.multiply(matrix);
        });
    }
}