import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(5, distribution.getMean(), 0.001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.03368974, distribution.probability(3), 0.000001);
        Assert.assertEquals(0.03368974, distribution.probability(4), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.15120388, distribution.cumulativeProbability(3), 0.000001);
        Assert.assertEquals(0.26502592, distribution.cumulativeProbability(4), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.59399666, distribution.normalApproximateProbability(3), 0.000001);
        Assert.assertEquals(0.70483743, distribution.normalApproximateProbability(4), 0.000001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        int sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.5), 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.001);
    }

}