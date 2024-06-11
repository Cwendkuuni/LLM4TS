import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

  @Test
  public void testConstructor_noData() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    assertEquals(0, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
    assertEquals(0, pearsonsCorrelation.getCorrelationMatrix().getColumnDimension());
  }

  @Test
  public void testConstructor_withData() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getColumnDimension());
  }

  @Test
  public void testConstructor_withMatrix() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}};
    RealMatrix matrix = new BlockRealMatrix(data);
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getColumnDimension());
  }

  @Test
  public void testConstructor_withCovariance() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}};
    Covariance covariance = new Covariance(new BlockRealMatrix(data));
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getColumnDimension());
  }

  @Test
  public void testConstructor_withCovarianceMatrix() {
    double[][] covarianceData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
    assertEquals(3, pearsonsCorrelation.getCorrelationMatrix().getColumnDimension());
  }

  @Test
  public void testComputeCorrelationMatrix() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}};
    RealMatrix matrix = new BlockRealMatrix(data);
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
    assertEquals(3, correlationMatrix.getRowDimension());
    assertEquals(3, correlationMatrix.getColumnDimension());
  }

  @Test
  public void testComputeCorrelationMatrix_withData() {
    double[][] data = {{1, 2, 3}, {4, 5, 6}};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);
    assertEquals(3, correlationMatrix.getRowDimension());
    assertEquals(3, correlationMatrix.getColumnDimension());
  }

  @Test
  public void testCorrelation() {
    double[] xArray = {1, 2, 3};
    double[] yArray = {4, 5, 6};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    double correlation = pearsonsCorrelation.correlation(xArray, yArray);
    assertEquals(1, correlation, 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCorrelation_invalidArrayLength() {
    double[] xArray = {1, 2};
    double[] yArray = {4, 5, 6};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    pearsonsCorrelation.correlation(xArray, yArray);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCorrelation_insufficientData() {
    double[] xArray = {1};
    double[] yArray = {4};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    pearsonsCorrelation.correlation(xArray, yArray);
  }

  @Test
  public void testCovarianceToCorrelation() {
    double[][] covarianceData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
    assertEquals(1, correlationMatrix.getEntry(0, 0), 0.001);
    assertEquals(1, correlationMatrix.getEntry(1, 1), 0.001);
    assertEquals(1, correlationMatrix.getEntry(2, 2), 0.001);
    assertEquals(0, correlationMatrix.getEntry(0, 1), 0.001);
    assertEquals(0, correlationMatrix.getEntry(0, 2), 0.001);
    assertEquals(0, correlationMatrix.getEntry(1, 0), 0.001);
    assertEquals(0, correlationMatrix.getEntry(1, 2), 0.001);
    assertEquals(0, correlationMatrix.getEntry(2, 0), 0.001);
    assertEquals(0, correlationMatrix.getEntry(2, 1), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckSufficientData_insufficientRows() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    RealMatrix matrix = new BlockRealMatrix(1, 2);
    pearsonsCorrelation.checkSufficientData(matrix);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckSufficientData_insufficientColumns() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    RealMatrix matrix = new BlockRealMatrix(2, 1);
    pearsonsCorrelation.checkSufficientData(matrix);
  }
}