import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(4.0, result.getEntry(0, 0), 0.0001);
        assertEquals(6.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 0.5);
        matrix2.setEntry(1, 1, 1.5);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(0.5, result.getEntry(0, 0), 0.0001);
        assertEquals(0.5, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        assertEquals(2.0, result.getEntry(0, 0), 0.0001);
        assertEquals(6.0, result.getEntry(1, 1), 0.0001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.add(matrix2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.subtract(matrix2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);

        matrix1.multiply(matrix2);
    }
}