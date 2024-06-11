
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;


public class UniformRealDistributionTest {
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double density = distribution.density(0.5);
        Assert.assertEquals(1.0, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, cumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double mean = distribution.getNumericalMean();
        Assert.assertEquals(0.5, mean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double variance = distribution.getNumericalVariance();
        Assert.assertEquals(0.0833, variance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double lowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0.0, lowerBound, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double upperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(1.0, upperBound, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0.0 && sample <= 1.0);
    }
}
