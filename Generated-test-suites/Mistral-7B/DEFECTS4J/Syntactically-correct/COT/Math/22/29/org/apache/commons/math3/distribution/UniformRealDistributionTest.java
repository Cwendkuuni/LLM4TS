import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.Assert;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstructorWithDefaultValues() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0.0, distribution.getLowerBound(), 0.01);
        Assert.assertEquals(1.0, distribution.getUpperBound(), 0.01);
    }

    @Test
    public void testUniformRealDistributionConstructorWithCustomValues() {
        double lower = 1.0;
        double upper = 5.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        Assert.assertEquals(lower, distribution.getLowerBound(), 0.01);
        Assert.assertEquals(upper, distribution.getUpperBound(), 0.01);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidValues() {
        double lower = 5.0;
        double upper = 1.0;
        new UniformRealDistribution(lower, upper);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double x = 0.5;
        double density = distribution.density(x);
        Assert.assertEquals(1.0 / (1.0 - 0.0), density, 0.01);
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double x = 0.5;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        Assert.assertEquals(0.5, cumulativeProbability, 0.01);
    }

    @Test
    public void testUniformRealDistributionCumulativeProbabilityOutOfRange() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double x = -1.0;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        Assert.assertEquals(0.0, cumulativeProbability, 0.01);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0.0 && sample <= 1.0);
    }

    @Test
    public void testUniformRealDistributionMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double mean = distribution.getNumericalMean();
        Assert.assertEquals(0.5, mean, 0.01);
    }

    @Test
    public void testUniformRealDistributionVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double variance = distribution.getNumericalVariance();
        Assert.assertEquals(0.000244948974278317, variance, 0.01);
    }

    @Test
    public void testUniformRealDistributionGetLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double lowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0.0, lowerBound, 0.01);
    }

    @Test
    public void testUniformRealDistributionGetUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double upperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(1.0, upperBound, 0.01);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }

    @Test
    public void testUniformRealDistributionCustomRandomGenerator() {
        double lower = 1.0;
        double upper = 5.0;
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, lower, upper, 0.00001);
        Assert.assertEquals(lower, distribution.getLowerBound(), 0.01);
        Assert.assertEquals(upper, distribution.getUpperBound(), 0.01);
    }
}