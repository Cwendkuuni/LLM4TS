import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(4, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix copy = matrix.copy();
        Assert.assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        Assert.assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
        Assert.assertNotSame(matrix, copy);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        matrix.setEntry(0, 0, 10.0);
        matrix.setEntry(1, 2, -5.0);
        Assert.assertEquals(10.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(-5.0, matrix.getEntry(1, 2), 0.0);
        Assert.assertEquals(0.0, matrix.getEntry(2, 3), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        matrix.setEntry(0, 0, 10.0);
        matrix.setEntry(1, 2, -5.0);
        Assert.assertEquals(10.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(-5.0, matrix.getEntry(1, 2), 0.0);
        matrix.setEntry(0, 0, 3.0);
        matrix.setEntry(2, 2, 7.0);
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(7.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 0, -1.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, -3.0);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        Assert.assertEquals(3.0, sum.getEntry(0, 0), 0.0);
        Assert.assertEquals(5.0, sum.getEntry(0, 1), 0.0);
        Assert.assertEquals(-4.0, sum.getEntry(1, 0), 0.0);
    }

    @Test
    public void testAddWithIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 2, -3.0);
        try {
            matrix1.add(matrix2);
            Assert.fail("Expected DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 0, -1.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, -3.0);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        Assert.assertEquals(1.0, difference.getEntry(0, 0), 0.0);
        Assert.assertEquals(1.0, difference.getEntry(0, 1), 0.0);
        Assert.assertEquals(2.0, difference.getEntry(1, 0), 0.0);
    }

    @Test
    public void testSubtractWithIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 2, -3.0);
        try {
            matrix1.subtract(matrix2);
            Assert.fail("Expected DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 0, -1.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, -3.0);
        matrix2.setEntry(1, 1, 0.5);
        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        Assert.assertEquals(-6.0, product.getEntry(0, 0), 0.0);
        Assert.assertEquals(4.0, product.getEntry(0, 1), 0.0);
        Assert.assertEquals(0.0, product.getEntry(1, 0), 0.0);
        Assert.assertEquals(1.5, product.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyWithIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 2, -3.0);
        try {
            matrix1.multiply(matrix2);
            Assert.fail("Expected DimensionMismatchException not thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 3);
        Assert.assertEquals(2, newMatrix.getRowDimension());
        Assert.assertEquals(3, newMatrix.getColumnDimension());
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        Assert.assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        Assert.assertEquals(4, matrix.getColumnDimension());
    }
}