package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NumberIsTooLargeException;

import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        assertEquals(3, mat.getRowDimension());
        assertEquals(3, mat.getColumnDimension());
        assertNull(mat.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        OpenMapRealMatrix copy = mat.copy();
        assertEquals(mat.getRowDimension(), copy.getRowDimension());
        assertEquals(mat.getColumnDimension(), copy.getColumnDimension());
        assertArrayEquals(new double[]{1.0, 2.0}, new double[]{copy.getEntry(0, 0), copy.getEntry(1, 0)});
    }

    @Test
    public void testOpenMapRealMatrixAddMatrix() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 3.0); put(1, 4.0);}}});
        OpenMapRealMatrix sum = mat1.add(mat2);
        assertEquals(mat1.getRowDimension(), sum.getRowDimension());
        assertEquals(mat1.getColumnDimension(), sum.getColumnDimension());
        assertArrayEquals(new double[]{4.0, 6.0}, new double[]{sum.getEntry(0, 0), sum.getEntry(1, 0)});
    }

    @Test
    public void testOpenMapRealMatrixSubtractMatrix() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 3.0); put(1, 4.0);}}});
        OpenMapRealMatrix diff = mat1.subtract(mat2);
        assertEquals(mat1.getRowDimension(), diff.getRowDimension());
        assertEquals(mat1.getColumnDimension(), diff.getColumnDimension());
        assertArrayEquals(new double[]{-2.0, -2.0}, new double[]{diff.getEntry(0, 0), diff.getEntry(1, 0)});
    }

    @Test
    public void testOpenMapRealMatrixMultiplyMatrix() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0); put(2, 3.0);}}});
        OpenMapRealMatrix product = mat1.multiply(mat2);
        assertEquals(mat1.getRowDimension(), product.getRowDimension());
        assertEquals(mat2.getColumnDimension(), product.getColumnDimension());
        assertArrayEquals(new double[]{3.0, 6.0}, new double[]{product.getEntry(0, 0), product.getEntry(1, 0)});
    }

    @Test
    public void testOpenMapRealMatrixMultiplyMatrixException() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        RealMatrix mat2 = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });
        assertThrows(ClassCastException.class, () -> mat1.multiply(mat2));
    }

    @Test
    public void testOpenMapRealMatrixAddEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.addToEntry(0, 0, 1.0);
        assertEquals(1.0, mat.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        assertEquals(1.0, mat.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        mat.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, mat.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrixAddEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> mat.addToEntry(3, 3, 1.0));
    }

    @Test
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> mat.setEntry(3, 3, 1.0));
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        assertThrows(IndexOutOfBoundsException.class, () -> mat.multiplyEntry(3, 3, 2.0));
    }

    @Test
    public void testOpenMapRealMatrixDimensionMismatchException() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 1.0); put(1, 2.0);}}});
        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap[]{new OpenIntToDoubleHashMap(){{put(0, 2.0); put(1, 2.0);}}});
        assertThrows(DimensionMismatchException.class, () -> mat1.multiply(mat2));
    }

    @Test
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        assertNull(mat.getEntry(3, 3));
    }

    @Test
    public void testOpenMapRealMatrixSetEntryWithZero() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 0.0);
        assertEquals(0.0, mat.getEntry(0, 0));
        assertFalse(mat.entries.containsKey(0));
    }
}