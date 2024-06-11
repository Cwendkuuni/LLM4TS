package org.apache.commons.math.linear;
import static org.junit.Assert.*;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        OpenMapRealMatrix copyMatrix = matrix.copy();

        assertEquals(copyMatrix.getEntry(0, 0), 1.0, 0.0001);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        OpenMapRealMatrix newMatrix = matrix.createMatrix(3, 3);

        assertEquals(newMatrix.getRowDimension(), 3);
        assertEquals(newMatrix.getColumnDimension(), 3);
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);

        assertEquals(matrix.getColumnDimension(), 4);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix sumMatrix = matrix1.add(matrix2);

        assertEquals(sumMatrix.getEntry(0, 0), 3.0, 0.0001);
        assertEquals(sumMatrix.getEntry(1, 1), 5.0, 0.0001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix differenceMatrix = matrix1.subtract(matrix2);

        assertEquals(differenceMatrix.getEntry(0, 0), 1.0, 0.0001);
        assertEquals(differenceMatrix.getEntry(1, 1), 1.0, 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix productMatrix = matrix1.multiply(matrix2);

        assertEquals(productMatrix.getEntry(0, 0), 2.0, 0.0001);
        assertEquals(productMatrix.getEntry(1, 1), 6.0, 0.0001);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 0, 2.0);

        assertEquals(matrix.getEntry(1, 0), 2.0, 0.0001);
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(4, 3);

        assertEquals(matrix.getRowDimension(), 4);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);

        assertEquals(matrix.getEntry(0, 0), 2.0, 0.0001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);

        assertEquals(matrix.getEntry(0, 0), 3.0, 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);

        assertEquals(matrix.getEntry(0, 0), 6.0, 0.0001);
    }
}