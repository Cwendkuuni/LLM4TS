import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixCreationWithNegativeSize() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        Assert.assertNotSame(original, copy);
        Assert.assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        Assert.assertEquals(1.0, result.getEntry(0, 0), 1e-14);
        Assert.assertEquals(4.0, result.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixAdditionWithIncompatibleMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        DenseMatrix matrix2 = new DenseMatrix(2, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        Assert.assertEquals(-2.0, result.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixSubtractionWithIncompatibleMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        DenseMatrix matrix2 = new DenseMatrix(2, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 2, 3.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        Assert.assertEquals(5.0, result.getEntry(0, 0), 1e-14);
        Assert.assertEquals(7.0, result.getEntry(1, 1), 1e-14);
        Assert.assertEquals(6.0, result.getEntry(1, 2), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplicationWithIncompatibleMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        DenseMatrix matrix2 = new DenseMatrix(2, 3);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        Assert.assertEquals(2, newMatrix.getRowDimension());
        Assert.assertEquals(2, newMatrix.getColumnDimension());
    }
}