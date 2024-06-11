
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.util.OpenIntToDoubleHashMap;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setup() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testCopy() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        OpenMapRealMatrix copy = matrix.copy();

        assertEquals(matrix.getEntry(0, 0), copy.getEntry(0, 0), 0.0);
        assertEquals(matrix.getEntry(1, 1), copy.getEntry(1, 1), 0.0);
        assertEquals(matrix.getEntry(2, 2), copy.getEntry(2, 2), 0.0);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);

        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);

        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);

        m2.setEntry(0, 0, 2.0);
        m2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = m1.add(m2);

        assertEquals(3.0, result.getEntry(0, 0), 0.0);
        assertEquals(5.0, result.getEntry(1, 1), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);

        m1.add(m2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);

        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);

        m2.setEntry(0, 0, 2.0);
        m2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = m1.subtract(m2);

        assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        assertEquals(-1.0, result.getEntry(1, 1), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);

        m1.subtract(m2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);

        m1.setEntry(0, 0, 1.0);
        m1.setEntry(1, 1, 2.0);

        m2.setEntry(0, 0, 2.0);
        m2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = m1.multiply(m2);

        assertEquals(2.0, result.getEntry(0, 0), 0.0);
        assertEquals(6.0, result.getEntry(1, 1), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);

        m1.multiply(m2);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 1, 1.0);
        matrix.addToEntry(1, 2, 2.0);
        matrix.addToEntry(2, 0, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 2), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix.multiplyEntry(0, 1, 2.0);
        matrix.multiplyEntry(1, 2, 3.0);
        matrix.multiplyEntry(2, 0, 4.0);

        assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        assertEquals(6.0, matrix.getEntry(1, 2), 0.0);
        assertEquals(0.0, matrix.getEntry(2, 0), 0.0);
    }
}

