import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testConstructorAndGetters() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covarianceMatrix = {{2.0, 1.0, 0.0}, {1.0, 3.0, 1.0}, {0.0, 1.0, 4.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covarianceMatrix);

        Assert.assertArrayEquals(means, distribution.getMeans(), EPSILON);
        Assert.assertEquals(covarianceMatrix.length, distribution.getCovariances().getRowDimension());
    }

    @Test
    public void testDensity() {
        double[] means = {0.0, 0.0};
        double[][] covarianceMatrix = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covarianceMatrix);

        double[] vals = {0.5, 0.5};
        double density = distribution.density(vals);
        Assert.assertEquals(0.159154943, density, EPSILON);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDensityDimension() {
        double[] means = {0.0, 0.0};
        double[][] covarianceMatrix = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covarianceMatrix);

        double[] vals = {0.5};
        distribution.density(vals);
    }

    @Test
    public void testSample() {
        double[] means = {0.0, 0.0};
        double[][] covarianceMatrix = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covarianceMatrix);

        double[] sample = distribution.sample();
        Assert.assertEquals(2, sample.length);
    }

    @Test
    public void testSampleMean() {
        double[] means = {1.0, 2.0};
        double[][] covarianceMatrix = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covarianceMatrix);

        int numSamples = 1000;
        double sumX = 0;
        double sumY = 0;
        for (int i = 0; i < numSamples; i++) {
            double[] sample = distribution.sample();
            sumX += sample[0];
            sumY += sample[1];
        }

        double meanX = sumX / numSamples;
        double meanY = sumY / numSamples;

        Assert.assertEquals(means[0], meanX, EPSILON);
        Assert.assertEquals(means[1], meanY, EPSILON);
    }
}