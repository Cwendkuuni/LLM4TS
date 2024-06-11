package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;

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
        assertEquals(0, matrix.size());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(m1);
        assertEquals(m1, m2);
    }

    /**
     * Test add method.
     */
    @Test
    public void testAdd() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m2.setEntry(0, 0, 2.0);
        m2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix result = m1.add(m2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(4, result.size());
        assertEquals(3.0, result.getEntry(0, 0), 0.001);
        assertEquals(3.0, result.getEntry(1, 1), 0.001);
        assertEquals(1.0, result.getEntry(1, 0), 0.001);
        assertEquals(2.0, result.getEntry(0, 1), 0.001);
    }

    /**
     * Test subtract method.
     */
    @Test
    public void testSubtract() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m2.setEntry(0, 0, 2.0);
        m2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix result = m1.subtract(m2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(-1.0, result.getEntry(0, 0), 0.001);
        assertEquals(-1.0, result.getEntry(1, 1), 0.001);
        assertEquals(1.0, result.getEntry(1, 0), 0.001);
        assertEquals(1.0, result.getEntry(0, 1), 0.001);
    }

    /**
     * Test multiply method with another matrix.
     */
    @Test
    public void testMultiply() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m2.setEntry(0, 0, 1.0);
        m2.setEntry(0, 1, 2.0);
        m2.setEntry(1, 0, 3.0);
        m2.setEntry(1, 1, 4.0);
        OpenMapRealMatrix result = m1.multiply(m2);
        assertEquals(3, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(6, result.size());
        assertEquals(5.0, result.getEntry(0, 0), 0.001);
        assertEquals(8.0, result.getEntry(0, 1), 0.001);
        assertEquals(9.0, result.getEntry(1, 0), 0.001);
        assertEquals(12.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(2, 0), 0.001);
        assertEquals(0.0, result.getEntry(2, 1), 0.001);
    }

    /**
     * Test getEntry method.
     */
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        assertEquals(1.0, m.getEntry(0, 0), 0.001);
        assertEquals(2.0, m.getEntry(1, 1), 0.001);
        try {
            m.getEntry(3, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    /**
     * Test setEntry method.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);
        assertEquals(1.0, m.getEntry(0, 0), 0.001);
        assertEquals(2.0, m.getEntry(1, 1), 0.001);
        assertEquals(3.0, m.getEntry(2, 2), 0.001);
    }

    /**
     * Test addToEntry method.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.addToEntry(0, 0, 2.0);
        m.addToEntry(1, 1, 3.0);
        assertEquals(3.0, m.getEntry(0, 0), 0.001);
        assertEquals(5.0, m.getEntry(1, 1), 0.001);
    }

    /**
     * Test multiplyEntry method.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.multiplyEntry(0, 0, 2.0);
        m.multiplyEntry(1, 1, 3.0);
        assertEquals(2.0, m.getEntry(0, 0), 0.001);
        assertEquals(6.0, m.getEntry(1, 1), 0.001);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        m.setEntry(2, 2, 3.0);
        assertEquals(3, m.getRowDimension());
        assertEquals(3, m.getColumnDimension());
        assertEquals(6, m.size());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m3 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);
        m1.setEntry(2, 2, 3.0);
        m2.setEntry(0, 0, 1.0);
        m2.setEntry(1, 1, 2.0);
        m2.setEntry(2, 2, 3.0);
        m3.setEntry(0, 0, 1.1);
        m3.setEntry(1, 1, 2.1);
        m3.setEntry(2, 2, 3.1);
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
    }

    /**
     * Test computeKey method.
     */
    @Test
    public void testComputeKey() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        assertEquals(0, m.computeKey(0, 0));
        assertEquals(3, m.computeKey(0, 1));
        assertEquals(6, m.computeKey(0, 2));
        assertEquals(1, m.computeKey(1, 0));
        assertEquals(4, m.computeKey(1, 1));
        assertEquals(7, m.computeKey(1, 2));
        assertEquals(2, m.computeKey(2, 0));
        assertEquals(5, m.computeKey(2, 1));
        assertEquals(8, m.computeKey(2, 2));
    }
}