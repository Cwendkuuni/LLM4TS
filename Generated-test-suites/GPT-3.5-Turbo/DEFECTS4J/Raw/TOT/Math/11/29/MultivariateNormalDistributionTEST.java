import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

  // Complete test cases for each method in the class

  // Test case for parameterized constructor - means and covariances
  @Test
  public void testConstructorWithMeansAndCovariances() {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};

    // Action
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      fail("Constructor threw an exception.");
    }

    // Verification
    assertNotNull(distribution);
    assertArrayEquals(means, distribution.getMeans(), 0);
    assertArrayEquals(covariances, distribution.getCovariances().getData(), 0);
  }

  // Test case for parameterized constructor - means and covariances with invalid dimension
  @Test(expected = DimensionMismatchException.class)
  public void testConstructorWithMeansAndCovariancesInvalidDimension() throws Exception {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0}, {0, 3}, {0, 0}};

    // Action
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    // Verification
    // Exception is expected to be thrown
  }

  // Test case for parameterized constructor - means and covariances with singular matrix
  @Test(expected = SingularMatrixException.class)
  public void testConstructorWithMeansAndCovariancesSingularMatrix() throws Exception {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 0}};

    // Action
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    // Verification
    // Exception is expected to be thrown
  }

  // Test case for parameterized constructor - means and covariances with negative eigenvalue
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testConstructorWithMeansAndCovariancesNegativeEigenvalue() throws Exception {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, -4}};

    // Action
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    // Verification
    // Exception is expected to be thrown
  }

  // Test case for parameterized constructor - means, covariances and RNG
  @Test
  public void testConstructorWithMeansCovariancesAndRNG() {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
    RandomGenerator rng = new Well19937c();

    // Action
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(rng, means, covariances);
    } catch (Exception e) {
      fail("Constructor threw an exception.");
    }

    // Verification
    assertNotNull(distribution);
    assertArrayEquals(means, distribution.getMeans(), 0);
    assertArrayEquals(covariances, distribution.getCovariances().getData(), 0);
  }

  // Test case for density method
  @Test
  public void testDensity() {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
    double[] vals = {1, 2, 3};
    double expectedDensity = 0.024295942898353866;

    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      fail("Constructor threw an exception.");
    }

    // Action
    double density = distribution.density(vals);

    // Verification
    assertEquals(expectedDensity, density, 0);
  }

  // Test case for density method with invalid dimension
  @Test(expected = DimensionMismatchException.class)
  public void testDensityInvalidDimension() throws Exception {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
    double[] vals = {1, 2};

    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    // Action
    double density = distribution.density(vals);

    // Verification
    // Exception is expected to be thrown
  }

  // Test case for getStandardDeviations method
  @Test
  public void testGetStandardDeviations() {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
    double[] expectedStdDeviations = {Math.sqrt(2), Math.sqrt(3), 2};

    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      fail("Constructor threw an exception.");
    }

    // Action
    double[] stdDeviations = distribution.getStandardDeviations();

    // Verification
    assertArrayEquals(expectedStdDeviations, stdDeviations, 0);
  }

  // Test case for sample method
  @Test
  public void testSample() {
    // Setup
    double[] means = {1, 2, 3};
    double[][] covariances = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};

    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      fail("Constructor threw an exception.");
    }

    // Action
    double[] sample = distribution.sample();

    // Verification
    assertNotNull(sample);
    assertEquals(means.length, sample.length);
  }

}