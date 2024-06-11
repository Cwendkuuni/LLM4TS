import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultivariateNormalDistributionTest {

    // Test the constructor with means and covariances
    @Test
    public void testConstructorWithMeansAndCovariances() {
        double[] means = {1, 2};
        double[][] covariances = {{3, 2}, {2, 4}};
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }

        Assert.assertArrayEquals(means, distribution.getMeans(), 1e-10);
        Assert.assertArrayEquals(covariances, distribution.getCovariances().getData(), 1e-10);
    }

    // Test the constructor with means and covariances (when dimensions are inconsistent)
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMeansAndCovariances_InconsistentDimensions() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1, 2};
        double[][] covariances = {{3, 2}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with means and covariances (when eigenvalue decomposition cannot be performed)
    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithMeansAndCovariances_EigenvalueDecompositionError() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1, 2};
        double[][] covariances = {{3, 3}, {6, 6}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with means and covariances (when eigenvalue is negative)
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithMeansAndCovariances_NegativeEigenvalue() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1, 2};
        double[][] covariances = {{-3, 2}, {2, 4}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    // Test the constructor with random number generator, means and covariances
    @Test
    public void testConstructorWithRNGMeansAndCovariances() {
        double[] means = {1, 2};
        double[][] covariances = {{3, 2}, {2, 4}};
        RandomDataGenerator rng = new RandomDataGenerator();
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }

        Assert.assertArrayEquals(means, distribution.getMeans(), 1e-10);
        Assert.assertArrayEquals(covariances, distribution.getCovariances().getData(), 1e-10);
    }

    // Test the constructor with random number generator, means and covariances (when dimensions are inconsistent)
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithRNGMeansAndCovariances_InconsistentDimensions() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1, 2};
        double[][] covariances = {{3, 2}};
        RandomDataGenerator rng = new RandomDataGenerator();
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }

    // Test the constructor with random number generator, means and covariances (when eigenvalue decomposition cannot be performed)
    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithRNGMeansAndCovariances_EigenvalueDecompositionError() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1, 2};
        double[][] covariances = {{3, 3}, {6, 6}};
        RandomDataGenerator rng = new RandomDataGenerator();
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }
  
  // Add more test cases for other methods

}