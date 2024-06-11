package org.apache.commons.math.linear;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertNotSame(matrix1, matrix2);
        assertArrayEquals(matrix1.getData(), matrix2.getData());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        RealMatrix createdMatrix = matrix.createMatrix(3, 3);
        assertEquals(OpenMapRealMatrix.class, createdMatrix.getClass());
        assertEquals(3, createdMatrix.getRowDimension());
        assertEquals(3, createdMatrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(4, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        RealMatrix expected = new OpenMapRealMatrix(3, 3);
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        RealMatrix expected = new OpenMapRealMatrix(3, 3);
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        RealMatrix expected = new OpenMapRealMatrix(3, 3);
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 5);
        assertEquals(5, matrix.getEntry(0, 0), 0);
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(4, 3);
        assertEquals(4, matrix.getRowDimension());
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 5);
        assertEquals(5, matrix.getEntry(0, 0), 0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 5);
        assertEquals(5, matrix.getEntry(0, 0), 0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 5);
        matrix.multiplyEntry(0, 0, 2);
        assertEquals(10, matrix.getEntry(0, 0), 0);
    }
}