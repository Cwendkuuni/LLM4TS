import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

  @Test
  public void testGetMeans() {
    double[] means = {1, 2, 3};
    double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    assertArrayEquals(means, distribution.getMeans(), 0);
  }

  @Test
  public void testGetCovariances() {
    double[] means = {1, 2, 3};
    double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    assertArrayEquals(covariances, distribution.getCovariances().getData());
  }

  @Test
  public void testDensity() {
    double[] means = {1, 2};
    double[][] covariances = {{1, 0}, {0, 2}};
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    double[] vals = {1, 2};
    double expected = 0.09653235263005393;
    assertEquals(expected, distribution.density(vals), 1e-10);
  }

  @Test
  public void testGetStandardDeviations() {
    double[] means = {1, 2};
    double[][] covariances = {{1, 0}, {0, 2}};
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    double[] expected = {1, Math.sqrt(2)};
    assertArrayEquals(expected, distribution.getStandardDeviations(), 1e-10);
  }

  @Test
  public void testSample() {
    double[] means = {1, 2};
    double[][] covariances = {{1, 0}, {0, 2}};
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

    double[] sample = distribution.sample();
    double[] expectedMeans = {1, 2};
    double[] expectedStandardDeviations = {1, Math.sqrt(2)};
    assertArrayEquals(expectedMeans, distribution.getMeans(), 1e-10);
    assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 1e-10);
  }

  @Test
  public void testGetExponentTerm() {
    double[] means = {1, 2};
    double[][] covariances = {{1, 0}, {0, 2}};
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    double[] vals = {1, 2};
    double expected = 0.1353352832366127;
    assertEquals(expected, distribution.getExponentTerm(vals), 1e-10);
  }

}