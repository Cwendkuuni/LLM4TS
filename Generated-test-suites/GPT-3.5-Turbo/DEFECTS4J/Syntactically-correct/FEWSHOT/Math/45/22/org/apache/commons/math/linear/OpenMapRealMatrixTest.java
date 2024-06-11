import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(2, 2);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(0, 0, 1);
        m.setEntry(0, 1, 2);
        m.setEntry(1, 0, 3);
        m.setEntry(1, 1, 4);

        OpenMapRealMatrix result = matrix.add(m);

        assertEquals(1, result.getEntry(0, 0), 0);
        assertEquals(2, result.getEntry(0, 1), 0);
        assertEquals(3, result.getEntry(1, 0), 0);
        assertEquals(4, result.getEntry(1, 1), 0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(0, 0, 1);
        m.setEntry(0, 1, 2);
        m.setEntry(1, 0, 3);
        m.setEntry(1, 1, 4);

        OpenMapRealMatrix result = matrix.subtract(m);

        assertEquals(-1, result.getEntry(0, 0), 0);
        assertEquals(-2, result.getEntry(0, 1), 0);
        assertEquals(-3, result.getEntry(1, 0), 0);
        assertEquals(-4, result.getEntry(1, 1), 0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(0, 0, 1);
        m.setEntry(0, 1, 2);
        m.setEntry(1, 0, 3);
        m.setEntry(1, 1, 4);

        OpenMapRealMatrix result = matrix.multiply(m);

        assertEquals(0, result.getEntry(0, 0), 0);
        assertEquals(0, result.getEntry(0, 1), 0);
        assertEquals(0, result.getEntry(1, 0), 0);
        assertEquals(0, result.getEntry(1, 1), 0);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1);

        assertEquals(1, matrix.getEntry(0, 0), 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 3);

        matrix.add(m);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 3);

        matrix.subtract(m);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 2);

        matrix.multiply(m);
    }
}