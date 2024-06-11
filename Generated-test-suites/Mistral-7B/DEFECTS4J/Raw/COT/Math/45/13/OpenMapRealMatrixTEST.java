package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

import java.util.Iterator;

import org.junit.Test;

/**
 * Test class for OpenMapRealMatrix.
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
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
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
     * Test add method.
     */
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix sum = matrix1.add(matrix2);

        assertEquals(3, sum.getRowDimension());
        assertEquals(3, sum.getColumnDimension());
        assertEquals(3.0, sum.getEntry(0, 0), 0.001);
        assertEquals(5.0, sum.getEntry(1, 1), 0.001);
    }

    /**
     * Test subtract method.
     */
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix difference = matrix1.subtract(matrix2);

        assertEquals(3, difference.getRowDimension());
        assertEquals(3, difference.getColumnDimension());
        assertEquals(-2.0, difference.getEntry(0, 0), 0.001);
        assertEquals(0.0, difference.getEntry(1, 1), 0.001);
    }

    /**
     * Test multiply method.
     */
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix2.setEntry(1, 0, 5.0);
        matrix2.setEntry(1, 1, 6.0);

        RealMatrix product = matrix1.multiply(matrix2);

        assertEquals(3, product.getRowDimension());
        assertEquals(3, product.getColumnDimension());
        assertEquals(15.0, product.getEntry(0, 0), 0.001);
        assertEquals(12.0, product.getEntry(1, 0), 0.001);
        assertEquals(11.0, product.getEntry(1, 1), 0.001);
    }

    /**
     * Test getEntry method.
     */
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test setEntry method.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test addToEntry method.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);

        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test multiplyEntry method.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    /**
     * Test exception handling when trying to set an entry with a value greater than Double.MAX_VALUE.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryWithTooLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));

        matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
    }

    /**
     * Test exception handling when trying to add an entry with a value greater than Double.MAX_VALUE.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddToEntryWithTooLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, Double.MAX_VALUE);

        matrix.addToEntry(0, 0, 1.0);
    }

    /**
     * Test exception handling when trying to multiply an entry with a value less than zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyEntryWithNegativeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);

        matrix.multiplyEntry(0, 0, -1.0);
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        Iterator<OpenIntToDoubleHashMap.Entry> iterator = matrix.entries.entrySet().iterator();
        iterator.next(); // skip the first entry with key 0

        OpenIntToDoubleHashMap.Entry entry = iterator.next();
        assertEquals(1, entry.getKey() / columns);
        assertEquals(1, entry.getKey() % columns);
        assertEquals(1.0, entry.getValue(), 0.001);
    }

}