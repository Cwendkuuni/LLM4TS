import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
        assertEquals(matrix.getEntry(0, 0), copy.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        assertEquals(matrix1.getRowDimension(), sum.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), sum.getColumnDimension());
        assertEquals(matrix1.getEntry(0, 0) + matrix2.getEntry(0, 0), sum.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(matrix1.getRowDimension(), difference.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), difference.getColumnDimension());
        assertEquals(matrix1.getEntry(0, 0) - matrix2.getEntry(0, 0), difference.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        RealMatrix product = matrix1.multiply(matrix2);
        assertEquals(matrix1.getRowDimension(), product.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), product.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 10.0);
        assertEquals(10.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(2, 2, 20.0);
        assertEquals(20.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 10.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(20.0, matrix.getEntry(1, 1), 0.0);
    }

}