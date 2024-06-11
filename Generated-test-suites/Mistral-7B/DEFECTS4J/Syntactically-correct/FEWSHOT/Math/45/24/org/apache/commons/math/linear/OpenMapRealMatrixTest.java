package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCreateMatrixWithNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCreateMatrixWithNegativeColumns() {
        new OpenMapRealMatrix(2, -1);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 2}, {1, 2}, {2, 3}}));

        OpenMapRealMatrix result = a.add(b);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 2}, {1, 3}, {2, 5}}));
        assertEquals(expected, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {3, 3}}));
        a.add(b);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 2}, {1, 2}, {2, 3}}));

        OpenMapRealMatrix result = a.subtract(b);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, -2}, {1, -1}, {2, -1}}));
        assertEquals(expected, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 2}, {3, 3}}));
        a.subtract(b);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}, {2, 0}, {2, 1}, {2, 2}}));

        RealMatrix result = a.multiply(b);
        RealMatrix expected = new Array2DRowRealMatrix(
            new double[][]{
                {2.0, 3.0},
                {5.0, 6.0},
                {8.0, 9.0}
            });
        assertEquals(expected, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {0, 1}, {2, 0}, {2, 1}, {3, 1}}));
        a.multiply(b);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        assertEquals(0.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(0.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testSize() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(new int[][]{{0, 0}, {1, 1}, {2, 2}}));
        assertEquals(3, matrix.size());
    }

    @Test
    public void testCreateMatrixFromArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(data);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testCreateMatrixFromVectorMatrixProduct() {
        RealMatrix a = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(a.operate(b));
        assertEquals(2, matrix.getRowDimension());
        assertEquals(1, matrix.getColumnDimension());
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
    }
}