package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealArray2D;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {

        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixCreationWithNegativeSize() {
        new OpenMapRealMatrix(-2, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());

        original.setEntry(0, 0, 1.0);
        original.setEntry(1, 1, 2.0);
        assertArrayEquals(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}
        }, new RealArray2D(original.getData()));

        assertArrayEquals(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}
        }, new RealArray2D(copy.getData()));
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 2, 1.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertArrayEquals(new double[][]{
                {3.0, 3.0},
                {3.0, 3.0}
        }, new RealArray2D(result.getData()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithIncompatibleSize() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 2, 1.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertArrayEquals(new double[][]{
                {1.0, -1.0},
                {1.0, 1.0}
        }, new RealArray2D(result.getData()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithIncompatibleSize() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{
                {14.0, 22.0},
                {36.0, 52.0}
        });

        assertArrayEquals(expectedResult.getData(), result.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithIncompatibleSize() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.multiply(matrix2);
    }

    @Test
    public void testSettingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 2.0);
        matrix.setEntry(0, 1, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);

        assertArrayEquals(new double[][]{
                {2.0, 3.0},
                {4.0, 5.0}
        }, new RealArray2D(matrix.getData()));
    }

    @Test
    public void testGettingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 2.0);
        matrix.setEntry(0, 1, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(3.0, matrix.getEntry(0, 1), 1e-14);
        assertEquals(4.0, matrix.getEntry(1, 0), 1e-14);
        assertEquals(5.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test
    public void testAddingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 2.0);
        matrix.setEntry(0, 1, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);

        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(0, 1, 2.0);
        matrix.addToEntry(1, 0, 3.0);
        matrix.addToEntry(1, 1, 4.0);

        assertArrayEquals(new double[][]{
                {3.0, 5.0},
                {7.0, 9.0}
        }, new RealArray2D(matrix.getData()));
    }

    @Test
    public void testMultiplyingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 2.0);
        matrix.setEntry(0, 1, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);

        matrix.multiplyEntry(0, 0, 1.5);
        matrix.multiplyEntry(0, 1, -0.5);
        matrix.multiplyEntry(1, 0, 0.5);
        matrix.multiplyEntry(1, 1, 2.0);

        assertArrayEquals(new double[][]{
                {3.0, -1.5},
                {2.0, 10.0}
        }, new RealArray2D(matrix.getData()));
    }
}