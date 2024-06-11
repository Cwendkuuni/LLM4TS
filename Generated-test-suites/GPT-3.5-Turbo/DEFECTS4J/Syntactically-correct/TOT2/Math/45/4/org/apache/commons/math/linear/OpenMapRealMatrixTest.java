import org.apache.commons.math.util.OpenIntToDoubleHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    // Test case for OpenMapRealMatrix constructor with positive dimensions
    @Test
    public void testConstructor_PositiveDimensions() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(4, matrix.getColumnDimension());
    }

    // Test case for OpenMapRealMatrix constructor with zero dimensions
    @Test
    public void testConstructor_ZeroDimensions() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(0, 0);
        assertEquals(0, matrix.getRowDimension());
        assertEquals(0, matrix.getColumnDimension());
    }

    // Test case for OpenMapRealMatrix copy method
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(matrix1.getEntry(0, 0), matrix2.getEntry(0, 0), 0.0);
    }

    // Test case for OpenMapRealMatrix add method
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 0.0);
    }

    // Test case for OpenMapRealMatrix subtract method
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
    }

    // Test case for OpenMapRealMatrix multiply method
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 3.0);
        RealMatrix result = matrix1.multiply(matrix2);
        assertEquals(6.0, result.getEntry(0, 0), 0.0);
    }

    // Test case for OpenMapRealMatrix getEntry method
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    // Test case for OpenMapRealMatrix setEntry method
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 0, 3.0);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0);
    }

    // Test case for OpenMapRealMatrix addToEntry method
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0);
    }

    // Test case for OpenMapRealMatrix multiplyEntry method
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 3.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(6.0, matrix.getEntry(1, 1), 0.0);
    }

}