
package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix.add(other);
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix.subtract(other);
        assertEquals(-1.0, result.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix.multiply(other);
        assertEquals(0.0, result.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testGetRow() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        double[] row = matrix.getRow(0);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, row, 0.0001);
    }

    @Test
    public void testGetColumn() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 0, 2.0);
        matrix.setEntry(2, 0, 3.0);
        double[] column = matrix.getColumn(0);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, column, 0.0001);
    }

    @Test
    public void testSetRow() {
        double[] row = new double[]{1.0, 2.0, 3.0};
        matrix.setRow(0, row);
        assertArrayEquals(row, matrix.getRow(0), 0.0001);
    }

    @Test
    public void testSetColumn() {
        double[] column = new double[]{1.0, 2.0, 3.0};
        matrix.setColumn(0, column);
        assertArrayEquals(column, matrix.getColumn(0), 0.0001);
    }

    @Test
    public void testGetSubMatrix() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        RealMatrix subMatrix = matrix.getSubMatrix(0, 1, 0, 1);
        assertNotNull(subMatrix);
        assertEquals(2, subMatrix.getRowDimension());
        assertEquals(2, subMatrix.getColumnDimension());
        assertEquals(1.0, subMatrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, subMatrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, subMatrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, subMatrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testTranspose() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        RealMatrix transposeMatrix = matrix.transpose();
        assertNotNull(transposeMatrix);
        assertEquals(3, transposeMatrix.getRowDimension());
        assertEquals(3, transposeMatrix.getColumnDimension());
        assertEquals(1.0, transposeMatrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, transposeMatrix.getEntry(1, 0), 0.0001);
        assertEquals(3.0, transposeMatrix.getEntry(2, 0), 0.0001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testValidate() {
        matrix.validate(2, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCheckSubMatrixIndex() {
        matrix.checkSubMatrixIndex(4, 4, 2, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCheckRowIndex() {
        matrix.checkRowIndex(4);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCheckColumnIndex() {
        matrix.checkColumnIndex(4);
    }
}

