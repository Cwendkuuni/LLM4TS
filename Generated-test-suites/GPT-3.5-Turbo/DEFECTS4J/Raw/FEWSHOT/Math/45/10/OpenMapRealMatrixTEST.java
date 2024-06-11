package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertNotNull(matrix);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertNotNull(copy);
        assertNotSame(matrix, copy);
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        RealMatrix createdMatrix = matrix.createMatrix(2, 2);
        assertNotNull(createdMatrix);
        assertTrue(createdMatrix instanceof OpenMapRealMatrix);
        assertEquals(2, createdMatrix.getRowDimension());
        assertEquals(2, createdMatrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sumMatrix = matrix.add(otherMatrix);
        assertNotNull(sumMatrix);
        assertEquals(0.0, sumMatrix.getEntry(0, 0), 0.0);
        assertEquals(0.0, sumMatrix.getEntry(1, 1), 0.0);
        assertEquals(0.0, sumMatrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix differenceMatrix = matrix.subtract(otherMatrix);
        assertNotNull(differenceMatrix);
        assertEquals(0.0, differenceMatrix.getEntry(0, 0), 0.0);
        assertEquals(0.0, differenceMatrix.getEntry(1, 1), 0.0);
        assertEquals(0.0, differenceMatrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiply() {
        RealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        RealMatrix productMatrix = matrix.multiply(otherMatrix);
        assertNotNull(productMatrix);
        assertTrue(productMatrix instanceof BlockRealMatrix);
        assertEquals(3, productMatrix.getRowDimension());
        assertEquals(3, productMatrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(0.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(0.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
    }
}