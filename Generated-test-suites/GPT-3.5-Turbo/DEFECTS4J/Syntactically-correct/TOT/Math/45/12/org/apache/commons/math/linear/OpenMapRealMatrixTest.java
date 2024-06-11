import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NumberIsTooLargeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);
        matrix.setEntry(2, 0, 7.0);
        matrix.setEntry(2, 1, 8.0);
        matrix.setEntry(2, 2, 9.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(0, 0, 10.0);
        otherMatrix.setEntry(0, 1, 11.0);
        otherMatrix.setEntry(0, 2, 12.0);
        otherMatrix.setEntry(1, 0, 13.0);
        otherMatrix.setEntry(1, 1, 14.0);
        otherMatrix.setEntry(1, 2, 15.0);
        otherMatrix.setEntry(2, 0, 16.0);
        otherMatrix.setEntry(2, 1, 17.0);
        otherMatrix.setEntry(2, 2, 18.0);

        OpenMapRealMatrix result = matrix.add(otherMatrix);

        double[][] expectedData = {
            {11.0, 13.0, 15.0},
            {17.0, 19.0, 21.0},
            {23.0, 25.0, 27.0}
        };

        assertArrayEquals(expectedData, result.getData());

    }

    @Test(expected = DimensionMismatchException.class)
    public void testAdd_withDifferentDimensions() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(2, 3);
        matrix.add(otherMatrix);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(0, 0, 1.0);
        otherMatrix.setEntry(0, 1, 2.0);
        otherMatrix.setEntry(0, 2, 3.0);
        otherMatrix.setEntry(1, 0, 4.0);
        otherMatrix.setEntry(1, 1, 5.0);
        otherMatrix.setEntry(1, 2, 6.0);
        otherMatrix.setEntry(2, 0, 7.0);
        otherMatrix.setEntry(2, 1, 8.0);
        otherMatrix.setEntry(2, 2, 9.0);

        OpenMapRealMatrix result = matrix.subtract(otherMatrix);

        double[][] expectedData = {
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        };

        assertArrayEquals(expectedData, result.getData());
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(3, 3);
        otherMatrix.setEntry(0, 0, 1.0);
        otherMatrix.setEntry(0, 1, 2.0);
        otherMatrix.setEntry(0, 2, 3.0);
        otherMatrix.setEntry(1, 0, 4.0);
        otherMatrix.setEntry(1, 1, 5.0);
        otherMatrix.setEntry(1, 2, 6.0);
        otherMatrix.setEntry(2, 0, 7.0);
        otherMatrix.setEntry(2, 1, 8.0);
        otherMatrix.setEntry(2, 2, 9.0);

        OpenMapRealMatrix result = matrix.multiply(otherMatrix);

        double[][] expectedData = {
            {30.0, 36.0, 42.0},
            {66.0, 81.0, 96.0},
            {102.0, 126.0, 150.0}
        };

        assertArrayEquals(expectedData, result.getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiply_withDifferentDimensions() {
        OpenMapRealMatrix otherMatrix = new OpenMapRealMatrix(2, 3);
        matrix.multiply(otherMatrix);
    }

    @Test
    public void testGetEntry() {
        double result = matrix.getEntry(1, 2);
        assertEquals(6.0, result, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testGetEntry_withInvalidRow() {
        matrix.getEntry(3, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testGetEntry_withInvalidColumn() {
        matrix.getEntry(0, 3);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 10.0);
        double result = matrix.getEntry(0, 0);
        assertEquals(10.0, result, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntry_withInvalidRow() {
        matrix.setEntry(3, 0, 10.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntry_withInvalidColumn() {
        matrix.setEntry(0, 3, 10.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 2.0);
        double result = matrix.getEntry(0, 0);
        assertEquals(3.0, result, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddToEntry_withInvalidRow() {
        matrix.addToEntry(3, 0, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddToEntry_withInvalidColumn() {
        matrix.addToEntry(0, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.multiplyEntry(0, 0, 2.0);
        double result = matrix.getEntry(0, 0);
        assertEquals(2.0, result, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyEntry_withInvalidRow() {
        matrix.multiplyEntry(3, 0, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyEntry_withInvalidColumn() {
        matrix.multiplyEntry(0, 3, 2.0);
    }

    @Test
    public void testCreateMatrix() {
        int rowDimension = 2;
        int columnDimension = 2;
        OpenMapRealMatrix result = matrix.createMatrix(rowDimension, columnDimension);

        assertEquals(rowDimension, result.getRowDimension());
        assertEquals(columnDimension, result.getColumnDimension());
        assertArrayEquals(new double[rowDimension][columnDimension], result.getData());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix result = matrix.copy();

        assertArrayEquals(matrix.getData(), result.getData());
    }

    @Test
    public void testGetRowDimension() {
        int result = matrix.getRowDimension();
        assertEquals(3, result);
    }

    @Test
    public void testGetColumnDimension() {
        int result = matrix.getColumnDimension();
        assertEquals(3, result);
    }

    private double[][] getMatrixData() {
        return matrix.getData();
    }

    private void assertArrayEquals(double[][] expected, double[][] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], 0.0);
        }
    }
}