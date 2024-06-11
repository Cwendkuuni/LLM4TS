import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
  
  private static final double EPSILON = 1e-10;
  private MultivariateNormalDistribution distribution;
  
  @Before
  public void setup() {
    double[] means = {0.0, 0.0};
    double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
      fail("Failed to initialize distribution");
    }
  }
  
  @Test
  public void testGetMeans() {
    double[] expectedMeans = {0.0, 0.0};
    double[] means = distribution.getMeans();
    Assert.assertArrayEquals(expectedMeans, means, EPSILON);
  }
  
  @Test
  public void testGetCovariances() {
    double[][] expectedCovariances = {{1.0, 0.0}, {0.0, 1.0}};
    double[][] covariances = distribution.getCovariances().getData();
    Assert.assertArrayEquals(expectedCovariances, covariances);
  }
  
  @Test
  public void testDensity() {
    double[] values = {0.0, 0.0};
    double density = distribution.density(values);
    assertEquals(0.1591549431, density, EPSILON);
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testDensityInvalidDimension() {
    double[] values = {0.0};
    distribution.density(values);
  }
  
  @Test
  public void testGetStandardDeviations() {
    double[] expectedStandardDeviations = {1.0, 1.0};
    double[] standardDeviations = distribution.getStandardDeviations();
    Assert.assertArrayEquals(expectedStandardDeviations, standardDeviations, EPSILON);
  }
  
  @Test
  public void testSample() {
    double[] sample = distribution.sample();
    assertEquals(2, sample.length);
  }
  
  @Test
  public void testSampleLargeDimension() {
    double[] means = new double[1000];
    double[][] covariances = new double[1000][1000];
    for (int i = 0; i < 1000; i++) {
      means[i] = i;
      covariances[i][i] = 1.0;
    }
    
    try {
      MultivariateNormalDistribution largeDimensionDistribution = new MultivariateNormalDistribution(means, covariances);
      double[] sample = largeDimensionDistribution.sample();
      assertEquals(1000, sample.length);
    } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
      fail("Failed to initialize large dimension distribution");
    }
  }
  
  @Test
  public void testGetDimension() {
    int dimension = distribution.getDimension();
    assertEquals(2, dimension);
  }
  
  @Test
  public void testToString() {
    String expectedString = "MultivariateNormalDistribution"
        + "(means=[0.0, 0.0], covarianceMatrix=[[1.0, 0.0], [0.0, 1.0]])";
    String distributionString = distribution.toString();
    assertEquals(expectedString, distributionString);
  }
  
  @Test
  public void testEqualsAndHashCode() {
    MultivariateNormalDistribution otherDistribution = distribution;
    assertTrue(distribution.equals(otherDistribution));
    assertTrue(distribution.hashCode() == otherDistribution.hashCode());
    
    double[] otherMeans = {0.0, 0.0};
    double[][] otherCovariances = {{1.0, 0.0}, {0.0, 1.0}};
    MultivariateNormalDistribution differentDistribution;
    try {
      differentDistribution = new MultivariateNormalDistribution(otherMeans, otherCovariances);
      assertFalse(distribution.equals(differentDistribution));
      assertFalse(distribution.hashCode() == differentDistribution.hashCode());
    } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
      fail("Failed to initialize different distribution");
    }
  }
  
}