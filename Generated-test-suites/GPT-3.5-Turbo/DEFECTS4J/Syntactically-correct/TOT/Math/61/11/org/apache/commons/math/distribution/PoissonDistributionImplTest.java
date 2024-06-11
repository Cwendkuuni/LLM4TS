import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        // Typical use case
        Assert.assertEquals(0.2034683695, dist.probability(2), 0.000001);

        // Edge case
        Assert.assertEquals(0.0820849986, dist.probability(0), 0.000001);

        // Error scenario
        Assert.assertEquals(0.0, dist.probability(-1), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        // Typical use case
        Assert.assertEquals(0.7442318076, dist.cumulativeProbability(2), 0.000001);

        // Edge case
        Assert.assertEquals(0.2231301601, dist.cumulativeProbability(0), 0.000001);

        // Error scenario
        Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        // Typical use case
        Assert.assertEquals(0.8026345996, dist.normalApproximateProbability(2), 0.000001);

        // Edge case
        Assert.assertEquals(0.6914624613, dist.normalApproximateProbability(0), 0.000001);

        // Error scenario
        Assert.assertEquals(0.0, dist.normalApproximateProbability(-1), 0.000001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);

        int sample = dist.sample();

        // Typical use case, the generated sample value should be greater than or equal to 0
        Assert.assertTrue(sample >= 0);

        // The generated sample value should be an integer
        Assert.assertEquals((int) sample, sample);
    }
}