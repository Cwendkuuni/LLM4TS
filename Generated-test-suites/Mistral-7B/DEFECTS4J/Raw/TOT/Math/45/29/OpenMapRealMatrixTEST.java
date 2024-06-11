package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    /**
     * Test constructor with correct dimensions.
     */
    @Test
    public void testOpenMapRealMatrixConstructorOK() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test constructor with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixConstructorKO() {
        new OpenMapRealMatrix(-1, 3);
    }

    /**
     * Test adding two matrices with the same dimensions.
     */
    @Test
    public void testAddMatricesSameSize() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}})));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}})));
        OpenMapRealMatrix result = matrix1.add(matrix2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{6.0, 8.0}, {10.0, 12.0}});
        assertEquals(expected, result);
    }

    /**
     * Test adding two matrices with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddMatricesDifferentSize() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 3.0}}));
        matrix1.add(matrix2);
    }

    /**
     * Test subtracting a matrix from another matrix.
     */
    @Test
    public void testSubtractMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}})));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{-1.0, -2.0}, {-3.0, -4.0}})));
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 0.0}});
        assertEquals(expected, result);
    }

    /**
     * Test multiplying a matrix with another matrix.
     */
    @Test
    public void testMultiplyMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{11.0, 14.0}, {19.0, 22.0}});
        assertEquals(expected, result);
    }

    /**
     * Test setting an entry to a matrix.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test setting an entry to a non-existent position.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    /**
     * Test adding an entry to a matrix.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test adding an entry to a non-existent position.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    /**
     * Test multiplying an entry with a factor.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test multiplying an entry with a zero factor.
     */
    @Test
    public void testMultiplyEntryZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 0.0);
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test adding a too large entry.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddTooLargeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test adding a negative entry.
     */
    @Test
    public void testAddNegativeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, -1.0);
        assertEquals(-1.0, matrix.getEntry(0, 0), 0.001);
    }
}