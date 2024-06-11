package org.apache.commons.math.linear;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(4, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix copy = matrix.copy();
        assertNotSame(matrix, copy);
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(2.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 1.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        assertEquals(2.0, result.getEntry(0, 0), 0.0);
        assertEquals(6.0, result.getEntry(1, 1), 0.0);
    }

    // Add more test cases for other methods as needed

}