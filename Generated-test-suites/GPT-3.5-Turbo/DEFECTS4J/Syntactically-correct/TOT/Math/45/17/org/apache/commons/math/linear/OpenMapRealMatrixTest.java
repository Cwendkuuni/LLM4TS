import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(0, 1, 5.0);
        matrix2.setEntry(0, 2, 6.0);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(3, 3);
        expectedResult.setEntry(0, 0, 5.0);
        expectedResult.setEntry(0, 1, 7.0);
        expectedResult.setEntry(0, 2, 9.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        // Assert that the result equals the expected result
        // TODO assert statements here
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(0, 1, 5.0);
        matrix2.setEntry(0, 2, 6.0);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(3, 3);
        expectedResult.setEntry(0, 0, -3.0);
        expectedResult.setEntry(0, 1, -3.0);
        expectedResult.setEntry(0, 2, -3.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        // Assert that the result equals the expected result
        // TODO assert statements here
    }

    @Test
    public void testMultiplyWithRealMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        matrix1.setEntry(2, 0, 5.0);
        matrix1.setEntry(2, 1, 6.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 2, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 6.0);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(3, 3);
        expectedResult.setEntry(0, 0, 9.0);
        expectedResult.setEntry(0, 1, 12.0);
        expectedResult.setEntry(0, 2, 15.0);
        expectedResult.setEntry(1, 0, 19.0);
        expectedResult.setEntry(1, 1, 26.0);
        expectedResult.setEntry(1, 2, 33.0);
        expectedResult.setEntry(2, 0, 29.0);
        expectedResult.setEntry(2, 1, 40.0);
        expectedResult.setEntry(2, 2, 51.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        // Assert that the result equals the expected result
        // TODO assert statements here
    }

    @Test
    public void testMultiplyWithRealMatrix_NotOpenMapMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        // Add test data to matrices

        // Assert that an exception is thrown for incompatible matrices
        // TODO assert statement here
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 2, 4.5);

        double expectedResult = 4.5;
        double result = matrix.getEntry(1, 2);

        // Assert that the result equals the expected result
        // TODO assert statement here
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        double result = matrix.getEntry(4, 2);

        // Assert that an exception is thrown due to out-of-bounds indices
        // TODO assert statement here
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(1, 2, 4.5);
        double expectedResult = 4.5;
        double result = matrix.getEntry(1, 2);

        // Assert that the result equals the expected result
        // TODO assert statement here
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(4, 2, 4.5);

        // Assert that an exception is thrown due to out-of-bounds indices
        // TODO assert statement here
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 2, 4.5);

        matrix.addToEntry(1, 2, 2.0);
        double expectedResult = 6.5;
        double result = matrix.getEntry(1, 2);

        // Assert that the result equals the expected result
        // TODO assert statement here
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.addToEntry(4, 2, 2.0);

        // Assert that an exception is thrown due to out-of-bounds indices
        // TODO assert statement here
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 2, 4.5);

        matrix.multiplyEntry(1, 2, 2.0);
        double expectedResult = 9.0;
        double result = matrix.getEntry(1, 2);

        // Assert that the result equals the expected result
        // TODO assert statement here
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntry_OutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.multiplyEntry(4, 2, 2.0);

        // Assert that an exception is thrown due to out-of-bounds indices
        // TODO assert statement here
    }

}