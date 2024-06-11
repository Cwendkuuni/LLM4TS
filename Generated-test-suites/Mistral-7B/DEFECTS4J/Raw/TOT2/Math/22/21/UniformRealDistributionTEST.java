import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0.0, distribution.getLowerBound(), 0.01);
        Assert.assertEquals(1.0, distribution.getUpperBound(), 0.01);
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 0.01);
        Assert.assertEquals(0.25, distribution.getNumericalVariance(), 0.01);
    }

    @Test
    public void testCustomConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0);
        Assert.assertEquals(1.0, distribution.getLowerBound(), 0.01);
        Assert.assertEquals(3.0, distribution.getUpperBound(), 0.01);
        Assert.assertEquals(2.0, distribution.getNumericalMean(), 0.01);
        Assert.assertEquals(0.9, distribution.getNumericalVariance(), 0.01);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(3.0, 1.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            Assert.assertEquals(1.0 / (1.0 - 0.0), density, 0.01);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = distribution.cumulativeProbability(x);
            Assert.assertEquals(x, cdf, 0.01);
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(new Well19937c(), 0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0.0);
            Assert.assertTrue(sample < 1.0);
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.01);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(1.0, distribution.getSupportUpperBound(), 0.01);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertTrue(distribution.isSupportConnected());
    }
}