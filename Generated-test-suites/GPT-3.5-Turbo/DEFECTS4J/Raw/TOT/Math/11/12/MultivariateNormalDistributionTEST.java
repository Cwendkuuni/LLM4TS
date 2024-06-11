import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

  @Test
  public void testConstructorWithValidValues() {
    // Typical use case
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
    };

    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      Assert.fail("Unexpected exception thrown");
    }

    Assert.assertNotNull(distribution);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testConstructorWithInvalidValues() {
    // Edge case: Dimension mismatch between means and covariance matrix
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0},
      {0.0, 1.0},
      {0.0, 0.0}
    };

    new MultivariateNormalDistribution(means, covariances);
  }

  @Test(expected = SingularMatrixException.class)
  public void testConstructorWithSingularMatrix() {
    // Edge case: Singular covariance matrix
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 1.0, 1.0},
      {1.0, 1.0, 1.0},
      {1.0, 1.0, 1.0}
    };

    new MultivariateNormalDistribution(means, covariances);
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testConstructorWithNonPositiveDefiniteMatrix() {
    // Edge case: Non-positive definite covariance matrix
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {2.0, 0.0, 0.0},
      {0.0, -1.0, 0.0},
      {0.0, 0.0, 3.0}
    };

    new MultivariateNormalDistribution(means, covariances);
  }

  @Test
  public void testDensity() {
    // Typical use case
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
    };
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      Assert.fail("Unexpected exception thrown");
    }

    double[] values = {1.0, 2.0, 3.0};
    double density = distribution.density(values);

    Assert.assertEquals(0.06349363593424098, density, 0.000001);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testDensityWithInvalidValues() {
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
    };
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      Assert.fail("Unexpected exception thrown");
    }

    double[] values = {1.0, 2.0};
    distribution.density(values);
  }

  @Test
  public void testGetMeans() {
    // Typical use case
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
    };
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      Assert.fail("Unexpected exception thrown");
    }

    double[] distributionMeans = distribution.getMeans();

    Assert.assertArrayEquals(means, distributionMeans, 0.0);
  }

  @Test
  public void testGetCovariances() {
    // Typical use case
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
    };
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      Assert.fail("Unexpected exception thrown");
    }

    double[][] distributionCovariances = distribution.getCovariances().getData();

    Assert.assertArrayEquals(covariances, distributionCovariances);
  }

  @Test
  public void testGetStandardDeviations() {
    // Typical use case
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
    };
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      Assert.fail("Unexpected exception thrown");
    }

    double[] standardDeviations = distribution.getStandardDeviations();

    Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, standardDeviations, 0.0);
  }

  @Test
  public void testSample() {
    // Typical use case
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
    };
    MultivariateNormalDistribution distribution = null;
    try {
      distribution = new MultivariateNormalDistribution(means, covariances);
    } catch (Exception e) {
      Assert.fail("Unexpected exception thrown");
    }

    double[] sample = distribution.sample();

    Assert.assertEquals(means[0], sample[0], 0.0);
    Assert.assertEquals(means[1], sample[1], 0.0);
    Assert.assertEquals(means[2], sample[2], 0.0);
  }
}