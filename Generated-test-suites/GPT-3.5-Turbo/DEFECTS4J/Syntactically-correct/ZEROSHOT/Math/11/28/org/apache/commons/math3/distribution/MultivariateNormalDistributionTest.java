import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        Assert.assertArrayEquals(means, distribution.getMeans(), 0);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        Assert.assertArrayEquals(covariances, distribution.getCovariances().getData(), 0);
    }

    @Test
    public void testDensity() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] vals = {1, 2};
        double expectedDensity = 0.15915494309189535;
        double actualDensity = distribution.density(vals);

        Assert.assertEquals(expectedDensity, actualDensity, 1e-9);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] expectedStandardDeviations = {1, 1, 1};
        double[] actualStandardDeviations = distribution.getStandardDeviations();

        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0);

    }

    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();

        Assert.assertEquals(sample.length, 3);

    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1, 2};
        double expectedExponentTerm = 1;
        double actualExponentTerm = distribution.getExponentTerm(values);

        Assert.assertEquals(expectedExponentTerm, actualExponentTerm, 1e-9);

    }
}