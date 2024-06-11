package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix1.getRowDimension());
        assertEquals(3, matrix1.getColumnDimension());
        assertEquals(0, matrix1.getSize());
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertEquals(3, matrix2.getRowDimension());
        assertEquals(3, matrix2.getColumnDimension());
        assertEquals(0, matrix2.getSize());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        assertNotSame(original, copy);
        assertEquals(original, copy);
        original.setEntry(1, 1, 1.0);
        assertNull(copy.getEntry(1, 1));
    }

    @Test
    public void testOpenMapRealMatrixAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(1.0, result.getEntry(0, 0), 1e-10);
        assertEquals(3.0, result.getEntry(1, 1), 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixAddDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 1, -1.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(2.0, result.getEntry(1, 1), 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixSubtractDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{ {1.0, 2.0} });
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(6.0, result.getEntry(0, 0), 1e-10);
        assertEquals(3.0, result.getEntry(1, 1), 1e-10);
        assertEquals(0.0, result.getEntry(0, 1), 1e-10);
        assertEquals(0.0, result.getEntry(1, 0), 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplyDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{ {1.0, 2.0}, {3.0, 4.0} });
        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
        assertNull(matrix.getEntry(1, 1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.getEntry(-1, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.getEntry(0, -1);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(-1, 0, 1.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, -1, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.addToEntry(-1, 0, 1.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.addToEntry(0, -1, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.multiplyEntry(-1, 0, 2.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.multiplyEntry(0, -1, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixAddToLargeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < 10000; i++) {
            matrix.addToEntry(0, 0, 1.0);
        }
    }

    @Test
    public void testOpenMapRealMatrixEntryRemoval() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        assertNull(matrix.getEntry(0, 0));
        matrix.entries.remove(matrix.computeKey(0, 0));
    }

    @Test
    public void testOpenMapRealMatrixMultiplyWithVector() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        OpenMapRealMatrix result = matrix.multiply(vector);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 1));
        expected.setEntry(0, 0, 1.0 * 1.0);
        expected.setEntry(1, 0, 1.0 * 2.0);
        expected.setEntry(2, 0, 1.0 * 3.0);
        assertArrayEquals(expected.toArray(), result.toArray(), 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplyWithInvalidVectorSize() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0});
        matrix.multiply(vector);
    }
}