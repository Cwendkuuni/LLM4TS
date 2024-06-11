import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OpenMapRealMatrix.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor with valid dimensions.
     */
    @Test
    public void testConstructorValidDimensions() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test adding matrices with compatible dimensions.
     */
    @Test
    public void testAddCompatibleMatrices() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix sum = matrix1.add(matrix2);
        RealMatrix expectedSum = RealMatrixUtils.createMatrix(new double[][]{{2.0, 4.0}, {6.0, 8.0}});
        Assert.assertJnhEquals(expectedSum, sum, 0.001);
    }

    /**
     * Test adding matrices with incompatible dimensions.
     */
    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAddIncompatibleMatrices() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(2, 0, 5.0);

        matrix1.add(matrix2);
    }

    /**
     * Test subtracting matrices with compatible dimensions.
     */
    @Test
    public void testSubtractCompatibleMatrices() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        RealMatrix expectedDifference = RealMatrixUtils.createMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
        Assert.assertJnhEquals(expectedDifference, difference, 0.001);
    }

    /**
     * Test subtracting matrices with incompatible dimensions.
     */
    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractIncompatibleMatrices() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(2, 0, 5.0);

        matrix1.subtract(matrix2);
    }

    /**
     * Test multiplying this matrix by another matrix.
     */
    @Test
    public void testMultiplyCompatibleMatrices() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(2, 0, 5.0);
        matrix2.setEntry(2, 1, 6.0);

        final OpenMapRealMatrix product = matrix1.multiply(matrix2);
        RealMatrix expectedProduct = RealMatrixUtils.createMatrix(new double[][]{{14.0, 18.0}, {22.0, 28.0}, {30.0, 36.0}});
        Assert.assertJnhEquals(expectedProduct, product, 0.001);
    }

    /**
     * Test multiplying this matrix by another matrix with incompatible dimensions.
     */
    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplyIncompatibleMatrices() {
        final OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);

        final OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 2, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 6.0);
        matrix2.setEntry(2, 0, 7.0);
        matrix2.setEntry(2, 1, 8.0);
        matrix2.setEntry(2, 2, 9.0);

        matrix1.multiply(matrix2);
    }

    /**
     * Test setting matrix entries.
     */
    @Test
    public void testSetEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(3.0, matrix.getEntry(0, 2), 0.001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(6.0, matrix.getEntry(1, 2), 0.001);
    }

    /**
     * Test getting matrix entries.
     */
    @Test
    public void testGetEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(3.0, matrix.getEntry(0, 2), 0.001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(6.0, matrix.getEntry(1, 2), 0.001);
    }

    /**
     * Test adding to matrix entries.
     */
    @Test
    public void testAddToEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);

        matrix.addToEntry(0, 0, 1.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.001);

        matrix.addToEntry(1, 1, 2.0);
        Assert.assertEquals(7.0, matrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test multiplying matrix entries.
     */
    @Test
    public void testMultiplyEntry() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);

        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.001);

        matrix.multiplyEntry(1, 1, 3.0);
        Assert.assertEquals(15.0, matrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test error handling when setting an entry beyond the matrix dimensions.
     */
    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.setEntry(2, 2, 1.0);
    }

    /**
     * Test error handling when getting an entry beyond the matrix dimensions.
     */
    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.getEntry(2, 2);
    }

    /**
     * Test error handling when adding to an entry beyond the matrix dimensions.
     */
    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.addToEntry(2, 2, 1.0);
    }

    /**
     * Test error handling when multiplying an entry beyond the matrix dimensions.
     */
    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.multiplyEntry(2, 2, 1.0);
    }

    /**
     * Test error handling when creating a matrix with negative dimensions.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }
}