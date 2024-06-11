package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.OpenMapRealMatrix.*;

/**
 * Tests for OpenMapRealMatrix class.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test construction with correct dimensions.
     */
    @Test
    public void testConstruction() {
        final int rows = 2;
        final int columns = 3;
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        assertEquals(rows, matrix.getRowDimension());
        assertEquals(columns, matrix.getColumnDimension());
    }

    /**
     * Test construction from another instance.
     */
    @Test
    public void testCopyConstructor() {
        final int rows = 2;
        final int columns = 3;
        final OpenMapRealMatrix original = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    /**
     * Test adding two matrices of the same size.
     */
    @Test
    public void testAddition() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix a = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix b = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix c = a.add(b);
        OpenMapRealMatrix d = new OpenMapRealMatrix(rows, columns);
        d.addToEntry(0, 0, 1.0);
        d.addToEntry(0, 1, 2.0);
        d.addToEntry(1, 0, 3.0);
        d.addToEntry(1, 1, 4.0);
        assertEquals(d, c);
    }

    /**
     * Test adding a matrix with different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDifferentSizes() {
        final int rowsA = 2;
        final int columnsA = 3;
        OpenMapRealMatrix a = new OpenMapRealMatrix(rowsA, columnsA);
        final int rowsB = 3;
        final int columnsB = 2;
        OpenMapRealMatrix b = new OpenMapRealMatrix(rowsB, columnsB);
        a.add(b);
    }

    /**
     * Test subtraction of two matrices of the same size.
     */
    @Test
    public void testSubtraction() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix a = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix b = new OpenMapRealMatrix(rows, columns);
        OpenMapRealMatrix c = a.subtract(b);
        OpenMapRealMatrix d = new OpenMapRealMatrix(rows, columns);
        d.addToEntry(0, 0, -1.0);
        d.addToEntry(0, 1, -2.0);
        d.addToEntry(1, 0, -3.0);
        d.addToEntry(1, 1, -4.0);
        assertEquals(d, c);
    }

    /**
     * Test subtraction of a matrix with different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionDifferentSizes() {
        final int rowsA = 2;
        final int columnsA = 3;
        OpenMapRealMatrix a = new OpenMapRealMatrix(rowsA, columnsA);
        final int rowsB = 3;
        final int columnsB = 2;
        OpenMapRealMatrix b = new OpenMapRealMatrix(rowsB, columnsB);
        a.subtract(b);
    }

    /**
     * Test multiplication of two matrices.
     */
    @Test
    public void testMultiplication() {
        final int rowsA = 3;
        final int columnsA = 2;
        final int rowsB = 2;
        final int columnsB = 3;
        OpenMapRealMatrix a = new OpenMapRealMatrix(rowsA, columnsA);
        OpenMapRealMatrix b = new OpenMapRealMatrix(rowsB, columnsB);
        OpenMapRealMatrix c = a.multiply(b);
        RealMatrix d = new Array2DRowRealMatrix(new double[][] {
            { 1.0, 2.0, 3.0 },
            { 4.0, 5.0, 6.0 },
            { 7.0, 8.0, 9.0 }
        });
        assertEquals(d, c);
    }

    /**
     * Test multiplication of matrix with different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationDifferentSizes() {
        final int rowsA = 3;
        final int columnsA = 2;
        final int rowsB = 2;
        final int columnsB = 3;
        OpenMapRealMatrix a = new OpenMapRealMatrix(rowsA, columnsA);
        OpenMapRealMatrix b = new OpenMapRealMatrix(rowsB, columnsB);
        a.multiply(b);
    }

    /**
     * Test setting an entry in the matrix.
     */
    @Test
    public void testSettingEntry() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), Precision.TOLERANCE);
    }

    /**
     * Test setting an entry with a large value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingEntryLargeValue() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.setEntry(0, 0, Double.MAX_VALUE);
    }

    /**
     * Test setting an entry with negative value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSettingEntryNegativeValue() {
        final int rows = 2;
        final int columns = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        matrix.setEntry(0, 0, -1.0);
    }
}