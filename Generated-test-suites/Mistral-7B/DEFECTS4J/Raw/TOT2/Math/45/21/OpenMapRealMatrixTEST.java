import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructors() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix matrix3 = matrix1.copy();

        Assert.assertNotNull(matrix1);
        Assert.assertEquals(2, matrix1.getRowDimension());
        Assert.assertEquals(2, matrix1.getColumnDimension());
        Assert.assertEquals(0, matrix1.size());

        Assert.assertNotNull(matrix2);
        Assert.assertEquals(2, matrix2.getRowDimension());
        Assert.assertEquals(2, matrix2.getColumnDimension());
        Assert.assertEquals(0, matrix2.size());

        Assert.assertNotNull(matrix3);
        Assert.assertEquals(2, matrix3.getRowDimension());
        Assert.assertEquals(2, matrix3.getColumnDimension());
        Assert.assertEquals(0, matrix3.size());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix result = matrix1.add(matrix2);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        matrix1.addToEntry(0, 0, 1.0);
        matrix1.addToEntry(1, 1, 2.0);

        matrix2.addToEntry(0, 0, 3.0);
        matrix2.addToEntry(1, 1, 4.0);

        matrix1.add(matrix2);

        Assert.assertEquals(4.0, matrix1.getEntry(0, 0), 0.001);
        Assert.assertEquals(6.0, matrix1.getEntry(1, 1), 0.001);

        Assert.assertEquals(result.getRowDimension(), 2);
        Assert.assertEquals(result.getColumnDimension(), 2);
        Assert.assertEquals(4, result.size());
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new OpenIntToDoubleHashMap[0].length > 0 ? new OpenMapRealMatrix(2, 2) : new double[0][];
        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        result = matrix1.multiply(matrix2);

        Assert.assertEquals(0, result.getRowDimension());
        Assert.assertEquals(0, result.getColumnDimension());
        Assert.assertEquals(0, result.size());
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 2);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.001);

        matrix.setEntry(0, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.001);

        matrix.addToEntry(0, 0, 2.0);
        matrix.addToEntry(1, 1, 3.0);

        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.001);

        matrix.multiplyEntry(0, 0, 3.0);
        matrix.multiplyEntry(1, 1, 4.0);

        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(8.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(2, 2, 1.0);
    }

    @Test
    public void testCreateMatrix() {
        RealMatrix matrix = new OpenMapRealMatrix[0].length > 0 ? new OpenMapRealMatrix(2, 2) : new double[0][];
        OpenMapRealMatrix result = OpenMapRealMatrix.createMatrix(matrix.getRowDimension(), matrix.getColumnDimension());

        Assert.assertEquals(2, result.getRowDimension());
        Assert.assertEquals(2, result.getColumnDimension());
        Assert.assertEquals(0, result.size());
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testCreateMatrixOutOfMemory() {
        OpenMapRealMatrix.createMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}