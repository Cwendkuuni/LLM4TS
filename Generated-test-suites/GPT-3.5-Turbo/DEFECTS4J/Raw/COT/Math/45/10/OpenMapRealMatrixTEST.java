import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        // Create two matrices to add
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        // Add matrices
        OpenMapRealMatrix result = matrix1.add(matrix2);

        // Check result
        Assert.assertEquals(3.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(5.0, result.getEntry(0, 1), 0.0);
        Assert.assertEquals(7.0, result.getEntry(1, 0), 0.0);
        Assert.assertEquals(9.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        // Create two matrices to subtract
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        // Subtract matrices
        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        // Check result
        Assert.assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(-1.0, result.getEntry(0, 1), 0.0);
        Assert.assertEquals(-1.0, result.getEntry(1, 0), 0.0);
        Assert.assertEquals(-1.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        // Create two matrices to multiply
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        // Multiply matrices
        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        // Check result
        Assert.assertEquals(10.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(13.0, result.getEntry(0, 1), 0.0);
        Assert.assertEquals(22.0, result.getEntry(1, 0), 0.0);
        Assert.assertEquals(29.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testGetEntry() {
        // Create matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        // Check entries
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        // Create matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        // Set entries
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        // Check entries
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        // Create matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);

        // Add to entry
        matrix.addToEntry(0, 0, 2.0);

        // Check entry
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        // Create matrix
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);

        // Multiply entry
        matrix.multiplyEntry(0, 0, 3.0);

        // Check entry
        Assert.assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
    }

}