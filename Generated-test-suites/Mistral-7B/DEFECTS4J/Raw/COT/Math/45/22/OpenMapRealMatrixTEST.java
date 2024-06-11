package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit test cases for the OpenMapRealMatrix class.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test the constructor with valid arguments.
     */
    @Test
    public void testOpenMapRealMatrixConstructors() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.getEntry(0, 0));
        assertTrue(matrix.isSparse());
    }

    /**
     * Test the copy constructor.
     */
    @Test
    public void testOpenMapRealMatrixCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);

        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());

        // Check that the entries are copied correctly
        Map<Integer, Double> originalEntries = original.entries.map();
        Map<Integer, Double> copyEntries = copy.entries.map();
        for (Map.Entry<Integer, Double> entry : originalEntries.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            Double copyValue = copyEntries.get(key);
            assertEquals(value, copyValue, 1e-10);
        }
    }

    /**
     * Test the getRowDimension method.
     */
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
    }

    /**
     * Test the getColumnDimension method.
     */
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test the isSparse method.
     */
    @Test
    public void testIsSparse() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertTrue(matrix.isSparse());
    }

    /**
     * Test the getEntry method with a valid index.
     */
    @Test
    public void testGetEntryValidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the getEntry method with an invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(4, 0);
    }

    /**
     * Test the getEntry method with an invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(0, 4);
    }

    /**
     * Test the setEntry method with a valid index and a non-zero value.
     */
    @Test
    public void testSetEntryValidIndexNonZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the setEntry method with a valid index and a zero value.
     */
    @Test
    public void testSetEntryValidIndexZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the setEntry method with an invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(4, 0, 1.0);
    }

    /**
     * Test the setEntry method with an invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 4, 1.0);
    }

    /**
     * Test the add method with a compatible matrix.
     */
    @Test
    public void testAddCompatibleMatrix() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix second = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = first.add(second);

        assertEquals(first.getRowDimension(), result.getRowDimension());
        assertEquals(first.getColumnDimension(), result.getColumnDimension());

        for (int row = 0; row < first.getRowDimension(); row++) {
            for (int col = 0; col < first.getColumnDimension(); col++) {
                double valueFirst = first.getEntry(row, col);
                double valueSecond = second.getEntry(row, col);
                double valueResult = result.getEntry(row, col);
                double expected = valueFirst + valueSecond;
                assertEquals(expected, valueResult, 1e-10);
            }
        }
    }

    /**
     * Test the add method with an incompatible matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddIncompatibleMatrix() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(3, 3);
        RealMatrix second = MatrixUtils.createRealIdentityMatrix(4);
        OpenMapRealMatrix result = first.add(second);
    }

    /**
     * Test the subtract method with a compatible matrix.
     */
    @Test
    public void testSubtractCompatibleMatrix() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix second = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = first.subtract(second);

        assertEquals(first.getRowDimension(), result.getRowDimension());
        assertEquals(first.getColumnDimension(), result.getColumnDimension());

        for (int row = 0; row < first.getRowDimension(); row++) {
            for (int col = 0; col < first.getColumnDimension(); col++) {
                double valueFirst = first.getEntry(row, col);
                double valueSecond = second.getEntry(row, col);
                double valueResult = result.getEntry(row, col);
                double expected = valueFirst - valueSecond;
                assertEquals(expected, valueResult, 1e-10);
            }
        }
    }

    /**
     * Test the subtract method with an incompatible matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractIncompatibleMatrix() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(3, 3);
        RealMatrix second = MatrixUtils.createRealIdentityMatrix(4);
        OpenMapRealMatrix result = first.subtract(second);
    }

    /**
     * Test the multiply method with a compatible matrix.
     */
    @Test
    public void testMultiplyCompatibleMatrix() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix second = new OpenMapRealMatrix(3, 3);
        RealMatrix result = first.multiply(second);

        assertEquals(first.getRowDimension(), result.getRowDimension());
        assertEquals(second.getColumnDimension(), result.getColumnDimension());

        for (int i = 0; i < first.getRowDimension(); i++) {
            for (int j = 0; j < second.getColumnDimension(); j++) {
                double valueFirst = first.getEntry(i, 0);
                double valueSecond = second.getEntry(0, j);
                double valueResult = result.getEntry(i, j);
                double expected = valueFirst * valueSecond;
                assertEquals(expected, valueResult, 1e-10);
            }
        }
    }

    /**
     * Test the multiply method with an incompatible matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyIncompatibleMatrix() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(3, 3);
        RealMatrix second = MatrixUtils.createRealIdentityMatrix(4);
        OpenMapRealMatrix result = (OpenMapRealMatrix) first.multiply(second);
    }

    /**
     * Test the multiplyEntry method with a valid index and a non-zero value.
     */
    @Test
    public void testMultiplyEntryValidIndexNonZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the multiplyEntry method with a valid index and a zero value.
     */
    @Test
    public void testMultiplyEntryValidIndexZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the multiplyEntry method with an invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(4, 1, 2.0);
    }

    /**
     * Test the multiplyEntry method with an invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 4, 2.0);
    }

    /**
     * Test the addToEntry method with a valid index and a non-zero value.
     */
    @Test
    public void testAddToEntryValidIndexNonZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the addToEntry method with a valid index and a zero value.
     */
    @Test
    public void testAddToEntryValidIndexZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the addToEntry method with an invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(4, 1, 2.0);
    }

    /**
     * Test the addToEntry method with an invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 4, 2.0);
    }

    /**
     * Test the computeKey method.
     */
    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int key = matrix.computeKey(i, j);
                int expected = i * 3 + j;
                assertEquals(expected, key);
            }
        }
    }

    /**
     * Test the OpenMapRealMatrix class with large dimensions.
     */
    @Test
    public void testOpenMapRealMatrixLargeDimensions() {
        try {
            OpenMapRealMatrix largeMatrix = new OpenMapRealMatrix(10000, 10000);
            // Test some methods to make sure the large matrix is created correctly
            largeMatrix.getRowDimension();
            largeMatrix.getColumnDimension();
            largeMatrix.getEntry(0, 0);
            largeMatrix.setEntry(0, 0, 1.0);
            largeMatrix.addToEntry(0, 0, 2.0);
            largeMatrix.multiplyEntry(0, 0, 3.0);
        } catch (OutOfMemoryError e) {
            fail("Test should not throw OutOfMemoryError");
        }
    }

    /**
     * Test the constructor with a negative number of rows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeRows() {
        new OpenMapRealMatrix(-1, 3);
    }

    /**
     * Test the constructor with a negative number of columns.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeColumns() {
        new OpenMapRealMatrix(3, -1);
    }

    /**
     * Test the constructor with zero number of rows or columns.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixZeroDimensions() {
        new OpenMapRealMatrix(0, 3);
        new OpenMapRealMatrix(3, 0);
    }
}