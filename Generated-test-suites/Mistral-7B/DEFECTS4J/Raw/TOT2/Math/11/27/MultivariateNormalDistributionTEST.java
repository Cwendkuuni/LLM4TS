import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributiontest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructorWithVectorAndMatrix() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        Assert.assertEquals(means.length, dist.getDimension());
        Assert.assertArrayEquals(means, dist.getMeans());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentLengths() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentRowsAndColumns() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularCovarianceMatrix() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{0.0, 0.5}, {0.5, 0.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeEigenvalues() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = new double[]{1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        Assert.assertArrayEquals(means, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covarianceMatrix = dist.getCovariances();
        Assert.assertEquals(covariances.length, covarianceMatrix.getRowDimension());
        Assert.assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        Assert.assertArrayEquals(covariances, covarianceMatrix.getData());
    }

    @Test
    public void testDensity() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = new double[]{2.5, 3.5};
        double density = dist.density(values);
        double expectedDensity = Math.exp(-1.0) * Math.pow(2.0 * Math.PI, -2.0) / (2.0 * Math.sqrt(Math.PI) * Math.sqrt(2.0)) *
               Math.exp(-((values[0] - means[0]) * dist.covarianceMatrixInverse.getEntry(0, 0) * (values[0] - means[0]) -
                (values[1] - means[1]) * dist.covarianceMatrixInverse.getEntry(1, 1) * (values[1] - means[1]) +
                (values[0] - means[0]) * dist.covarianceMatrixInverse.getEntry(0, 1) * (values[1] - means[1])));
        Assert.assertEquals(expectedDensity, density, 1e-10);
    }

    @Test
    public void testSample() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            Assert.assertNotNull(sample[i]);
        }
        Assert.assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(sample[i] >= -Double.MAX_VALUE && sample[i] <= Double.MAX_VALUE);
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = dist.getStandardDeviations();
        Assert.assertEquals(means.length, standardDeviations.length);
        for (int i = 0; i < standardDeviations.length; i++) {
            Assert.assertTrue(standardDeviations[i] > 0);
        }
    }
}