import org.junit.Test;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.exception.DimensionMismatchException;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 3.0);
        expected.setEntry(0, 1, 6.0);
        expected.setEntry(1, 0, 9.0);
        expected.setEntry(1, 1, 12.0);

        org.junit.Assert.assertEquals(expected, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddWithDifferentDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, -1.0);
        expected.setEntry(0, 1, -2.0);
        expected.setEntry(1, 0, -3.0);
        expected.setEntry(1, 1, -4.0);

        org.junit.Assert.assertEquals(expected, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractWithDifferentDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 14.0);
        expected.setEntry(0, 1, 20.0);
        expected.setEntry(1, 0, 30.0);
        expected.setEntry(1, 1, 44.0);

        org.junit.Assert.assertEquals(expected, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        double result = matrix.getEntry(1, 1);

        org.junit.Assert.assertEquals(4.0, result, 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.setEntry(1, 1, 5.0);

        double result = matrix.getEntry(1, 1);

        org.junit.Assert.assertEquals(5.0, result, 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(1, 1, 2.0);

        double result = matrix.getEntry(1, 1);

        org.junit.Assert.assertEquals(6.0, result, 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(1, 1, 2.0);

        double result = matrix.getEntry(1, 1);

        org.junit.Assert.assertEquals(8.0, result, 0.0);
    }
}