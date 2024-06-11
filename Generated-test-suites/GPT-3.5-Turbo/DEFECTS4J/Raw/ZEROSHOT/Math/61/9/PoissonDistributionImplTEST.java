import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.22404180765538775, dist.probability(2), 0.000001);
        Assert.assertEquals(0.14936120510359185, dist.probability(3), 0.000001);
        Assert.assertEquals(0.049787068367863944, dist.probability(5), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.9905361502750062, dist.cumulativeProbability(4), 0.000001);
        Assert.assertEquals(0.18126924692201818, dist.cumulativeProbability(7), 0.000001);
        Assert.assertEquals(0.9999999808442608, dist.cumulativeProbability(100), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2);
        Assert.assertEquals(0.9104423499358489, dist.normalApproximateProbability(2), 0.000001);
        Assert.assertEquals(0.9999683287581669, dist.normalApproximateProbability(5), 0.000001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        int sample = dist.sample();
        Assert.assertTrue(sample >= 0);
        Assert.assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3);
        Assert.assertEquals(3, dist.getMean(), 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1);
        Assert.assertEquals(0, dist.getDomainLowerBound(0.5), 0.000001);
        Assert.assertEquals(0, dist.getDomainLowerBound(0), 0.000001);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1);
        Assert.assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5), 0.000001);
        Assert.assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(1), 0.000001);
    }
}