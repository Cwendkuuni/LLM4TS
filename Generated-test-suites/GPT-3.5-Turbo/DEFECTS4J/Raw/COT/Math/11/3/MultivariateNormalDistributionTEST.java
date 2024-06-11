import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

  // Create instance of the test class
  MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[]{0, 0}, new double[][]{{1, 0}, {0, 1}});

  // Test getMeans method
  @Test
  public void testGetMeans() {
    double[] expected = {0, 0};
    assertArrayEquals(expected, dist.getMeans(), 0);
  }

  // Test getCovariances method
  @Test
  public void testGetCovariances() {
    double[][] expected = {{1, 0}, {0, 1}};
    assertArrayEquals(expected, dist.getCovariances().getData(), 0);
  }

  // Test density method
  @Test
  public void testDensity() {
    double[] vals = {0, 0};
    double expected = 0.15915494309189535;
    assertEquals(expected, dist.density(vals), 0);
  }

  // Test getStandardDeviations method
  @Test
  public void testGetStandardDeviations() {
    double[] expected = {1, 1};
    assertArrayEquals(expected, dist.getStandardDeviations(), 0);
  }

  // Test sample method
  @Test
  public void testSample() {
    double[] sample = dist.sample();
    assertEquals(2, sample.length);
  }

  // Test getExponentTerm method
  @Test
  public void testGetExponentTerm() {
    double[] values = {0, 0};
    double expected = 1.0;
    assertEquals(expected, dist.getExponentTerm(values), 0);
  }

  // Additional test cases for edge cases and exception handling

  // Test constructor with invalid dimension
  @Test(expected = DimensionMismatchException.class)
  public void testConstructorInvalidDimension() {
    double[] means = {0, 0};
    double[][] covariances = {{1, 0}, {0, 1}, {0, 0}};
    new MultivariateNormalDistribution(means, covariances);
  }

  // Test density method with invalid dimension
  @Test(expected = DimensionMismatchException.class)
  public void testDensityInvalidDimension() {
    double[] vals = {0, 0, 0};
    dist.density(vals);
  }

  // Test sample method with invalid dimension
  @Test(expected = DimensionMismatchException.class)
  public void testSampleInvalidDimension() {
    double[] means = {0, 0};
    double[][] covariances = {{1, 0}, {0, 1}, {0, 0}};
    MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    dist.sample();
  }

  // Test constructor with singular matrix
  @Test(expected = SinglaurMatrixException.class)
  public void testConstructorSingularMatrix() {
    double[] means = {0, 0};
    double[][] covariances = {{1, 1}, {1, 1}};
    new MultivariateNormalDistribution(means, covariances);
  }

  // Test constructor with negative eigenvalues
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testConstructorNegativeEigenvalues() {
    double[] means = {0, 0};
    double[][] covariances = {{1, 0}, {0, -1}};
    new MultivariateNormalDistribution(means, covariances);
  }

  // Test density method with NaN input
  @Test
  public void testDensityNaNInput() {
    double[] vals = {Double.NaN, Double.NaN};
    double expected = 0.0;
    assertEquals(expected, dist.density(vals), 0);
  }

  // Test sample method multiple times
  @Test
  public void testSampleMultipleTimes() {
    for (int i = 0; i < 100; i++) {
      double[] sample = dist.sample();
      assertEquals(2, sample.length);
    }
  }

}