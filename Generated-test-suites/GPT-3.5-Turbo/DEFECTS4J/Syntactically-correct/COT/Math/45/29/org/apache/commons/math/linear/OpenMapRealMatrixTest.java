import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix copyMatrix = matrix.copy();
        Assert.assertEquals(matrix.getRowDimension(), copyMatrix.getRowDimension());
        Assert.assertEquals(matrix.getColumnDimension(), copyMatrix.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                Assert.assertEquals(matrix.getEntry(i, j), copyMatrix.getEntry(i, j), 0);
            }
        }
    }

    @Test
    public void testCreateMatrix() {
        int rowDimension = 2;
        int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        Assert.assertEquals(rowDimension, matrix.getRowDimension());
        Assert.assertEquals(columnDimension, matrix.getColumnDimension());
    }

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
        Assert.assertEquals(6, result.getEntry(0, 0), 0);
        Assert.assertEquals(8, result.getEntry(0, 1), 0);
        Assert.assertEquals(10, result.getEntry(1, 0), 0);
        Assert.assertEquals(12, result.getEntry(1, 1), 0);
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
        Assert.assertEquals(-4, result.getEntry(0, 0), 0);
        Assert.assertEquals(-4, result.getEntry(0, 1), 0);
        Assert.assertEquals(-4, result.getEntry(1, 0), 0);
        Assert.assertEquals(-4, result.getEntry(1, 1), 0);
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
        Assert.assertEquals(19, result.getEntry(0, 0), 0);
        Assert.assertEquals(22, result.getEntry(0, 1), 0);
        Assert.assertEquals(43, result.getEntry(1, 0), 0);
        Assert.assertEquals(50, result.getEntry(1, 1), 0);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);
        Assert.assertEquals(1, matrix.getEntry(0, 0), 0);
        Assert.assertEquals(2, matrix.getEntry(0, 1), 0);
        Assert.assertEquals(3, matrix.getEntry(1, 0), 0);
        Assert.assertEquals(4, matrix.getEntry(1, 1), 0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);
        Assert.assertEquals(1, matrix.getEntry(0, 0), 0);
        Assert.assertEquals(2, matrix.getEntry(0, 1), 0);
        Assert.assertEquals(3, matrix.getEntry(1, 0), 0);
        Assert.assertEquals(4, matrix.getEntry(1, 1), 0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);
        matrix.addToEntry(0, 0, 1);
        Assert.assertEquals(2, matrix.getEntry(0, 0), 0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(1, 0, 3);
        matrix.setEntry(1, 1, 4);
        matrix.multiplyEntry(0, 0, 2);
        Assert.assertEquals(2, matrix.getEntry(0, 0), 0);
    }

}