import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;

public class MultivariateNormalDistributionTest {

  @Test
  public void testConstructor() throws DimensionMismatchException,
                                          NonPositiveDefiniteMatrixException {
    // Test case 1: Test valid inputs
    double[] means = new double[2]; 
    double[][] covariances = new double[2][2];
    MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

    // Test case 2: Test invalid dimension of means
    double[] means2 = new double[3]; 
    double[][] covariances2 = new double[2][2];
    try {
      MultivariateNormalDistribution mvn2 = new MultivariateNormalDistribution(means2, covariances2);
      fail("Expected a DimensionMismatchException to be thrown");
    } catch(DimensionMismatchException e) {
      // Exception thrown as expected
    }

    // Test case 3: Test invalid dimension of covariances
    double[] means3 = new double[2]; 
    double[][] covariances3 = new double[2][3];
    try {
      MultivariateNormalDistribution mvn3 = new MultivariateNormalDistribution(means3, covariances3);
      fail("Expected a DimensionMismatchException to be thrown");
    } catch(DimensionMismatchException e) {
      // Exception thrown as expected
    }

    // Test case 4: Test negative eigenvalues
    double[] means4 = new double[2]; 
    double[][] covariances4 = {{1, 2}, {2, -1}};
    try {
      MultivariateNormalDistribution mvn4 = new MultivariateNormalDistribution(means4, covariances4);
      fail("Expected a NonPositiveDefiniteMatrixException to be thrown");
    } catch(NonPositiveDefiniteMatrixException e) {
      // Exception thrown as expected
    }
  }

  @Test
  public void testDensity() throws DimensionMismatchException {
    double[] means = new double[2]; 
    double[][] covariances = new double[2][2];
    MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

    // Test case 1: Test valid inputs
    double[] vals = new double[2];
    double density = mvn.density(vals);
    // TODO: Add assertions

    // Test case 2: Test invalid dimension of vals
    double[] vals2 = new double[3];
    try {
      double density2 = mvn.density(vals2);
      fail("Expected a DimensionMismatchException to be thrown");
    } catch(DimensionMismatchException e) {
      // Exception thrown as expected
    }
  }

  @Test
  public void testGetStandardDeviations() {
    double[] means = new double[2]; 
    double[][] covariances = new double[2][2];
    MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

    // Test case 1: Test valid inputs
    double[] std = mvn.getStandardDeviations();
    // TODO: Add assertions
  }

  @Test
  public void testSample() {
    double[] means = new double[2]; 
    double[][] covariances = new double[2][2];
    MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

    // Test case 1: Test valid inputs
    double[] sample = mvn.sample();
    // TODO: Add assertions
  }
}