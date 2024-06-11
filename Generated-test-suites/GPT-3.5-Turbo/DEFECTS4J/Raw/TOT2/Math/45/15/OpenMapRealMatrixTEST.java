import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

  @Test
  public void testCopy() {
    OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
    matrix.setEntry(1, 1, 5.0);
    
    OpenMapRealMatrix copiedMatrix = matrix.copy();
    
    assertEquals(matrix.getEntry(1, 1), copiedMatrix.getEntry(1, 1), 0.0);
    assertNotSame(matrix, copiedMatrix);
  }
  
  @Test
  public void testAdd() {
    OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
    OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
    
    matrix1.setEntry(0, 0, 2.0);
    matrix2.setEntry(1, 1, 3.0);
    
    OpenMapRealMatrix result = matrix1.add(matrix2);
    
    assertEquals(2.0, result.getEntry(0, 0), 0.0);
    assertEquals(3.0, result.getEntry(1, 1), 0.0);
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testAddDimensionMismatch() {
    OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
    OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
    
    matrix1.add(matrix2);
  }
  
  @Test
  public void testSubtract() {
    OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
    OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
    
    matrix1.setEntry(0, 0, 5.0);
    matrix2.setEntry(1, 1, 3.0);
    
    OpenMapRealMatrix result = matrix1.subtract(matrix2);
    
    assertEquals(5.0, result.getEntry(0, 0), 0.0);
    assertEquals(-3.0, result.getEntry(1, 1), 0.0);
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testSubtractDimensionMismatch() {
    OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
    OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
    
    matrix1.subtract(matrix2);
  }

  // Add more test cases for other methods as required
  
}