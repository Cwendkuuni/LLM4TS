import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

  // Test case for constructor with no data
  @Test
  public void testConstructorNoData() {
    PearsonsCorrelation pc = new PearsonsCorrelation();
    assertNull(pc.getCorrelationMatrix());
    assertEquals(0, pc.getCorrelationStandardErrors().getRowDimension());
    assertEquals(0, pc.getCorrelationPValues().getRowDimension());
  }

  // Test case for constructor with 2x2 data array
  @Test
  public void testConstructorDataArray() {
    double[][] data = {{1, 2}, {3, 4}};
    PearsonsCorrelation pc = new PearsonsCorrelation(data);
    RealMatrix cm = pc.getCorrelationMatrix();
    assertEquals(1.0, cm.getEntry(0, 0), 0.0001);
    assertEquals(-1.0, cm.getEntry(0, 1), 0.0001);
    assertEquals(-1.0, cm.getEntry(1, 0), 0.0001);
    assertEquals(1.0, cm.getEntry(1, 1), 0.0001);
  }

  // Test case for constructor with 2x2 data matrix
  @Test
  public void testConstructorDataMatrix() {
    double[][] data = {{1, 2}, {3, 4}};
    RealMatrix matrix = new BlockRealMatrix(data);
    PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
    RealMatrix cm = pc.getCorrelationMatrix();
    assertEquals(1.0, cm.getEntry(0, 0), 0.0001);
    assertEquals(-1.0, cm.getEntry(0, 1), 0.0001);
    assertEquals(-1.0, cm.getEntry(1, 0), 0.0001);
    assertEquals(1.0, cm.getEntry(1, 1), 0.0001);
  }

  // Test case for constructor with covariance
  @Test
  public void testConstructorCovariance() {
    double[][] data = {{1, 2}, {3, 4}};
    PearsonsCorrelation pc = new PearsonsCorrelation(new Covariance(new BlockRealMatrix(data)));
    RealMatrix cm = pc.getCorrelationMatrix();
    assertEquals(1.0, cm.getEntry(0, 0), 0.0001);
    assertEquals(-1.0, cm.getEntry(0, 1), 0.0001);
    assertEquals(-1.0, cm.getEntry(1, 0), 0.0001);
    assertEquals(1.0, cm.getEntry(1, 1), 0.0001);
  }

  // Test case for constructor with covariance matrix
  @Test
  public void testConstructorCovarianceMatrix() {
    double[][] covarianceData = {{2, -1}, {-1, 2}};
    RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
    PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 2);
    RealMatrix cm = pc.getCorrelationMatrix();
    assertEquals(1.0, cm.getEntry(0, 0), 0.0001);
    assertEquals(-1.0, cm.getEntry(0, 1), 0.0001);
    assertEquals(-1.0, cm.getEntry(1, 0), 0.0001);
    assertEquals(1.0, cm.getEntry(1, 1), 0.0001);
  }

  // Test case for computeCorrelationMatrix with 2x2 data array
  @Test
  public void testComputeCorrelationMatrixDataArray() {
    double[][] data = {{1, 2}, {3, 4}};
    PearsonsCorrelation pc = new PearsonsCorrelation();
    RealMatrix cm = pc.computeCorrelationMatrix(data);
    assertEquals(1.0, cm.getEntry(0, 0), 0.0001);
    assertEquals(-1.0, cm.getEntry(0, 1), 0.0001);
    assertEquals(-1.0, cm.getEntry(1, 0), 0.0001);
    assertEquals(1.0, cm.getEntry(1, 1), 0.0001);
  }

  // Test case for computePearsonCorrelation with 2x2 data arrays
  @Test
  public void testComputePearsonCorrelation() {
    PearsonsCorrelation pc = new PearsonsCorrelation();
    double[] xArray = {1, 2, 3};
    double[] yArray = {2, 3, 4};
    double result = pc.correlation(xArray, yArray);
    assertEquals(1.0, result, 0.0001);
  }

}