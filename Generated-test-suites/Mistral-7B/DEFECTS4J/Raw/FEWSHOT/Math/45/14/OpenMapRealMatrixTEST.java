package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.apache.commons.math.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math.linear.MatrixUtils.swapRows;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixUtils;
import org.apache.commons.math.linear.SparseRealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreation() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 3);
        assertEquals(2, m.getRowDimension());
        assertEquals(3, m.getColumnDimension());
        assertEquals(6, m.size());
        assertNull(m.getEntry(0, 0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCreationWithTooLargeSize() {
        new OpenMapRealMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        RealMatrix c = createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        a.add(b);
        assertEquals(c, a);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAdditionWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        a.add(b);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2))).subtract(new OpenMapRealMatrix(2, 2));
        RealMatrix expected = createRealMatrix(new double[][]{{0, 0}, {0, 0}});
        assertEquals(expected, a);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractionWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        a.subtract(b);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        RealMatrix c = RealMatrixUtils.multiply(new OpenMapRealMatrix(2, 3).transpose(), b);
        assertEquals(c, a.multiply(b));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplicationWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 2);
        a.multiply(b);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        double[][] expected = {{1.0, 0.0}, {0.0, 2.0}};
        assertEquals(expected, m.getData());
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        assertEquals(1.0, m.getEntry(0, 0), 1e-14);
        assertEquals(2.0, m.getEntry(1, 1), 1e-14);
        assertNull(m.getEntry(0, 1));
        assertNull(m.getEntry(1, 0));
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(0, 0, 1.0);
        m.addToEntry(0, 0, 2.0);
        assertEquals(3.0, m.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(0, 0, 1.0);
        m.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, m.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testSwapRows() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        swapRows(m, 0, 1);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(m.copy());
        expected.setEntry(0, 0, m.getEntry(1, 0));
        expected.setEntry(0, 1, m.getEntry(1, 1));
        expected.setEntry(1, 0, m.getEntry(0, 0));
        expected.setEntry(1, 1, m.getEntry(0, 1));
        assertEquals(expected, m);
    }

    @Test
    public void testSize() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        int size = 0;
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                if (m.getEntry(i, j) != null) {
                    size++;
                }
            }
        }
        assertEquals(m.size(), size);
    }
}