package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.MatrixUtils;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isEmpty());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        original.setEntry(0, 0, 1.0);
        original.setEntry(1, 1, 2.0);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);

        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);

        b.setEntry(0, 0, 3.0);
        b.setEntry(1, 1, 4.0);
        b.setEntry(2, 2, 5.0);

        OpenMapRealMatrix c = a.add(b);

        assertEquals(3.0, c.getEntry(0, 0), 0.001);
        assertEquals(4.0, c.getEntry(1, 1), 0.001);
        assertEquals(5.0, c.getEntry(1, 2), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);

        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);

        b.setEntry(0, 0, 3.0);
        b.setEntry(1, 1, 4.0);
        b.setEntry(2, 2, 5.0);

        OpenMapRealMatrix c = a.subtract(b);

        assertEquals(-2.0, c.getEntry(0, 0), 0.001);
        assertEquals(-2.0, c.getEntry(1, 1), 0.001);
        assertEquals(-4.0, c.getEntry(1, 2), 0.001);
        assertNull(c.getEntry(1, 2));
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        RealMatrix b = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {3.0, 4.0} });

        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);
        a.setEntry(2, 2, 3.0);

        RealMatrix c = a.multiply(b);

        assertTrue(c instanceof BlockRealMatrix);
        BlockRealMatrix blockC = (BlockRealMatrix) c;

        Array2DRowRealMatrix expected = new Array2DRowRealMatrix(new double[][] { {5.0, 8.0}, {12.0, 18.0} });
        assertEquals(expected, blockC);
    }

    @Test
    public void testOpenMapRealMatrixMultiplicationWithSparseMatrix() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);

        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);
        a.setEntry(2, 2, 3.0);

        b.setEntry(0, 0, 1.0);
        b.setEntry(1, 1, 2.0);
        b.setEntry(1, 2, 3.0);

        OpenMapRealMatrix c = a.multiply(b);

        assertEquals(7.0, c.getEntry(0, 0), 0.001);
        assertEquals(11.0, c.getEntry(1, 1), 0.001);
        assertEquals(12.0, c.getEntry(1, 2), 0.001);
        assertEquals(9.0, c.getEntry(2, 2), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixMultiplicationWithInvalidDimensions() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 2);

        a.setEntry(0, 0, 1.0);
        a.setEntry(1, 1, 2.0);
        a.setEntry(2, 2, 3.0);

        a.multiply(b);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(1, 1, 1.0);

        assertEquals(1.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(1, 1, 1.0);

        assertEquals(1.0, matrix.getEntry(1, 1), 0.001);

        matrix.setEntry(2, 2, 2.0);

        assertEquals(2.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(1, 1, 1.0);

        matrix.addToEntry(1, 1, 2.0);

        assertEquals(3.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(1, 1, 1.0);

        matrix.multiplyEntry(1, 1, 2.0);

        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }
}