import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertNotNull(matrix);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());

        double[][] data = {{1, 2}, {3, 4}};
        matrix = new OpenMapRealMatrix(data);
        assertNotNull(matrix);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copyMatrix = matrix.copy();
        assertNotNull(copyMatrix);
        assertEquals(3, copyMatrix.getRowDimension());
        assertEquals(3, copyMatrix.getColumnDimension());

        double[][] data = {{1, 2}, {3, 4}};
        matrix = new OpenMapRealMatrix(data);
        copyMatrix = matrix.copy();
        assertNotNull(copyMatrix);
        assertEquals(2, copyMatrix.getRowDimension());
        assertEquals(2, copyMatrix.getColumnDimension());
        assertEquals(matrix.getEntry(0, 0), copyMatrix.getEntry(0, 0), 0.0);
        assertEquals(matrix.getEntry(1, 1), copyMatrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = OpenMapRealMatrix.createMatrix(3, 3);
        assertNotNull(matrix);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        double[][] data = {{1, 2}, {3, 4}};
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(data);
        assertEquals(matrix.getEntry(0, 0), 1.0, 0.0);
        assertEquals(matrix.getEntry(1, 1), 4.0, 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(matrix.getEntry(1, 1), 5.0, 0.0);
    }

    @Test
    public void testAdd() {
        double[][] data1 = {{1, 2}, {3, 4}};
        double[][] data2 = {{5, 6}, {7, 8}};
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(data1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(data2);
        OpenMapRealMatrix sumMatrix = matrix1.add(matrix2);

        assertEquals(sumMatrix.getEntry(0, 0), 6.0, 0.0);
        assertEquals(sumMatrix.getEntry(1, 1), 12.0, 0.0);
    }

    @Test
    public void testSubtract() {
        double[][] data1 = {{1, 2}, {3, 4}};
        double[][] data2 = {{5, 6}, {7, 8}};
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(data1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(data2);
        OpenMapRealMatrix subtractMatrix = matrix1.subtract(matrix2);

        assertEquals(subtractMatrix.getEntry(0, 0), -4.0, 0.0);
        assertEquals(subtractMatrix.getEntry(1, 1), -4.0, 0.0);
    }

    @Test
    public void testMultiply() {
        double[][] data1 = {{1, 2}, {3, 4}};
        double[][] data2 = {{5, 6}, {7, 8}};
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(data1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(data2);
        OpenMapRealMatrix multiplyMatrix = matrix1.multiply(matrix2);

        assertEquals(multiplyMatrix.getEntry(0, 0), 19.0, 0.0);
        assertEquals(multiplyMatrix.getEntry(1, 1), 53.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyDimensionsMismatch() {
        double[][] data1 = {{1, 2}, {3, 4}};
        double[][] data2 = {{5, 6, 7}, {8, 9, 10}};
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(data1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(data2);
        matrix1.multiply(matrix2);
    }
}