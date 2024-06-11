
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Assert;

public class MultivariateNormalDistributionTest {

    // Test objects
    double[] means = {1.0, 2.0, 3.0};
    double[][] covariances = {{1.0, 0.0, 0.0},
                              {0.0, 1.0, 0.0},
                              {0.0, 0.0, 1.0}};
    MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

    // Test for getMeans()
    @Test
    public void testGetMeans() {
        double[] means = distribution.getMeans();
        Assert.assertArrayEquals(means, new double[]{1.0, 2.0, 3.0}, 0.0001);
    }

    // Test for getCovariances()
    @Test
    public void testGetCovariances() {
        double[][] covariances = distribution.getCovariances().getData();
        double[][] expected = {{1.0, 0.0, 0.0},
                               {0.0, 1.0, 0.0},
                               {0.0, 0.0, 1.0}};
        Assert.assertArrayEquals(covariances, expected);
    }

    // Test for density()
    @Test
    public void testDensity() {
        double[] vals = {1.0, 2.0, 3.0};
        double density = distribution.density(vals);
        Assert.assertEquals(density, 0.06349363593424097, 0.0001);
    }

    // Test for getStandardDeviations()
    @Test
    public void testGetStandardDeviations() {
        double[] std = distribution.getStandardDeviations();
        Assert.assertArrayEquals(std, new double[]{1.0, 1.0, 1.0}, 0.0001);
    }

    // Test for sample()
    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertEquals(sample.length, 3);
    }
}
