package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorFactory;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test
    public void testCreateCopyOpenMapRealMatrix() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix copyMatrix = originalMatrix.copy();
        assertEquals(originalMatrix, copyMatrix);
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        assertEquals(2, matrix.getColumnDimension());
    }

    @Test
    public void testSize() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        assertEquals(0, matrix.size());
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        assertEquals(4, matrix.size());
    }

    @Test
    public void testAddOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix resultMatrix = matrix1.add(matrix2);
        OpenMapRealMatrix expectedMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        expectedMatrix.setEntry(0, 0, 2.0);
        expectedMatrix.setEntry(0, 1, 4.0);
        expectedMatrix.setEntry(1, 0, 6.0);
        expectedMatrix.setEntry(1, 1, 8.0);

        assertEquals(expectedMatrix, resultMatrix);
    }

    @Test
    public void testSubtractOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix resultMatrix = matrix1.subtract(matrix2);
        OpenMapRealMatrix expectedMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        // No need to set Entry as it is already zero by default

        assertEquals(expectedMatrix, resultMatrix);
    }

    @Test
    public void testMultiplyOpenMapRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        RealMatrix resultMatrix = matrix1.multiply(matrix2);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{6.0, 8.0}, {15.0, 20.0}});

        assertArrayEquals(expectedMatrix.getData(), resultMatrix.getData(), 0.0);
    }

    @Test
    public void testGetEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);

        assertNull(matrix.getEntry(2, 2));
    }

    @Test
    public void testSetEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.setEntry(2, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAddToEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 1.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);

        matrix.addToEntry(1, 1, 2.5);
        assertEquals(6.5, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntryOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);

        matrix.multiplyEntry(1, 1, 0.5);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testGetRowDimensionOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        assertEquals(2, matrix.getRowDimension());
    }

    @Test
    public void testSetEntryOutOfRangeRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(3, 0, 1.0));
    }

    @Test
    public void testSetEntryOutOfRangeColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(0, 3, 1.0));
    }

    @Test
    public void testAddOpenMapRealMatrixDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(1, 2));

        assertThrows(DimensionMismatchException.class, () -> matrix1.add(matrix2));
    }

    @Test
    public void testMultiplyOpenMapRealMatrixDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(1, 2));

        assertThrows(MatrixDimensionMismatchException.class, () -> matrix1.multiply(matrix2));
    }

    @Test
    public void testMultiplyOpenMapRealMatrixWithVector() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        RealVector vector = RealVectorFactory.getInstance().createVector(new double[] {1.0, 2.0});

        RealVector resultVector = matrix.multiply(vector);
        RealVector expectedVector = RealVectorFactory.getInstance().createVector(new double[] {5.0, 11.0});

        assertArrayEquals(expectedVector.toArray(), resultVector.toArray(), 0.0);
    }
}