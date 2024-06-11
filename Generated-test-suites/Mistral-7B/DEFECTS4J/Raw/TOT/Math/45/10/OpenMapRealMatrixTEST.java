package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    /**
     * Test the construction of an OpenMapRealMatrix with given dimensions.
     */
    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test the addition of two matrices with compatible dimensions.
     */
    @Test
    public void testAdditionCompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        OpenMapRealMatrix sum = a.add(b);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {2.0, 4.0, 6.0},
                {8.0, 10.0, 12.0},
                {14.0, 12.0, 15.0}
        }));

        assertArrayEquals(expected.getColumnVector(0).toArray(), sum.getColumnVector(0).toArray(), 0.0);
        assertArrayEquals(expected.getColumnVector(1).toArray(), sum.getColumnVector(1).toArray(), 0.0);
        assertArrayEquals(expected.getColumnVector(2).toArray(), sum.getColumnVector(2).toArray(), 0.0);
    }

    /**
     * Test the addition of two matrices with incompatible dimensions.
     */
    @Test(expected = org.apache.commons.math3.linear.DimensionMismatchException.class)
    public void testAdditionIncompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}
        })));

        a.add(b);
    }

    /**
     * Test the subtraction of two matrices with compatible dimensions.
     */
    @Test
    public void testSubtractionCompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.0, 2.0, 3.0},
                {4.0, 1.0, 4.0},
                {4.0, 8.0, 7.0}
        })));

        OpenMapRealMatrix diff = a.subtract(b);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 4.0, 2.0},
                {3.0, -3.0, 1.0}
        }));

        assertArrayEquals(expected.getColumnVector(0).toArray(), diff.getColumnVector(0).toArray(), 0.0);
        assertArrayEquals(expected.getColumnVector(1).toArray(), diff.getColumnVector(1).toArray(), 0.0);
        assertArrayEquals(expected.getColumnVector(2).toArray(), diff.getColumnVector(2).toArray(), 0.0);
    }

    /**
     * Test the subtraction of two matrices with incompatible dimensions.
     */
    @Test(expected = org.apache.commons.math3.linear.DimensionMismatchException.class)
    public void testSubtractionIncompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}
        })));

        a.subtract(b);
    }

    /**
     * Test the multiplication of two matrices with compatible dimensions.
     */
    @Test
    public void testMultiplicationCompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        })));

        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {5.0, 6.0},
                {7.0, 8.0}
        })));

        RealMatrix product = a.multiply(b);

        Array2DRowRealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {5.0*1.0+6.0*3.0, 5.0*2.0+6.0*4.0},
                {7.0*1.0+8.0*3.0, 7.0*2.0+8.0*4.0}
        });

        assertEquals(expected.getRowDimension(), product.getRowDimension());
        assertEquals(expected.getColumnDimension(), product.getColumnDimension());
        assertArrayEquals(expected.getData(), product.getData(), 0.0);
    }

    /**
     * Test the multiplication of two matrices with incompatible dimensions.
     */
    @Test(expected = org.apache.commons.math3.linear.DimensionMismatchException.class)
    public void testMultiplicationIncompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        })));

        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {5.0, 6.0},
                {7.0, 8.0},
                {9.0, 10.0}
        })));

        a.multiply(b);
    }

    /**
     * Test the setting of an entry in the matrix.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test the setting of an entry that already exists.
     */
    @Test
    public void testSetEntryExists() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 5.0);
        matrix.setEntry(0, 0, 10.0);
        assertEquals(10.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test the removing of an entry.
     */
    @Test
    public void testRemoveEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        matrix.setEntry(0, 0, 0.0);
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test the addToEntry method.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        matrix.addToEntry(0, 0, 2.0);
        assertEquals(1.0 + 2.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test the multiplyEntry method.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })));

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0 * 1.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test the creation of a key for a matrix entry.
     */
    @Test
    public void testComputeKey() {
        final int row = 0;
        final int column = 0;
        final int key = new OpenMapRealMatrix(row, column).computeKey(row, column);
        assertEquals(row * column + column, key);
    }

    /**
     * Test the creation of a key for a matrix entry out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testComputeKeyOutOfBoundsRow() {
        final int column = 0;
        new OpenMapRealMatrix(2, column).computeKey(-1, column);
    }

    /**
     * Test the creation of a key for a matrix entry out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testComputeKeyOutOfBoundsColumn() {
        final int row = 0;
        new OpenMapRealMatrix(row, 3).computeKey(row, 3);
    }

    /**
     * Test the creation of a key for a matrix entry with negative dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeKeyNegativeDimensions() {
        new OpenMapRealMatrix(-1, -1).computeKey(0, 0);
    }

    /**
     * Test the creation of a key for a matrix entry with zero dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeKeyZeroDimensions() {
        new OpenMapRealMatrix(0, 0).computeKey(0, 0);
    }
}