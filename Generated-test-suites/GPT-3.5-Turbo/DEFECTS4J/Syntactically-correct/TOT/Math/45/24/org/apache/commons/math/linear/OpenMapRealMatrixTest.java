import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        RealMatrix copiedMatrix = matrix.copy();

        Assert.assertArrayEquals(matrix.getData(), copiedMatrix.getData());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);

        RealMatrix result = matrix1.add(matrix2);

        Assert.assertEquals(3.0, result.getEntry(0, 0), 0.0001);
        Assert.assertEquals(5.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testAdd_DimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        try {
            matrix1.add(matrix2);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);

        RealMatrix result = matrix1.subtract(matrix2);

        Assert.assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        Assert.assertEquals(1.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix2.setEntry(0, 0, 7.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(1, 0, 9.0);
        matrix2.setEntry(1, 1, 10.0);
        matrix2.setEntry(2, 0, 11.0);
        matrix2.setEntry(2, 1, 12.0);

        RealMatrix result = matrix1.multiply(matrix2);

        Assert.assertEquals(58.0, result.getEntry(0, 0), 0.0001);
        Assert.assertEquals(64.0, result.getEntry(0, 1), 0.0001);
        Assert.assertEquals(139.0, result.getEntry(1, 0), 0.0001);
        Assert.assertEquals(154.0, result.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiply_MatrixDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);

        try {
            matrix1.multiply(matrix2);
            Assert.fail("Expected MatrixDimensionMismatchException");
        } catch (MatrixDimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetEntry_IndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        try {
            matrix.getEntry(2, 2);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.0001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSetEntry_IndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        try {
            matrix.setEntry(2, 2, 5.0);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);

        matrix.addToEntry(0, 0, 3.0);
        Assert.assertEquals(5.0, matrix.getEntry(0, 0), 0.0001);

        matrix.addToEntry(0, 0, -2.0);
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);

        matrix.multiplyEntry(0, 0, 3.0);
        Assert.assertEquals(6.0, matrix.getEntry(0, 0), 0.0001);

        matrix.multiplyEntry(0, 0, 0.5);
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.0001);
    }
}