import org.junit.Test;
import org.junit.Assert;

public class OpenMapRealMatrixTest {

    @Test
    public void testCopy() {
        // create a matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        matrix.setEntry(2, 0, 5.0);
        matrix.setEntry(2, 1, 6.0);

        // create a copy of matrix
        OpenMapRealMatrix copy = matrix.copy();

        // assert that the copy is equal to matrix
        Assert.assertEquals(matrix, copy);
    }

    @Test
    public void testCreateMatrix() {
        // create a matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        // create a new matrix using createMatrix method
        OpenMapRealMatrix newMatrix = matrix.createMatrix(3, 3);

        // assert that the new matrix has the same dimensions as the original matrix
        Assert.assertEquals(matrix.getRowDimension(), newMatrix.getRowDimension());
        Assert.assertEquals(matrix.getColumnDimension(), newMatrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        // create two matrices
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        // add the matrices
        OpenMapRealMatrix result = matrix1.add(matrix2);

        // assert that the result is as expected
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 6.0);
        expected.setEntry(0, 1, 8.0);
        expected.setEntry(1, 0, 10.0);
        expected.setEntry(1, 1, 12.0);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        // create two matrices
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        // subtract the matrices
        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        // assert that the result is as expected
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, -4.0);
        expected.setEntry(0, 1, -4.0);
        expected.setEntry(1, 0, -4.0);
        expected.setEntry(1, 1, -4.0);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        // create two matrices
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        // multiply the matrices
        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        // assert that the result is as expected
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 19.0);
        expected.setEntry(0, 1, 22.0);
        expected.setEntry(1, 0, 43.0);
        expected.setEntry(1, 1, 50.0);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetEntry() {
        // create a matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        // get an entry from the matrix
        double entry = matrix.getEntry(1, 1);

        // assert that the entry is as expected
        Assert.assertEquals(4.0, entry, 0.0);
    }

    @Test
    public void testSetEntry() {
        // create a matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        // set an entry in the matrix
        matrix.setEntry(1, 1, 4.0);

        // assert that the entry is set correctly
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        // create a matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 4.0);

        // add a value to an entry in the matrix
        matrix.addToEntry(1, 1, 2.0);

        // assert that the value is added correctly
        Assert.assertEquals(6.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        // create a matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 4.0);

        // multiply an entry in the matrix by a value
        matrix.multiplyEntry(1, 1, 0.5);

        // assert that the value is multiplied correctly
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
    }
}