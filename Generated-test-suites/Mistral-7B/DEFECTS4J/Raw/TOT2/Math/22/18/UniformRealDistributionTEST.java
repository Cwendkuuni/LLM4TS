import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1);
        Assert.assertEquals(0, dist.getSupportLowerBound(), 0);
        Assert.assertEquals(1, dist.getSupportUpperBound(), 0);
        Assert.assertEquals(true, dist.isSupportLowerBoundInclusive());
        Assert.assertEquals(false, dist.isSupportUpperBoundInclusive());
        Assert.assertEquals(true, dist.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1);
        for (double i = 0; i < 100; i++) {
            double x = dist.sample();
            double y = dist.cumulativeProbability(x);
            Assert.assertTrue(y <= x);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionIllegalArguments() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = null;
        try {
            dist = new UniformRealDistribution(rng, 5, 3);
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1);
        for (int i = 0; i < 100; i++) {
            double x = dist.sample();
            Assert.assertTrue(x >= 0 && x < 1);
        }
    }

    @Test
    public void testUniformRealDistributionDensity() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1);
        for (double x = 0; x < 1; x += 0.01) {
            double y = dist.density(x);
            Assert.assertEquals(1, y, 0.001);
        }
    }
}