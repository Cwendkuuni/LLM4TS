import org.junit.Test;
import org.junit.Assert;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
    }
    
    @Test
    public void testCopy() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(data);
        
        OpenMapRealMatrix copy = matrix.copy();
        
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                Assert.assertEquals(matrix.getEntry(i, j), copy.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testAdd() {
        double[][] data1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] data2 = {{9, 8, 7}, {6, 5, 4}, {3, 2, 1}};
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(data1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(data2);
        
        OpenMapRealMatrix result = matrix1.add(matrix2);
        
        double[][] expectedData = {{10, 10, 10}, {10, 10, 10}, {10, 10, 10}};
        OpenMapRealMatrix expected = new OpenMapRealMatrix(expectedData);
        
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                Assert.assertEquals(expected.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testSubtract() {
        double[][] data1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] data2 = {{9, 8, 7}, {6, 5, 4}, {3, 2, 1}};
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(data1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(data2);
        
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        
        double[][] expectedData = {{-8, -6, -4}, {-2, 0, 2}, {4, 6, 8}};
        OpenMapRealMatrix expected = new OpenMapRealMatrix(expectedData);
        
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                Assert.assertEquals(expected.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testMultiply() {
        double[][] data1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] data2 = {{1, 1}, {2, 2}, {3, 3}};
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(data1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(data2);
        
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        
        double[][] expectedData = {{14, 14}, {32, 32}, {50, 50}};
        OpenMapRealMatrix expected = new OpenMapRealMatrix(expectedData);
        
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                Assert.assertEquals(expected.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testGetEntry() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(data);
        
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
        Assert.assertEquals(9.0, matrix.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(2, 2, 9.0);
        
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
        Assert.assertEquals(9.0, matrix.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testAddToEntry() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(data);
        
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, 1.0);
        matrix.addToEntry(2, 2, 1.0);
        
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(6.0, matrix.getEntry(1, 1), 0.0);
        Assert.assertEquals(10.0, matrix.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(data);
        
        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(1, 1, 2.0);
        matrix.multiplyEntry(2, 2, 2.0);
        
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(10.0, matrix.getEntry(1, 1), 0.0);
        Assert.assertEquals(18.0, matrix.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        
        Assert.assertEquals(0, matrix.computeKey(0, 0));
        Assert.assertEquals(1, matrix.computeKey(0, 1));
        Assert.assertEquals(2, matrix.computeKey(0, 2));
        Assert.assertEquals(3, matrix.computeKey(1, 0));
        Assert.assertEquals(4, matrix.computeKey(1, 1));
        Assert.assertEquals(5, matrix.computeKey(1, 2));
        Assert.assertEquals(6, matrix.computeKey(2, 0));
        Assert.assertEquals(7, matrix.computeKey(2, 1));
        Assert.assertEquals(8, matrix.computeKey(2, 2));
    }
}