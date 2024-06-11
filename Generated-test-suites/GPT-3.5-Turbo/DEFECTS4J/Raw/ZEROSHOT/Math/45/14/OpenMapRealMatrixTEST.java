import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1);
        matrix1.setEntry(0, 1, 2);
        matrix1.setEntry(1, 0, 3);
        matrix1.setEntry(1, 1, 4);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5);
        matrix2.setEntry(0, 1, 6);
        matrix2.setEntry(1, 0, 7);
        matrix2.setEntry(1, 1, 8);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        Assert.assertEquals(6.0, result.getEntry(0, 0), 0.000001);
        Assert.assertEquals(8.0, result.getEntry(0, 1), 0.000001);
        Assert.assertEquals(10.0, result.getEntry(1, 0), 0.000001);
        Assert.assertEquals(12.0, result.getEntry(1, 1), 0.000001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1);
        matrix1.setEntry(0, 1, 2);
        matrix1.setEntry(1, 0, 3);
        matrix1.setEntry(1, 1, 4);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5);
        matrix2.setEntry(0, 1, 6);
        matrix2.setEntry(1, 0, 7);
        matrix2.setEntry(1, 1, 8);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        Assert.assertEquals(-4.0, result.getEntry(0, 0), 0.000001);
        Assert.assertEquals(-4.0, result.getEntry(0, 1), 0.000001);
        Assert.assertEquals(-4.0, result.getEntry(1, 0), 0.000001);
        Assert.assertEquals(-4.0, result.getEntry(1, 1), 0.000001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1);
        matrix1.setEntry(0, 1, 2);
        matrix1.setEntry(1, 0, 3);
        matrix1.setEntry(1, 1, 4);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5);
        matrix2.setEntry(0, 1, 6);
        matrix2.setEntry(1, 0, 7);
        matrix2.setEntry(1, 1, 8);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        Assert.assertEquals(19.0, result.getEntry(0, 0), 0.000001);
        Assert.assertEquals(22.0, result.getEntry(0, 1), 0.000001);
        Assert.assertEquals(43.0, result.getEntry(1, 0), 0.000001);
        Assert.assertEquals(50.0, result.getEntry(1, 1), 0.000001);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.000001);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.000001);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.000001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.000001);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);

        matrix.setEntry(0, 0, 5);
        matrix.setEntry(0, 1, 6);
        matrix.setEntry(1, 0, 7);
        matrix.setEntry(1, 1, 8);

        Assert.assertEquals(5.0, matrix.getEntry(0, 0), 0.000001);
        Assert.assertEquals(6.0, matrix.getEntry(0, 1), 0.000001);
        Assert.assertEquals(7.0, matrix.getEntry(1, 0), 0.000001);
        Assert.assertEquals(8.0, matrix.getEntry(1, 1), 0.000001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);

        matrix.addToEntry(0, 0, 5);
        matrix.addToEntry(0, 1, 6);
        matrix.addToEntry(1, 0, 7);
        matrix.addToEntry(1, 1, 8);

        Assert.assertEquals(6.0, matrix.getEntry(0, 0), 0.000001);
        Assert.assertEquals(8.0, matrix.getEntry(0, 1), 0.000001);
        Assert.assertEquals(10.0, matrix.getEntry(1, 0), 0.000001);
        Assert.assertEquals(12.0, matrix.getEntry(1, 1), 0.000001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);

        matrix.multiplyEntry(0, 0, 5);
        matrix.multiplyEntry(0, 1, 6);
        matrix.multiplyEntry(1, 0, 7);
        matrix.multiplyEntry(1, 1, 8);

        Assert.assertEquals(5.0, matrix.getEntry(0, 0), 0.000001);
        Assert.assertEquals(12.0, matrix.getEntry(0, 1), 0.000001);
        Assert.assertEquals(21.0, matrix.getEntry(1, 0), 0.000001);
        Assert.assertEquals(32.0, matrix.getEntry(1, 1), 0.000001);
    }
}