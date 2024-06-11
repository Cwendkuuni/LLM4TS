import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
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

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 6.0);
        expected.setEntry(0, 1, 8.0);
        expected.setEntry(1, 0, 10.0);
        expected.setEntry(1, 1, 12.0);

        Assert.assertEquals(expected, matrix1.add(matrix2));
    }

    @Test
    public void testSubtract() {
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

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, -4.0);
        expected.setEntry(0, 1, -4.0);
        expected.setEntry(1, 0, -4.0);
        expected.setEntry(1, 1, -4.0);

        Assert.assertEquals(expected, matrix1.subtract(matrix2));
    }

    @Test
    public void testMultiply() {
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

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 19.0);
        expected.setEntry(0, 1, 22.0);
        expected.setEntry(1, 0, 43.0);
        expected.setEntry(1, 1, 50.0);

        Assert.assertEquals(expected, matrix1.multiply(matrix2));
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        
        matrix.addToEntry(0, 0, 5);
        matrix.addToEntry(0, 1, -2);
        matrix.addToEntry(0, 2, 10);
        
        Assert.assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, matrix.getEntry(0, 1), 0.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        
        matrix.multiplyEntry(0, 0, 2);
        matrix.multiplyEntry(0, 1, -1);
        matrix.multiplyEntry(0, 2, 3);
        
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(-2.0, matrix.getEntry(0, 1), 0.0);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 2, 1.0);
    }
}

