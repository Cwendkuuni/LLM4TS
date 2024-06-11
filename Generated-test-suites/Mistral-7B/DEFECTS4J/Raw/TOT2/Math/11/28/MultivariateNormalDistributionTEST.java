import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.AbstractMultivariateRealDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionCreation() {
        // Test creating a multivariate normal distribution with a given mean vector and covariance matrix
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = null;
        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
            Assert.assertNotNull(mvnd);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
        Assert.assertEquals(means.length, mvnd.getDimension());
        Assert.assertArrayEquals(means, mvnd.getMeans());
        RealMatrix covarianceMatrix = mvnd.getCovariances();
        Assert.assertEquals(means.length, covarianceMatrix.getRowDimension());
        Assert.assertEquals(means.length, covarianceMatrix.getColumnDimension());
        Assert.assertEquals(covariances[0].length, covarianceMatrix.getData()[0].length);
        Assert.assertEquals(covariances[1].length, covarianceMatrix.getData()[1].length);
        for (int i = 0; i < means.length; i++) {
            for (int j = 0; j < means.length; j++) {
                Assert.assertEquals(covariances[i][j], covarianceMatrix.getData()[i][j], 0.001);
            }
        }
    }

    @Test
    public void testMultivariateNormalDistributionMean() {
        // Test getting the mean vector
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, new double[means.length][means.length]);
        double[] actualMeans = mvnd.getMeans();
        Assert.assertArrayEquals(means, actualMeans);
    }

    @Test
    public void testMultivariateNormalDistributionCovarianceMatrix() {
        // Test getting the covariance matrix
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        RealMatrix actualCovarianceMatrix = mvnd.getCovariances();
        Assert.assertArrayEquals(covariances, actualCovarianceMatrix.getData(), 0.001);
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        // Test sampling from the distribution
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        RandomGenerator rng = new Well19937c();
        double[] actualSample = mvnd.sample();
        double[] expectedSample = new double[means.length];
        for (int i = 0; i < means.length; i++) {
            expectedSample[i] = mvnd.density(new double[]{i + 0.5}) / mvnd.density(new double[]{means[i]});
            expectedSample[i] *= FastMath.sqrt(FastMath.abs(covariances[i][i]));
            Assert.assertEquals(means[i], expectedSample[i], 0.01);
        }
        Assert.assertArrayEquals(expectedSample, actualSample, 0.01);
    }

    @Test
    public void testMultivariateNormalDistributionStandardDeviations() {
        // Test getting the standard deviations
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        double[] actualStandardDeviations = mvnd.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{FastMath.sqrt(covariances[0][0]), FastMath.sqrt(covariances[1][1])}, actualStandardDeviations, 0.01);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionSampleWithWrongDimension() {
        // Test sampling with a wrong dimension
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        mvnd.sample();
    }
}