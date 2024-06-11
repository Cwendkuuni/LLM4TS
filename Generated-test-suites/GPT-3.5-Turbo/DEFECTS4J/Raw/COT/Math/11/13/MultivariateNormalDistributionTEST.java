
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        Assert.assertTrue(Arrays.equals(distribution.getMeans(), means));
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        Assert.assertEquals(distribution.getCovariances().getData(), covariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1.0, 2.0, 3.0};
        double density = distribution.density(vals);
        Assert.assertEquals(density, 0.06349363593424098, 1e-9);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = distribution.getStandardDeviations();
        Assert.assertTrue(Arrays.equals(stdDeviations, new double[]{1.0, 1.0, 1.0}));
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        Assert.assertEquals(sample.length, 3);
    }
}
