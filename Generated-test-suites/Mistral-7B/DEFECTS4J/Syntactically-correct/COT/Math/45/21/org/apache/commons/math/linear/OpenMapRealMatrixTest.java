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
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;

import org.apache.commons.math.linear.OpenMapRealMatrix;

/**
 * Test for OpenMapRealMatrix class.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test add method.
     */
    @Test
    public void testAdd() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix c = a.add(b);

        a.setEntry(0, 0, 1.0);
        a.setEntry(0, 1, 2.0);
        a.setEntry(1, 0, 3.0);
        a.setEntry(1, 1, 4.0);

        b.setEntry(0, 0, 5.0);
        b.setEntry(0, 1, 6.0);
        b.setEntry(1, 0, 7.0);
        b.setEntry(1, 1, 8.0);

        c.setEntry(0, 0, 6.0);
        c.setEntry(0, 1, 8.0);
        c.setEntry(1, 0, 10.0);
        c.setEntry(1, 1, 12.0);

        RealMatrix d = new Array2DRowRealMatrix(new double[][] { { 6.0, 8.0 }, { 10.0, 12.0 } });
        assertEquals(d, c);
    }

    /**
     * Test add with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddDifferentDimensions() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        a.add(b);
    }

    /**
     * Test subtract method.
     */
    @Test
    public void testSubtract() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix c = a.subtract(b);

        a.setEntry(0, 0, 1.0);
        a.setEntry(0, 1, 2.0);
        a.setEntry(1, 0, 3.0);
        a.setEntry(1, 1, 4.0);

        b.setEntry(0, 0, 1.0);
        b.setEntry(0, 1, 2.0);
        b.setEntry(1, 0, 3.0);
        b.setEntry(1, 1, 4.0);

        c.setEntry(0, 0, 0.0);
        c.setEntry(0, 1, 0.0);
        c.setEntry(1, 0, 0.0);
        c.setEntry(1, 1, 0.0);

        RealMatrix d = new Array2DRowRealMatrix(new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } });
        assertEquals(d, c);
    }

    /**
     * Test subtract with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDifferentDimensions() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        a.subtract(b);
    }

    /**
     * Test multiply method.
     */
    @Test
    public void testMultiply() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        RealMatrix c = a.multiply(b);

        a.setEntry(0, 0, 1.0);
        a.setEntry(0, 1, 2.0);
        a.setEntry(1, 0, 3.0);
        a.setEntry(1, 1, 4.0);

        b.setEntry(0, 0, 5.0);
        b.setEntry(0, 1, 6.0);
        b.setEntry(1, 0, 7.0);
        b.setEntry(1, 1, 8.0);

        double[][] result = new double[][] { { 37.0, 52.0 }, { 52.0, 68.0 } };
        RealMatrix d = new Array2DRowRealMatrix(result);
        assertEquals(d, c);
    }

    /**
     * Test multiply with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyDifferentDimensions() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        a.multiply(b);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        a.setEntry(0, 0, 1.0);
        a.setEntry(0, 1, 2.0);
        a.setEntry(1, 0, 3.0);
        a.setEntry(1, 1, 4.0);

        OpenMapRealMatrix b = new OpenMapRealMatrix(a);

        assertEquals(a, b);
    }

    /**
     * Test getColumnDimension method.
     */
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test getEntry method.
     */
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0));
    }

    /**
     * Test getEntry with negative index.
     */
    @Test
    public void testGetEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertNull(matrix.getEntry(-1, 0));
    }

    /**
     * Test getEntry with out of bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.getEntry(2, 2);
    }

    /**
     * Test getRowDimension method.
     */
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
    }

    /**
     * Test setEntry method.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0));
    }

    /**
     * Test setEntry with negative index.
     */
    @Test
    public void testSetEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertFalse(matrix.setEntry(-1, 0, 1.0));
    }

    /**
     * Test setEntry with out of bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 1.0);
    }

    /**
     * Test addToEntry method.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0));
    }

    /**
     * Test addToEntry with negative index.
     */
    @Test
    public void testAddToEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertFalse(matrix.addToEntry(-1, 0, 1.0));
    }

    /**
     * Test addToEntry with out of bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 1.0);
    }

    /**
     * Test multiplyEntry method.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test multiplyEntry with negative index.
     */
    @Test
    public void testMultiplyEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertFalse(matrix.multiplyEntry(-1, 0, 2.0));
    }

    /**
     * Test multiplyEntry with out of bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(2, 2, 2.0);
    }

    /**
     * Test exception handling for NumberIsTooLargeException.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        OpenIntToDoubleHashMap map = new OpenIntToDoubleHashMap(2);
        map.put(Integer.MAX_VALUE, 1.0);
    }
}