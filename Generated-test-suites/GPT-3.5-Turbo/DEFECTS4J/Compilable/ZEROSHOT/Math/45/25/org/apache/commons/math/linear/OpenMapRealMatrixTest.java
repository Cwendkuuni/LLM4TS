package org.apache.commons.math.linear;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testAddMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(3.0, result.getEntry(0, 0), 0.0001);
        assertEquals(5.0, result.getEntry(0, 1), 0.0001);
        assertEquals(7.0, result.getEntry(1, 0), 0.0001);
        assertEquals(9.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSubtractMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(-1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(-1.0, result.getEntry(0, 1), 0.0001);
        assertEquals(-1.0, result.getEntry(1, 0), 0.0001);
        assertEquals(-1.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiplyMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        assertEquals(10.0, result.getEntry(0, 0), 0.0001);
        assertEquals(13.0, result.getEntry(0, 1), 0.0001);
        assertEquals(22.0, result.getEntry(1, 0), 0.0001);
        assertEquals(29.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testAddToEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 2.0);
        matrix.addToEntry(0, 1, -1.0);
        matrix.addToEntry(1, 0, 1.0);
        matrix.addToEntry(1, 1, -2.0);

        assertEquals(3.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(1.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiplyEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 1, -1.0);
        matrix.multiplyEntry(1, 0, 0.5);
        matrix.multiplyEntry(1, 1, -0.5);

        assertEquals(2.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(-2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(1.5, matrix.getEntry(1, 0), 0.0001);
        assertEquals(-2.0, matrix.getEntry(1, 1), 0.0001);
    }
    
    // Add more test methods for the remaining methods
    
}