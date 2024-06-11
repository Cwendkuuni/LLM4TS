import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] actualMeans = distribution.getMeans();

        Assert.assertArrayEquals(means, actualMeans, EPSILON);
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
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1.0, 1.0};

        double expectedDensity = 0.05854983152431917;
        double actualDensity = distribution.density(values);
        
        Assert.assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] expectedStandardDeviations = {1.0, 1.0, 1.0};
        double[] actualStandardDeviations = distribution.getStandardDeviations();

        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, EPSILON);
    }

    @Test
    public void testSample() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();

        Assert.assertNotNull(sample);
        Assert.assertEquals(2, sample.length);
    }
}