import org.junit.Test;
import org.apache.commons.math.linear.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);
        matrix1.setEntry(2, 0, 7.0);
        matrix1.setEntry(2, 1, 8.0);
        matrix1.setEntry(2, 2, 9.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 9.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 4.0);
        matrix2.setEntry(2, 0, 3.0);
        matrix2.setEntry(2, 1, 2.0);
        matrix2.setEntry(2, 2, 1.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        double[][] expected = {{10.0, 10.0, 10.0}, {10.0, 10.0, 10.0}, {10.0, 10.0, 10.0}};
        double[][] actual = result.getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);
        matrix1.setEntry(2, 0, 7.0);
        matrix1.setEntry(2, 1, 8.0);
        matrix1.setEntry(2, 2, 9.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 9.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 4.0);
        matrix2.setEntry(2, 0, 3.0);
        matrix2.setEntry(2, 1, 2.0);
        matrix2.setEntry(2, 2, 1.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        double[][] expected = {{-8.0, -6.0, -4.0}, {-2.0, 0.0, 2.0}, {4.0, 6.0, 8.0}};
        double[][] actual = result.getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        matrix1.setEntry(2, 0, 5.0);
        matrix1.setEntry(2, 1, 6.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 7.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(0, 2, 9.0);
        matrix2.setEntry(1, 0, 10.0);
        matrix2.setEntry(1, 1, 11.0);
        matrix2.setEntry(1, 2, 12.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        double[][] expected = {{27.0, 30.0, 33.0}, {61.0, 68.0, 75.0}, {97.0, 108.0, 119.0}};
        double[][] actual = result.getData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);
        matrix.setEntry(2, 0, 7.0);
        matrix.setEntry(2, 1, 8.0);
        matrix.setEntry(2, 2, 9.0);

        double expected = 5.0;
        double actual = matrix.getEntry(1, 1);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        matrix.setEntry(0, 0, 5.0);
        double expected = 5.0;
        double actual = matrix.getEntry(0, 0);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        matrix.addToEntry(0, 0, 5.0);
        double expected = 6.0;
        double actual = matrix.getEntry(0, 0);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);

        matrix.multiplyEntry(0, 0, 5.0);
        double expected = 10.0;
        double actual = matrix.getEntry(0, 0);
        assertEquals(expected, actual, 0.0);
    }
}