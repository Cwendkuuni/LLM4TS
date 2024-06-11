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
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
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
    public void testAdd() {
        OpenMapRealMatrix matrixToAdd = new OpenMapRealMatrix(3, 3);
        matrixToAdd.setEntry(0, 0, 1.0);
        matrixToAdd.setEntry(1, 1, 2.0);
        matrixToAdd.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.add(matrixToAdd);

        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrixToSubtract = new OpenMapRealMatrix(3, 3);
        matrixToSubtract.setEntry(0, 0, 1.0);
        matrixToSubtract.setEntry(1, 1, 2.0);
        matrixToSubtract.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.subtract(matrixToSubtract);

        assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        assertEquals(-2.0, result.getEntry(1, 1), 0.0);
        assertEquals(-3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrixToMultiply = new OpenMapRealMatrix(3, 3);
        matrixToMultiply.setEntry(0, 0, 1.0);
        matrixToMultiply.setEntry(1, 1, 2.0);
        matrixToMultiply.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix.multiply(matrixToMultiply);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();

        assertEquals(matrix.getEntry(0, 0), copy.getEntry(0, 0), 0.0);
        assertEquals(matrix.getEntry(1, 1), copy.getEntry(1, 1), 0.0);
        assertEquals(matrix.getEntry(2, 2), copy.getEntry(2, 2), 0.0);
    }
}

