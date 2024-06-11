package org.apache.commons.math.linear;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



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
        assertNotSame(matrix, copy);
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix createdMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, createdMatrix.getRowDimension());
        assertEquals(2, createdMatrix.getColumnDimension());
        assertEquals(0, createdMatrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(1, 1, 3.0);

        OpenMapRealMatrix resultMatrix = matrix.add(otherMatrix);
        assertEquals(3.0, resultMatrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(1, 1, 3.0);
        
        OpenMapRealMatrix resultMatrix = matrix.subtract(otherMatrix);
        assertEquals(-3.0, resultMatrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(1, 1, 2.0);
        
        RealMatrix resultMatrix = matrix.multiply(otherMatrix);
        assertEquals(0.0, resultMatrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(1, 2, 2.0);
        matrix.multiplyEntry(1, 2, 3.0);
        assertEquals(6.0, matrix.getEntry(1, 2), 0.0);
    }

}

