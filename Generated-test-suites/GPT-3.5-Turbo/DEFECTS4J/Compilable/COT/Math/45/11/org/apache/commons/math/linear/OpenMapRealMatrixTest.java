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
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = m1.add(m2);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = m1.subtract(m2);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = m1.multiply(m2);

        assertEquals(0.0, result.getEntry(0, 0), 0.0);
        assertEquals(0.0, result.getEntry(1, 1), 0.0);
        assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(1, 1, 5.0);
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(7.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(1, 1, 5.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(10.0, matrix.getEntry(1, 1), 0.0);
    }

}