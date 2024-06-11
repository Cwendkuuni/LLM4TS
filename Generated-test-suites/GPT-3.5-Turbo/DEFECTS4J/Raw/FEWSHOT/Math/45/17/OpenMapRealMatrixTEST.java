package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.util.OpenIntToDoubleHashMap;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setup() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copyMatrix = matrix.copy();
        assertEquals(3, copyMatrix.getRowDimension());
        assertEquals(3, copyMatrix.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        double value = matrix.getEntry(0, 0);
        assertEquals(0.0, value, 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntry_OutOfBounds() {
        matrix.getEntry(4, 4);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        double value = matrix.getEntry(0, 0);
        assertEquals(1.0, value, 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntry_OutOfBounds() {
        matrix.setEntry(4, 4, 1.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix.add(matrix2);
        double value = result.getEntry(0, 0);
        assertEquals(3.0, value, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAdd_DifferentDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix.subtract(matrix2);
        double value = result.getEntry(0, 0);
        assertEquals(1.0, value, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtract_DifferentDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        OpenMapRealMatrix result = matrix.multiply(matrix2);
        double value = result.getEntry(0, 0);
        assertEquals(6.0, value, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiply_DifferentDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix.multiply(matrix2);
    }

    @Test
    public void testGetRowDimension() {
        int dimension = matrix.getRowDimension();
        assertEquals(3, dimension);
    }

    @Test
    public void testGetColumnDimension() {
        int dimension = matrix.getColumnDimension();
        assertEquals(3, dimension);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 1.0);
        double value = matrix.getEntry(0, 0);
        assertEquals(1.0, value, 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntry_OutOfBounds() {
        matrix.addToEntry(4, 4, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        double value = matrix.getEntry(0, 0);
        assertEquals(6.0, value, 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntry_OutOfBounds() {
        matrix.multiplyEntry(4, 4, 1.0);
    }

    @Test
    public void testComputeKey() {
        int key = matrix.computeKey(1, 1);
        assertEquals(4, key);
    }
}