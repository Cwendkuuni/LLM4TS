import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        // Test construction with valid input
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.5, 0.5}, {0.5, 2.5}};
        MultivariateNormalDistribution distribution = null;
        try {
            distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            Assert.fail("Failed to construct MultivariateNormalDistribution: " + e.getMessage());
        }

        // Test getters
        Assert.assertArrayEquals(means, distribution.getMeans(), 0.001);
        RealMatrix covarianceMatrix = distribution.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        Assert.assertEquals(expectedCovarianceMatrix, covarianceMatrix);

        // Test density calculation
        double[] values = new double[]{1.2, 2.4};
        double density = distribution.density(values);
        double expectedDensity = Math.exp(-1.5) * FastMath.pow(2 * FastMath.PI, -2.0) / (2 * Math.sqrt(2 * Math.PI)) / (2 * Math.sqrt(2 * Math.PI)) * FastMath.exp(-0.5 * (FastMath.pow(values[0] - means[0], 2) / 1.5 + FastMath.pow(values[1] - means[1], 2) / 2.5));
        Assert.assertEquals(expectedDensity, density, 0.001);

        // Test sample calculation
        RealArray2DSample sample = distribution.sample(1, 1);
        double[] sampleValues = sample.getColumnVector(0).toArray();
        Assert.assertArrayEquals(values, sampleValues, 0.001);

        // Test exception handling
        try {
            new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0}, covariances);
        } catch (DimensionMismatchException e) {
            Assert.assertEquals("The length of the means vector must match the number of dimensions.", e.getMessage());
        }

        try {
            new MultivariateNormalDistribution(new Well19937c(), means, null);
        } catch (DimensionMismatchException e) {
            Assert.assertEquals("The number of rows and columns in the covariance matrix must match the number of dimensions.", e.getMessage());
        }

        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            for (int i = 0; i < covariances[0].length; i++) {
                covariances[0][i] = -1.0;
            }
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("The covariance matrix cannot be negative definite.", e.getMessage());
        }

        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            for (int i = 0; i < covariances.length; i++) {
                for (int j = 0; j < covariances[i].length; j++) {
                    covariances[i][j] = 0.0;
                }
            }
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (SingularMatrixException e) {
            Assert.assertEquals("The covariance matrix is singular.", e.getMessage());
        }
    }
}