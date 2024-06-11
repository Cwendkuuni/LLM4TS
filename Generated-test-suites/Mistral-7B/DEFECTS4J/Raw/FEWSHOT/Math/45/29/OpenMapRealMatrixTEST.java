package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixCreationWithTooLargeDimension() {
        new OpenMapRealMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        assertEquals(originalMatrix, copiedMatrix);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 3.0);
            put(1, 4.0);
        }});
        OpenMapRealMatrix sumMatrix = firstMatrix.add(secondMatrix);
        assertEquals(3, sumMatrix.getRowDimension());
        assertEquals(2, sumMatrix.getColumnDimension());
        assertEquals(4.0, sumMatrix.getEntry(0, 0), 0.001);
        assertEquals(6.0, sumMatrix.getEntry(0, 1), 0.001);
        assertEquals(5.0, sumMatrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 2.0);
            put(1, 3.0);
        }});
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        OpenMapRealMatrix differenceMatrix = firstMatrix.subtract(secondMatrix);
        assertEquals(2, differenceMatrix.getRowDimension());
        assertEquals(2, differenceMatrix.getColumnDimension());
        assertEquals(1.0, differenceMatrix.getEntry(0, 0), 0.001);
        assertEquals(1.0, differenceMatrix.getEntry(0, 1), 0.001);
        assertEquals(1.0, differenceMatrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, new OpenIntToDoubleHashMap() {{
                put(0, 1.0);
                put(1, 2.0);
            }});
            put(1, new OpenIntToDoubleHashMap() {{
                put(0, 3.0);
                put(1, 4.0);
            }});
        }});
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 5.0);
            put(1, 6.0);
        }});
        OpenMapRealMatrix productMatrix = firstMatrix.multiply(secondMatrix);
        assertEquals(2, productMatrix.getRowDimension());
        assertEquals(2, productMatrix.getColumnDimension());
        assertEquals(31.0, productMatrix.getEntry(0, 0), 0.001);
        assertEquals(37.0, productMatrix.getEntry(0, 1), 0.001);
        assertEquals(33.0, productMatrix.getEntry(1, 0), 0.001);
        assertEquals(42.0, productMatrix.getEntry(1, 1), 0.001);

        RealMatrix referenceMatrix = new Array2DRowRealMatrix(new double[][]{{31.0, 37.0}, {33.0, 42.0}});
        assertEquals(referenceMatrix, productMatrix);
    }

    @Test
    public void testOpenMapRealMatrixSelfMultiplication() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, new OpenIntToDoubleHashMap() {{
                put(0, 1.0);
                put(1, 2.0);
            }});
            put(1, new OpenIntToDoubleHashMap() {{
                put(0, 3.0);
                put(1, 4.0);
            }});
        }});
        OpenMapRealMatrix result = matrix.multiply(matrix);
        OpenMapRealMatrix reference = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, new OpenIntToDoubleHashMap() {{
                put(0, 1.0 * 1.0 + 2.0 * 3.0, 1.0);
                put(1, 1.0 * 4.0 + 2.0 * 4.0, 1.0);
            }});
            put(1, new OpenIntToDoubleHashMap() {{
                put(0, 3.0 * 1.0 + 4.0 * 3.0, 1.0);
                put(1, 3.0 * 2.0 + 4.0 * 4.0, 1.0);
            }});
        }});

        assertEquals(reference, result);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        matrix.getEntry(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        matrix.setEntry(-1, 0, 4.0);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        matrix.setEntry(0, 0, 4.0);
        assertEquals(4.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }
}