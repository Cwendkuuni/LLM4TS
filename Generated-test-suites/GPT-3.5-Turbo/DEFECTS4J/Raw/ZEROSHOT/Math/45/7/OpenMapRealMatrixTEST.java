import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix1;
    private OpenMapRealMatrix matrix2;

    @Before
    public void setUp() {
        matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);
        matrix1.setEntry(2, 0, 7.0);
        matrix1.setEntry(2, 1, 8.0);
        matrix1.setEntry(2, 2, 9.0);

        matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 9.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 4.0);
        matrix2.setEntry(2, 0, 3.0);
        matrix2.setEntry(2, 1, 2.0);
        matrix2.setEntry(2, 2, 1.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(10.0, result.getEntry(0, 0), 0.000001);
        assertEquals(10.0, result.getEntry(0, 1), 0.000001);
        assertEquals(10.0, result.getEntry(0, 2), 0.000001);
        assertEquals(10.0, result.getEntry(1, 0), 0.000001);
        assertEquals(10.0, result.getEntry(1, 1), 0.000001);
        assertEquals(10.0, result.getEntry(1, 2), 0.000001);
        assertEquals(10.0, result.getEntry(2, 0), 0.000001);
        assertEquals(10.0, result.getEntry(2, 1), 0.000001);
        assertEquals(10.0, result.getEntry(2, 2), 0.000001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(2, 2);
        matrix1.add(matrix3);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(-8.0, result.getEntry(0, 0), 0.000001);
        assertEquals(-6.0, result.getEntry(0, 1), 0.000001);
        assertEquals(-4.0, result.getEntry(0, 2), 0.000001);
        assertEquals(-2.0, result.getEntry(1, 0), 0.000001);
        assertEquals(0.0, result.getEntry(1, 1), 0.000001);
        assertEquals(2.0, result.getEntry(1, 2), 0.000001);
        assertEquals(4.0, result.getEntry(2, 0), 0.000001);
        assertEquals(6.0, result.getEntry(2, 1), 0.000001);
        assertEquals(8.0, result.getEntry(2, 2), 0.000001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(2, 2);
        matrix1.subtract(matrix3);
    }

    @Test
    public void testMultiply() {
        RealMatrix result = matrix1.multiply(matrix2);

        assertEquals(30.0, result.getEntry(0, 0), 0.000001);
        assertEquals(24.0, result.getEntry(0, 1), 0.000001);
        assertEquals(18.0, result.getEntry(0, 2), 0.000001);
        assertEquals(84.0, result.getEntry(1, 0), 0.000001);
        assertEquals(69.0, result.getEntry(1, 1), 0.000001);
        assertEquals(54.0, result.getEntry(1, 2), 0.000001);
        assertEquals(138.0, result.getEntry(2, 0), 0.000001);
        assertEquals(114.0, result.getEntry(2, 1), 0.000001);
        assertEquals(90.0, result.getEntry(2, 2), 0.000001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(2, 2);
        matrix1.multiply(matrix3);
    }

    @Test
    public void testGetEntry() {
        assertEquals(1.0, matrix1.getEntry(0, 0), 0.000001);
        assertEquals(2.0, matrix1.getEntry(0, 1), 0.000001);
        assertEquals(3.0, matrix1.getEntry(0, 2), 0.000001);
        assertEquals(4.0, matrix1.getEntry(1, 0), 0.000001);
        assertEquals(5.0, matrix1.getEntry(1, 1), 0.000001);
        assertEquals(6.0, matrix1.getEntry(1, 2), 0.000001);
        assertEquals(7.0, matrix1.getEntry(2, 0), 0.000001);
        assertEquals(8.0, matrix1.getEntry(2, 1), 0.000001);
        assertEquals(9.0, matrix1.getEntry(2, 2), 0.000001);
    }

    @Test
    public void testSetEntry() {
        matrix1.setEntry(0, 0, 10.0);
        assertEquals(10.0, matrix1.getEntry(0, 0), 0.000001);
    }

    @Test
    public void testAddToEntry() {
        matrix1.addToEntry(0, 0, 10.0);
        assertEquals(11.0, matrix1.getEntry(0, 0), 0.000001);
    }

    @Test
    public void testMultiplyEntry() {
        matrix1.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix1.getEntry(0, 0), 0.000001);
    }
}