package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testInitialization() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.getSize());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testMultiplicationCompatibility() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        RealMatrix m3 = new BlockRealMatrix(2, 3);
        MatrixUtils.assertAdditionCompatible(m1, m2);
        MatrixUtils.assertAdditionCompatible(m1, m3);
        MatrixUtils.assertMultiplicationCompatible(m1, m2);
        MatrixUtils.assertMultiplicationCompatible(m2, m1);
        MatrixUtils.assertMultiplicationCompatible(m2, m3);
        MatrixUtils.assertMultiplicationCompatible(m3, m1);
        MatrixUtils.assertMultiplicationCompatible(m3, m2);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m3 = m1.add(m2);

        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(1, 0, 3.0);
        m1.setEntry(1, 1, 4.0);

        m2.setEntry(0, 0, 5.0);
        m2.setEntry(0, 1, 6.0);
        m2.setEntry(1, 0, 7.0);
        m2.setEntry(1, 1, 8.0);

        m3.setEntry(0, 0, 6.0);
        m3.setEntry(0, 1, 8.0);
        m3.setEntry(1, 0, 10.0);
        m3.setEntry(1, 1, 12.0);

        assertEquals(m3, m1.add(m2));
        assertEquals(m3, m2.add(m1));
        assertEquals(m3, m1.add(m1));
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m3 = m1.subtract(m2);

        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(1, 0, 3.0);
        m1.setEntry(1, 1, 4.0);

        m2.setEntry(0, 0, 0.5);
        m2.setEntry(0, 1, 1.5);
        m2.setEntry(1, 0, 2.5);
        m2.setEntry(1, 1, 3.5);

        m3.setEntry(0, 0, 0.5);
        m3.setEntry(0, 1, 0.5);
        m3.setEntry(1, 0, 0.5);
        m3.setEntry(1, 1, -1.5);

        assertEquals(m3, m1.subtract(m2));
        assertEquals(m3, m2.subtract(m1));
        assertThrows(ClassCastException.class, () -> m1.subtract(new Object() {}));
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        RealMatrix m3 = new BlockRealMatrix(2, 2);

        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(1, 0, 3.0);
        m1.setEntry(1, 1, 4.0);

        m2.setEntry(0, 0, 5.0);
        m2.setEntry(0, 1, 6.0);
        m2.setEntry(1, 0, 7.0);
        m2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix product = m1.multiply(m2);

        product.setEntry(0, 0, 15.0);
        product.setEntry(0, 1, 22.0);
        product.setEntry(1, 0, 21.0);
        product.setEntry(1, 1, 28.0);

        assertEquals(product, m1.multiply(m2));
        assertEquals(product, m2.multiply(m1));

        m3.setEntry(0, 0, 1.0);
        m3.setEntry(0, 1, 2.0);
        m3.setEntry(1, 0, 3.0);
        m3.setEntry(1, 1, 4.0);

        RealMatrix product2 = m1.multiply(m3);

        product2.setEntry(0, 0, 7.0);
        product2.setEntry(0, 1, 13.0);
        product2.setEntry(1, 0, 13.0);
        product2.setEntry(1, 1, 21.0);

        assertEquals(product2, m1.multiply(m3));
        assertEquals(product2, m3.multiply(m1));
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);

        assertEquals(1.0, m.getEntry(0, 0), 1e-14);
        assertEquals(2.0, m.getEntry(0, 1), 1e-14);
        assertEquals(3.0, m.getEntry(1, 0), 1e-14);
        assertEquals(4.0, m.getEntry(1, 1), 1e-14);

        assertThrows(IndexOutOfBoundsException.class, () -> m.getEntry(2, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getEntry(-1, -1));
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);

        m.setEntry(0, 0, 5.0);
        m.setEntry(1, 1, 6.0);

        assertEquals(5.0, m.getEntry(0, 0), 1e-14);
        assertEquals(6.0, m.getEntry(1, 1), 1e-14);

        assertThrows(IndexOutOfBoundsException.class, () -> m.setEntry(2, 2, 7.0));
        assertThrows(IndexOutOfBoundsException.class, () -> m.setEntry(-1, -1, 8.0));
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);

        m.addToEntry(0, 0, 2.0);
        m.addToEntry(0, 1, 3.0);
        m.addToEntry(1, 0, 4.0);
        m.addToEntry(1, 1, 5.0);

        assertEquals(3.0, m.getEntry(0, 0), 1e-14);
        assertEquals(5.0, m.getEntry(0, 1), 1e-14);
        assertEquals(7.0, m.getEntry(1, 0), 1e-14);
        assertEquals(9.0, m.getEntry(1, 1), 1e-14);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);

        m.multiplyEntry(0, 0, 2.0);
        m.multiplyEntry(0, 1, 3.0);
        m.multiplyEntry(1, 0, 4.0);
        m.multiplyEntry(1, 1, 5.0);

        assertEquals(2.0, m.getEntry(0, 0), 1e-14);
        assertEquals(6.0, m.getEntry(0, 1), 1e-14);
        assertEquals(12.0, m.getEntry(1, 0), 1e-14);
        assertEquals(20.0, m.getEntry(1, 1), 1e-14);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        assertEquals(0, m.computeKey(0, 0));
        assertEquals(1, m.computeKey(0, 1));
        assertEquals(2, m.computeKey(1, 0));
        assertEquals(3, m.computeKey(1, 1));

        assertEquals(4, m.computeKey(0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.computeKey(2, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.computeKey(-1, -1));
    }

    @Test
    public void testGetSize() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        assertEquals(4, m.getSize());

        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        m.setEntry(1, 0, 3.0);
        m.setEntry(1, 1, 4.0);

        assertEquals(4, m.getSize());
    }

    @Test
    public void testResize() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        assertEquals(4, m.getSize());

        m.resize(3, 3);

        assertEquals(9, m.getSize());
        assertNull(m.getEntry(2, 2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testResizeTooLarge() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);

        m.resize(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }
}