import org.junit.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
  
  private double[][] data;
  private MultivariateNormalMixtureExpectationMaximization em;
  
  @Before
  public void setup() {
    data = new double[][]{{1, 2}, {3, 4}, {5, 6}};
    em = new MultivariateNormalMixtureExpectationMaximization(data);
  }
  
  @Test
  public void testConstructor() {
    MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    assertNotNull(em);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithEmptyData() {
    double[][] emptyData = new double[][]{};
    MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(emptyData);
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testConstructorWithDifferentLengthArrays() {
    double[][] invalidData = new double[][]{{1, 2}, {3, 4, 5}, {6, 7}};
    MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithSmallArrayLength() {
    double[][] invalidData = new double[][]{{1, 2}};
    MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
  }
  
  @Test
  public void testFit() {
    MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(null);
    em.fit(initialMixture, 100, 1e-3);
    assertNotNull(em.getFittedModel());
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testFitWithInvalidMaxIterations() {
    MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(null);
    em.fit(initialMixture, 0, 1e-3);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testFitWithInvalidThreshold() {
    MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(null);
    em.fit(initialMixture, 100, 0);
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testFitWithDifferentNumCols() {
    double[][] invalidData = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(null);
    em.fit(initialMixture, 100, 1e-3);
  }
  
  @Test(expected = ConvergenceException.class)
  public void testFitWithNoConvergence() {
    double[][] invalidData = new double[][]{{1, 1}, {2, 2}, {3, 3}};
    MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(null);
    em.fit(initialMixture, 1, 1e-3);
  }
  
  @Test
  public void testFitWithDefaultParameters() {
    MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(null);
    em.fit(initialMixture);
    assertNotNull(em.getFittedModel());
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testEstimateWithSmallNumRows() {
    double[][] invalidData = new double[][]{{1, 1},};
    MultivariateNormalMixtureExpectationMaximization.estimate(invalidData, 2);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testEstimateWithSmallNumComponents() {
    MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testEstimateWithLargeNumComponents() {
    MultivariateNormalMixtureExpectationMaximization.estimate(data, 10);
  }
  
  @Test
  public void testGetLogLikelihood() {
    assertEquals(em.getLogLikelihood(), 0.0, 0.0);
  }
  
  @Test
  public void testGetFittedModel() {
    MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
    assertNotNull(fittedModel);
  }
  
  @Test
  public void testDataRowConstructor() {
    double[] rowData = new double[]{1, 2, 3};
    MultivariateNormalMixtureExpectationMaximization.DataRow row = new MultivariateNormalMixtureExpectationMaximization.DataRow(rowData);
    assertNotNull(row);
    assertArrayEquals(rowData, row.getRow(), 0.0);
    assertNotNull(row.mean);
  }
  
  @Test
  public void testDataRowEquals() {
    double[] rowData1 = new double[]{1, 2, 3};
    double[] rowData2 = new double[]{1, 2, 3};
    MultivariateNormalMixtureExpectationMaximization.DataRow row1 = new MultivariateNormalMixtureExpectationMaximization.DataRow(rowData1);
    MultivariateNormalMixtureExpectationMaximization.DataRow row2 = new MultivariateNormalMixtureExpectationMaximization.DataRow(rowData2);
    assertTrue(row1.equals(row1));
    assertTrue(row1.equals(row2));
    assertTrue(row2.equals(row1));
  }
  
  @Test
  public void testDataRowHashCode() {
    double[] rowData = new double[]{1, 2, 3};
    MultivariateNormalMixtureExpectationMaximization.DataRow row = new MultivariateNormalMixtureExpectationMaximization.DataRow(rowData);
    assertNotNull(row);
    assertEquals(row.hashCode(), rowData.hashCode());
  }
}