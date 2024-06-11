import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copyMatrix = originalMatrix.copy();
        Assert.assertEquals(originalMatrix, copyMatrix);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixWithInvalidDimension() {
        new OpenMapRealMatrix(-1, 2);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(3, 3);

        mat1.setEntry(0, 0, 2.0);
        mat1.setEntry(1, 1, 3.0);
        mat2.setEntry(0, 0, 1.0);
        mat2.setEntry(1, 1, 2.0);
        mat2.setEntry(2, 2, 1.0);

        OpenMapRealMatrix result = mat1.add(mat2);

        Assert.assertEquals(mat1.getRowDimension(), result.getRowDimension());
        Assert.assertEquals(mat1.getColumnDimension(), result.getColumnDimension());

        Assert.assertEquals(3.0, result.getEntry(0, 0), 0.01);
        Assert.assertEquals(5.0, result.getEntry(1, 1), 0.01);
        Assert.assertEquals(1.0, result.getEntry(2, 2), 0.01);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(3, 3);

        mat1.setEntry(0, 0, 2.0);
        mat1.setEntry(1, 1, 3.0);
        mat1.setEntry(2, 2, 1.0);
        mat2.setEntry(0, 0, 1.0);
        mat2.setEntry(1, 1, 2.0);
        mat2.setEntry(2, 2, -1.0);

        OpenMapRealMatrix result = mat1.subtract(mat2);

        Assert.assertEquals(mat1.getRowDimension(), result.getRowDimension());
        Assert.assertEquals(mat1.getColumnDimension(), result.getColumnDimension());

        Assert.assertEquals(1.0, result.getEntry(0, 0), 0.01);
        Assert.assertEquals(1.0, result.getEntry(1, 1), 0.01);
        Assert.assertEquals(2.0, result.getEntry(2, 2), 0.01);
    }

    @Test
    public void testMultiplicationWithDenseMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        RealMatrix denseMatrix = new BlockRealMatrix(3, 3);

        matrix.setEntry(0, 0, 2.0);
        matrix.setEntry(1, 1, 3.0);
        matrix.setEntry(2, 2, 1.0);

        denseMatrix.setEntry(0, 0, 1.0);
        denseMatrix.setEntry(0, 1, 2.0);
        denseMatrix.setEntry(0, 2, 3.0);
        denseMatrix.setEntry(1, 0, 4.0);
        denseMatrix.setEntry(1, 1, 5.0);
        denseMatrix.setEntry(1, 2, 6.0);
        denseMatrix.setEntry(2, 0, 7.0);
        denseMatrix.setEntry(2, 1, 8.0);
        denseMatrix.setEntry(2, 2, 9.0);

        RealMatrix result = matrix.multiply(denseMatrix);

        Assert.assertEquals(matrix.getRowDimension(), result.getRowDimension());
        Assert.assertEquals(denseMatrix.getColumnDimension(), result.getColumnDimension());

        Assert.assertEquals(14.0, result.getEntry(0, 0), 0.01);
        Assert.assertEquals(22.0, result.getEntry(0, 1), 0.01);
        Assert.assertEquals(21.0, result.getEntry(0, 2), 0.01);
        Assert.assertEquals(32.0, result.getEntry(1, 0), 0.01);
        Assert.assertEquals(51.0, result.getEntry(1, 1), 0.01);
        Assert.assertEquals(49.0, result.getEntry(1, 2), 0.01);
        Assert.assertEquals(31.0, result.getEntry(2, 0), 0.01);
        Assert.assertEquals(54.0, result.getEntry(2, 1), 0.01);
        Assert.assertEquals(45.0, result.getEntry(2, 2), 0.01);
    }

    @Test
    public void testMultiplicationWithSparseMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(1, 1, 3.0);
        matrix1.setEntry(2, 2, 1.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(2, 2, 1.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        Assert.assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        Assert.assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());

        Assert.assertEquals(6.0, result.getEntry(0, 0), 0.01);
        Assert.assertEquals(15.0, result.getEntry(1, 1), 0.01);
        Assert.assertEquals(1.0, result.getEntry(2, 2), 0.01);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.01);

        matrix.setEntry(1, 1, 3.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 1), 0.01);

        matrix.setEntry(2, 2, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(2, 2), 0.01);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.01);

        matrix.setEntry(1, 1, 3.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 1), 0.01);

        matrix.setEntry(2, 2, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(2, 2), 0.01);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.addToEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.01);

        matrix.addToEntry(1, 1, 3.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 1), 0.01);

        matrix.addToEntry(2, 2, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(2, 2), 0.01);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(4.0, matrix.getEntry(0, 0), 0.01);

        matrix.multiplyEntry(1, 1, 3.0);
        Assert.assertEquals(9.0, matrix.getEntry(1, 1), 0.01);

        matrix.multiplyEntry(2, 2, 1.5);
        Assert.assertEquals(1.5, matrix.getEntry(2, 2), 0.01);
    }

    @Test
    public void testInvalidEntryAccess() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        try {
            matrix.getEntry(-1, 0);
            Assert.fail("Expected NumberIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            matrix.getEntry(0, -1);
            Assert.fail("Expected NumberIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testNegativeEntryValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(0, 0, -2.0);
        Assert.assertEquals(-2.0, matrix.getEntry(0, 0), 0.01);
    }

    @Test
    public void testEmptyMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(0, 3);
        Assert.assertEquals(0, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertTrue(matrix.isEmpty());
    }
}