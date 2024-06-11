import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] actualMeans = distribution.getMeans();

        Assert.assertArrayEquals(means, actualMeans, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[][] actualCovariances = distribution.getCovariances().getData();

        Assert.assertArrayEquals(covariances, actualCovariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1.0, 2.0, 3.0};
        double actualDensity = distribution.density(values);

        Assert.assertEquals(0.1591, actualDensity, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] expectedStandardDeviations = {1.0, 1.0, 1.0};
        double[] actualStandardDeviations = distribution.getStandardDeviations();

        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0.0001);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();

        Assert.assertEquals(3, sample.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatch() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1.0, 2.0};
        distribution.density(values);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetStandardDeviationsDimensionMismatch() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] actualStandardDeviations = distribution.getStandardDeviations();
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSampleDimensionMismatch() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
    }
}