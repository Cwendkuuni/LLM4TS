package org.apache.commons.math.linear.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrix_constructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrix_constructor_sizeLimitExceeded() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Test
    public void testOpenMapRealMatrix_copyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        original.setEntry(0, 0, 1.0);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original.getEntry(0, 0), copy.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrix_copy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        original.setEntry(0, 0, 1.0);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original.getEntry(0, 0), copy.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrix_add() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);
        b.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = a.add(b);
        assertEquals(a.getEntry(0, 0), result.getEntry(0, 0));
        assertEquals(a.getEntry(1, 1), result.getEntry(1, 1), 0.001);
        assertEquals(3.0, result.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrix_add_dimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        a.add(b);
    }

    @Test
    public void testOpenMapRealMatrix_subtract() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);
        b.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = a.subtract(b);
        assertEquals(a.getEntry(0, 0), result.getEntry(0, 0));
        assertEquals(-1.0, result.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrix_subtract_dimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        RealMatrix b = new BlockRealMatrix(2, 2).setIdentity();
        a.subtract(b);
    }

    @Test
    public void testOpenMapRealMatrix_multiply() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);
        b.setEntry(0, 0, 3.0);
        b.setEntry(0, 1, 4.0);
        b.setEntry(1, 0, 5.0);
        b.setEntry(1, 1, 6.0);

        RealMatrix result = a.multiply(b);
        BlockRealMatrix expected = new BlockRealMatrix(2, 2);
        expected.setEntry(0, 0, 15.0);
        expected.setEntry(0, 1, 18.0);
        expected.setEntry(1, 0, 21.0);
        expected.setEntry(1, 1, 24.0);
        assertEquals(expected, result);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrix_multiply_dimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        RealMatrix b = new BlockRealMatrix(3, 2).setIdentity();
        a.multiply(b);
    }

    @Test
    public void testOpenMapRealMatrix_getEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertNull(matrix.getEntry(0, 1));
        assertNull(matrix.getEntry(1, 0));
        assertNull(matrix.getEntry(1, 1));
    }

    @Test
    public void testOpenMapRealMatrix_setEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrix_addToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrix_multiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0 * matrix.getEntry(0, 0), matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrix_computeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        int key = matrix.computeKey(0, 0);
        int expectedKey = 0;
        assertEquals(expectedKey, key);

        key = matrix.computeKey(1, 1);
        expectedKey = 2;
        assertEquals(expectedKey, key);

        key = matrix.computeKey(1, 0);
        expectedKey = 1;
        assertEquals(expectedKey, key);
    }
}