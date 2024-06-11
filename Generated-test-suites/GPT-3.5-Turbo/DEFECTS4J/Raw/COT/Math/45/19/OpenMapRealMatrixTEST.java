import org.junit.Test;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class OpenMapRealMatrixTest {

    // Test add method
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        matrix1.setSubMatrix(testData, 0, 0);
        matrix2.setSubMatrix(testData, 0, 0);
        
        RealMatrix result = matrix1.add(matrix2);
        double[][] expectedData = {{2.0, 4.0}, {6.0, 8.0}};
        assertArrayEquals(expectedData, result.getData());
    }

    // Test subtract method
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        matrix1.setSubMatrix(testData, 0, 0);
        matrix2.setSubMatrix(testData, 0, 0);
        
        RealMatrix result = matrix1.subtract(matrix2);
        double[][] expectedData = {{0.0, 0.0}, {0.0, 0.0}};
        assertArrayEquals(expectedData, result.getData());
    }

    // Test multiply method
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        matrix1.setSubMatrix(testData, 0, 0);
        matrix2.setSubMatrix(testData, 0, 0);
        
        RealMatrix result = matrix1.multiply(matrix2);
        double[][] expectedData = {{7.0, 10.0}, {15.0, 22.0}};
        assertArrayEquals(expectedData, result.getData());
    }

    // Test getEntry method
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        matrix.setSubMatrix(testData, 0, 0);
        
        double result = matrix.getEntry(1, 1);
        assertEquals(4.0, result, 0.0);
    }

    // Test setEntry method
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        
        double result = matrix.getEntry(1, 1);
        assertEquals(5.0, result, 0.0);
    }

    // Test addToEntry method
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.addToEntry(1, 1, 2.0);
        
        double result = matrix.getEntry(1, 1);
        assertEquals(7.0, result, 0.0);
    }

    // Test multiplyEntry method
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.multiplyEntry(1, 1, 2.0);
        
        double result = matrix.getEntry(1, 1);
        assertEquals(10.0, result, 0.0);
    }

    // Test copy method
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        matrix.setSubMatrix(testData, 0, 0);
        
        OpenMapRealMatrix copy = matrix.copy();
        
        assertArrayEquals(matrix.getData(), copy.getData());
    }

    // Test createMatrix method
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix result = matrix.createMatrix(3, 3);
        
        int resultRows = result.getRowDimension();
        int resultColumns = result.getColumnDimension();
        assertEquals(3, resultRows);
        assertEquals(3, resultColumns);
    }

    // Test getColumnDimension method
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        int result = matrix.getColumnDimension();
        assertEquals(3, result);
    }

    // Test getRowDimension method
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        int result = matrix.getRowDimension();
        assertEquals(2, result);
    }

}