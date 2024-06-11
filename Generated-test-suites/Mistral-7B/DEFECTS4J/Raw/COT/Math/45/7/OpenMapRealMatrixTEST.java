import static org.junit.Assert.*;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isEmpty());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)).multiply(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2))));
        OpenMapRealMatrix copy = originalMatrix.copy();

        assertNotEquals(originalMatrix, copy);
        assertEquals(originalMatrix.getRowDimension(), copy.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copy.getColumnDimension());
        assertArrayEquals(originalMatrix.getEntryValues(), copy.getEntryValues(), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).multiply(new OpenMapRealMatrix(2, 2, 1.0)));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2).multiply(new OpenMapRealMatrix(2, 2, 1.0));
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2).add(matrix1, matrix2);

        assertEquals(matrix1, result);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2).add(matrix1, matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).multiply(new OpenMapRealMatrix(2, 2, 1.0)));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2).multiply(new OpenMapRealMatrix(2, 2, 1.0));
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2).subtract(matrix1, matrix2);

        assertEquals(new OpenMapRealMatrix(2, 2), result);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2).subtract(matrix1, matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).multiply(new OpenMapRealMatrix(2, 2, 1.0)));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2).multiply(new OpenMapRealMatrix(2, 2, 1.0));
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2).multiply(matrix1, matrix2);

        RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{
                {14.0, 14.0},
                {14.0, 14.0}
        });

        assertArrayEquals(expectedResult.getData(), result.getEntryValues(), 0.001);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2).multiply(matrix1, matrix2);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0).setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.001);

        try {
            matrix.getEntry(3, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test(expected = org.apache.commons.math.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 2);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.001);

        matrix.setEntry(2, 2, 5.0);

        assertEquals(5.0, matrix.getEntry(2, 2), 0.001);

        try {
            matrix.setEntry(3, 3, 6.0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test(expected = org.apache.commons.math.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 5.0);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0).setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 2.0);
        matrix.addToEntry(0, 1, 3.0);
        matrix.addToEntry(1, 0, 4.0);
        matrix.addToEntry(1, 1, 5.0);

        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(5.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(7.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(9.0, matrix.getEntry(1, 1), 0.001);

        try {
            matrix.addToEntry(3, 3, 10.0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test(expected = org.apache.commons.math.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 5.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0).setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 1, 3.0);
        matrix.multiplyEntry(1, 0, 4.0);
        matrix.multiplyEntry(1, 1, 5.0);

        assertEquals(2.0 * 1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0 * 2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(4.0 * 3.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(5.0 * 4.0, matrix.getEntry(1, 1), 0.001);

        try {
            matrix.multiplyEntry(3, 3, 10.0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test(expected = org.apache.commons.math.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(2, 2, 5.0);
    }
}