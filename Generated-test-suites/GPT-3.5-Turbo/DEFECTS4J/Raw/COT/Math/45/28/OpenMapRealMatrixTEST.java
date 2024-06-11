import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.linear.AbstractRealMatrix;
import org.apache.commons.math.linear.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    // Test createMatrix method
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        Assert.assertTrue(matrix instanceof OpenMapRealMatrix);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(2, matrix.getColumnDimension());
    }

    // Test getColumnDimension method
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    // Test add method
    @Test(expected = DimensionMismatchException.class)
    public void testAddWithDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        Assert.assertEquals(6.0, result.getEntry(0, 0), 0.0001);
        Assert.assertEquals(8.0, result.getEntry(0, 1), 0.0001);
        Assert.assertEquals(10.0, result.getEntry(1, 0), 0.0001);
        Assert.assertEquals(12.0, result.getEntry(1, 1), 0.0001);
    }

    // Test subtract method
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractWithDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        matrix1.setEntry(0, 0, 5.0);
        matrix1.setEntry(0, 1, 6.0);
        matrix1.setEntry(1, 0, 7.0);
        matrix1.setEntry(1, 1, 8.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        Assert.assertEquals(4.0, result.getEntry(0, 0), 0.0001);
        Assert.assertEquals(4.0, result.getEntry(0, 1), 0.0001);
        Assert.assertEquals(4.0, result.getEntry(1, 0), 0.0001);
        Assert.assertEquals(4.0, result.getEntry(1, 1), 0.0001);
    }

    // Test multiply method
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        Assert.assertEquals(19.0, result.getEntry(0, 0), 0.0001);
        Assert.assertEquals(22.0, result.getEntry(0, 1), 0.0001);
        Assert.assertEquals(43.0, result.getEntry(1, 0), 0.0001);
        Assert.assertEquals(50.0, result.getEntry(1, 1), 0.0001);
    }

    // Test getEntry method
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    // Test setEntry method
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    // Test addToEntry method
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.addToEntry(0, 1, 2.0);

        Assert.assertEquals(3.0, matrix.getEntry(0, 1), 0.0001);
    }

    // Test multiplyEntry method
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.multiplyEntry(0, 1, 2.0);

        Assert.assertEquals(4.0, matrix.getEntry(0, 1), 0.0001);
    }
}