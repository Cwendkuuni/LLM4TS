import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.MathException;

public class PearsonsCorrelationTest {
  
  private PearsonsCorrelation pearsonsCorrelation;
  
  @Before
  public void setUp() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = new BlockRealMatrix(data);
    pearsonsCorrelation = new PearsonsCorrelation(matrix);
  }
  
  @Test
  public void testGetCorrelationMatrix() {
    RealMatrix result = pearsonsCorrelation.getCorrelationMatrix();
    assertNotNull(result);
    double[][] expectedData = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    RealMatrix expected = new BlockRealMatrix(expectedData);
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test
  public void testGetCorrelationStandardErrors() {
    RealMatrix result = pearsonsCorrelation.getCorrelationStandardErrors();
    assertNotNull(result);
    double[][] expectedData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    RealMatrix expected = new BlockRealMatrix(expectedData);
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test
  public void testGetCorrelationPValues() throws MathException {
    RealMatrix result = pearsonsCorrelation.getCorrelationPValues();
    assertNotNull(result);
    double[][] expectedData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    RealMatrix expected = new BlockRealMatrix(expectedData);
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test
  public void testComputeCorrelationMatrix() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = new BlockRealMatrix(data);
    RealMatrix result = pearsonsCorrelation.computeCorrelationMatrix(matrix);
    assertNotNull(result);
    double[][] expectedData = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    RealMatrix expected = new BlockRealMatrix(expectedData);
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test
  public void testComputeCorrelationMatrixWithInvalidData() {
    double[][] data = {{1, 2}, {3, 4}};
    RealMatrix matrix = new BlockRealMatrix(data);
    try {
      pearsonsCorrelation.computeCorrelationMatrix(matrix);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testComputeCorrelationMatrixWithEmptyData() {
    double[][] data = {};
    RealMatrix matrix = new BlockRealMatrix(data);
    try {
      pearsonsCorrelation.computeCorrelationMatrix(matrix);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testCorrelation() {
    double[] xArray = {1, 2, 3};
    double[] yArray = {4, 5, 6};
    double result = pearsonsCorrelation.correlation(xArray, yArray);
    assertEquals(1.0, result, 0.0);
  }
  
  @Test
  public void testCorrelationWithInvalidData() {
    double[] xArray = {1, 2};
    double[] yArray = {3, 4, 5};
    try {
      pearsonsCorrelation.correlation(xArray, yArray);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testCorrelationWithInsufficientData() {
    double[] xArray = {1};
    double[] yArray = {2};
    try {
      pearsonsCorrelation.correlation(xArray, yArray);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testCovarianceToCorrelation() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix covarianceMatrix = new BlockRealMatrix(data);
    RealMatrix result = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
    assertNotNull(result);
    double[][] expectedData = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    RealMatrix expected = new BlockRealMatrix(expectedData);
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test
  public void testCheckSufficientData() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = new BlockRealMatrix(data);
    try {
      pearsonsCorrelation.checkSufficientData(matrix);
      assertTrue(true);
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not be thrown");
    }
  }
  
  @Test
  public void testCheckSufficientDataWithInvalidData() {
    double[][] data = {{1, 2}, {3, 4}};
    RealMatrix matrix = new BlockRealMatrix(data);
    try {
      pearsonsCorrelation.checkSufficientData(matrix);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testCheckSufficientDataWithInsufficientDataColumns() {
    double[][] data = {{1, 2}, {3, 4}, {5, 6}};
    RealMatrix matrix = new BlockRealMatrix(data);
    try {
      pearsonsCorrelation.checkSufficientData(matrix);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testCheckSufficientDataWithInsufficientDataRows() {
    double[][] data = {{1, 2, 3}};
    RealMatrix matrix = new BlockRealMatrix(data);
    try {
      pearsonsCorrelation.checkSufficientData(matrix);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
}