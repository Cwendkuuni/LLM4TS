import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;
    private double[] means;
    private double[][] covariances;

    @Before
    public void setUp() {
        RandomGenerator rng = new Well19937c();
        means = new double[]{1.0, 2.0};
        covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        try {
            distribution = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Failed to create MultivariateNormalDistribution");
        }
    }

    @Test
    public void testGetMeans() {
        double[] actualMeans = distribution.getMeans();
        Assert.assertArrayEquals("Failed to get means", means, actualMeans, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[][] actualCovariances = distribution.getCovariances().getData();
        Assert.assertArrayEquals("Failed to get covariances", covariances, actualCovariances);
    }

    @Test
    public void testDensity() {
        double[] values = new double[]{1.5, 2.5};
        double actualDensity = distribution.density(values);
        double expectedDensity = 0.08893616793777607;
        Assert.assertEquals("Failed to calculate density", expectedDensity, actualDensity, 0.0);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        double[] expectedStandardDeviations = new double[]{1.0, 1.0};
        Assert.assertArrayEquals("Failed to get standard deviations", expectedStandardDeviations, actualStandardDeviations, 0.0);
    }

    @Test
    public void testSample() {
        double[] actualSample = distribution.sample();
        Assert.assertEquals("Failed to generate sample", means.length, actualSample.length);
        Assert.assertTrue("Failed to generate valid sample", isValidSample(actualSample));
    }

    private boolean isValidSample(double[] sample) {
        for (int i = 0; i < sample.length; i++) {
            if (sample[i] < means[i] - 5 || sample[i] > means[i] + 5) {
                return false;
            }
        }
        return true;
    }
}