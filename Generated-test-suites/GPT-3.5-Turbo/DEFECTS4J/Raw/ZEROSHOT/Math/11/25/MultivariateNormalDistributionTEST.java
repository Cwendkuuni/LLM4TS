import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

  // Test variables
  private final double[] means = {1, 2, 3};
  private final double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
  private final double[] vals = {1, 1, 1};
  private final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

  @Test
  public void testGetMeans() {
    Assert.assertArrayEquals(means, distribution.getMeans(), 0);
  }

  @Test
  public void testGetCovariances() {
    double[][] expected = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    Assert.assertArrayEquals(expected, distribution.getCovariances().getData());
  }

  @Test
  public void testDensity() {
    double expected = 0.06349363593424097;
    Assert.assertEquals(expected, distribution.density(vals), 0);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testInvalidDensity() {
    double[] invalidVals = {1, 1};
    distribution.density(invalidVals);
  }

  @Test
  public void testGetStandardDeviations() {
    double[] expected = {1, 1, 1};
    Assert.assertArrayEquals(expected, distribution.getStandardDeviations(), 0);
  }

  @Test
  public void testSample() {
    double[] sample = distribution.sample();
    Assert.assertEquals(means.length, sample.length);
  }

  @Test
  public void testGetExponentTerm() {
    double[] centered = MathArrays.ebeSubtract(vals, means);
    double[] preMultiplied = distribution.getCovariances().inverse().preMultiply(centered);
    double sum = 0;
    for (int i = 0; i < preMultiplied.length; i++) {
      sum += preMultiplied[i] * centered[i];
    }
    double expected = Math.exp(-0.5 * sum);
    double result = distribution.getExponentTerm(vals);
    Assert.assertEquals(expected, result, 0);
  }
}