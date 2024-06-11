import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OpenMapRealMatrixTest {

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);

        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(2, 2, 6.0);

        assertEquals(4.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(6.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix sum = matrix1.add(matrix2);

        assertEquals(3.0, sum.getEntry(0, 0), 0.0);
        assertEquals(5.0, sum.getEntry(0, 1), 0.0);
        assertEquals(7.0, sum.getEntry(1, 0), 0.0);
        assertEquals(9.0, sum.getEntry(1, 1), 0.0);
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
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix difference = matrix1.subtract(matrix2);

        assertEquals(-1.0, difference.getEntry(0, 0), 0.0);
        assertEquals(-1.0, difference.getEntry(0, 1), 0.0);
        assertEquals(-1.0, difference.getEntry(1, 0), 0.0);
        assertEquals(-1.0, difference.getEntry(1, 1), 0.0);
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
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix product = matrix1.multiply(matrix2);

        assertEquals(10.0, product.getEntry(0, 0), 0.0);
        assertEquals(13.0, product.getEntry(0, 1), 0.0);
        assertEquals(22.0, product.getEntry(1, 0), 0.0);
        assertEquals(29.0, product.getEntry(1, 1), 0.0);
    }
}